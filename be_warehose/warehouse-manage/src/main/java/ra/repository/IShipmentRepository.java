package ra.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ra.model.Shipment;


public interface IShipmentRepository extends JpaRepository<Shipment,Long> {
    Shipment findByShipName(String shipName);
   Page<Shipment> findAllByShipNameContainingIgnoreCase(String shipName, Pageable pageable);

}
