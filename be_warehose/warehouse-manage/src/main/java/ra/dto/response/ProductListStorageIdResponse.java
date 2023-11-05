package ra.dto.response;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductListStorageIdResponse {
    private String productName;
    private Double price;
    private Double weight;
    private String avatar;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private String createdDate;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private String dueDate;
    private Long categoryId;
    private List<Long> storageId;
    private int quantity;
}
