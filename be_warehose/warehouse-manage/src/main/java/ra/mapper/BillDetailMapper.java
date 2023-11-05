package ra.mapper;

import org.springframework.stereotype.Component;
import ra.dto.response.BillDetailResponse;
import ra.dto.response.BillResponse;
import ra.model.Bill;
import ra.model.BillDetail;

@Component
public class BillDetailMapper {
    public BillDetailResponse convertBillDetail(BillDetail billDetail) {
        return BillDetailResponse.builder()
                .id(billDetail.getId())
                .product(billDetail.getProduct())
                .quantity(billDetail.getQuantity())
                .total(billDetail.getQuantity()*billDetail.getProduct().getPrice())
                .build();
    }
}
