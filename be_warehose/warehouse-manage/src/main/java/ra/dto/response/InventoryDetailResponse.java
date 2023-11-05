package ra.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryDetailResponse {
    private Long id;
    private int quantity;
    private int quantityToday;
    private String reason;
    private Long productId;
    private String productName;
}
