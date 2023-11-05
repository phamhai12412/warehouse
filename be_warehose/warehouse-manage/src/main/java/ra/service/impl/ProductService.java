package ra.service.impl;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ra.dto.request.ProductRequest;
import ra.dto.response.ProductResponse;
import ra.exception.DataNotFoundException;
import ra.exception.MyCustomRuntimeException;
import ra.mapper.ProductMapper;
import ra.model.*;
import ra.repository.ICategoryRepository;
import ra.repository.IProductRepository;
import ra.repository.IStorageRepository;
import ra.service.IProductService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.Normalizer;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductService implements IProductService {
    @Autowired
    private IProductRepository productRepository;
    @Autowired
    private IStorageRepository storageRepository;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ICategoryRepository categoryRepository;
    @Autowired
    private CoverCode coverCode;
    @Override
    //tìm tất cá sản phẩm
    public Page<ProductResponse> findAll(Pageable pageable) {
        Page<Product> productPage = productRepository.findAll(pageable);
        return productPage.map(product -> productMapper.toResponse(product));
    }

    @Override
    //tìm kiếm sản phẩm dựa vào tên
    public Page<ProductResponse> findByName(String name, Pageable pageable) throws DataNotFoundException {
        Page<Product> productPage = productRepository.findAllByProductNameContainingIgnoreCase(name, pageable);
        Page<ProductResponse> productResponsePage = productPage.map(product -> productMapper.toResponse(product));
        if (productResponsePage.isEmpty()) {
            throw new DataNotFoundException("No result found.");
        }
        return productResponsePage;
    }

    @Override
    //tìm kiếm sản phẩm dựa vào statusName
    public Page<ProductResponse> findByStatus(StatusName statusName, Pageable pageable) throws DataNotFoundException {
        Page<Product> productPage = productRepository.findAllByStatusName(statusName, pageable);
        Page<ProductResponse> productResponsePage = productPage.map(product -> productMapper.toResponse(product));
        if (productResponsePage.isEmpty()) {
            throw new DataNotFoundException("No result found.");
        }
        return productResponsePage;
    }

    @Override
    //tìm kiếm sản phẩm theo id
    public ProductResponse findById(Long id) throws DataNotFoundException {
        Optional<Product> productOptional = productRepository.findById(id);
        if (!productOptional.isPresent()) {
            throw new DataNotFoundException("Product's id " + id + " not found.");
        }
        return productMapper.toResponse(productOptional.get());
    }

    @Override
    //cập nhật thông tin sản phẩm
    //không cho sửa số lượng vì tránh tinh trạng khó kiểm soát
    // sản pham trong kho thay đổi thông qua quá trình xuất nhập, như vậy chức năng thêm mới sẽ hỗ trợ thêm chức năng nhập kho,
    //để đảm bảo khi có hàng hóa bị thất thoát dễ dàng truy vết
    // nhớ phần tạo mới thêm logic nhâp kho xét hàng đã tồn tại
    public ProductResponse update(ProductRequest productRequest, Long id, List<String> list) throws DataNotFoundException {
        Optional<Product> productOptional = productRepository.findById(id);
        if (!productOptional.isPresent()) {
            throw new DataNotFoundException("Product's id " + id + " not found.");
        }
        Product oldDataProduct = productOptional.get();
        if (productRequest.getProductName() != null) {
            if(!productRequest.getProductName().equals(oldDataProduct.getProductName())){
                oldDataProduct.setProductName(productRequest.getProductName().toUpperCase());


            }

        }
        if(oldDataProduct.getStatusName().equals(StatusName.PENDING)){
            oldDataProduct.setCreated(productRequest.getCreated());
        }
        if(oldDataProduct.getStatusName().equals(StatusName.PENDING)){
            oldDataProduct.setDueDate(productRequest.getDueDate());
        }

        if (productRequest.getWeight() != null) {
            oldDataProduct.setWeight(productRequest.getWeight());
        }
        if (productRequest.getCategoryId() != null) {
            if(!productRequest.getCategoryId().equals(oldDataProduct.getCategory().getId())){
                oldDataProduct.setCategory(categoryRepository.findById(productRequest.getCategoryId()).orElseThrow(() -> new MyCustomRuntimeException("category không tồn tại")));
            }


        }


            oldDataProduct.setPrice(productRequest.getPrice());
        List<Product> productscheck =   productRepository.findByProductNameAndCreatedAndDueDateAndCategory_IdAndPriceAndWeight(oldDataProduct.getProductName(), oldDataProduct.getCreated(), oldDataProduct.getDueDate(),oldDataProduct.getCategory().getId(), oldDataProduct.getPrice(), oldDataProduct.getWeight());
        if(productscheck.isEmpty()){
            String codeP = coverCode.removeVietnameseDiacriticsAndSpaces(oldDataProduct.getProductName());
            String[] partscd = oldDataProduct.getCreated().toString().split("-"); // Tách chuỗi bằng dấu gạch ngang
            String partscdkq = partscd[1] + partscd[2]; // Kết hợp hai phần tháng và ngày
            String code=codeP+"A"+partscdkq+oldDataProduct.getId();
            oldDataProduct.setCode(code.toUpperCase());
        }else {
            oldDataProduct.setCode(productscheck.get(0).getCode());
        }

        return productMapper.toResponse(productRepository.save(oldDataProduct));
    }

    @Override
    //chuyển sang tran thái delete
    public ProductResponse deleteById(Long id) throws MyCustomRuntimeException {
        Product product = productRepository.findById(id).orElseThrow(() -> new MyCustomRuntimeException("không tìm thấy sản phẩm"));
        product.setStatusName(StatusName.DELETE);
        product = productRepository.save(product);
        return productMapper.toResponse(productRepository.save(product));
    }

    @Override
    //chuyển sang trạng thái ACCEPT // giữ lại phương thức này nếu sau muốn thêm logic (tạm thời ko sử dụng)
    public ProductResponse acceptById(Long id) throws MyCustomRuntimeException {
        Product product = productRepository.findById(id).orElseThrow(() -> new MyCustomRuntimeException("không tìm thấy sản phẩm"));
        product.setStatusName(StatusName.ACCEPT);
        product = productRepository.save(product);
        return productMapper.toResponse(productRepository.save(product));
    }

    @Override
    //chuyển sang trạng thái ACCEPT
    public ProductResponse approveProduct(Long id) throws MyCustomRuntimeException {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new MyCustomRuntimeException("không tồn tại sản phẩm"));

        // Kiểm tra xem product có ở trạng thái pending không
        if (!product.getStatusName().equals(StatusName.PENDING)) {
            throw new MyCustomRuntimeException("không ở trạng thái pending");
        }

//lấy  sản phẩm có cùng tên , ngày sản xuất, ngày hêt hạn trong kho ở trạng thái ACCEPT và delete

        Product productCheck = productRepository.findByProductNameAndCreatedAndDueDateAndStorage_IdAndStatusNameAndCategory_IdAndPriceAndWeight(product.getProductName(), product.getCreated(), product.getDueDate(), product.getStorage().getId(),StatusName.ACCEPT,product.getCategory().getId(),product.getPrice(), product.getWeight());
        Product productCheck1 = productRepository.findByProductNameAndCreatedAndDueDateAndStorage_IdAndStatusNameAndCategory_IdAndPriceAndWeight(product.getProductName(), product.getCreated(), product.getDueDate(), product.getStorage().getId(),StatusName.DELETE,product.getCategory().getId(),product.getPrice(), product.getWeight());
        if(productCheck!=null){
            //tồn tại ACCEPT thì mă định tăng số lượng
            productCheck.setQuantity(productCheck.getQuantity() + product.getQuantity());
            productRepository.delete(product);
            productCheck.setStatusName(StatusName.ACCEPT);
            productCheck = productRepository.save(productCheck);
            return productMapper.toResponse(productCheck);
        }else if(productCheck1!=null) {
            //nếu rơi vào ko tồn tại ACCEPT mà delete thì thay trạng thái delete và tang số lượng
            productCheck1.setQuantity(productCheck1.getQuantity() + product.getQuantity());
            productRepository.delete(product);
            productCheck1.setStatusName(StatusName.ACCEPT);
            productCheck1 = productRepository.save(productCheck1);
            return productMapper.toResponse(productCheck1);
        }

//nếu mà ko tồn tại sản phẩm có trung thông tin thì phê duyệt bình thường
        product.setStatusName(StatusName.ACCEPT);
        product = productRepository.save(product);
        return productMapper.toResponse(product);
    }

    @Override
    //chuyển sang trang thái từ chối
    public ProductResponse rejectProduct(Long id) throws MyCustomRuntimeException {
        Optional<Product> optionalProduct = productRepository.findById(id);
        Product product;
        if (optionalProduct.isPresent()) {
            product = optionalProduct.get();
            if (!product.getStatusName().equals(StatusName.PENDING)) {
                product.setStatusName(StatusName.DELETE);
                return productMapper.toResponse(productRepository.save(product));
            }
            productRepository.deleteById(id);
            return productMapper.toResponse(optionalProduct.get());
        }
        throw new MyCustomRuntimeException("product not found");
    }

    @Override
    //chức năng tìm kiếm tổng hợp
    public Page<ProductResponse> findAllByProductNameContainingIgnoreCaseAndStorage_IdAndCode(Optional<String> productName, Optional<String> category, Long storageId, Pageable pageable) {
        List<ProductResponse> products=new ArrayList<>();

    if (productName.isPresent()) {
            // Kiểm tra nếu từ khóa tìm kiếm theo tên sản phẩm tồn tại có thể là tên sản phẩm hoăc code
            if (!category.get().equals("ALL")) {
                // Nếu category không phải "ALL", tìm sản phẩm theo tên sản phẩm, tên danh mụcvà id kho
              List<ProductResponse>  productByName = productRepository.findByProductNameContainingIgnoreCaseAndCategoryCategoryNameAndStorageId(
                        productName.get(), category.get(), storageId).stream().map(product -> productMapper.toResponse(product)).collect(Collectors.toList());
                List<ProductResponse>  productByCode = productRepository.findByCodeContainingIgnoreCaseAndCategoryCategoryNameAndStorageId(
                        productName.get(), category.get(), storageId).stream().map(product -> productMapper.toResponse(product)).collect(Collectors.toList());
                // Tạo một Set để lọc ra sản phẩm không trùng lặp
                Set<ProductResponse> uniqueProducts = new HashSet<>();
                uniqueProducts.addAll(productByName);
                uniqueProducts.addAll(productByCode);

// Chuyển lại thành một danh sách sản phẩm không trùng lặp
                List<ProductResponse> uniqueProductList = new ArrayList<>(uniqueProducts);
                products=uniqueProductList;

            } else {
                // Nếu category là "ALL", tìm sản phẩm theo tên sản phẩm và id kho

                List<ProductResponse>  productByName = productRepository.findByProductNameContainingIgnoreCaseAndStorageId(
                        productName.get(), storageId).stream().map(product -> productMapper.toResponse(product)).collect(Collectors.toList());
                List<ProductResponse>  productByCode = productRepository.findAllByCodeContainingIgnoreCaseAndStorage_Id(
                        productName.get(), storageId).stream().map(product -> productMapper.toResponse(product)).collect(Collectors.toList());
                // Tạo một Set để lọc ra sản phẩm không trùng lặp
                Set<ProductResponse> uniqueProducts = new HashSet<>();
                uniqueProducts.addAll(productByName);
                uniqueProducts.addAll(productByCode);

// Chuyển lại thành một danh sách sản phẩm không trùng lặp
                List<ProductResponse> uniqueProductList = new ArrayList<>(uniqueProducts);
                products=uniqueProductList;

            }
        } else {
            // Nếu cả mã code và tên sản phẩm không tồn tại
            if (!category.get().equals("ALL")) {
                // Nếu category không phải "ALL", tìm sản phẩm theo tên danh mục và id kho
                products = productRepository.findByCategoryCategoryNameAndStorageId(category.get(), storageId).stream().map(product -> productMapper.toResponse(product)).collect(Collectors.toList());
            } else {
                // Nếu category là "ALL", tìm tất cả sản phẩm theo id kho
                products = productRepository.findAllByStorage_Id(storageId).stream().map(product -> productMapper.toResponse(product)).collect(Collectors.toList());
            }
        }

        // Trả về một trang kết quả
        return new PageImpl<>(products, pageable, products.size());
    }


    @Override
    //tìm kiếm tất cả sản phẩm theo id kho
    public Page<ProductResponse> findAllByStorage_Id(Long storage_id, Pageable pageable) {
        Page<Product> products = productRepository.findAllByStorage_Id(storage_id, pageable);
        return products.map(product -> productMapper.toResponse(product));

    }

    @Override
    //tìm kiếm tất cả theo statusName và theo id kho
    public Page<ProductResponse> findAllByStatusNameAndStorage_Id(String statusName, Long storage_id, Pageable pageable) {
        StatusName statusNames = StatusName.valueOf((statusName));
        Page<Product> products = productRepository.findAllByStatusNameAndStorage_Id(statusNames, storage_id, pageable);
        return products.map(product -> productMapper.toResponse(product));

    }

    @Override
    //in excel danh sách tất cả sản phảm theo kho và có trạng thái ACCEPT
    public byte[] exportToExcel(Long storage_id) throws IOException, MyCustomRuntimeException {
        Storage storage = storageRepository.findById(storage_id).orElseThrow(() -> new MyCustomRuntimeException("không tìm thấy kho"));
        List<Product> products = productRepository.findAllByStorage_IdAndStatusName(storage_id, StatusName.ACCEPT);

        // Tạo tệp Excel và ghi dữ liệu vào đó (sử dụng Apache POI)
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Danh sách sản phẩm trong kho " + storage.getStorageName());
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
        headerCell.setCellValue("Danh sách sản phẩm của kho: " + storage.getStorageName());
        headerCell.setCellStyle(centerCellStyle);

        // Merge các ô từ B1 đến J1 để tạo một ô lớn cho tiêu đề
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 1, 7));

        Row headerRow = sheet.createRow(1);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("Tên sản phẩm");
        headerRow.createCell(2).setCellValue("Mã sản phẩm");
        headerRow.createCell(3).setCellValue("Giá");
        headerRow.createCell(4).setCellValue("Trọng lượng");
        headerRow.createCell(5).setCellValue("Ngày sản xuất");
        headerRow.createCell(6).setCellValue("Ngày hết hạn");
        headerRow.createCell(7).setCellValue("Danh mục");
        headerRow.createCell(8).setCellValue("Trạng thái");
        headerRow.createCell(9).setCellValue("Số lượng");

// Định nghĩa một DecimalFormat với ký tự ngăn cách hàng nghìn và đơn vị tiền tệ là "vdn"
        DecimalFormat df = new DecimalFormat("#,##0 VND", new DecimalFormatSymbols(new Locale("vi", "VN")));

        // Đổ dữ liệu từ danh sách categories vào các dòng
        int rowNum = 2;
        for (Product product : products) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(product.getId());
            row.createCell(1).setCellValue(product.getProductName());
            row.createCell(2).setCellValue(product.getCode());
            row.createCell(3).setCellValue(df.format(product.getPrice()));
            row.createCell(4).setCellValue(product.getWeight());
            row.createCell(5).setCellValue(product.getCreated().toString());
            row.createCell(6).setCellValue(product.getDueDate().toString());
            row.createCell(7).setCellValue(product.getCategory().getCategoryName());
            row.createCell(8).setCellValue(product.getStatusName().toString());
            row.createCell(9).setCellValue(product.getQuantity());
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

        // Chuyển workbook thành mảng byte
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        workbook.write(byteArrayOutputStream);
        byte[] excelBytes = byteArrayOutputStream.toByteArray();
        workbook.close();
        byteArrayOutputStream.close();
        return excelBytes;
    }

    @Override
    //thay đổi statusName
    public ProductResponse changeStatus(Long id, String statusName) throws MyCustomRuntimeException {

        Product product = productRepository.findById(id).orElseThrow(() -> new MyCustomRuntimeException("không tìm thấy product"));
        product.setStatusName(StatusName.valueOf(statusName));
        product = productRepository.save(product);
        return productMapper.toResponse(product);
    }

    @Override
    //tạo sản phẩm mới nếu là admin thì sp se có trạng thái ACCEPT, nếu ko phải admin thì kiểm tra xem người tạo có điều hành kho đó ko
    @Transactional(rollbackFor = MyCustomRuntimeException.class)
    public ProductResponse save(ProductRequest productRequest, List<String> list, Long iduse) {
        try {
            Storage storage = storageRepository.findById(productRequest.getStorageId()).orElseThrow(() -> new MyCustomRuntimeException("không tồn tại storage"));

//lấy  sản phẩm có cùng tên , ngày sản xuất, ngày hêt hạn trong kho ở trạng thái ACCEPT và delete
            Product   productCheck3=productRepository.findByProductNameAndCreatedAndDueDateAndStorage_IdAndStatusNameAndCategory_IdAndPriceAndWeight(productRequest.getProductName(), productRequest.getCreated(), productRequest.getDueDate(), storage.getId(),StatusName.PENDING,productRequest.getCategoryId(), productRequest.getPrice(), productRequest.getWeight());
            Product productCheck = productRepository.findByProductNameAndCreatedAndDueDateAndStorage_IdAndStatusNameAndCategory_IdAndPriceAndWeight(productRequest.getProductName(), productRequest.getCreated(), productRequest.getDueDate(), storage.getId(),StatusName.ACCEPT,productRequest.getCategoryId(),productRequest.getPrice(), productRequest.getWeight());
            Product productCheck1 = productRepository.findByProductNameAndCreatedAndDueDateAndStorage_IdAndStatusNameAndCategory_IdAndPriceAndWeight(productRequest.getProductName(), productRequest.getCreated(), productRequest.getDueDate(), storage.getId(),StatusName.DELETE,productRequest.getCategoryId(),productRequest.getPrice(), productRequest.getWeight());
            //chú ý trạng thái pending thì sẽ thực hiện logic cộng dồn sp chờ admin xét duêt nên sẽ phải check tồn tại trạng thái này không
            //vì nếu ko check sẽ xảy ra bug sẽ tạo ra 2 sản phẩm ở trạng thái pending với chức năng của manage
            //productCheck != null||productCheck1!=null||productCheck3!=null có sẽ tiến hàng cộng số lương
            //ngược lại ko tồn tại thì thêm với trạng thái ACCEPT với admin và pending với manage
            if (productCheck != null||productCheck1!=null||productCheck3!=null) {
                //đã tồn tại san phẩm có 1 trong 3 trạng thái ACCEPT, delete, pending
                if (list.get(0).equals(RoleName.ROLE_ADMIN.toString())) {
                    //nếu là admin thì thay đổi luôn số lượng và chuyển sang trạng thái ACCEPT ưu tiên trangj thái ACCEPT
                    if(productCheck!=null){
                        //tồn tại ACCEPT thì mă định tăng số lượng
                        productCheck.setQuantity(productCheck.getQuantity() + productRequest.getQuantity());
                        productCheck.setStatusName(StatusName.ACCEPT);
                        productCheck = productRepository.save(productCheck);
                        return productMapper.toResponse(productCheck);
                    }else if(productCheck1!=null) {
                        //nếu rơi vào ko tồn tại ACCEPT mà delete thì thay trạng thái delete và tang số lượng
                        productCheck1.setQuantity(productCheck1.getQuantity() + productRequest.getQuantity());
                        productCheck1.setStatusName(StatusName.ACCEPT);
                        productCheck1 = productRepository.save(productCheck1);
                        return productMapper.toResponse(productCheck1);
                    }
                } else {
                    //nếu ko phải admin check xem có phải người quản lý kho này ko
                    if (!storage.getUsers().getId().equals(iduse)) {
                        throw new MyCustomRuntimeException("bạn không quản lý kho này: " + storage.getStorageName());
                    }

                    //nếu sản phẩm tìm thấy vẫn đang ở pending thì tiến hành cộng số lương
                    if (productCheck3!=null) {
                        productCheck3.setQuantity(productCheck3.getQuantity() + productRequest.getQuantity());
                        productCheck3.setStatusName(StatusName.PENDING);
                        productCheck3 = productRepository.save(productCheck3);
                        return productMapper.toResponse(productCheck3);
                    }
                    //nếu ko phải là pending thì sẽ tiến hành thêm mới dưới dạng pending chờ admin phê duyệt( nhớ xử lý logic phê duyệt)
                    Product product = productMapper.toEntity(productRequest);
                    product=productRepository.save(product);

                List<Product> productscheck =   productRepository.findByProductNameAndCreatedAndDueDateAndCategory_IdAndPriceAndWeight(productRequest.getProductName(), productRequest.getCreated(), productRequest.getDueDate(),productRequest.getCategoryId(), productRequest.getPrice(), productRequest.getWeight());
                if(productscheck.isEmpty()){
                    String codeP = coverCode.removeVietnameseDiacriticsAndSpaces(product.getProductName());
                    String[] partscd = product.getCreated().toString().split("-"); // Tách chuỗi bằng dấu gạch ngang
                    String partscdkq = partscd[1] + partscd[2]; // Kết hợp hai phần tháng và ngày
                    String code=codeP+partscdkq+product.getId();
                    product.setCode(code.toUpperCase());

                }else {
                    product.setCode(productscheck.get(0).getCode());
                }
                    product.setStatusName(StatusName.PENDING);
                    product.setStorage(storage);
                    product = productRepository.save(product);
                    return productMapper.toResponse(product);

                }
            }
            Product product = productMapper.toEntity(productRequest);
            List<Product> productscheck =   productRepository.findByProductNameAndCreatedAndDueDateAndCategory_IdAndPriceAndWeight(productRequest.getProductName(), productRequest.getCreated(), productRequest.getDueDate(),productRequest.getCategoryId(), productRequest.getPrice(), productRequest.getWeight());
            product=productRepository.save(product);
            if(productscheck.isEmpty()){
                String codeP = coverCode.removeVietnameseDiacriticsAndSpaces(product.getProductName());
                String[] partscd = product.getCreated().toString().split("-"); // Tách chuỗi bằng dấu gạch ngang
                String partscdkq = partscd[1] + partscd[2]; // Kết hợp hai phần tháng và ngày
                String code=codeP+partscdkq+product.getId();
                product.setCode(code.toUpperCase());
            }else {
                product.setCode(productscheck.get(0).getCode());
            }

            if (list.get(0).equals(RoleName.ROLE_ADMIN.toString())) {
                product.setStatusName(StatusName.ACCEPT);
                product.setStorage(storage);
            } else {
                if (!storage.getUsers().getId().equals(iduse)) {
                    throw new MyCustomRuntimeException("bạn không quản lý kho này: " + storage.getStorageName());
                }
                product.setStatusName(StatusName.PENDING);
                product.setStorage(storage);
            }
            product.setStorage(storage);
            product = productRepository.save(product);
            return productMapper.toResponse(product);
        } catch (MyCustomRuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new MyCustomRuntimeException("Lỗi xảy ra khi tạo sản phẩm");
        }

    }

    @Override
    //tìm kiếm tất cả sản phẩm trong kho theo tên
    public List<ProductResponse> findAllByProductNameContainingIgnoreCaseAndStorage_Id(String productName, Long storage_id) {
        List<Product> products = productRepository.findAllByProductNameContainingIgnoreCaseAndStorage_Id(productName, storage_id);
        return products.stream().map(product -> productMapper.toResponse(product)).collect(Collectors.toList());

    }

    @Override
    //tìm tất cả sản phẩm theo kho có phân trang
    public Page<ProductResponse> findAllGeneralSearch(String search, Long storage_id, Pageable pageable) {
        List<Product> productList = productRepository.findAllByStorage_Id(storage_id);
        List<Product> products = new ArrayList<>();

        if (search.isEmpty()) {
            products = productList;
        } else {
            for (Product p : productList) {
                if (p.getProductName().contains(search) || p.getCategory().getCategoryName().contains(search)) {
                    products.add(p);
                }
            }
        }

        int totalProducts = products.size();
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), totalProducts);

        List<ProductResponse> paginatedResponses = products.subList(start, end)
                .stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());

        return new PageImpl<>(paginatedResponses, pageable, totalProducts);
    }

    //tìm all product theo category không phân biệt kho admin
    @Override
    public List<ProductResponse> findAllByCategory_CategoryName(Long categoryId) {
        List<Product> products = productRepository.findAllByCategory_Id(categoryId);
        return products.stream().map(product -> productMapper.toResponse(product)).collect(Collectors.toList());

    }

    //tìm all product theo category theo kho
    @Override
    public List<ProductResponse> findAllByCategory_CategoryNameAndStorage(Long categoryId, Long id) {
        List<Product> products = productRepository.findAllByCategory_IdAndStorage_Id(categoryId, id);
        return products.stream().map(product -> productMapper.toResponse(product)).collect(Collectors.toList());

    }
}