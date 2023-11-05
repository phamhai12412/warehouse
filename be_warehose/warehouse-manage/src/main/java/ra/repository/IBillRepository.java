package ra.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ra.model.Bill;
import ra.model.DeliveryName;
import ra.model.StatusName;
import ra.model.TypeStorage;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface IBillRepository extends JpaRepository<Bill, Long> {

    @Query("SELECT b FROM Bill b WHERE DATE(b.created) = DATE(:date) ")
    Page<Bill> findByCreated(@Param("date") Date date, Pageable pageable);
    @Query("SELECT b FROM Bill b WHERE DATE(b.created) = DATE(:date) AND b.end.id=:id ")//phiếu nhập
    Page<Bill> findByCreatedAndStorageAndImport(@Param("date") Date date,@Param("id") Long id, Pageable pageable);
    @Query("SELECT b FROM Bill b WHERE DATE(b.created) = DATE(:date) AND b.start.id=:id ")//phiếu xuất
    Page<Bill> findByCreatedAndStorageAndExport(@Param("date") Date date,@Param("id") Long id, Pageable pageable);
    Page<Bill> findAllByStart_IdAndDelivery(Long start_id, DeliveryName delivery, Pageable pageable);//phiếu xuất
    Page<Bill> findAllByEnd_IdAndDelivery(Long end_id, DeliveryName delivery, Pageable pageable);//phiếu nhập
    Page<Bill> findAllByStart_Id(Long start_id, Pageable pageable);//phiếu xuất
    Page<Bill> findAllByEnd_Id(Long end_id, Pageable pageable);//phiếu nhập
    Page<Bill> findAllByStart_IdAndDeliveryAndEnd_StorageNameContainingIgnoreCase(Long start_id, DeliveryName delivery, String end_storageName, Pageable pageable);//phiếu xuất
    Page<Bill> findAllByEnd_IdAndDeliveryAndStart_StorageNameContainingIgnoreCase(Long end_id, DeliveryName delivery, String start_storageName, Pageable pageable);//phiếu nhập
    @Query("SELECT SUM(b.total + b.priceShip) FROM Bill b WHERE YEAR(b.created) = :year AND MONTH(b.created) = :month AND b.delivery = :delivery AND b.start.id = :storageId AND b.end.typeStorage = :storageName")
    Double findByCreatedGVCStorageId(@Param("year") int year, @Param("month") int month, @Param("delivery") DeliveryName delivery, @Param("storageId") Long storageId, @Param("storageName")  TypeStorage storageName);
    @Query("SELECT SUM(b.total + b.priceShip) FROM Bill b WHERE YEAR(b.created) = :year AND MONTH(b.created) = :month AND b.delivery = :delivery AND b.end.typeStorage = :storageName")
    Double findByCreatedGVCS(@Param("year") int year, @Param("month") int month, @Param("delivery") DeliveryName delivery, @Param("storageName") TypeStorage storageName);
    @Query("SELECT SUM(b.total + b.priceShip) FROM Bill b WHERE YEAR(b.created) = :year AND b.delivery = :delivery AND b.end.typeStorage = :storageName")
    Double GVCSByYear(@Param("year") int year, @Param("delivery") DeliveryName delivery, @Param("storageName") TypeStorage storageName);
    @Query("SELECT count (b) FROM Bill b WHERE YEAR(b.created) = :year AND b.delivery = :delivery AND b.end.typeStorage = :storageName")
    Integer TotalBillByYear(@Param("year") int year, @Param("delivery") DeliveryName delivery, @Param("storageName") TypeStorage storageName);
    @Query("SELECT SUM(b.total + b.priceShip) FROM Bill b WHERE YEAR(b.created) = :year AND b.delivery = :delivery AND b.end.typeStorage = :storageName AND b.start.id = :storageId")
    Double GVCSByYearAndStorage(@Param("year") int year, @Param("delivery") DeliveryName delivery,@Param("storageId") Long storageId, @Param("storageName") TypeStorage storageName);
    @Query("SELECT count (b) FROM Bill b WHERE YEAR(b.created) = :year AND b.delivery = :delivery AND b.end.typeStorage = :storageName AND b.start.id = :storageId")
    Integer TotalBillByYearAndStorage(@Param("year") int year, @Param("delivery") DeliveryName delivery,@Param("storageId") Long storageId, @Param("storageName") TypeStorage storageName);
    @Query("SELECT count (b) FROM Bill b WHERE YEAR(b.created) = :year AND MONTH(b.created) = :month AND DAY(b.created) = :day AND b.end.id = :id") // Phiếu nhập
    Integer findByCreatedAndStorageAndImportDashboard(@Param("year") int year, @Param("month") int month, @Param("day") int day, @Param("id") Long id);

    @Query("SELECT count (b) FROM Bill b WHERE YEAR(b.created) = :year AND MONTH(b.created) = :month AND DAY(b.created) = :day AND b.start.id = :id") // Phiếu xuất
    Integer findByCreatedAndStorageAndExportDashboard(@Param("year") int year, @Param("month") int month, @Param("day") int day, @Param("id") Long id);
    @Query("SELECT count (b) FROM Bill b WHERE YEAR(b.created) = :year AND MONTH(b.created) = :month AND b.delivery = :delivery AND b.end.id = :storageId") //phiếu nhập
    Integer dashboardImportAndCancel(@Param("year") int year, @Param("month") int month, @Param("delivery") DeliveryName delivery, @Param("storageId") Long storageId);
    @Query("SELECT count (b) FROM Bill b WHERE YEAR(b.created) = :year AND MONTH(b.created) = :month AND b.delivery = :delivery AND b.start.id = :storageId") //phiếu xuất
    Integer dashboardExportAndCancel(@Param("year") int year, @Param("month") int month, @Param("delivery") DeliveryName delivery, @Param("storageId") Long storageId);
    List<Bill> findByLocationStartContainingIgnoreCaseAndDelivery(String locationStart, DeliveryName delivery);
    List<Bill> findAllByLocationStartContainingIgnoreCase(String locationStart);

    Page<Bill> findAllByStartIdAndLocationEndContainingIgnoreCase(Long start_id, String locationEnd, Pageable pageable);
    Page<Bill> findAllByEndIdAndLocationStartContainingIgnoreCase(Long end_id, String locationStart, Pageable pageable);

    Page<Bill> findAllByStartIdAndDeliveryAndLocationEndContainingIgnoreCase(Long start_id, DeliveryName delivery, String locationEnd, Pageable pageable);
    Page<Bill> findAllByEndIdAndDeliveryAndLocationStartContainingIgnoreCase(Long end_id, DeliveryName delivery, String locationStart, Pageable pageable);
}
