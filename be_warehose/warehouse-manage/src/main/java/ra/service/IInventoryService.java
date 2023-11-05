package ra.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ra.dto.request.InventoryItemRequest;
import ra.dto.request.InventoryRequest;
import ra.dto.response.CompareResponse;
import ra.dto.response.InventoryBasicResponse;
import ra.dto.response.InventoryDetailResponse;
import ra.dto.response.InventoryResponse;
import ra.exception.MyCustomException;
import ra.exception.MyCustomRuntimeException;
import ra.model.Inventory;
import ra.model.InventoryDetail;
import ra.model.Storage;
import ra.model.TypeStorage;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public interface IInventoryService {
 InventoryResponse saveInventoryReport(InventoryRequest inventoryRequest, Long useId,String note) throws MyCustomRuntimeException;
 Page<InventoryBasicResponse> findAllByCreatedAndTypeStorage(Date created, String storageType, Pageable pageable);

 Page<InventoryBasicResponse> findAllByCreated(Date created, Pageable pageable);

 List<InventoryDetailResponse> findAllByInventoryCreatedAndInventoryStorageID(Date inventoryCreated, Long id);

 InventoryResponse findAllByInventoryId(Long inventoryId) throws MyCustomRuntimeException;
 CompareResponse updateInventoryItem(Long inventoryDetailId, InventoryItemRequest newItem) throws MyCustomRuntimeException;
 byte[] exportToExcel(Date created, Long storage_id) throws IOException, MyCustomRuntimeException;
 byte[] exportToExcelProduct(Long storage_id) throws IOException, MyCustomRuntimeException;
InventoryResponse importInventoryReport( MultipartFile file,String note, Long id, Authentication authentication) throws IOException;
 Page<InventoryBasicResponse> findAllByStorageIdAndCreated(Date created,Long storageId, Pageable pageable);
 Page<CompareResponse> findAllByCompareInventoryId(Long inventoryId,Pageable pageable,String search) throws MyCustomRuntimeException;
 Page<InventoryBasicResponse> findAllByStorage_IdAndNote(Long storageId,String note, Pageable pageable);
 byte[] exportToExcelInventoryId(Long inventoryId) throws IOException, MyCustomRuntimeException;
 List<InventoryBasicResponse> findAll();
}
