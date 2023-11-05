package ra.service.impl;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ra.dto.request.DeliveryRequest;
import ra.dto.request.DeliveryUpdateRequest;
import ra.dto.request.ProductDeliveryRequest;
import ra.dto.response.BillDetailResponse;
import ra.dto.response.BillResponse;
import ra.dto.response.ShippingReportResponse;
import ra.exception.MyCustomException;
import ra.exception.MyCustomRuntimeException;
import ra.mapper.BillDetailMapper;
import ra.mapper.BillMapper;
import ra.model.*;
import ra.repository.*;
import ra.service.IDeliveryService;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeliveryService implements IDeliveryService {

    @Autowired
    private IBillRepository billRepository;
    @Autowired
    private IShipmentRepository shipmentRepository;
    @Autowired
    private IStorageRepository storageRepository;
    @Autowired
    private IProductRepository productRepository;
    @Autowired
    private IBillDetailRepository billDetailRepository;
    @Autowired
    private BillMapper billMapper;
    @Autowired
    private BillDetailMapper billDetailMapper;
    @Autowired
    private CoverCode coverCode;
    @Override
    //tạo 1 đơn hàng
    @Transactional(rollbackFor = MyCustomRuntimeException.class)
    public ShippingReportResponse createDelivery(DeliveryRequest deliveryRequest, Long useId) throws MyCustomRuntimeException, MyCustomException {
       try {
        Shipment shipment = shipmentRepository.findById(deliveryRequest.getShipmentId()).orElseThrow(() -> new MyCustomRuntimeException("Đơn vị giao hàng không tồn tại"));

        // Tìm kho xuất phát và kho đích từ ID
        Storage startStorage = storageRepository.findById(deliveryRequest.getStartStorageId())
                .orElseThrow(() -> new MyCustomRuntimeException("Điểm xuất phát không tồn tại"));
        Storage endStorage = storageRepository.findById(deliveryRequest.getEndStorageId())
                .orElseThrow(() -> new MyCustomRuntimeException("Điểm đến không tồn tại"));
        //kiểm tra kho đó có thuộc quản lý của nhân viên đó không
        if (!startStorage.getUsers().getId().equals(useId)) {
            throw new MyCustomRuntimeException("bạn không có quyền điều hành kho này");
        }
        List<ProductDeliveryRequest> productRequests = deliveryRequest.getProducts();
        //kiểm tra tồn kho
        for (ProductDeliveryRequest productRequest : productRequests) {
            Product product = productRepository.findById(productRequest.getProductId())
                    .orElseThrow(() -> new MyCustomException("Sản phẩm không tồn tại"));
        }
        // Tạo một phiếu giao hàng mới
        Bill bill = new Bill();



        //Thiết lập thông tin phiếu giao hàng
        bill.setStart(startStorage);
        bill.setEnd(endStorage);
        bill.setCreated(new Date());
        bill.setLocationStart(startStorage.getAddress());
        bill.setLocationEnd(endStorage.getAddress());
        bill.setShipment(shipment);  //thực hiện logic tìm đơn vị vận chuyển theo id và set vào
        bill.setDelivery(DeliveryName.PENDING);
        // Tạo các chi tiết phiếu giao hàng cho từng sản phẩm
        List<BillDetail> billDetails = new ArrayList<>();
        double totalPrice = 0;
        double totalPriceshipment = 0;
        billRepository.save(bill);
        for (ProductDeliveryRequest productRequest : productRequests) {
            Product product = productRepository.findById(productRequest.getProductId())
                    .orElseThrow(() -> new MyCustomException("Sản phẩm không tồn tại"));

            //tổng tiền mua sản phẩm chưa tính ship giá sản phẩm * số lượng logic cho đẩy sản phẩm khác date nhung chỉ cùng chủng loại
            // (giá, trọng lượng nên logic tính ship và giá tiền sẽ ko đổi lấy luôn số lượng gửi lên tính tổng tiền chưa kể ship, và tinh ship riêng nếu đơn hàng tạo thành công)
            totalPrice = totalPrice + product.getPrice() * productRequest.getQuantity();
            //lấy ra sp được chọn tiến hành kiểm tra
           boolean check;
           if(product.getQuantity()>=productRequest.getQuantity()){
               check=true;
           }else {
               check=false;
           }
            if(check){
                BillDetail billDetail = new BillDetail();
                billDetail.setProduct(product);
                billDetail.setQuantity(productRequest.getQuantity());
                billDetail.setBill(bill);
                billDetailRepository.save(billDetail);
                billDetails.add(billDetail);
            }else {
               int productTr= product.getQuantity();
               int productBsQ=productRequest.getQuantity()-productTr;
                BillDetail billDetail = new BillDetail();
                billDetail.setProduct(product);
                billDetail.setQuantity(productTr);
                billDetail.setBill(bill);
                billDetailRepository.save(billDetail);
                billDetails.add(billDetail);
               //kiểm tra có date cao phù hợp không, loại bỏ date thấp hơp date hiện tại vì chỉ bổ sung date lớn hơn date đc chọn
                List<Product> productBsS=productRepository.findAllByStorage_IdAndStatusNameAndCategoryAndQuantityGreaterThanAndProductNameAndPriceAndWeightAndDueDateGreaterThan(product.getStorage().getId(),StatusName.ACCEPT,product.getCategory(),0, product.getProductName(), product.getPrice(),product.getWeight(),product.getDueDate(), Sort.by(Sort.Direction.ASC, "dueDate"));
                if (productBsS.isEmpty()){
                    throw new MyCustomRuntimeException(" số lượng trong kho ko đủ");

                }   else {
                    int total=0;
                    for (Product p:productBsS){
                        total=total+p.getQuantity();
                    }
                    if(total<productBsQ){
                        throw new MyCustomRuntimeException(" số lượng trong kho ko đủ");
                    }
                    for (Product p1:productBsS){
                        if(p1.getQuantity()<productBsQ){
                        int    productBsQ1=productBsQ-p1.getQuantity();
                            BillDetail billDetailBs = new BillDetail();
                            billDetailBs.setProduct(p1);
                            billDetailBs.setQuantity(p1.getQuantity());
                            billDetailBs.setBill(bill);
                          billDetailBs=  billDetailRepository.save(billDetailBs);
                            billDetails.add(billDetailBs);
                            productBsQ=productBsQ1;
                        }else {
                            BillDetail billDetailBs = new BillDetail();
                            billDetailBs.setProduct(p1);
                            billDetailBs.setQuantity(productBsQ);
                            billDetailBs.setBill(bill);
                            billDetailBs=  billDetailRepository.save(billDetailBs);
                            billDetails.add(billDetailBs);
                            break;
                        }

                    }

                }
            }




            //tổng ship bằng giá ship * số lượng * cân nặng sản phẩm
            totalPriceshipment = totalPriceshipment + shipment.getPrice() * productRequest.getQuantity() * product.getWeight();




        }
        bill.setTotal(totalPrice);//tổng gía trị đơn hàng dựa theo sản phẩm
        bill.setPriceShip(totalPriceshipment); //thực hiện logic tính tiền theo cân
        billRepository.save(bill);

        ShippingReportResponse shippingReportResponse = ShippingReportResponse.builder()
                .bill(bill)
                .billDetail(billDetails.stream().map(billDetail -> billDetailMapper.convertBillDetail(billDetail)).collect(Collectors.toList()))
                .delivery(String.valueOf(bill.getDelivery()))
                .userCreate(bill.getStart().getUsers())
                .start(startStorage)
                .end(endStorage)
                .created(String.valueOf(bill.getCreated()))
                .build();
        return shippingReportResponse;
    }catch (MyCustomRuntimeException e) {
        throw e;
    } catch (Exception e) {
        throw new MyCustomRuntimeException("Lỗi xảy ra khi ta đơn");
    }}
    @Override
    //tìm tất cả đơn hàng theo trạng thái vận chuyển thết kế chức năng kết hợp để FE dễ dàng thực hiện chức năng
    public Page<BillResponse> findBillsByDeliveryName(Optional<String> deliveryName, Optional<String> search, Pageable pageable) {
        List<BillResponse> billResponses;
        if (deliveryName.isPresent()) {
            if (!deliveryName.get().equals("ALL")) {
                //nếu trạng thái khác all tìm tất cả các đơn hàng bắt đầu từ kho có địa chỉ được tìm chứa từ khóa tìm kiếm va trạng thái giao hàng
                billResponses = billRepository.findByLocationStartContainingIgnoreCaseAndDelivery(search.get(), DeliveryName.valueOf(deliveryName.get())).stream()
                        .map(item -> billMapper.convert(item))
                        .collect(Collectors.toList());

            } else {
                //nếu trạng thái all tìm tất cả các đơn hàng bắt đầu từ kho có địa chỉ được tìm chứa từ khóa tìm kiếm
                billResponses = billRepository.findAllByLocationStartContainingIgnoreCase(search.get()).stream()
                        .map(item -> billMapper.convert(item))
                        .collect(Collectors.toList());
            }
        } else {
            //FE ko gửi lên trạng thái giao hàng mặc định tìm tất cả bill ko phân biệt trạng thái
            billResponses = billRepository.findAll().stream()
                    .map(item -> billMapper.convert(item))
                    .collect(Collectors.toList());
        }
        billResponses.sort(Comparator.comparing(BillResponse::getCreated).reversed());
        return new PageImpl<>(billResponses, pageable, billResponses.size());
    }
    @Override
//tìm tất cả các BillDetail dựa theo Id bill
    public List<BillDetailResponse> findBillDetailByBillId(Long id) throws MyCustomRuntimeException {
        List<BillDetail> billDetail = billDetailRepository.findAllByBill_Id(id);
        if (billDetail.isEmpty()) {
            throw new MyCustomRuntimeException("không tìm thấy BillDetail");
        }

        return billDetail.stream().map(billDetail1 -> billDetailMapper.convertBillDetail(billDetail1)).collect(Collectors.toList());
    }
    @Override
    //tìm tất cả các bill theo ngày tao
    public Page<BillResponse> findBillsByCreated(Date created, Pageable pageable) {
        Page<Bill> bills = billRepository.findByCreated(created, pageable);

        return bills.map(bill -> billMapper.convert(bill));
    }

    //thay đổi trạng thái đơn hàng chức năng của admin
    @Override
    public BillResponse updateBillStatus(Long id, String deliveryName) throws MyCustomException {
        Bill bill = billRepository.findById(id)
                .orElseThrow(() -> new MyCustomException("không tồn tại bill "));
        DeliveryName deliveryName1;
        try {
            deliveryName1 = DeliveryName.valueOf(deliveryName);
        } catch (IllegalArgumentException e) {
            throw new MyCustomRuntimeException("deliveryName không tồn tại");
        }
        switch (bill.getDelivery()) {
            case CANCEL:
            case PENDING:
            case SUCCESS:
                throw new MyCustomRuntimeException("Đơn hàng này không thể thay đổi trạng thái");
            case PREPARE:
                if (deliveryName1.equals(DeliveryName.DELIVERY)) {
                    bill.setDelivery(deliveryName1);
                } else {
                    throw new MyCustomRuntimeException("Đơn hàng này chỉ có thể lên trạng thái DELIVERY");
                }
                break;
            case DELIVERY:
                if (deliveryName1.equals(DeliveryName.SUCCESS)) {
                    bill.setDelivery(deliveryName1);
                    //thực hiện tăng số lượng trong kho
                    List<BillDetail> billDetails = new ArrayList<>();
                    billDetails = billDetailRepository.findAllByBill_Id(bill.getId());
                    for (BillDetail billDetail : billDetails) {
                        Product productCheck = productRepository.findByProductNameAndCreatedAndDueDateAndStorage_IdAndStatusNameAndCategory_IdAndPriceAndWeight(billDetail.getProduct().getProductName(), billDetail.getProduct().getCreated(), billDetail.getProduct().getDueDate(), bill.getEnd().getId(),StatusName.ACCEPT,billDetail.getProduct().getCategory().getId(),billDetail.getProduct().getPrice(),billDetail.getProduct().getWeight());
                        Product productCheck1 = productRepository.findByProductNameAndCreatedAndDueDateAndStorage_IdAndStatusNameAndCategory_IdAndPriceAndWeight(billDetail.getProduct().getProductName(), billDetail.getProduct().getCreated(), billDetail.getProduct().getDueDate(), bill.getEnd().getId(),StatusName.DELETE,billDetail.getProduct().getCategory().getId(),billDetail.getProduct().getPrice(),billDetail.getProduct().getWeight());
                        if(productCheck!=null){
                            //tồn tại ACCEPT thì mă định tăng số lượng
                            productCheck.setQuantity(productCheck.getQuantity() + billDetail.getQuantity());
                            productCheck.setStatusName(StatusName.ACCEPT);
                            productRepository.save(productCheck);

                        }else if(productCheck1!=null) {
                            //nếu rơi vào ko tồn tại ACCEPT mà delete thì thay trạng thái delete và tang số lượng
                            productCheck1.setQuantity(productCheck1.getQuantity() + billDetail.getQuantity());
                            productCheck1.setStatusName(StatusName.ACCEPT);
                            productRepository.save(productCheck1);
                        }
                        else {
                            //chưa có tạo mới
                            Product product1 = Product.builder()
                                    .quantity(billDetail.getQuantity())
                                    .weight(billDetail.getProduct().getWeight())
                                    .price(billDetail.getProduct().getPrice())
                                    .productName(billDetail.getProduct().getProductName())
                                    .dueDate(billDetail.getProduct().getDueDate())
                                    .storage(storageRepository.findById(bill.getEnd().getId()).orElseThrow(() -> new MyCustomRuntimeException("không tồn tại kho này")))
                                    .category(billDetail.getProduct().getCategory())
                                    .created(billDetail.getProduct().getCreated())
                                    .statusName(billDetail.getProduct().getStatusName())
                                    .build();
                            product1.setStatusName(StatusName.ACCEPT);
                            List<Product> productsCheck=productRepository.findByProductNameAndCreatedAndDueDateAndCategory_IdAndPriceAndWeight(billDetail.getProduct().getProductName(), billDetail.getProduct().getCreated(), billDetail.getProduct().getDueDate(),billDetail.getProduct().getCategory().getId(),billDetail.getProduct().getPrice(),billDetail.getProduct().getWeight());
                            product1=productRepository.save(product1);
                            if(productsCheck.isEmpty()){
                                String codeP = coverCode.removeVietnameseDiacriticsAndSpaces(product1.getProductName());
                                String[] partscd = product1.getCreated().toString().split("-"); // Tách chuỗi bằng dấu gạch ngang
                                String partscdkq = partscd[1] + partscd[2]; // Kết hợp hai phần tháng và ngày
                                String code=codeP+partscdkq+product1.getId();
                                product1.setCode(code.toUpperCase());
                            }
                            else {
                                product1.setCode(productsCheck.get(0).getCode());
                            }
                            productRepository.save(product1);
                        }

                    }
                    bill.setDelivery(DeliveryName.SUCCESS);
                    bill = billRepository.save(bill);
                    return billMapper.convert(bill);
                } else {
                    throw new MyCustomRuntimeException("Đơn hàng này chỉ có thể lên trạng thái SUCCESS");
                }

            default:
                throw new MyCustomRuntimeException("Đơn hàng này không thể thay đổi trạng thái");
        }
        bill = billRepository.save(bill);
        return billMapper.convert(bill);
    }
    // không chấp yêu cầu chuyển hàng chức năng của admin
    @Override
    public BillResponse rejectBill(Long billId) throws MyCustomException {

        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new MyCustomException("bill không tồn tại"));

        // Kiểm tra xem bill có ở trạng thái pending không nếu ko thì chuyển sang hủy có thì xóa khỏi data kết hợp chức năng
        if (!bill.getDelivery().equals(DeliveryName.PENDING)) {
            //thực hiện tăng số lượng trong kho
            //      kiểm tra tồn kho
            List<BillDetail> billDetails = new ArrayList<>();
            billDetails = billDetailRepository.findAllByBill_Id(bill.getId());
            for (BillDetail billDetail : billDetails) {
                //nếu mà tồn tại sản phẩm trùng tên, ngày sản xuất, ngày hết hạn,trong kho thì xét tới 2 trường hợp
                //nếu mà nó ở trạng thái ACCEPT thì cộng thêm số lượng vào kho
                //nếu nó ở trạng thái delete tạm khóa thì tiến hành mở khóa sang ACCEPT và cộng vào kho
                Product productCheck = productRepository.findByProductNameAndCreatedAndDueDateAndStorage_IdAndStatusNameAndCategory_IdAndPriceAndWeight(billDetail.getProduct().getProductName(), billDetail.getProduct().getCreated(), billDetail.getProduct().getDueDate(), bill.getStart().getId(),StatusName.ACCEPT,billDetail.getProduct().getCategory().getId(),billDetail.getProduct().getPrice(),billDetail.getProduct().getWeight());
                Product productCheck1 = productRepository.findByProductNameAndCreatedAndDueDateAndStorage_IdAndStatusNameAndCategory_IdAndPriceAndWeight(billDetail.getProduct().getProductName(), billDetail.getProduct().getCreated(), billDetail.getProduct().getDueDate(), bill.getStart().getId(),StatusName.DELETE,billDetail.getProduct().getCategory().getId(),billDetail.getProduct().getPrice(),billDetail.getProduct().getWeight());
                if(productCheck!=null){
                    //tồn tại ACCEPT thì mặc định tăng số lượng
                    productCheck.setQuantity(productCheck.getQuantity() + billDetail.getQuantity());
                    productCheck.setStatusName(StatusName.ACCEPT);
                    productRepository.save(productCheck);

                }else if(productCheck1!=null) {
                    //nếu rơi vào ko tồn tại ACCEPT mà delete thì thay trạng thái delete và tang số lượng
                    productCheck1.setQuantity(productCheck1.getQuantity() + billDetail.getQuantity());
                    productCheck1.setStatusName(StatusName.ACCEPT);
                    productRepository.save(productCheck1);
                }
                else {
                    //chưa có tạo mới
                    Product product1 = Product.builder()
                            .quantity(billDetail.getQuantity())
                            .weight(billDetail.getProduct().getWeight())
                            .price(billDetail.getProduct().getPrice())
                            .productName(billDetail.getProduct().getProductName())
                            .dueDate(billDetail.getProduct().getDueDate())
                            .storage(storageRepository.findById(bill.getEnd().getId()).orElseThrow(() -> new MyCustomRuntimeException("không tồn tại kho này")))
                            .category(billDetail.getProduct().getCategory())
                            .created(billDetail.getProduct().getCreated())
                            .statusName(billDetail.getProduct().getStatusName())
                            .build();
                    product1.setStatusName(StatusName.ACCEPT);
                    List<Product> productsCheck=productRepository.findByProductNameAndCreatedAndDueDateAndCategory_IdAndPriceAndWeight(billDetail.getProduct().getProductName(), billDetail.getProduct().getCreated(), billDetail.getProduct().getDueDate(),billDetail.getProduct().getCategory().getId(),billDetail.getProduct().getPrice(),billDetail.getProduct().getWeight());
                    product1=productRepository.save(product1);
                    if(productsCheck.isEmpty()){
                        String codeP = coverCode.removeVietnameseDiacriticsAndSpaces(product1.getProductName());
                        String[] partscd = product1.getCreated().toString().split("-"); // Tách chuỗi bằng dấu gạch ngang
                        String partscdkq = partscd[1] + partscd[2]; // Kết hợp hai phần tháng và ngày
                        String code=codeP+partscdkq+product1.getId();
                        product1.setCode(code.toUpperCase());
                    }
                    else {
                        product1.setCode(productsCheck.get(0).getCode());
                    }
                    productRepository.save(product1);
                }

            }
            bill.setDelivery(DeliveryName.CANCEL);
            return billMapper.convert(billRepository.save(bill));
        }
        billDetailRepository.deleteAll(billDetailRepository.findAllByBill_Id(billId));
        BillResponse billResponse = billMapper.convert(bill);
        billRepository.delete(bill);
        return billResponse;
    }

    @Override
    //chấp nhận yêu cầu chuyển hàng chức năng của admin
    public BillResponse approveBill(Long billId) throws MyCustomException {

        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new MyCustomException("bill không tồn tại"));

        // Kiểm tra xem bill có ở trạng thái pending không
        if (!bill.getDelivery().equals(DeliveryName.PENDING)) {
            throw new MyCustomException("bill không ở trạng thái pending");
        }
        //thực hiện tăng giảm số lượng trong kho
        //      kiểm tra tồn kho
        List<BillDetail> billDetails = new ArrayList<>();
        billDetails = billDetailRepository.findAllByBill_Id(billId);
        //nếu số lượng sản phẩm ko đủ lập tức hủy đơn yêu cầu
        for (BillDetail billDetail : billDetails) {
            Product product = productRepository.findById(billDetail.getProduct().getId())
                    .orElseThrow(() -> new MyCustomException("Sản phẩm không tồn tại"));
            if (billDetail.getQuantity() > product.getQuantity()) {
//                bill.setDelivery(DeliveryName.CANCEL);
//                billRepository.save(bill);
                throw new MyCustomRuntimeException("tồn kho của sản phẩm " + product.getProductName() + " này không đủ ");
            }
        }
        //nếu số lượng sản phẩm bị trừ về 0 chuyển trạng thái delete để Fe ẩn khỏi giao diện
        for (BillDetail billDetail : billDetails) {
            Product product = productRepository.findById(billDetail.getProduct().getId())
                    .orElseThrow(() -> new MyCustomException("Sản phẩm không tồn tại"));
            product.setQuantity(product.getQuantity() - billDetail.getQuantity());
            if (product.getQuantity() == 0) {
                product.setStatusName(StatusName.DELETE);
            }
            productRepository.save(product);
        }
        bill.setDelivery(DeliveryName.PREPARE);
        bill = billRepository.save(bill);
        return billMapper.convert(bill);
    }

    @Override
    //in pdf phiếu nhập, xuất, hóa đơn xuất hàng
    public byte[] findByIdBillReport(Long billId, int id) throws MyCustomRuntimeException, DocumentException, IOException {
        // Tìm Bill
        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new MyCustomRuntimeException("Không tìm thấy bill"));

        // Lấy danh sách chi tiết hóa đơn bằng billId
        List<BillDetail> billDetails = billDetailRepository.findAllByBill_Id(billId);

        // Tạo một tài liệu PDF và cấu hình kích thước
        Document document = new Document(PageSize.A3.rotate());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, baos);
        // Đặt tiêu đề của tài liệu
        switch (id) {
            case 1:
                document.addTitle("Phiếu Nhập Kho: " + bill.getLocationEnd());
                break;
            case 2:
                document.addTitle("Phiếu Xuất Kho: " + bill.getLocationStart());
                break;
            case 3:
                document.addTitle("Hóa Đơn Xuất Hàng: " + bill.getLocationStart());
                break;
        }

        document.open();
        BaseFont baseFont = BaseFont.createFont("C:\\Users\\Hai Computer\\Desktop\\vntime\\arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        // Tạo và thêm tiêu đề
        Font titleFont = new Font(baseFont, 18, Font.BOLD, new BaseColor(203, 30, 11));
        Paragraph title;
        switch (id) {
            case 1:
                title = new Paragraph("Thông Tin Phiếu Nhập Kho", titleFont);
                title.setAlignment(Element.ALIGN_CENTER);
                document.add(title);
                break;
            case 2:
                title = new Paragraph("Thông Tin Phiếu Xuất Kho", titleFont);
                title.setAlignment(Element.ALIGN_CENTER);
                document.add(title);
                break;
            case 3:
                title = new Paragraph("Thông Tin Hóa Đơn", titleFont);
                title.setAlignment(Element.ALIGN_CENTER);
                document.add(title);
                break;
        }

        // Thêm dòng trắng
        document.add(new Paragraph(" "));

        int numColumns1 = 8;
        PdfPTable table1 = new PdfPTable(numColumns1);
        float[] columnWidths1 = {0.5f, 2f, 2f, 3f, 3f, 3f, 2.5f, 2.5f};
        table1.setWidths(columnWidths1);

        // Định dạng font chữ và màu chữ
        Font labelFont = new Font(baseFont, 12, Font.BOLD, BaseColor.DARK_GRAY);
        Font valueFont = new Font(baseFont, 12, Font.NORMAL, BaseColor.BLACK);

        // Thêm các cột vào bảng
        table1.addCell(createCell("ID", labelFont));
        table1.addCell(createCell("Trạng thái", labelFont));
        table1.addCell(createCell("Ngày tạo", labelFont));
        table1.addCell(createCell("Kho xuất phát", labelFont));
        table1.addCell(createCell("Kho tiếp nhận", labelFont));
        table1.addCell(createCell("Đơn vị vận chuyển", labelFont));
        table1.addCell(createCell("Phí vận chuyển", labelFont));
        table1.addCell(createCell("Tổng tiền", labelFont));
// Định nghĩa một DecimalFormat với ký tự ngăn cách hàng nghìn và đơn vị tiền tệ là "vnd"
        DecimalFormat df = new DecimalFormat("#,##0 VND", new DecimalFormatSymbols(new Locale("vi", "VN")));

// Định dạng phí vận chuyển và tổng tiền
        String formattedPriceShip = df.format(bill.getPriceShip());
        String formattedTotal = df.format(bill.getPriceShip() + bill.getTotal());
        // Thêm giá trị vào các cột trong bảng
        table1.addCell(createCell(String.valueOf(billId), valueFont));
        table1.addCell(createCell(String.valueOf(bill.getDelivery()), valueFont));
        table1.addCell(createCell(String.valueOf(bill.getCreated()), valueFont));
        table1.addCell(createCell(bill.getLocationStart(), valueFont));
        table1.addCell(createCell(bill.getLocationEnd(), valueFont));
        table1.addCell(createCell(bill.getShipment().getShipName(), valueFont));
        table1.addCell(createCell(formattedPriceShip, valueFont));
        table1.addCell(createCell(formattedTotal, valueFont));

        // Thêm bảng vào tài liệu
        document.add(table1);

        // Thêm dòng trắng và tiêu đề cho bảng chi tiết
        document.add(new Paragraph(" "));
        Paragraph billDetailsTitle = new Paragraph("Thông Tin Chi Tiết", titleFont);
        billDetailsTitle.setAlignment(Element.ALIGN_CENTER);
        document.add(billDetailsTitle);
        document.add(new Paragraph(" "));

        // Tạo bảng chi tiết
        int numColumns = 5;
        PdfPTable table = new PdfPTable(numColumns);
        table.setWidthPercentage(40);
        float[] columnWidths = {1.5f, 1f, 1f, 1f, 0.7f};
        table.setWidths(columnWidths);

        // Định dạng font và màu chữ
        Font tableHeaderFont = new Font(baseFont, 12, Font.BOLD, BaseColor.DARK_GRAY);
        Font tableBodyFont = new Font(baseFont, 12, Font.NORMAL, BaseColor.BLACK);

        // Thêm các cột vào bảng
        table.addCell(createCell("Mã sản phẩm", tableHeaderFont));
        table.addCell(createCell("Sản phẩm", tableHeaderFont));
        table.addCell(createCell("Ngày sản xuất", tableHeaderFont));
        table.addCell(createCell("Ngày hết hạn", tableHeaderFont));
        table.addCell(createCell("Số lượng", tableHeaderFont));

        // Thêm dữ liệu chi tiết vào các cột trong bảng
        for (BillDetail billDetailResponse : billDetails) {
            table.addCell(createCell(String.valueOf(billDetailResponse.getProduct().getCode()), tableBodyFont));
            table.addCell(createCell(billDetailResponse.getProduct().getProductName(), tableBodyFont));
            table.addCell(createCell(String.valueOf(billDetailResponse.getProduct().getCreated()), tableBodyFont));
            table.addCell(createCell(String.valueOf(billDetailResponse.getProduct().getDueDate()), tableBodyFont));
            table.addCell(createCell(String.valueOf(billDetailResponse.getQuantity()), tableBodyFont));
        }

        // Thêm bảng vào tài liệu
        document.add(table);

        // Đóng tài liệu và trả về dữ liệu PDF dưới dạng mảng byte
        document.close();
        return baos.toByteArray();
    }

    // Hàm hỗ trợ để tạo ô cell trong bảng
    private PdfPCell createCell(String text, Font font) {
        return new PdfPCell(new Phrase(text, font));
    }

    @Override
// lấy tất cả các bil nhập theo id kho và ngày tạo( hỗ trợ chức năng in phiếu nhập)
    public Page<BillResponse> findByCreatedAndStorageAndImport(Date date, Long id, Pageable pageable) {
        Page<Bill> bills = billRepository.findByCreatedAndStorageAndImport(date, id, pageable);
        return bills.map(bill -> billMapper.convert(bill));
    }

    @Override
    // lấy tất cả các bil xuất theo id kho và ngày tạo( hỗ trợ chức năng in xuất nhập)
    public Page<BillResponse> findByCreatedAndStorageAndExport(Date date, Long id, Pageable pageable) {
        Page<Bill> bills = billRepository.findByCreatedAndStorageAndExport(date, id, pageable);
        return bills.map(bill -> billMapper.convert(bill));

    }

    @Override
    //tìm tất cả các bill
    public Page<BillResponse> findBills(Pageable pageable) {
        Page<Bill> bills;
        bills = billRepository.findAll(pageable);
        return bills.map(bill -> billMapper.convert(bill));
    }

    @Override
    //tìm kiếm bill theo id hiện thông tin chi tiết của bill bao gồm billDetail
    public ShippingReportResponse findByBillId(Long id) {
        Bill bill = billRepository.findById(id).orElseThrow(() -> new MyCustomRuntimeException("Không tìm thấy bill"));
        List<BillDetail> billDetails = billDetailRepository.findAllByBill_Id(id);
        ShippingReportResponse shippingReportResponse = ShippingReportResponse.builder()
                .bill(bill)
                .billDetail(billDetails.stream().map(billDetail -> billDetailMapper.convertBillDetail(billDetail)).collect(Collectors.toList()))
                .delivery(String.valueOf(bill.getDelivery()))
                .userCreate(bill.getStart().getUsers())
                .start(bill.getStart())
                .end(bill.getEnd())
                .created(String.valueOf(bill.getCreated()))
                .build();
        return shippingReportResponse;
    }

    @Override
    //cập nhật thông tin đơn giao hàng nhớ tính lại tổng tiền sản phẩm ko gồm ship, và tổng phí ship
    public ShippingReportResponse updateDelivery(DeliveryUpdateRequest deliveryRequest) throws MyCustomException {
        Bill bill = billRepository.findById(deliveryRequest.getBillId()).orElseThrow(() -> new MyCustomRuntimeException("không tìm thấy bill"));
        BillDetail billDetail = billDetailRepository.findById(deliveryRequest.getBillDetailId()).orElseThrow(() -> new MyCustomRuntimeException("không tìm thấy billDetail"));
        Product product = productRepository.findById(deliveryRequest.getProduct().getProductId()).orElseThrow(() -> new MyCustomRuntimeException("không tìm thấy product"));
        if (!bill.getDelivery().equals(DeliveryName.PENDING)) {
            throw new MyCustomException("bill không ở trạng thái pending");
        }

        if (deliveryRequest.getShipmentId() != null) {
            Shipment shipment = shipmentRepository.findById(deliveryRequest.getShipmentId())
                    .orElseThrow(() -> new MyCustomRuntimeException("không tìm thấy shipment"));
            bill.setShipment(shipment);
        }
        billDetail.setProduct(product);
        billDetail.setBill(bill);
        billDetail.setQuantity(deliveryRequest.getProduct().getQuantity());

        billDetailRepository.save(billDetail);

        List<BillDetail> billDetails = billDetailRepository.findAllByBill_Id(deliveryRequest.getBillId());
        double totalPrice = 0;
        double totalPriceshipment = 0;
        for (BillDetail billDetail1 : billDetails) {

            totalPrice = totalPrice + billDetail1.getQuantity() * billDetail1.getProduct().getPrice();
            totalPriceshipment = totalPriceshipment + bill.getShipment().getPrice() * billDetail1.getQuantity();
        }
        bill.setTotal(totalPrice);//tổng gía trị đơn hàng dựa theo sản phẩm
        bill.setPriceShip(totalPriceshipment); //thực hiện logic tính tiền theo cân
        billRepository.save(bill);

        ShippingReportResponse shippingReportResponse = ShippingReportResponse.builder()
                .bill(bill)
                .billDetail(billDetails.stream().map(billDetail2 -> billDetailMapper.convertBillDetail(billDetail2)).collect(Collectors.toList()))
                .delivery(String.valueOf(bill.getDelivery()))
                .userCreate(bill.getStart().getUsers())
                .start(bill.getStart())
                .end(bill.getEnd())
                .created(String.valueOf(bill.getCreated()))
                .build();
        return shippingReportResponse;
    }

    @Override
    //thay đổi trạng thái đơn hàng sang DELIVERY
    public BillResponse deliveryBill(Long id) throws MyCustomRuntimeException {
        Bill bill = billRepository.findById(id).orElseThrow(() -> new MyCustomRuntimeException("Không tìm thấy bill"));
        bill.setDelivery(DeliveryName.DELIVERY);
        bill = billRepository.save(bill);
        return billMapper.convert(bill);
    }

    @Override
    //thay đổi trạng thái đơn hàng sang SUCCESS, nhớ thực hiện logic tăng so lượng hàng trong kho nếu đã tồn tại
    //sp này trong kho thì tăng số lượng, chưa có thì tạo sản phẩm cho kho và tăng số lượng dựa theo phiếu nhập
    public BillResponse successBill(Long id) throws MyCustomRuntimeException {
        Bill bill = billRepository.findById(id).orElseThrow(() -> new MyCustomRuntimeException("Không tìm thấy bill"));
        //thực hiện tăng số lượng trong kho
        //      kiểm tra tồn kho
        List<BillDetail> billDetails = new ArrayList<>();
        billDetails = billDetailRepository.findAllByBill_Id(bill.getId());
        for (BillDetail billDetail : billDetails) {
            //nếu mà tồn tại sản phẩm trùng tên, ngày sản xuất, ngày hết hạn,trong kho thì xét tới 2 trường hợp
            //nếu mà nó ở trạng thái ACCEPT thì cộng thêm số lượng vào kho
            //nếu nó ở trạng thái delete tạm khóa thì tiến hành mở khóa sang ACCEPT và cộng vào kho
            Product productCheck = productRepository.findByProductNameAndCreatedAndDueDateAndStorage_IdAndStatusNameAndCategory_IdAndPriceAndWeight(billDetail.getProduct().getProductName(), billDetail.getProduct().getCreated(), billDetail.getProduct().getDueDate(), bill.getEnd().getId(),StatusName.ACCEPT,billDetail.getProduct().getCategory().getId(),billDetail.getProduct().getPrice(),billDetail.getProduct().getWeight());
            Product productCheck1 = productRepository.findByProductNameAndCreatedAndDueDateAndStorage_IdAndStatusNameAndCategory_IdAndPriceAndWeight(billDetail.getProduct().getProductName(), billDetail.getProduct().getCreated(), billDetail.getProduct().getDueDate(), bill.getEnd().getId(),StatusName.DELETE,billDetail.getProduct().getCategory().getId(),billDetail.getProduct().getPrice(),billDetail.getProduct().getWeight());
            if(productCheck!=null){
                //tồn tại ACCEPT thì mặc định tăng số lượng
                productCheck.setQuantity(productCheck.getQuantity() + billDetail.getQuantity());
                productCheck.setStatusName(StatusName.ACCEPT);
                productRepository.save(productCheck);

            }else if(productCheck1!=null) {
                //nếu rơi vào ko tồn tại ACCEPT mà delete thì thay trạng thái delete và tang số lượng
                productCheck1.setQuantity(productCheck1.getQuantity() + billDetail.getQuantity());
                productCheck1.setStatusName(StatusName.ACCEPT);
                 productRepository.save(productCheck1);
            }
           else {
                //chưa có tạo mới
                Product product1 = Product.builder()
                        .quantity(billDetail.getQuantity())
                        .weight(billDetail.getProduct().getWeight())
                        .price(billDetail.getProduct().getPrice())
                        .productName(billDetail.getProduct().getProductName())
                        .dueDate(billDetail.getProduct().getDueDate())
                        .storage(storageRepository.findById(bill.getEnd().getId()).orElseThrow(() -> new MyCustomRuntimeException("không tồn tại kho này")))
                        .category(billDetail.getProduct().getCategory())
                        .created(billDetail.getProduct().getCreated())
                        .statusName(billDetail.getProduct().getStatusName())
                        .build();
                product1.setStatusName(StatusName.ACCEPT);
                List<Product> productsCheck=productRepository.findByProductNameAndCreatedAndDueDateAndCategory_IdAndPriceAndWeight(billDetail.getProduct().getProductName(), billDetail.getProduct().getCreated(), billDetail.getProduct().getDueDate(),billDetail.getProduct().getCategory().getId(),billDetail.getProduct().getPrice(),billDetail.getProduct().getWeight());
             product1=productRepository.save(product1);
               if(productsCheck.isEmpty()){
                   String codeP = coverCode.removeVietnameseDiacriticsAndSpaces(product1.getProductName());
                   String[] partscd = product1.getCreated().toString().split("-"); // Tách chuỗi bằng dấu gạch ngang
                   String partscdkq = partscd[1] + partscd[2]; // Kết hợp hai phần tháng và ngày
                   String code=codeP+partscdkq+product1.getId();
                   product1.setCode(code.toUpperCase());
               }
               else {
                   product1.setCode(productsCheck.get(0).getCode());
               }
                productRepository.save(product1);
            }

        }
        bill.setDelivery(DeliveryName.SUCCESS);
        bill = billRepository.save(bill);
        return billMapper.convert(bill);
    }

    @Override
    //cập nhật thông tin đơn giao hàng nhớ tính lại tổng tiền sản phẩm ko gồm ship, và tổng phí ship và tra về thông tin đầy đủ  bao gồm BillDetail
    @Transactional(rollbackFor = MyCustomRuntimeException.class)
    public ShippingReportResponse updateDeliveryAll(DeliveryRequest deliveryRequest, Long billId) throws MyCustomRuntimeException {
        try {


            Shipment shipment = shipmentRepository.findById(deliveryRequest.getShipmentId()).orElseThrow(() -> new MyCustomRuntimeException("Đơn vị giao hàng không tồn tại"));
            List<ProductDeliveryRequest> productRequests = deliveryRequest.getProducts();
//      kiểm tra tồn kho
            for (ProductDeliveryRequest productRequest : productRequests) {
                Product product = productRepository.findById(productRequest.getProductId())
                        .orElseThrow(() -> new MyCustomException("Sản phẩm không tồn tại"));

                if (product.getQuantity() < productRequest.getQuantity()) {
                    throw new MyCustomRuntimeException("tồn kho của sản phẩm " + product.getProductName() + " này không đủ");
                }
            }
            Bill bill = billRepository.findById(billId).orElseThrow(() -> new MyCustomRuntimeException("không tìm thấy bill"));
            bill.setShipment(shipment);  //thực hiện logic tìm đơn vị vận chuyển theo id và set vào
            List<BillDetail> billDetails = new ArrayList<>();
            double totalPrice = 0;
            double totalPriceshipment = 0;
            billDetailRepository.deleteAll(billDetailRepository.findAllByBill_Id(billId));
            billRepository.save(bill);
            for (ProductDeliveryRequest productRequest : productRequests) {
                Product product = productRepository.findById(productRequest.getProductId())
                        .orElseThrow(() -> new MyCustomException("Sản phẩm không tồn tại"));

                totalPrice = totalPrice + product.getPrice() * productRequest.getQuantity();
                BillDetail billDetail = new BillDetail();
                billDetail.setProduct(product);
                billDetail.setQuantity(productRequest.getQuantity());
                billDetail.setBill(bill);
                billDetailRepository.save(billDetail);
                billDetails.add(billDetail);
                totalPriceshipment = totalPriceshipment + shipment.getPrice() * productRequest.getQuantity();
            }
            bill.setTotal(totalPrice);//tổng gía trị đơn hàng dựa theo sản phẩm
            bill.setPriceShip(totalPriceshipment); //thực hiện logic tính tiền theo cân
            billRepository.save(bill);

            ShippingReportResponse shippingReportResponse = ShippingReportResponse.builder()
                    .bill(bill)
                    .billDetail(billDetails.stream().map(billDetail -> billDetailMapper.convertBillDetail(billDetail)).collect(Collectors.toList()))
                    .delivery(String.valueOf(bill.getDelivery()))
                    .userCreate(bill.getStart().getUsers())
                    .start(bill.getStart())
                    .end(bill.getEnd())
                    .created(String.valueOf(bill.getCreated()))
                    .build();
            return shippingReportResponse;
        } catch (MyCustomRuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new MyCustomRuntimeException("Lỗi xảy ra khi lưu báo cáo tồn kho");
        }
    }

    @Override
// lấy tất cả các bil nhập theo id kho( hỗ trợ chức năng in phiếu nhập)
    public Page<BillResponse> findByStorageAndImport(Long id, String statusName, Pageable pageable, String search) {
        Page<Bill> bills;
        List<BillResponse> billResponses;
        if (search.isEmpty()) {
            //nếu từ khóa tìm kiếm trống (từ khóa hỗ trợ tìm kiếm kho xuất)
            if (statusName.equals("ALL")) {
                //nếu statusName là all thì tìm kiếm tất cả các phiếu nhập theo kho
                bills = billRepository.findAllByEnd_Id(id, pageable);
                return bills.map(bill -> billMapper.convert(bill));
            } else {
                //nếu statusName ko phải all thì tìm kiếm tất cả các phiếu nhập theo kho kèm trạng thái
                billResponses = billRepository.findAllByEnd_IdAndDelivery(id, DeliveryName.valueOf(statusName), pageable).stream()
                        .map(item -> billMapper.convert(item))
                        .collect(Collectors.toList());


            }
        } else {
            //nếu tồn tại từ khóa tìm kiếm
            if (statusName.equals("ALL")) {
                //hỗ trợ tìm kiếm tất cả phiếu nhập theo kho kèm địa chỉ kho xuất chứa từ khóa
                bills = billRepository.findAllByEndIdAndLocationStartContainingIgnoreCase(id, search, pageable);
                return bills.map(bill -> billMapper.convert(bill));
            } else {
                //hỗ trợ tìm tất cả phiếu nhập theo kho và có trạng thái kèm địa chỉ kho xuất chứa từ khóa
                billResponses = billRepository.findAllByEndIdAndDeliveryAndLocationStartContainingIgnoreCase(id, DeliveryName.valueOf(statusName), search, pageable).stream()
                        .map(item -> billMapper.convert(item))
                        .collect(Collectors.toList());
            }


        }
        billResponses.sort(Comparator.comparing(BillResponse::getCreated).reversed());
        return new PageImpl<>(billResponses, pageable, billResponses.size());
    }

    @Override
    // lấy tất cả các bil xuất theo id kho( hỗ trợ chức năng in xuất)
    public Page<BillResponse> findByStorageAndExport(Long id, String statusName, Pageable pageable, String search) {
        Page<Bill> bills;
        List<BillResponse> billResponses;
        if (search.isEmpty()) {
            //nếu từ khóa tìm kiếm trống (từ khóa hỗ trợ tìm kiếm kho nhập)
            if (statusName.equals("ALL")) {
                //nếu statusName là all thì tìm kiếm tất cả các phiếu xuất theo kho
                bills = billRepository.findAllByStart_Id(id, pageable);
                return bills.map(bill -> billMapper.convert(bill));
            } else {
                //nếu statusName ko phải all thì tìm kiếm tất cả các phiếu xuất theo kho kèm trạng thái
                billResponses = billRepository.findAllByStart_IdAndDelivery(id, DeliveryName.valueOf(statusName), pageable).stream()
                        .map(item -> billMapper.convert(item))
                        .collect(Collectors.toList());


            }
        } else {
            //nếu tồn tại từ khóa tìm kiếm
            if (statusName.equals("ALL")) {
                //hỗ trợ tìm kiếm tất cả phiếu xuất theo kho kèm địa chỉ kho đích chứa từ khóa
                bills = billRepository.findAllByStartIdAndLocationEndContainingIgnoreCase(id, search, pageable);
                return bills.map(bill -> billMapper.convert(bill));
            } else {
                //hỗ trợ tìm tất cả phiếu xuát theo kho và có trạng thái kèm địa chỉ kho đích chứa từ khóa
                billResponses = billRepository.findAllByStartIdAndDeliveryAndLocationEndContainingIgnoreCase(id, DeliveryName.valueOf(statusName), search, pageable).stream()
                        .map(item -> billMapper.convert(item))
                        .collect(Collectors.toList());
            }


        }
        billResponses.sort(Comparator.comparing(BillResponse::getCreated).reversed());
        return new PageImpl<>(billResponses, pageable, billResponses.size());
    }
}
