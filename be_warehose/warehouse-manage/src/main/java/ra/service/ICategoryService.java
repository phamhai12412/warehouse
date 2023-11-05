package ra.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ra.dto.request.CategoryRequest;
import ra.dto.response.CategoryResponse;
import ra.exception.DataNotFoundException;
import ra.exception.MyCustomRuntimeException;

import java.util.List;

public interface ICategoryService {
    CategoryResponse findById(Long id) throws DataNotFoundException;
    CategoryResponse save(CategoryRequest categoryRequest);
    Page<CategoryResponse> findAll(String name,Pageable pageable);
    Page<CategoryResponse> findByName(String name,Pageable pageable) throws DataNotFoundException;
    CategoryResponse update(CategoryRequest categoryRequest, Long id) throws DataNotFoundException;
    CategoryResponse deleteById(Long id) throws DataNotFoundException;
    CategoryResponse changeStatus(Long id) throws MyCustomRuntimeException;
}