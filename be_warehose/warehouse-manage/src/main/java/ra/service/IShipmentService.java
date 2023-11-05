package ra.service;

import org.springframework.data.domain.Page;
import ra.dto.request.ShipmentRequest;
import ra.dto.response.ShipmentResponse;
import ra.exception.MyCustomRuntimeException;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface IShipmentService {
    ShipmentResponse addShipment(ShipmentRequest shipmentRequest) throws MyCustomRuntimeException;
    
    ShipmentResponse getShipment(Long id) throws MyCustomRuntimeException;

    Page<ShipmentResponse> getShipments(String name, Pageable pageable);

    ShipmentResponse editShipment(Long id, ShipmentRequest shipmentRequest) throws MyCustomRuntimeException;
    ShipmentResponse lock(Long id) throws MyCustomRuntimeException;
}
