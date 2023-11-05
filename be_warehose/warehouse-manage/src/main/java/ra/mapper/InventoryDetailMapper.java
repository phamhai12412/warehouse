package ra.mapper;

import org.springframework.stereotype.Component;
import ra.dto.response.BillResponse;
import ra.dto.response.InventoryDetailResponse;
import ra.model.Bill;
import ra.model.InventoryDetail;

@Component
public class InventoryDetailMapper {
    public InventoryDetailResponse convertDetail(InventoryDetail inventoryDetail) {
        return InventoryDetailResponse.builder()
                .id(inventoryDetail.getId())
                .productName(inventoryDetail.getProduct().getProductName())
                .reason(inventoryDetail.getReason())
                .quantityToday(inventoryDetail.getQuantityToday())
                .productId(inventoryDetail.getProduct().getId())
                .quantity(inventoryDetail.getQuantity())
                .build();
    }
}
