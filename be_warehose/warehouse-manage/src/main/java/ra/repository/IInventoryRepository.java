package ra.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ra.model.Inventory;
import ra.model.TypeStorage;

import java.util.Date;
import java.util.List;

@Repository
public interface IInventoryRepository extends JpaRepository<Inventory, Long> {
    @Query("SELECT i FROM Inventory i WHERE DATE(i.created) = DATE(:created) AND i.storage.typeStorage = :typeStorage")
    Page<Inventory> findAllByCreatedAndStorage_TypeStorage(@Param("created") Date created,
                                                           @Param("typeStorage") TypeStorage typeStorage, Pageable pageable);
    @Query("SELECT i FROM Inventory i WHERE DATE(i.created) = DATE(:created)")
    Page<Inventory> findAllByCreated(@Param("created") Date created, Pageable pageable);
    @Query("SELECT i FROM Inventory i WHERE DATE(i.created) = DATE(:created) AND  i.storage.id=:id")
    List<Inventory> findAllByCreatedList(@Param("created") Date created, @Param("id") Long id);
    Page<Inventory> findAllByStorage_Id(Long storage_id, Pageable pageable);
    @Query("SELECT i FROM Inventory i WHERE DATE(i.created) = DATE(:created) AND i.storage.id = :storageId")
    Page<Inventory> findAllByCreatedAndStorage_id(@Param("created") Date created,
                                                           @Param("storageId") Long storageId, Pageable pageable);
    Page<Inventory> findAllByStorage_IdAndNoteContainingIgnoreCase(Long storage_id, String note, Pageable pageable);

}
