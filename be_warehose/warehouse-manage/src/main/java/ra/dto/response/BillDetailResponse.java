package ra.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ra.model.Product;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BillDetailResponse {
    private Long id;
    private int quantity;
    private Product product;
    private double total;

}
