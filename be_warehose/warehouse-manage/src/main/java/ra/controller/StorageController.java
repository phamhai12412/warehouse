package ra.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;
import ra.dto.request.StorageRequest;
import ra.dto.response.StorageResponse;
import ra.exception.MyCustomException;
import ra.model.Storage;
import ra.security.user_principal.UserPrinciple;
import ra.service.IStorageService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/storage")
@CrossOrigin("*")
public class StorageController {
    @Autowired
    private IStorageService storageService;

    @GetMapping()
    //lây ra tất cả các kho c hỗ trợ tìm kiếm và phân trang
    public ResponseEntity<Page<StorageResponse>> getStorages(
            @RequestParam(defaultValue = "") String name,
            @PageableDefault(page = 0, size = 100, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<StorageResponse> storages = storageService.getStorages(name, pageable);
        return new ResponseEntity<>(storages, HttpStatus.OK);
    }


    @GetMapping("/{id}")
    //lây ra kho theo id
    public ResponseEntity<StorageResponse> getStorageById(@PathVariable Long id) throws MyCustomException {
        StorageResponse storageResponse = storageService.getStorage(id);
        return new ResponseEntity<>(storageResponse, HttpStatus.OK);
    }


    @PostMapping
    //tạo mới 1 kho
    public ResponseEntity<StorageResponse> createStorage(Authentication authentication, @RequestBody @Valid StorageRequest storageRequest) throws MyCustomException {
        UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
        Long iduse = userPrinciple.getId();
        List<String> list = userPrinciple.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        StorageResponse storageResponse = storageService.addStorage(storageRequest, list, iduse);
        return new ResponseEntity<>(storageResponse, HttpStatus.CREATED);
    }


    @PutMapping("/{id}")
    //cập nhật thông tin của 1 kho
    public ResponseEntity<StorageResponse> updateStorage(@PathVariable Long id, @RequestBody @Valid StorageRequest storageRequest) throws MyCustomException {
        StorageResponse updatedStorage = storageService.editStorage(id, storageRequest);

        return new ResponseEntity<>(updatedStorage, HttpStatus.OK);
    }

    @PutMapping("/{idstorage}/status")
    //thay đổi trạng thái của kho
    public ResponseEntity<StorageResponse> updateStorageStatus(
            @RequestParam String statusName,
            @PathVariable Long idstorage) throws MyCustomException {
        StorageResponse updatedStorage = storageService.updateStorageStatus(idstorage, statusName);

        return new ResponseEntity<>(updatedStorage, HttpStatus.OK);
    }


    @PutMapping("/{id}/approve")
    // chấp nhận kho
    public ResponseEntity<StorageResponse> approveStorage(@PathVariable Long id) throws MyCustomException {
        StorageResponse storageResponse = storageService.approveStorage(id);
        return new ResponseEntity<>(storageResponse, HttpStatus.OK);
    }

    @PutMapping("/{id}/reject")
    // từ chối kho
    public ResponseEntity<StorageResponse> rejectStorage(@PathVariable Long id) throws MyCustomException {
        StorageResponse storageResponse = storageService.rejectStorage(id);
        return new ResponseEntity<>(storageResponse, HttpStatus.OK);
    }

    @GetMapping("/storages-by-status")
    //lấy ra tất cả các kho có statusName
    public Page<StorageResponse> getAllStoragesByStatus(
            @RequestParam(name = "statusName") String statusName,
            @PageableDefault(page = 0, size = 100, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return storageService.findAllByStatusName(statusName, pageable);
    }

    @GetMapping("/findByUserId/{userId}")
    //lấy ra tất cả các kho có userId quan lý
    public ResponseEntity<StorageResponse> findByUserId(@PathVariable Long userId) {
        StorageResponse storageList = storageService.findByUserId(userId);
        return ResponseEntity.ok(storageList);
    }
    @PutMapping("/{id}/delete")
    //chuyển trạng thái sang DELETE
    public ResponseEntity<StorageResponse> deleteStorage(@PathVariable Long id) throws MyCustomException {
        StorageResponse storageResponse = storageService.deleteStorage(id);
        return new ResponseEntity<>(storageResponse, HttpStatus.OK);
    }
    @GetMapping("/listStorages")
    //lấy ra tất cả các kho có hỗ trợ tìm kiếm
    public ResponseEntity<List<StorageResponse>> getStorages(
            @RequestParam(defaultValue = "") String name){

        List<StorageResponse> storages = storageService.getStoragesList(name);
        return new ResponseEntity<>(storages, HttpStatus.OK);
    }

}
