package ra.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.itextpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ra.dto.request.DeliveryRequest;
import ra.dto.request.DeliveryUpdateRequest;
import ra.dto.response.BillDetailResponse;
import ra.dto.response.BillResponse;
import ra.dto.response.ShippingReportResponse;
import ra.dto.response.ZoneResponse;
import ra.exception.MyCustomException;
import ra.exception.MyCustomRuntimeException;
import ra.security.user_principal.UserPrinciple;
import ra.service.IDeliveryService;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/shipping-report")
@CrossOrigin("*")
public class DeliveryController {
    @Autowired
    private IDeliveryService deliveryService;

    //tạo mới 1 đơn giao hàng
    @PostMapping("/create")
    public ResponseEntity<ShippingReportResponse> createDelivery(Authentication authentication,
                                                                 @RequestBody @Valid DeliveryRequest deliveryRequest
    ) throws MyCustomException {
        UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
        Long userId = userPrinciple.getId();
        ShippingReportResponse shippingReportResponse = deliveryService.createDelivery(deliveryRequest, userId);
        return new ResponseEntity<>(shippingReportResponse, HttpStatus.OK);
    }

    //lấy tất cả các đơn theo tình trạng vận chuyển
    @GetMapping("/all-by-status")
    public ResponseEntity<Page<BillResponse>> getAllBillByDeliveryName(
            @RequestParam(defaultValue = "ALL") Optional<String> deliveryName,
            @RequestParam(defaultValue = "") Optional<String> search,
            @PageableDefault(page = 0, size = 100, sort = "created", direction = Sort.Direction.DESC) Pageable pageable) {
        return new ResponseEntity<>(deliveryService.findBillsByDeliveryName(deliveryName,search, pageable), HttpStatus.OK);
    }

    @GetMapping("/{billId}/bill-detail")
    //lấy tất cả BillDetail theo bill id
    public ResponseEntity<List<BillDetailResponse>> findBillDetailByBillId(@PathVariable Long billId) {
        List<BillDetailResponse> billDetail = deliveryService.findBillDetailByBillId(billId);
        return new ResponseEntity<>(billDetail, HttpStatus.OK);
    }

    //lấy tất cả các đơn theo ngày tạo
    @GetMapping("/all-by-created")
    public ResponseEntity<Page<BillResponse>> getAllBillByCreated(
            @RequestParam(name = "created")     @JsonFormat(pattern = "dd/MM/yyyy") Date created,
            @PageableDefault(page = 0, size = 100, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(deliveryService.findBillsByCreated(created, pageable), HttpStatus.OK);
    }
    @GetMapping("/all")
    //lấy tất cả các bill có hỗ tợ phân trang
    public ResponseEntity<Page<BillResponse>> getAllBill(
            @PageableDefault(page = 0, size = 100, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(deliveryService.findBills( pageable), HttpStatus.OK);
    }
    //thay đổi trạng thái đơn hàng
    @PutMapping("/updateBillStatus/{id}")
    public ResponseEntity<BillResponse> updateBillStatus(@PathVariable Long id, @RequestParam String deliveryName) throws MyCustomException {

        BillResponse billResponse = deliveryService.updateBillStatus(id, deliveryName);
        return new ResponseEntity<>(billResponse, HttpStatus.OK);


    }

    //từ chối hóa đơn
    @PutMapping("/rejectBill/{billId}")
    public ResponseEntity<BillResponse> rejectBill(@PathVariable Long billId) throws MyCustomException {
        BillResponse billResponse = deliveryService.rejectBill(billId);
        return new ResponseEntity<>(billResponse, HttpStatus.OK);
    }

    //chấp nhận hóa đơn
    @PutMapping("/approveBill/{billId}")
    public ResponseEntity<BillResponse> approveBill(@PathVariable Long billId) throws MyCustomException {

        BillResponse billResponse = deliveryService.approveBill(billId);
        return new ResponseEntity<>(billResponse, HttpStatus.OK);

    }

    //in hóa đơn dưới dạng pdf
    //in phiếu nhập
    @GetMapping(value = "/{billId}/import", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> generateBillImport(@PathVariable Long billId) {
        return generateBillReport(billId, 1);
    }

    @GetMapping(value = "/{billId}/export", produces = MediaType.APPLICATION_PDF_VALUE)
    //in phiếu xuât
    public ResponseEntity<byte[]> generateBillExport(@PathVariable Long billId) {
        return generateBillReport(billId, 2);
    }

    @GetMapping(value = "/{billId}/bill", produces = MediaType.APPLICATION_PDF_VALUE)
    //in hóa đơn
    public ResponseEntity<byte[]> generateBill(@PathVariable Long billId) {
        return generateBillReport(billId, 3);
    }

    private ResponseEntity<byte[]> generateBillReport(Long billId, int id) {
        try {
            byte[] pdfContent = deliveryService.findByIdBillReport(billId, id);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "bill_report.pdf");
            headers.setCacheControl("must-revalidate, no-store");
            return new ResponseEntity<>(pdfContent, headers, HttpStatus.OK);
        } catch (MyCustomRuntimeException | DocumentException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    //lấy tất cả hóa đơn nhập của kho cụ thể theo ngày tạo và id kho
    @GetMapping("/import")
    public ResponseEntity<Page<BillResponse>> findByCreatedAndStorageAndImport(
            @RequestParam("created") @JsonFormat(pattern = "dd/MM/yyyy") Date date,
            @RequestParam("id") Long id,
            @PageableDefault(size = 100, page = 0) Pageable pageable) {
        Page<BillResponse> bills = deliveryService.findByCreatedAndStorageAndImport(date, id, pageable);
        return new ResponseEntity<>(bills, HttpStatus.OK);

    }
    //lấy tất cả hóa xuất của kho cụ thể theo ngày tạo và id kho

    @GetMapping("/export")
    public ResponseEntity<Page<BillResponse>> findByCreatedAndStorageAndExport(
            @RequestParam("created") @JsonFormat(pattern = "dd/MM/yyyy") Date date,
            @RequestParam("id") Long id,
            @PageableDefault(size = 100, page = 0) Pageable pageable) {
        Page<BillResponse> bills = deliveryService.findByCreatedAndStorageAndExport(date, id, pageable);
        return new ResponseEntity<>(bills, HttpStatus.OK);

    }
    @GetMapping("/bill/{id}")
    // lấy tất cả các thông tin chi tiêt về bill theo id
    public ResponseEntity<ShippingReportResponse> getBillById(@PathVariable Long id) throws MyCustomRuntimeException {
        ShippingReportResponse shippingReportResponse = deliveryService.findByBillId(id);
        return new ResponseEntity<>(shippingReportResponse, HttpStatus.OK);
    }
    @PutMapping("/update")
    //cập nhật đơn giao hàng
    public ResponseEntity<ShippingReportResponse> updateDelivery(@RequestBody DeliveryUpdateRequest deliveryRequest) throws MyCustomException {
            ShippingReportResponse response = deliveryService.updateDelivery(deliveryRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PutMapping("/delivery/{billId}")
    //thay đổi trạng thái đơn hàng sang DELIVERY
    public ResponseEntity<BillResponse> deliveryBill(@PathVariable Long billId) throws MyCustomRuntimeException {
        BillResponse response = deliveryService.deliveryBill(billId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PutMapping("/success/{billId}")
    //thay đổi trạng thái đơn hàng sang success
    public ResponseEntity<BillResponse> successBill(@PathVariable Long billId) throws MyCustomRuntimeException {
        BillResponse response = deliveryService.successBill(billId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PutMapping("/updateAll/{billId}")
    //cập nhật toan bộ thông thông tin đơn hàng
    public ResponseEntity<ShippingReportResponse> updateDeliveryAll(@RequestBody DeliveryRequest deliveryRequest, @PathVariable Long billId ) throws MyCustomException {
        ShippingReportResponse response = deliveryService.updateDeliveryAll(deliveryRequest,billId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/export-storage/{id}")
    // lấy tất cả các bil xuất theo id kho( hỗ trợ chức năng in xuất nhập)
    public ResponseEntity<Page<BillResponse>> findByStorageAndExport(
            @PathVariable("id") Long id,
            @PageableDefault(page = 0, size = 100, sort = "created", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(defaultValue = "ALL") String statusName,
            @RequestParam(defaultValue = "") String search

    )

    {
        Page<BillResponse> bills = deliveryService.findByStorageAndExport(id,statusName ,pageable,search);
        return new ResponseEntity<>(bills, HttpStatus.OK);

    }

    @GetMapping("/import-storage/{id}")
// lấy tất cả các bil nhập theo id kho( hỗ trợ chức năng in phiếu nhập)
    public ResponseEntity<Page<BillResponse>> findByStorageAndImport(
            @PathVariable("id") Long id,
            @PageableDefault(page = 0, size = 100, sort = "created", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(defaultValue = "ALL") String statusName,
            @RequestParam(defaultValue = "") String search) {
        Page<BillResponse> bills = deliveryService.findByStorageAndImport(id,statusName, pageable,search);
        return new ResponseEntity<>(bills, HttpStatus.OK);

    }
}