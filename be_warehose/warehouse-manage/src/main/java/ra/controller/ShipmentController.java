package ra.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.dto.request.ShipmentRequest;
import ra.dto.response.ShipmentResponse;
import ra.service.IShipmentService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/shipment")
@CrossOrigin("*")
public class ShipmentController {
    @Autowired
    private IShipmentService shipmentService;


    @PostMapping
    public ResponseEntity<ShipmentResponse> addShipment(@RequestBody @Valid ShipmentRequest shipmentRequest) {
        ShipmentResponse response = shipmentService.addShipment(shipmentRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShipmentResponse> getShipment(@PathVariable Long id) {

        ShipmentResponse response = shipmentService.getShipment(id);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @GetMapping
    public ResponseEntity<Page<ShipmentResponse>> getShipments(@RequestParam(defaultValue = "") String name,
                                                               @PageableDefault(page = 0, size = 100, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<ShipmentResponse> responses = shipmentService.getShipments(name, pageable);
        return new ResponseEntity<>(responses, HttpStatus.OK);

    }

    @PutMapping("/{id}")
    public ResponseEntity<ShipmentResponse> editShipment(@PathVariable Long id, @RequestBody @Valid ShipmentRequest shipmentRequest) {
        ShipmentResponse response = shipmentService.editShipment(id, shipmentRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/status/{id}")


    public ResponseEntity<ShipmentResponse> lock(@PathVariable Long id) {
        ShipmentResponse shipmentResponse = shipmentService.lock(id);
        return new ResponseEntity<>(shipmentResponse, HttpStatus.OK);
    }
}