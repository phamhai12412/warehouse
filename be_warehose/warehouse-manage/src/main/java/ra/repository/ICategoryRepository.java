package ra.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.model.Category;
import ra.model.Product;

import java.util.Optional;

@Repository
public interface ICategoryRepository extends JpaRepository<Category,Long> {
    Optional<Category> findByCategoryName(String name);

    Page<Category> findByCategoryNameContainingIgnoreCase(String name, Pageable pageable);

    Page<Category> findAll(Pageable pageable);

}
