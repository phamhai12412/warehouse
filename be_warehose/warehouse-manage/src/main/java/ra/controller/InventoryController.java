package ra.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ra.dto.request.InventoryItemRequest;
import ra.dto.request.InventoryRequest;
import ra.dto.response.CompareResponse;
import ra.dto.response.InventoryBasicResponse;
import ra.dto.response.InventoryDetailResponse;
import ra.dto.response.InventoryResponse;
import ra.exception.MyCustomException;
import ra.exception.MyCustomRuntimeException;
import ra.security.user_principal.UserPrinciple;
import ra.service.IInventoryService;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.io.FileOutputStream;

@RestController
@RequestMapping("/api/v1/inventory")
@CrossOrigin("*")
public class InventoryController {

    @Autowired
    private IInventoryService inventoryService;

    @PostMapping("/save")
    //tạo 1 báo cáo tồn kho
    public ResponseEntity<InventoryResponse> saveInventoryReport(Authentication authentication,@RequestParam("note") String note, @RequestBody @Valid InventoryRequest inventoryRequest) throws MyCustomException {
        UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
        Long userId = userPrinciple.getId();
        InventoryResponse response = inventoryService.saveInventoryReport(inventoryRequest, userId,note);
        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }
  //tìm tất cả báo cáo tồn kho theo ngày và dựa theo là báo cáo kho hay siêu thị
    @GetMapping("/findByCreatedAndTypeStorage")
    public ResponseEntity<Page<InventoryBasicResponse>> findByCreatedAndTypeStorage(@RequestParam @JsonFormat(pattern = "dd/MM/yyyy")
                                                                                    Date created, @RequestParam String storageType, @PageableDefault(page = 0, size = 100, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return new ResponseEntity<>(inventoryService.findAllByCreatedAndTypeStorage(created, storageType, pageable), HttpStatus.OK);
    }
  //tìm tất cả báo cáo tôn kho theo ngày không phân biệt kho hay siêu thị
    @GetMapping("/findByCreated")
    public ResponseEntity<Page<InventoryBasicResponse>> findByCreated(@RequestParam @JsonFormat(pattern = "dd/MM/yyyy") Date created, @PageableDefault(page = 0, size = 100, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return new ResponseEntity<>(inventoryService.findAllByCreated(created, pageable), HttpStatus.OK);
    }
   //tìm tất cả báo cáo tôn kho theo ngày và theo id kho
    @GetMapping("/findByInventoryCreatedAndInventoryStorageID")
    public ResponseEntity<List<InventoryDetailResponse>> findByInventoryCreatedAndInventoryStorageID(
            @RequestParam @JsonFormat(pattern = "mm/DD/yyyy") Date inventoryCreated, @RequestParam Long id) {
        return new ResponseEntity<>(inventoryService.findAllByInventoryCreatedAndInventoryStorageID(inventoryCreated, id), HttpStatus.OK);
    }
   //tìm theo id báo cáo
    @GetMapping("/findByInventoryId/{id}")
    public ResponseEntity<InventoryResponse> findByInventoryId(@PathVariable Long id) throws MyCustomRuntimeException {
        return new ResponseEntity<>(inventoryService.findAllByInventoryId(id), HttpStatus.OK);
    }
   //thay đổi báo cáo
    @PutMapping("/inventoryDetail/{id}")
    public ResponseEntity<CompareResponse> updateInventoryDetail(
            @RequestBody @Valid InventoryItemRequest inventoryItemRequest,
            @PathVariable Long id) {
        return new ResponseEntity<>(inventoryService.updateInventoryItem(id, inventoryItemRequest), HttpStatus.OK);
    }
    //xuất báo cáo tồn kho theo ngày và id kho
    @GetMapping("/export")
    public ResponseEntity<byte[]> exportToExcel(@RequestParam("created") @JsonFormat(pattern = "dd/MM/yyyy") Date created, @RequestParam("storage_id") Long storageId)  throws MyCustomRuntimeException{
        try {
            byte[] excelBytes = inventoryService.exportToExcel(created,storageId);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "exported-data.xlsx");
            headers.setContentLength(excelBytes.length);
            return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    //tải mẫu báo cáo theo sản phẩm hiện tại của kho hỗ trợ chức năng import excel
    @GetMapping("/export-report-form/{storageId}")
    public ResponseEntity<byte[]> exportToExcelProduct( @PathVariable("storageId") Long storageId)  throws MyCustomRuntimeException{
        try {
            byte[] excelBytes = inventoryService.exportToExcelProduct(storageId);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "exported-data.xlsx");
            headers.setContentLength(excelBytes.length);
            return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/importInventoryReportExcel/{id}")
    //chức năng tạo báo cáo tồn kho từ file excel báo cáo
    public ResponseEntity<InventoryResponse> importInventoryReport(@RequestParam("file") MultipartFile file,@RequestParam("note") String note, @PathVariable("id") Long id, Authentication authentication) {
        try {
            InventoryResponse response = inventoryService.importInventoryReport(file,note, id, authentication);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            throw new MyCustomRuntimeException("Error while reading the file: " + e.getMessage());
        }
    }
   //lấy tất cả báo cáo tồn kho theo id kho có hỗ trợ tìm kiếm theo note
    @GetMapping("/inventory-storage/{storageId}")
    public ResponseEntity<Page<InventoryBasicResponse>> findAllByStorageId(
            @PathVariable Long storageId,
            @RequestParam(defaultValue = "") String note,
             @PageableDefault(page = 0, size = 100, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return new ResponseEntity<>( inventoryService.findAllByStorage_IdAndNote(storageId,note, pageable),HttpStatus.OK);
    }
    //lấy tất cả báo cáo tồn kho theo id kho kèm ngày
    @GetMapping("/inventory-storage-created/{storageId}")
    public ResponseEntity<Page<InventoryBasicResponse>> findAllByStorageId(@RequestParam("created") @JsonFormat(pattern = "dd/MM/yyyy") Date created ,@PathVariable Long storageId, Pageable pageable) {
        return new ResponseEntity<>( inventoryService.findAllByStorageIdAndCreated(created,storageId, pageable),HttpStatus.OK);
    }
    //trả về 1 list sản phẩm theo báo cáo tồn kho thực tế hệ thống và tồn kho thực tế của kho
    @GetMapping("/indAllByCompareInventory/{inventoryId}")
    public ResponseEntity<Page<CompareResponse>> findAllByCompareInventoryId(@PathVariable("inventoryId") Long inventoryId,@RequestParam("search") String search,
                                                                             @PageableDefault(page = 0, size = 100, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return new ResponseEntity<>( inventoryService.findAllByCompareInventoryId(inventoryId,pageable,search),HttpStatus.OK);
    }
    //xuất báo cáo tồn kho theo inventoryId
    @GetMapping("/export/{inventoryId}")
    public ResponseEntity<byte[]> exportToExcel( @PathVariable("inventoryId") Long inventoryId)  throws MyCustomRuntimeException{
        try {
            byte[] excelBytes = inventoryService.exportToExcelInventoryId(inventoryId);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "exported-data.xlsx");
            headers.setContentLength(excelBytes.length);
            return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/findAll")
    public ResponseEntity<List<InventoryBasicResponse>> findAll() {
        return new ResponseEntity<>(inventoryService.findAll(), HttpStatus.OK);
    }
}