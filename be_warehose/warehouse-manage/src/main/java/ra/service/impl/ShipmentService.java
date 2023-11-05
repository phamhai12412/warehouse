package ra.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import ra.dto.request.ShipmentRequest;
import ra.dto.response.ShipmentResponse;
import ra.exception.MyCustomRuntimeException;
import ra.mapper.ShipMapper;
import ra.model.Shipment;
import ra.repository.IShipmentRepository;
import org.springframework.data.domain.Pageable;
import ra.service.IShipmentService;
@Service
public class ShipmentService implements IShipmentService {
    @Autowired
    IShipmentRepository shipmentRepository;
    @Autowired
    private ShipMapper shipMapper;
    @Override
    public ShipmentResponse addShipment(ShipmentRequest shipmentRequest) throws MyCustomRuntimeException {
        if (shipmentRepository.findByShipName(shipmentRequest.getShipName()) != null) {
            throw new MyCustomRuntimeException("Đã tồn tại đơn vị vận chuyển này ");
        }
        Shipment shipment = new Shipment();
        shipment.setShipName(shipmentRequest.getShipName());
        shipment.setPrice(shipmentRequest.getPrice());
        shipment.setStatus(true);
        Shipment saveshipment = shipmentRepository.save(shipment);

        return shipMapper.toResponse(saveshipment);
    }

    @Override
    public ShipmentResponse getShipment(Long id) throws MyCustomRuntimeException {
        Shipment shipment = shipmentRepository.findById(id).orElseThrow(() -> new MyCustomRuntimeException("không tìm thấy đơn vị vận chuyển này"));
        return shipMapper.toResponse(shipment);
    }

    @Override
    public Page<ShipmentResponse> getShipments(String name, Pageable pageable) {
        Page<Shipment> shipments;
        if (name.isEmpty()) {
            shipments = shipmentRepository.findAll(pageable);
        } else {
            shipments = shipmentRepository.findAllByShipNameContainingIgnoreCase(name, pageable);
        }

        return shipments.map(shipMapper::toResponse);
    }

    @Override
    public ShipmentResponse editShipment(Long id, ShipmentRequest shipmentRequest) throws MyCustomRuntimeException {

        Shipment shipment = shipmentRepository.findById(id)
                .orElseThrow(() -> new MyCustomRuntimeException("Không tìm thấy khu vực này"));

        if (shipmentRequest.getShipName() != null) {
            shipment.setShipName(shipmentRequest.getShipName());
        }
        if (shipmentRequest.getPrice() > 0) {
            shipment.setPrice(shipmentRequest.getPrice());
        }

        Shipment updatedShipment = shipmentRepository.save(shipment);
        return shipMapper.toResponse(updatedShipment);

    }
    @Override
    public ShipmentResponse lock(Long id) throws MyCustomRuntimeException {
        Shipment shipment = shipmentRepository.findById(id)
                .orElseThrow(() -> new MyCustomRuntimeException("Không tìm thấy đơn vị vận chuyển này"));


        shipment.setStatus(!shipment.isStatus());
        shipment=   shipmentRepository.save(shipment);
        return shipMapper.toResponse(shipment);

    }

}
