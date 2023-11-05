package ra.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;
import ra.dto.request.ProductRequest;
import ra.dto.response.ProductResponse;
import ra.exception.DataNotFoundException;
import ra.exception.MyCustomException;
import ra.exception.MyCustomRuntimeException;
import ra.model.StatusName;
import ra.security.user_principal.UserPrinciple;
import ra.service.IProductService;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.http.HttpHeaders;

@RestController
@RequestMapping("/api/v1/products")
@CrossOrigin("*")
public class ProductController {
    @Autowired
    private IProductService productService;


    @GetMapping()
    //lấy tất cả các sản phâm
    public ResponseEntity<Page<ProductResponse>> findAll(
            @PageableDefault(page = 0, size = 100, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return new ResponseEntity<>(productService.findAll(pageable), HttpStatus.OK);
    }

    @GetMapping("/search-by-name")
    //lấy tất cả các sản phẩm có tên chứa từ khóa tìm kiếm
    public ResponseEntity<?> searchByName(
            @RequestParam() String search,
            @PageableDefault(page = 0, size = 100, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) throws DataNotFoundException {
        return new ResponseEntity<>(productService.findByName(search, pageable), HttpStatus.OK);

    }

    @GetMapping("/search-by-status")
    //lấy tất cả sản phẩm theo statusName
    public ResponseEntity<?> searchByStatus(
            @RequestParam(defaultValue = "") StatusName statusName,
            @PageableDefault(page = 0, size = 100, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) throws DataNotFoundException {
        return new ResponseEntity<>(productService.findByStatus(statusName, pageable), HttpStatus.OK);
    }


    @GetMapping("/{id}")
    //lấy ra sản phẩm theo id
    public ResponseEntity<?> getOne(@PathVariable Long id) throws DataNotFoundException {
        return new ResponseEntity<>(productService.findById(id), HttpStatus.OK);
    }
    @PutMapping("/{id}")
    //cập nhật thông tin sản phẩm
    public ResponseEntity<ProductResponse> edit(

            @RequestBody  @Valid ProductRequest productRequest,
            @PathVariable Long id, Authentication authentication
    ) throws DataNotFoundException {
        UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
        List<String> list = userPrinciple.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        return new ResponseEntity<>(productService.update(productRequest, id,list), HttpStatus.OK);
    }

    @PutMapping("/{id}/approve")
    //phê duyệt sản phẩm
    public ResponseEntity<ProductResponse> approveStorage(@PathVariable Long id) throws MyCustomException {
        ProductResponse productResponse = productService.approveProduct(id);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @PutMapping("/{id}/reject")
    //từ chối sản phẩm
    public ResponseEntity<ProductResponse> rejectStorage(@PathVariable Long id) throws MyCustomException {
        ProductResponse productResponse = productService.rejectProduct(id);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @GetMapping("/findAll/{storageId}")
    //lấy ra tất cả sản phẩm theo kho có hỗ trợ tìm kiếm theo tên sản phẩm hoặc code và phân trang
    public ResponseEntity<Page<ProductResponse>> findAllByProductNameContainingIgnoreCaseAndStorage_Id(
            @RequestParam(defaultValue = "") Optional<String> search,
            @RequestParam(defaultValue = "ALL") Optional<String> category,
            @PathVariable Long storageId,
            @PageableDefault(page = 0, size = 100, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return new ResponseEntity<>(productService.findAllByProductNameContainingIgnoreCaseAndStorage_IdAndCode(search,category, storageId, pageable),HttpStatus.OK) ;
    }

    @GetMapping("/statusandstorageid")
    //lấy ra tất cả sản phẩm theo kho có statusName
    public ResponseEntity<Page<ProductResponse>>  findAllByStatusNameAndStorage_Id(
            @RequestParam String statusName,
            @RequestParam Long storage_id,
            @PageableDefault(page = 0, size = 100, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return new ResponseEntity<>(productService.findAllByStatusNameAndStorage_Id(statusName, storage_id, pageable),HttpStatus.OK);
    }

    @GetMapping("/export/{storageId}")
    //xuất excel thông tin tất cả sản phẩm trong kho dựa vào id
    public ResponseEntity<byte[]> exportToExcel(@PathVariable("storageId") Long storageId) throws MyCustomRuntimeException{
        try {
            byte[] excelBytes = productService.exportToExcel(storageId);
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

    @PutMapping("/{id}/change-status")
    //thay đổi statusName của sản phẩm
    public ResponseEntity<ProductResponse> changeStatus(
            @PathVariable Long id,
            @RequestParam String statusName) {

        ProductResponse response = productService.changeStatus(id, statusName);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PostMapping()
    //tạo mới 1 sản phẩm
    public ResponseEntity< ProductResponse> save( Authentication authentication,@RequestBody @Valid ProductRequest productRequest) {
        UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
        Long iduse = userPrinciple.getId();
        List<String> list = userPrinciple.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
            ProductResponse savedProducts = productService.save(productRequest, list, iduse);
            return new ResponseEntity<>(savedProducts,HttpStatus.OK);
    }
    @PutMapping("/{id}/delete")
    //chuyển sa phẩm về trạng thái delete
    public ResponseEntity<ProductResponse> deleteProductById(@PathVariable Long id) {

            ProductResponse productResponse = productService.deleteById(id);
        return new ResponseEntity<>(productResponse,HttpStatus.OK);

    }

    @PutMapping("/{id}/accept")
    //phê duệt sản phẩm
    public ResponseEntity<ProductResponse> acceptProductById(@PathVariable Long id) {
            ProductResponse productResponse = productService.acceptById(id);
            return new ResponseEntity<>(productResponse,HttpStatus.OK);
    }
    @GetMapping("/findAllByProductName/{storageId}")
    //lấy tất cả sản phẩm theo kho có hỗ trợ tìm kiếm theo tên
    public ResponseEntity<List<ProductResponse>>  findAllByProductNameContainingIgnoreCaseAndStorage_Id(
            @PathVariable Long storageId,  @RequestParam() String search

    ) {
        return new ResponseEntity<>(productService.findAllByProductNameContainingIgnoreCaseAndStorage_Id(search, storageId),HttpStatus.OK);
    }
    @GetMapping("/products/{storageId}/search")
    //hỗ trợ tìm kiếm sản phẩm nâng cao tìm kếm theo tên sản phẩm lẫn tên category
    public ResponseEntity<Page<ProductResponse>> findAllGeneralSearch(
            @PathVariable("storageId") Long storageId,
            @RequestParam() String search,
            @PageableDefault(page = 0, size = 100, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<ProductResponse> productResponsePage = productService.findAllGeneralSearch(search, storageId, pageable);
        return ResponseEntity.ok(productResponsePage);
    }

    //lấy tất cả product theo category name
    @GetMapping("/category/{categoryId}")
    public  ResponseEntity <List<ProductResponse> >getProductsByCategory(@PathVariable Long categoryId) {
        List<ProductResponse> products = productService.findAllByCategory_CategoryName(categoryId);
        return new ResponseEntity<>(products,HttpStatus.OK);
    }
    @GetMapping("/category/{categoryId}/{id}")
    //tìm tất cả sản phẩm của kho theo category
    public  ResponseEntity <List<ProductResponse> >getProductsByCategoryAndStorage(@PathVariable Long categoryId,@PathVariable Long id) {
        List<ProductResponse> products = productService.findAllByCategory_CategoryNameAndStorage(categoryId,id);
        return new ResponseEntity<>(products,HttpStatus.OK);
    }
}
