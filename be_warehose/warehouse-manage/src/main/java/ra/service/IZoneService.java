package ra.service;

import org.springframework.data.domain.Page;
import ra.dto.request.ZoneRequest;
import ra.dto.response.ZoneResponse;
import ra.exception.MyCustomException;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IZoneService {
    ZoneResponse addZone(ZoneRequest zoneRequest) throws MyCustomException;

    ZoneResponse getZone(Long id) throws MyCustomException;

    Page<ZoneResponse> getZones(String name, Pageable pageable);

    ZoneResponse editZone(Long id, ZoneRequest zoneRequest) throws MyCustomException;

}