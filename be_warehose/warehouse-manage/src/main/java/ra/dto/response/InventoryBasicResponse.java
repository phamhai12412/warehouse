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
public class InventoryBasicResponse {
    private Long id;
    private String note;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private String created;
    @JsonIgnoreProperties({"created", "statusName"})
    private Storage storage;
    private List<InventoryDetailResponse> inventoryDetails;
}