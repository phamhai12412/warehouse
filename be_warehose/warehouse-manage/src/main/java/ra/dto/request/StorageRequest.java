package ra.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StorageRequest {
    private String storageName;
    private String address;
    private String typeStorage;
    private Long zoneId;
    private Long userId;

}
