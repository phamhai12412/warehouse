package ra.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ra.model.Zone;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StorageResponse {
    private Long id;
    private String storageName;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private String created;
    private String address;
    private String typeStorage;
    private Zone zone;
    private List<ProductResponse> productResponseList;
    private String status;
    private UserResponse users;

}
