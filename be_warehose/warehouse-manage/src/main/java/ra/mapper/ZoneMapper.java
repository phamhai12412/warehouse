package ra.mapper;

import org.springframework.stereotype.Component;
import ra.dto.request.ZoneRequest;
import ra.dto.response.ZoneResponse;
import ra.mapper.IGenericMapper;
import ra.model.Zone;

@Component
public class ZoneMapper implements IGenericMapper<Zone, ZoneRequest, ZoneResponse> {
    @Override
    public Zone toEntity(ZoneRequest zoneRequest) {
        return Zone.builder()
                .zoneName(zoneRequest.getZoneName())
                .build();
    }

    @Override
    public ZoneResponse toResponse(Zone zone) {
        return ZoneResponse.builder()
                .id(zone.getId())
                .zoneName(zone.getZoneName())
                .build();
    }
}