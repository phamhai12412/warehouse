package ra.service.impl;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ra.dto.request.InventoryItemRequest;
import ra.dto.request.InventoryRequest;
import ra.dto.request.ProductDeliveryRequest;
import ra.dto.response.CompareResponse;
import ra.dto.response.InventoryBasicResponse;
import ra.dto.response.InventoryDetailResponse;
import ra.dto.response.InventoryResponse;
import ra.exception.MyCustomRuntimeException;
import ra.mapper.InventoryDetailMapper;
import ra.mapper.InventoryMapper;
import ra.mapper.ProductMapper;
import ra.model.*;
import ra.repository.IInventoryDetailRepository;
import ra.repository.IInventoryRepository;
import ra.repository.IProductRepository;
import ra.repository.IStorageRepository;
import ra.security.user_principal.UserPrinciple;
import ra.service.IInventoryService;

import java.io.*;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@Service
public class InventoryService implements IInventoryService {
    @Autowired
    private IInventoryRepository inventoryRepository;
    @Autowired
    private IInventoryDetailRepository inventoryDetailRepository;
    @Autowired
    private IStorageRepository storageRepository;
    @Autowired
    private InventoryMapper inventoryMapper;
    @Autowired
    private InventoryDetailMapper inventoryDetailMapper;
    @Autowired
    private IProductRepository productRepository;
    @Autowired
    private ProductMapper productMapper;
    @Override
    //tạo báo cáo tồn kho, nếu hôm nay đã tạo sẽ hiện thông báo và ko cho phép tạo thêm chỉ cho phép sửa và bắt buộc ghi nôi dung sửa chi tiết
    @Transactional(rollbackFor = MyCustomRuntimeException.class)
    public InventoryResponse saveInventoryReport(InventoryRequest inventoryRequest, Long useId,String note) throws MyCustomRuntimeException {
        try {
            Storage storage = storageRepository.findById(inventoryRequest.getStorageId()).orElseThrow(() -> new MyCustomRuntimeException("kho không tồn tại"));

            List<InventoryItemRequest> inventoryItems = inventoryRequest.getInventoryItems();
            List<Inventory> inventoryList = inventoryRepository.findAllByCreatedList(new Date(), inventoryRequest.getStorageId());
            if (!inventoryList.isEmpty()) {
                throw new MyCustomRuntimeException("Bạn đã tạo báo cáo tồn kho trong hôm nay rồi.");
            }
            if (inventoryItems.isEmpty()) {
                throw new MyCustomRuntimeException("thông tin báo cáo không đầy đủ");
            }
            //kiểm tra kho đó có thuộc quản lý của nhân viên đó không
            if (!storage.getUsers().getId().equals(useId)) {
                throw new MyCustomRuntimeException("bạn không có quyền điều hành kho này");
            }
            Inventory inventory = new Inventory();
            inventory.setStorage(storage);
            inventory.setCreated(new Date());
        inventory.setNote(note);

            // Lưu báo cáo tồn kho vào cơ sở dữ liệu
            inventory = inventoryRepository.save(inventory);
            // Lưu chi tiết tồn kho vào cơ sở dữ liệu
            List<InventoryDetail> inventoryDetails = new ArrayList<>();
            for (InventoryItemRequest itemRequest : inventoryItems) {
                InventoryDetail detail = new InventoryDetail();
                detail.setInventory(inventory);
                Product product = productRepository.findById(itemRequest.getProductId()).orElseThrow(() -> new MyCustomRuntimeException("không tồn tại sản phẩm"));
                if (!productRepository.existsProductByIdAndStorage_Id(product.getId(), inventoryRequest.getStorageId())) {
                    throw new MyCustomRuntimeException("kho không tồn tại sản phẩm này");
                }
                detail.setProduct(product);
                detail.setQuantityToday(product.getQuantity());
                detail.setQuantity(itemRequest.getQuantity());
                if(itemRequest.getReason().isEmpty()){
                    detail.setReason("báo cáo tồn kho");
                }else {
                    detail.setReason(itemRequest.getReason());
                }

                detail = inventoryDetailRepository.save(detail);
                inventoryDetails.add(detail);
            }
            return InventoryResponse.builder()
                    .id(inventory.getId())
                    .note(inventory.getNote())
                    .created(inventory.getCreated().toString())
                    .storage(inventory.getStorage())
                    .inventoryDetails(inventoryDetails.stream().map(inventoryDetail -> inventoryDetailMapper.convertDetail(inventoryDetail)).collect(Collectors.toList()))
                    .userIdCreated(inventory.getStorage().getUsers().getId())
                    .userNameCreated(inventory.getStorage().getUsers().getFirstName() + " " + inventory.getStorage().getUsers().getLastName())
                    .build();
        } catch (MyCustomRuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new MyCustomRuntimeException("Lỗi xảy ra khi lưu báo cáo tồn kho");
        }

    }

    @Override
    //tìm kếm tất cả báo cáo theo ngày tạo và storageType(kho hay siêu thị)
    public Page<InventoryBasicResponse> findAllByCreatedAndTypeStorage(Date created, String storageType, Pageable pageable) {
        TypeStorage typeStorage;
        try {
            typeStorage = TypeStorage.valueOf(storageType);
        } catch (IllegalArgumentException e) {
            throw new MyCustomRuntimeException("typeStorage không tồn tại");
        }

        Page<Inventory> inventoryList = inventoryRepository.findAllByCreatedAndStorage_TypeStorage(created, typeStorage, pageable);
        return inventoryList.map(inventory -> inventoryMapper.convertInventory(inventory));
    }

    @Override
    //tìm tất cả báo cáo theo ngày tạo
    public Page<InventoryBasicResponse> findAllByCreated(Date created, Pageable pageable) {
        Page<Inventory> inventoryList = inventoryRepository.findAllByCreated(created, pageable);
        return inventoryList.map(inventory -> inventoryMapper.convertInventory(inventory));
    }

    @Override
    //tìm tất cả báo cáo tồn kho của kho theo ngày tạo
    public List<InventoryDetailResponse> findAllByInventoryCreatedAndInventoryStorageID(Date inventoryCreated, Long id) {
        List<InventoryDetail> inventoryDetailList = inventoryDetailRepository.findAllByInventory_CreatedAndInventory_Storage_Id(inventoryCreated, id);
        return inventoryDetailList.stream().map(inventoryDetail -> inventoryDetailMapper.convertDetail(inventoryDetail)).collect(Collectors.toList());
    }

    @Override
    //tìm báo cáo tồn kho theo id nhơ hiện đầy đủ tất cả các thông tin chi tiết
    public InventoryResponse findAllByInventoryId(Long inventoryId) throws MyCustomRuntimeException {
        Inventory inventory = inventoryRepository.findById(inventoryId).orElseThrow(() -> new MyCustomRuntimeException("Không tìm thấy báo cáo"));
        List<InventoryDetail> inventoryDetailList = inventoryDetailRepository.findAllByInventory_Id(inventoryId);
        return InventoryResponse.builder()
                .id(inventory.getId())
                .note(inventory.getNote())
                .created(inventory.getCreated().toString())
                .storage(inventory.getStorage())
                .inventoryDetails(inventoryDetailList.stream().map(inventoryDetail -> inventoryDetailMapper.convertDetail(inventoryDetail)).collect(Collectors.toList()))
                .userIdCreated(inventory.getStorage().getUsers().getId())
                .userNameCreated(inventory.getStorage().getUsers().getFirstName() + " " + inventory.getStorage().getUsers().getLastName())
                .build();

    }

    @Override
    //cập nhật báo cáo tồn kho , nhớ phải bắt nhập chi tiết nội sung điều chỉnh, cập nhật theo dạng sửa theo inventoryDetailId
    public CompareResponse updateInventoryItem(Long inventoryDetailId, InventoryItemRequest newItem) throws MyCustomRuntimeException {
        if (newItem.getReason() == null) {
            throw new MyCustomRuntimeException("bạn phải nhập vào nội dung điều chỉnh");
        }
        InventoryDetail inventoryDetail = inventoryDetailRepository.findById(inventoryDetailId)
                .orElseThrow(() -> new MyCustomRuntimeException("Không tìm thấy mục với ID: " + inventoryDetailId));

        // Cập nhật thông tin mục
        inventoryDetail.setQuantity(newItem.getQuantity());
        inventoryDetail.setReason(newItem.getReason());

        // Lưu mục đã cập nhật vào cơ sở dữ liệu
        inventoryDetail = inventoryDetailRepository.save(inventoryDetail);
        return CompareResponse.builder()
                .id(inventoryDetail.getId())
                .product(productMapper.toResponse(inventoryDetail.getProduct()))
                .reason(inventoryDetail.getReason())
                .quantity(inventoryDetail.getQuantity())
                .quantityToday(inventoryDetail.getQuantityToday())
                .build();
    }

    @Override
    //xuất excel báo cáo tồn kho
    public byte[] exportToExcel(Date created, Long storage_id) throws IOException, MyCustomRuntimeException {
        Storage storage = storageRepository.findById(storage_id).orElseThrow(() -> new MyCustomRuntimeException("Không tìm thấy kho"));
        List<Inventory> inventoryList = inventoryRepository.findAllByCreatedList(created, storage_id);
        if (inventoryList.isEmpty()) {
            throw new MyCustomRuntimeException("Không tìm thấy báo cáo");
        }
        Inventory inventory = inventoryList.get(0);

        // Tạo tệp Excel và ghi dữ liệu vào đó (sử dụng Apache POI)
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Báo cáo tồn kho " + inventory.getStorage().getStorageName());
        // Tạo tiêu đề cho các cột
        // Tạo một CellStyle để định dạng văn bản và căn giữa
        CellStyle centerCellStyle = workbook.createCellStyle();
        centerCellStyle.setAlignment(HorizontalAlignment.CENTER);
        centerCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        // Tạo một FontStyle để định dạng font của cell
        Font font = workbook.createFont();
        font.setBold(true); // Đặt in đậm
        font.setFontHeightInPoints((short) 16); // Đặt kích thước font
        centerCellStyle.setFont(font);

        // Tạo một ô trên dòng 3 cột 0
        Cell headerRow1 = sheet.createRow(0).createCell(0);
        headerRow1.setCellValue("Báo Cáo Tồn Kho: "+storage.getStorageName());

        // Trộn ô từ cột 0 đến 8 trên dòng 0
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 8));
        headerRow1.setCellStyle(centerCellStyle);

        Row headerRow = sheet.createRow(1);
        headerRow.createCell(0).setCellValue("Kho_Id");
        headerRow.createCell(1).setCellValue("Ngày tạo kho ");
        headerRow.createCell(2).setCellValue("Tên kho");
        headerRow.createCell(3).setCellValue("Địa chỉ");
        headerRow.createCell(4).setCellValue("Ngày lập phiếu");
        headerRow.createCell(5).setCellValue("Loại kho");
        headerRow.createCell(6).setCellValue("Khu vực");
        headerRow.createCell(7).setCellValue("Người lập phiếu");
        headerRow.createCell(8).setCellValue("Quản lý");

        Row headerRow2 = sheet.createRow(2);
        headerRow2.createCell(0).setCellValue(storage.getId());
        headerRow2.createCell(1).setCellValue(storage.getCreated().toString());
        headerRow2.createCell(2).setCellValue(storage.getStorageName());
        headerRow2.createCell(3).setCellValue(storage.getAddress());
        headerRow2.createCell(4).setCellValue(inventory.getCreated().toString());
        headerRow2.createCell(5).setCellValue(storage.getTypeStorage().toString());
        headerRow2.createCell(6).setCellValue(storage.getZone().getZoneName());
        headerRow2.createCell(7).setCellValue(storage.getUsers().getFirstName() + " " + storage.getUsers().getLastName());
        headerRow2.createCell(8).setCellValue(storage.getUsers().getRoles().getRoleName() == RoleName.ROLE_MANAGER ? "Kho" : "Siêu thị");Cell cáoheaderRow3 = sheet.createRow(3).createCell(0);

        Row headerRow3 = sheet.createRow(3);
        Cell headerCell = headerRow3.createCell(0);
        headerCell.setCellValue("Báo Cáo Chi Tiết ");
        sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, 8));
        headerCell.setCellStyle(centerCellStyle);


        Row headerRow4 = sheet.createRow(4);
        headerRow4.createCell(2).setCellValue("Tên sản phẩm");
        headerRow4.createCell(3).setCellValue("Ngày sản xuất");
        headerRow4.createCell(4).setCellValue("Ngày hết hạn");
        headerRow4.createCell(5).setCellValue("Số lượng thực tế");
        headerRow4.createCell(6).setCellValue("Số lượng hệ thống");
        headerRow4.createCell(7).setCellValue("Ghi chú");
        // Đổ dữ liệu từ danh sách InventoryDetail vào các dòng
        List<InventoryDetail> inventoryDetails = inventoryDetailRepository.findAllByInventory_Id(inventory.getId());
        int rowNum = 5;
        for (InventoryDetail inventoryDetail : inventoryDetails) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(2).setCellValue(inventoryDetail.getProduct().getProductName());
            row.createCell(3).setCellValue(inventoryDetail.getProduct().getCreated().toString());
            row.createCell(4).setCellValue(inventoryDetail.getProduct().getDueDate().toString());
            row.createCell(5).setCellValue(inventoryDetail.getQuantity());
            row.createCell(6).setCellValue(inventoryDetail.getQuantityToday());
            row.createCell(7).setCellValue(inventoryDetail.getReason());
        }
        // Đặt độ rộng cột để thỏa mãn nội dung
        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);
        sheet.autoSizeColumn(3);
        sheet.autoSizeColumn(5);
        sheet.autoSizeColumn(6);
        sheet.autoSizeColumn(7);
        sheet.autoSizeColumn(8);
        sheet.autoSizeColumn(8);

        // Chuyển workbook thành mảng byte
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        workbook.write(byteArrayOutputStream);
        byte[] excelBytes = byteArrayOutputStream.toByteArray();
        workbook.close();
        byteArrayOutputStream.close();
        return excelBytes;
    }


    //tạo mẫu excel báo cáo tồn kho dùng để import
    @Override
    public byte[] exportToExcelProduct(Long storage_id) throws IOException, MyCustomRuntimeException {
        Storage storage = storageRepository.findById(storage_id).orElseThrow(() -> new MyCustomRuntimeException("không tìm thấy kho"));
        List<Product> products = productRepository.findAllByStorage_IdAndOtherStatusName(storage_id, StatusName.PENDING);

        // Tạo tệp Excel và ghi dữ liệu vào đó (sử dụng Apache POI)
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Mẫu báo cáo tồn kho " + storage.getStorageName());
        // Tạo tiêu đề cho các cột
        Row headerRow1 = sheet.createRow(0);
        // Tạo một CellStyle để định dạng văn bản và căn giữa
        CellStyle centerCellStyle = workbook.createCellStyle();
        centerCellStyle.setAlignment(HorizontalAlignment.CENTER);
        centerCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        // Tạo một FontStyle để định dạng font của cell
        Font font = workbook.createFont();
        font.setBold(true); // Đặt in đậm
        font.setFontHeightInPoints((short) 16); // Đặt kích thước font
        centerCellStyle.setFont(font);

        // Tạo cell "Danh sách nhân viên" và đặt style
        Cell headerCell = headerRow1.createCell(1);
        headerCell.setCellValue("Báo cáo tồn kho: " + storage.getStorageName());
        headerCell.setCellStyle(centerCellStyle);

        // Merge các ô từ A0 đến k0 để tạo một ô lớn cho tiêu đề
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 1, 10));

        Row headerRow = sheet.createRow(1);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("Tên sản phẩm");
        headerRow.createCell(2).setCellValue("Mã sản phẩm");
        headerRow.createCell(3).setCellValue("Giá sản phẩm");
        headerRow.createCell(4).setCellValue("Trọng lượng");
        headerRow.createCell(5).setCellValue("Ngày sản xuất");
        headerRow.createCell(6).setCellValue("Ngày hết hạn");
        headerRow.createCell(7).setCellValue("Danh mục");
        headerRow.createCell(8).setCellValue("Trạng thái");
        headerRow.createCell(9).setCellValue("Số lượng hệ thống");
        headerRow.createCell(10).setCellValue("Số lượng thực tế");
        headerRow.createCell(11).setCellValue("Ghi chú");


        // Đổ dữ liệu từ danh sách categories vào các dòng
        int rowNum = 2;
        for (Product product : products) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(product.getId());
            row.createCell(1).setCellValue(product.getProductName());
            row.createCell(2).setCellValue(product.getCode());
            row.createCell(3).setCellValue(product.getPrice());
            row.createCell(4).setCellValue(product.getWeight());
            row.createCell(5).setCellValue(product.getCreated().toString());
            row.createCell(6).setCellValue(product.getDueDate().toString());
            row.createCell(7).setCellValue(product.getCategory().getCategoryName());
            row.createCell(8).setCellValue(product.getStatusName().toString());
            row.createCell(9).setCellValue(product.getQuantity());
            row.createCell(10);
            row.createCell(11);

        }

        // Đặt độ rộng cột để thỏa mãn nội dung
        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);
        sheet.autoSizeColumn(3);
        sheet.autoSizeColumn(5);
        sheet.autoSizeColumn(6);
        sheet.autoSizeColumn(7);
        sheet.autoSizeColumn(8);
        sheet.autoSizeColumn(9);
        sheet.autoSizeColumn(10);
        sheet.setColumnWidth(11,10000);

        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);

        for (int i = 0; i < products.size(); i++) {
            Row row = sheet.getRow(i + 2);
            Cell cell = row.getCell(10);
            Cell cell1 = row.getCell(11);
            cell.setCellStyle(style);
            cell1.setCellStyle(style);
        }

        // Chuyển workbook thành mảng byte
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        workbook.write(byteArrayOutputStream);
        byte[] excelBytes = byteArrayOutputStream.toByteArray();
        workbook.close();
        byteArrayOutputStream.close();
        return excelBytes;
    }
    @Override

    //tạo báo kho tồn kho bằng import excel
    @Transactional(rollbackFor = MyCustomRuntimeException.class)
    public InventoryResponse importInventoryReport(MultipartFile file,String note, Long id, Authentication authentication) {
        try {
            File excelFile = convertMultipartFileToFile(file);
            try (FileInputStream fileInputStream = new FileInputStream(excelFile);
                 Workbook workbook = new XSSFWorkbook(fileInputStream)) {
                Sheet sheet = workbook.getSheetAt(0);

                List<InventoryItemRequest> inventoryItemRequestList = new ArrayList<>();
                for (int rowIndex = 2; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                    Row row = sheet.getRow(rowIndex);

                    Cell productIdCell = row.getCell(0);
                    Cell quantityCell = row.getCell(10);
                    Cell reasonCell = row.getCell(11);


                    Long productId = (long) productIdCell.getNumericCellValue();
                    int quantity = (int) quantityCell.getNumericCellValue();
                    String reason =  reasonCell.getStringCellValue();

                    InventoryItemRequest inventoryItemRequest = new InventoryItemRequest();
                    inventoryItemRequest.setProductId(productId);
                    inventoryItemRequest.setQuantity(quantity);
                    inventoryItemRequest.setReason(reason);
                    inventoryItemRequestList.add(inventoryItemRequest);
                }

                UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
                Long userId = userPrinciple.getId();

                InventoryRequest inventoryRequest = InventoryRequest.builder()
                        .storageId(id)
                        .inventoryItems(inventoryItemRequestList)
                        .build();

                return saveInventoryReport(inventoryRequest, userId, note);
            } catch (IOException e) {
                throw new MyCustomRuntimeException("Lỗi khi đọc file Excel: " + e.getMessage());
            }
        } catch (IOException e) {
            throw new MyCustomRuntimeException("Lỗi khi tạo tệp tạm thời: " + e.getMessage());
        }
    }


    private File convertMultipartFileToFile(MultipartFile multipartFile) throws IOException {
        File file = File.createTempFile("tempExcel", ".xlsx");
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(multipartFile.getBytes());
        }
        return file;
    }
    @Override

    //All báo cáo tồn kho theo từng kho kèm tìm kiếm theo note
    public Page<InventoryBasicResponse> findAllByStorage_IdAndNote(Long storageId,String note, Pageable pageable) {
        return inventoryRepository.findAllByStorage_IdAndNoteContainingIgnoreCase(storageId,note, pageable).map(inventory -> inventoryMapper.convertInventory(inventory));
    }
    @Override

    //All báo cáo tồn kho theo từng kho kèm ngày
    public Page<InventoryBasicResponse> findAllByStorageIdAndCreated(Date created,Long storageId, Pageable pageable) {
        return inventoryRepository.findAllByCreatedAndStorage_id(created, storageId, pageable).map(inventory -> inventoryMapper.convertInventory(inventory));
    }
    //báo cáo chi tiết tồn kho theo id báo cáo dữ liệu trả về gồm tồn kho thực tế và tồn kho hệ thống để phục vụ so sánh
    @Override
    public Page<CompareResponse> findAllByCompareInventoryId(Long inventoryId,Pageable pageable,String search) throws MyCustomRuntimeException {
        Page<InventoryDetail> inventoryDetailList = inventoryDetailRepository.findAllByInventory_IdAndProduct_ProductNameContainingIgnoreCase(inventoryId,search,pageable);


        List<CompareResponse> list = inventoryDetailList.stream().map(inventoryDetail -> CompareResponse.builder()
                .id(inventoryDetail.getId())
                .quantity(inventoryDetail.getQuantity())
                .reason(inventoryDetail.getReason())
                .quantityToday(inventoryDetail.getQuantityToday())
                .product(productMapper.toResponse(inventoryDetail.getProduct()))
                .build()).collect(Collectors.toList());
        return new PageImpl<>(list,pageable,list.size());
    }
    @Override
    //xuất báo cáo dưới dạng excel theo id
    public byte[] exportToExcelInventoryId(Long inventoryId) throws IOException, MyCustomRuntimeException {
        Inventory inventory = inventoryRepository.findById(inventoryId).orElseThrow(()->new MyCustomRuntimeException("không tìm thấy báo cáo tồn kho"));
        Storage storage=inventory.getStorage();
        Storage storage1=storageRepository.findById(storage.getId()).orElseThrow(()->new  MyCustomRuntimeException("không tìm thấy kho"));
        // Tạo tệp Excel và ghi dữ liệu vào đó (sử dụng Apache POI)
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Báo cáo tồn kho " + inventory.getStorage().getStorageName());
        // Tạo tiêu đề cho các cột
        // Tạo một CellStyle để định dạng văn bản và căn giữa
        CellStyle centerCellStyle = workbook.createCellStyle();
        centerCellStyle.setAlignment(HorizontalAlignment.CENTER);
        centerCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        // Tạo một FontStyle để định dạng font của cell
        Font font = workbook.createFont();
        font.setBold(true); // Đặt in đậm
        font.setFontHeightInPoints((short) 16); // Đặt kích thước font
        centerCellStyle.setFont(font);

        // Tạo một ô trên dòng 3 cột 0
        Cell headerRow1 = sheet.createRow(0).createCell(0);
        headerRow1.setCellValue("Báo Cáo Tồn Kho: "+storage.getStorageName());

         // Trộn ô từ cột 0 đến 8 trên dòng 0
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 8));
        headerRow1.setCellStyle(centerCellStyle);

        Row headerRow = sheet.createRow(1);
        headerRow.createCell(0).setCellValue("Kho_id");
        headerRow.createCell(1).setCellValue("Ngày tạo kho");
        headerRow.createCell(2).setCellValue("Tên kho");
        headerRow.createCell(3).setCellValue("Địa chỉ");
        headerRow.createCell(4).setCellValue("Ngày lập phiếu");
        headerRow.createCell(5).setCellValue("Loại kho");
        headerRow.createCell(6).setCellValue("Khu vực");
        headerRow.createCell(7).setCellValue("Người lập phiếu");
        headerRow.createCell(8).setCellValue("Quản lý");

        Row headerRow2 = sheet.createRow(2);
        headerRow2.createCell(0).setCellValue(storage.getId());
        headerRow2.createCell(1).setCellValue(storage1.getCreated().toString());
        headerRow2.createCell(2).setCellValue(storage.getStorageName());
        headerRow2.createCell(3).setCellValue(storage.getAddress());
        headerRow2.createCell(4).setCellValue(inventory.getCreated().toString());
        headerRow2.createCell(5).setCellValue(storage.getTypeStorage().toString());
        headerRow2.createCell(6).setCellValue(storage.getZone().getZoneName());
        headerRow2.createCell(7).setCellValue(storage.getUsers().getFirstName() + " " + storage.getUsers().getLastName());
        headerRow2.createCell(8).setCellValue(storage.getUsers().getRoles().getRoleName() == RoleName.ROLE_MANAGER ? "Kho" : "Siêu thị");

        Row headerRow3 = sheet.createRow(3);
        Cell headerCell = headerRow3.createCell(0);
        headerCell.setCellValue("Báo Cáo Chi Tiết ");
        sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, 8));
        headerCell.setCellStyle(centerCellStyle);


        Row headerRow4 = sheet.createRow(4);
        headerRow4.createCell(2).setCellValue("Tên sản phẩm");
        headerRow4.createCell(3).setCellValue("Mã sản phẩm");
        headerRow4.createCell(4).setCellValue("Ngày sản xuất");
        headerRow4.createCell(5).setCellValue("Ngày hêt hạn");
        headerRow4.createCell(6).setCellValue("Số lượng thực tế");
        headerRow4.createCell(7).setCellValue("Số lượng hệ thống");
        headerRow4.createCell(8).setCellValue("Ghi chú");
        // Đổ dữ liệu từ danh sách InventoryDetail vào các dòng
        List<InventoryDetail> inventoryDetails = inventoryDetailRepository.findAllByInventory_Id(inventory.getId());
        int rowNum = 5;
        for (InventoryDetail inventoryDetail : inventoryDetails) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(2).setCellValue(inventoryDetail.getProduct().getProductName());
            row.createCell(3).setCellValue(inventoryDetail.getProduct().getCode());
            row.createCell(4).setCellValue(inventoryDetail.getProduct().getCreated().toString());
            row.createCell(5).setCellValue(inventoryDetail.getProduct().getDueDate().toString());
            row.createCell(6).setCellValue(inventoryDetail.getQuantity());
            row.createCell(7).setCellValue(inventoryDetail.getProduct().getQuantity());
            row.createCell(8).setCellValue(inventoryDetail.getReason());
        }
        // Đặt độ rộng cột để thỏa mãn nội dung
        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);
        sheet.autoSizeColumn(3);
        sheet.autoSizeColumn(5);
        sheet.autoSizeColumn(6);
        sheet.autoSizeColumn(7);
        sheet.autoSizeColumn(8);
        sheet.autoSizeColumn(8);

        // Chuyển workbook thành mảng byte
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        workbook.write(byteArrayOutputStream);
        byte[] excelBytes = byteArrayOutputStream.toByteArray();
        workbook.close();
        byteArrayOutputStream.close();
        return excelBytes;
    }
    //All báo cáo tồn kho
    @Override
    public List<InventoryBasicResponse> findAll() {
        return inventoryRepository.findAll().stream().map(inventory -> inventoryMapper.convertInventory(inventory)).collect(Collectors.toList());
    }
}
