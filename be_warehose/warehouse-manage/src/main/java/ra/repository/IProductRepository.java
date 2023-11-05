package ra.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ra.model.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface IProductRepository extends JpaRepository<Product,Long> {
    Product findByProductNameAndStorage_Id(String productName, Long storage_id);
    Page<Product> findAllByProductNameContainingIgnoreCase(String productName, Pageable pageable);

    Page<Product> findAllByStatusName(StatusName statusName, Pageable pageable);

    Page<Product> findAllByProductNameContainingIgnoreCaseAndCategoryCategoryNameAndStorageId(String productName, String category_categoryName, Long storage_id, Pageable pageable);
    Page<Product> findAllByStorage_Id(Long storage_id, Pageable pageable);
    List<Product> findAllByStorage_Id(Long storage_id);

    Page<Product> findAllByStatusNameAndStorage_Id(StatusName statusName, Long storage_id, Pageable pageable);
    boolean existsProductByIdAndStorage_Id(Long id, Long storage_id);
    List<Product> findAllByProductNameContainingIgnoreCaseAndStorage_Id(String productName, Long storage_id);

    @Query("SELECT sum(p.quantity) FROM Product p WHERE p.statusName = :statusName")
    Integer totalProduct(@Param("statusName") StatusName statusName);
    @Query("SELECT sum (p.quantity) FROM Product p WHERE p.statusName=:statusName AND p.storage.id = :storageId ")
    Integer totalProductStorage(@Param("statusName") StatusName statusName, @Param("storageId") Long storageId);
    List<Product> findAllByStorage_IdAndStatusName(Long storage_id, StatusName statusName);

    @Query("SELECT SUM (p.quantity) FROM Product p WHERE p.category = :category")
    Integer totalProductByCategory(@Param("category") Category category);
    List<Product> findAllByCategory_Id(Long category_id);
    List<Product> findAllByCategory_IdAndStorage_Id(Long category_id, Long storage_id);
    Product findByProductNameAndCreatedAndDueDateAndStorage_Id(String productName, Date created, Date dueDate, Long storage_id);
    Product findByProductNameAndCreatedAndDueDateAndStorage_IdAndStatusNameAndCategory_IdAndPriceAndWeight(String productName, Date created, Date dueDate, Long storage_id, StatusName statusName, Long category_id, double price, double weight);
    List<Product> findAllByProductNameAndCreatedAndDueDateAndStorage_Id(String productName, Date created, Date dueDate, Long storage_id);
    @Query("SELECT p FROM Product p WHERE p.storage.id = :storage_id AND p.statusName <> :statusName AND p.quantity>0" )
    List<Product> findAllByStorage_IdAndOtherStatusName(@Param("storage_id") Long storage_id, @Param("statusName") StatusName statusName);
    List<Product> findAllByCodeContainingIgnoreCaseAndStorage_Id(String code, Long storage_id);
   List<Product> findByCodeContainingIgnoreCaseAndCategoryCategoryNameAndStorageId(String code, String category_categoryName, Long storage_id);
  List<Product>  findByCodeContainingIgnoreCaseAndStorageId(String code, Long storage_id);
    List<Product> findByProductNameContainingIgnoreCaseAndCategoryCategoryNameAndCodeContainingIgnoreCaseAndStorageId(String productName, String category_categoryName, String code, Long storage_id);
    List<Product> findByProductNameContainingIgnoreCaseAndStorageId(String productName, Long storage_id);
    List<Product> findByCategoryCategoryNameAndStorageId(String category_categoryName, Long storage_id);
    List<Product> findByProductNameContainingIgnoreCaseAndCategoryCategoryNameAndStorageId(String productName, String category_categoryName, Long storage_id);
    List<Product> findByProductNameAndCreatedAndDueDateAndCategory_IdAndPriceAndWeight(String productName, Date created, Date dueDate, Long category_id, double price, double weight);
    List<Product> findAllByStorage_IdAndStatusNameAndCategoryAndQuantityGreaterThanAndProductNameAndPriceAndWeightAndDueDateGreaterThan(Long storage_id, StatusName statusName, Category category, int quantity, String productName, double price, double weight, Date dueDate, Sort sort);
    List<Product> findAllByStorage_IdAndStatusNameAndCategoryAndQuantityGreaterThanAndProductNameAndPriceAndWeightAndDueDateLessThan(Long storage_id, StatusName statusName, Category category, int quantity, String productName, double price, double weight, Date dueDate);
}
