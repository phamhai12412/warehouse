package ra.service;

import org.springframework.data.domain.Page;
import ra.dto.request.StorageRequest;
import ra.dto.response.StorageResponse;
import ra.exception.MyCustomException;
import org.springframework.data.domain.Pageable;
import ra.exception.MyCustomRuntimeException;
import ra.model.StatusName;
import ra.model.Storage;

import java.util.List;

public interface IStorageService {
    StorageResponse addStorage(StorageRequest storageRequest, List<String> list,Long iduse) throws MyCustomRuntimeException;

    StorageResponse getStorage(Long id) throws MyCustomException;

    Page<StorageResponse> getStorages(String name, Pageable pageable);

    StorageResponse editStorage(Long id, StorageRequest storageRequest) throws MyCustomException;
    StorageResponse updateStorageStatus(Long id, String statusName) throws MyCustomException;
    StorageResponse rejectStorage(Long storageId) throws MyCustomException;
    public StorageResponse approveStorage(Long storageId) throws MyCustomException;
    Page<StorageResponse> findAllByStatusName(String statusName, Pageable pageable);
    StorageResponse findByUserId(Long userId);
    StorageResponse deleteStorage(Long storageId) throws MyCustomRuntimeException;
    List<StorageResponse> getStoragesList(String name);
}
