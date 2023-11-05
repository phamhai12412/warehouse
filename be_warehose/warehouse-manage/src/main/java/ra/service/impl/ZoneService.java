package ra.service.impl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import ra.dto.request.ZoneRequest;
import ra.dto.response.ZoneResponse;
import ra.exception.MyCustomRuntimeException;
import ra.mapper.ZoneMapper;
import ra.model.Zone;
import ra.repository.IZoneRepository;
import org.springframework.data.domain.Pageable;
import ra.service.IZoneService;

@Service
public class ZoneService implements IZoneService {
    @Autowired
    IZoneRepository zoneRepository;
    @Autowired
    private ZoneMapper zoneMapper;

    @Override

    public ZoneResponse addZone(ZoneRequest zoneRequest) throws MyCustomRuntimeException {
        if (zoneRepository.findByZoneName(zoneRequest.getZoneName()) != null) {
            throw new MyCustomRuntimeException("Đã tồn tại khu vực này");
        }
        Zone zone = new Zone();
        zone.setZoneName(zoneRequest.getZoneName());


        Zone savezone = zoneRepository.save(zone);

        return zoneMapper.toResponse(savezone);
    }


    @Override
    public ZoneResponse getZone(Long id) throws MyCustomRuntimeException {
        Zone zone = zoneRepository.findById(id).orElseThrow(() -> new MyCustomRuntimeException("không tìm thấy khu vực này"));
        return zoneMapper.toResponse(zone);
    }


    @Override
    public Page<ZoneResponse> getZones(String name, Pageable pageable) {
        Page<Zone> zones;
        if (name.isEmpty()) {
            zones = zoneRepository.findAll(pageable);
        } else {
            zones = zoneRepository.findAllByZoneNameContainingIgnoreCase(name, pageable);
        }

        return zones.map(zoneMapper::toResponse);
    }


    @Override
    public ZoneResponse editZone(Long id, ZoneRequest zoneRequest) throws MyCustomRuntimeException {
        Zone zone = zoneRepository.findById(id)
                .orElseThrow(() -> new MyCustomRuntimeException("Không tìm thấy khu vực này"));

        if (zoneRequest.getZoneName() != null) {
            zone.setZoneName(zoneRequest.getZoneName());
        }

        Zone updatedZone = zoneRepository.save(zone);
        return zoneMapper.toResponse(updatedZone);

    }


}
