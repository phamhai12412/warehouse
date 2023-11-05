package ra.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {
    private String productName;
    private Double price;
    private Double weight;
    private String avatar;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date created;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date dueDate;
    private Long categoryId;
    private Long storageId;
    private int quantity;
}
