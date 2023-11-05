package ra.service;

import com.itextpdf.text.DocumentException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ra.dto.request.DeliveryRequest;
import ra.dto.request.DeliveryUpdateRequest;
import ra.dto.response.BillDetailResponse;
import ra.dto.response.BillResponse;
import ra.dto.response.ShippingReportResponse;
import ra.dto.response.StorageResponse;
import ra.exception.MyCustomException;
import ra.exception.MyCustomRuntimeException;
import ra.model.Bill;
import ra.model.BillDetail;
import ra.model.DeliveryName;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface IDeliveryService {

    ShippingReportResponse createDelivery(DeliveryRequest deliveryRequest, Long userId) throws MyCustomException;
    Page<BillResponse> findBillsByDeliveryName(Optional<String> deliveryName, Optional<String> search, Pageable pageable);
    List<BillDetailResponse> findBillDetailByBillId(Long id);
    Page<BillResponse> findBillsByCreated(Date created, Pageable pageable);
    BillResponse updateBillStatus(Long id,String deliveryName) throws MyCustomException;
    BillResponse rejectBill(Long billId) throws MyCustomException;
    public BillResponse approveBill(Long billId) throws MyCustomException;
    byte[] findByIdBillReport(Long billId,int id) throws MyCustomRuntimeException, DocumentException, IOException;
    Page<BillResponse> findByCreatedAndStorageAndImport(Date date, Long id, Pageable pageable);
    Page<BillResponse> findByCreatedAndStorageAndExport(Date date, Long id, Pageable pageable);
    Page<BillResponse> findBills( Pageable pageable);
    ShippingReportResponse findByBillId( Long id);
    ShippingReportResponse updateDelivery(DeliveryUpdateRequest deliveryRequest) throws MyCustomException;
    BillResponse deliveryBill(Long id) throws  MyCustomRuntimeException;
    BillResponse successBill(Long id) throws  MyCustomRuntimeException;
    ShippingReportResponse updateDeliveryAll(DeliveryRequest deliveryRequest, Long billId) throws MyCustomRuntimeException, MyCustomException;
    Page<BillResponse> findByStorageAndExport(Long id, String statusName, Pageable pageable,String search);
    Page<BillResponse> findByStorageAndImport(Long id, String statusName, Pageable pageable, String search);
}
