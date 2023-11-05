package ra.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompareResponse {
    private Long id;
    private int quantity;//số lượng tế trong kho
    private String reason;
    private ProductResponse product;
    private int quantityToday;

}
