package ra.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ra.model.StatusName;
import ra.model.Storage;

import java.util.List;
import java.util.Optional;


public interface IStorageRepository extends JpaRepository<Storage,Long> {
    Page<Storage> findAllByStorageNameContainingIgnoreCase(String name, Pageable pageable);
    Storage findByStorageName(String storageName);
    Page<Storage> findAllByStatusName(StatusName statusName, Pageable pageable);
   Storage findByUsers_Id(Long users_id);
   boolean existsByStorageName(String storageName);
    List<Storage> findAllByStorageNameContainingIgnoreCase(String name);

}
