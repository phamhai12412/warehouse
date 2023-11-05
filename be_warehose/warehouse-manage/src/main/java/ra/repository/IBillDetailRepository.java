package ra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.model.BillDetail;

import java.util.List;

@Repository
public interface IBillDetailRepository extends JpaRepository<BillDetail, Long> {
    List<BillDetail> findAllByBill_Id(Long bill_id);
}