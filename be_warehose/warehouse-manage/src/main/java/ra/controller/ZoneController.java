package ra.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.dto.request.ZoneRequest;
import ra.dto.response.ZoneResponse;
import ra.exception.MyCustomException;
import ra.service.IZoneService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/zone")
@CrossOrigin("*")
public class ZoneController {
    @Autowired
    private IZoneService zoneService;

    @GetMapping()
    // GET /api/v1/zone
    public ResponseEntity<Page<ZoneResponse>> getZones(
            @RequestParam(defaultValue = "") String name,
            @PageableDefault(page = 0, size = 100, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<ZoneResponse> categories = zoneService.getZones(name, pageable);
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    // GET /api/v1/zone/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ZoneResponse> getZoneById(@PathVariable Long id) throws MyCustomException {
        ZoneResponse zoneResponse = zoneService.getZone(id);
        return new ResponseEntity<>(zoneResponse, HttpStatus.OK);
    }

    // POST /api/v1/zone
    @PostMapping
    public ResponseEntity<ZoneResponse> createZone(@RequestBody ZoneRequest zoneRequest) throws MyCustomException {
        ZoneResponse zoneResponse = zoneService.addZone(zoneRequest);
        return new ResponseEntity<>(zoneResponse, HttpStatus.CREATED);
    }

    // PUT /api/v1/zone/{id}
    @PutMapping("/{id}")
    public ResponseEntity<ZoneResponse> updateZone(@PathVariable Long id, @RequestBody @Valid ZoneRequest zoneRequest) throws MyCustomException {
        ZoneResponse updatedCategory = zoneService.editZone(id, zoneRequest);

        return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
    }

}
