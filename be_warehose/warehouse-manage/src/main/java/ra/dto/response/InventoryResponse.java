package ra.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ra.model.Storage;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryResponse {
    private Long id;
    private String note;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private String created;
    @JsonIgnoreProperties({"created", "users", "statusName"})
    private Storage storage;
    List<InventoryDetailResponse> inventoryDetails;
    private Long userIdCreated;
    private String userNameCreated;
}
