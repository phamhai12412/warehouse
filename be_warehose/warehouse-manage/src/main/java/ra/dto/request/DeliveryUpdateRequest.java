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
public class DeliveryUpdateRequest {
    private Long shipmentId;                    // ID của đơn vị vận chuyển
    private ProductDeliveryRequest product; // Danh sách các sản phẩm cần giao
    private Long billId;
    private Long billDetailId;

}
