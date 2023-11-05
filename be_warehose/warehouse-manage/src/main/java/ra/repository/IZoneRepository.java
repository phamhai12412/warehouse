package ra.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import ra.model.Zone;

public interface IZoneRepository extends PagingAndSortingRepository<Zone, Long> {
    Page<Zone> findAllByZoneNameContainingIgnoreCase(String name, Pageable pageable);
    Zone findByZoneName(String zoneName);
}
