package ra.mapper;
import org.springframework.stereotype.Component;
import ra.dto.request.ShipmentRequest;
import ra.dto.response.ShipmentResponse;
import ra.model.Shipment;
@Component
public class ShipMapper implements IGenericMapper<Shipment, ShipmentRequest, ShipmentResponse>  {
    @Override
    public Shipment toEntity(ShipmentRequest shipmentRequest) {
        return Shipment.builder()
                .shipName(shipmentRequest.getShipName())
                .price(shipmentRequest.getPrice())
                .build();
    }

    @Override
    public ShipmentResponse toResponse(Shipment shipment) {
        return ShipmentResponse.builder()
                .status(shipment.isStatus())
                .id(shipment.getId())
                .price(shipment.getPrice())
                .shipName(shipment.getShipName())
                .build();
    }
}
