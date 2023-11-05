package ra.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDeliveryRequest {
    private Long productId;  // ID của sản phẩm
    private int quantity;   // Số lượng sản phẩm cần giao


}
