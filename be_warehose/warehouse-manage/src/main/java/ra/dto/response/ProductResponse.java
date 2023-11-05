package ra.dto.response;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ra.model.Storage;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse {
    private Long id;
    private String productName;
    private double price;
    private double weight;
    private String code;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private String createdDate;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private String dueDate;
    private CategoryResponse category;
    private int quantity;
    private String statusName;
    private Storage storage;
}
