package ra.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ra.dto.request.ProductRequest;
import ra.dto.response.ProductResponse;
import ra.exception.DataNotFoundException;
import ra.exception.MyCustomRuntimeException;
import ra.model.Product;
import ra.model.StatusName;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface IProductService  {
    Page<ProductResponse> findAll(Pageable pageable);
    ProductResponse update(ProductRequest productRequest, Long id, List<String> list) throws DataNotFoundException;
    public ProductResponse deleteById(Long id) throws MyCustomRuntimeException;
    ProductResponse acceptById(Long id) throws MyCustomRuntimeException;
    ProductResponse findById(Long id) throws DataNotFoundException;
    Page<ProductResponse> findByName(String name, Pageable pageable) throws DataNotFoundException;
    Page<ProductResponse> findByStatus(StatusName statusName,Pageable pageable) throws DataNotFoundException;
    ProductResponse approveProduct(Long id) throws MyCustomRuntimeException;
    ProductResponse rejectProduct(Long id) throws MyCustomRuntimeException;
    Page<ProductResponse> findAllByStorage_Id(Long storage_id, Pageable pageable);
    Page<ProductResponse> findAllByProductNameContainingIgnoreCaseAndStorage_IdAndCode(Optional<String> productName, Optional<String> category, Long storage_id, Pageable pageable);
    Page<ProductResponse> findAllByStatusNameAndStorage_Id(String statusName, Long storage_id, Pageable pageable);
    byte[] exportToExcel(Long storage_id) throws IOException,MyCustomRuntimeException;
    ProductResponse changeStatus(Long id, String statusName) throws MyCustomRuntimeException;
    ProductResponse save(ProductRequest productRequest, List<String> list, Long iduse);
    List<ProductResponse> findAllByProductNameContainingIgnoreCaseAndStorage_Id(String productName ,Long storage_id);
    Page<ProductResponse> findAllGeneralSearch(String search, Long storage_id, Pageable pageable);
    List<ProductResponse> findAllByCategory_CategoryName(Long categoryId );
    List<ProductResponse> findAllByCategory_CategoryNameAndStorage(Long categoryId, Long id);
}
