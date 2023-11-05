package ra.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryRequest {
    private Long startStorageId;             // ID của kho xuất phát
    private Long endStorageId;               // ID của kho đích
    private Long shipmentId;                    // ID của đơn vị vận chuyển
    private List<ProductDeliveryRequest> products; // Danh sách các sản phẩm cần giao


}
