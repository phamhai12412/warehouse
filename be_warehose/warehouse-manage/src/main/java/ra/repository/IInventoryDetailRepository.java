package ra.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ra.model.InventoryDetail;
import ra.model.Storage;

import java.util.Date;
import java.util.List;


public interface IInventoryDetailRepository extends JpaRepository<InventoryDetail, Long> {
    @Query("SELECT i FROM InventoryDetail i WHERE DATE(i.inventory.created) = DATE(:inventory_created) AND i.inventory.storage.id = :inventory_storage_id")
   List< InventoryDetail> findAllByInventory_CreatedAndInventory_Storage_Id(Date inventory_created, Long inventory_storage_id);
    List< InventoryDetail>  findAllByInventory_Id(Long inventory_id);
    Page< InventoryDetail> findAllByInventory_IdAndProduct_ProductNameContainingIgnoreCase(Long inventory_id, String product_productName, Pageable pageable);

}
