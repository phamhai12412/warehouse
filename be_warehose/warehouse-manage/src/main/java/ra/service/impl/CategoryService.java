package ra.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ra.dto.request.CategoryRequest;
import ra.dto.response.CategoryResponse;
import ra.exception.DataNotFoundException;
import ra.exception.MyCustomRuntimeException;
import ra.mapper.CategoryMapper;
import ra.model.Category;
import ra.model.Zone;
import ra.repository.ICategoryRepository;
import ra.service.ICategoryService;
import java.util.Optional;
@Service
public class CategoryService implements ICategoryService {
    @Autowired
    private ICategoryRepository categoryRepository;
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    //nếu từ khóa tìm kiếm trống thì hiện toàn bộ category
    //ngược lại thì tìm kiếm toàn bộ category có tên chứa từ khóa, và nhớ phân trang
    public Page<CategoryResponse> findAll(String name,Pageable pageable) {
        Page<Category> categories;
        if (name.isEmpty()) {
            categories = categoryRepository.findAll(pageable);
        } else {
            categories = categoryRepository.findByCategoryNameContainingIgnoreCase(name, pageable);
        }

        return categories.map(categoryMapper::toResponse);
    }


    @Override
    //ti kiếm tất cả category có tên trùng với từ khóa và phân trang
    public Page<CategoryResponse> findByName(String name, Pageable pageable) throws DataNotFoundException {
        Page<Category> categoryPage = categoryRepository.findByCategoryNameContainingIgnoreCase(name, pageable);
        Page<CategoryResponse> categoryResponsePage = categoryPage.map(category -> categoryMapper.toResponse(category));
        if (categoryResponsePage.isEmpty()) {
            throw new DataNotFoundException("No result found.");
        }
        return categoryResponsePage;
    }

    @Override
    //tìm kiếm category theo id
    public CategoryResponse findById(Long id) throws DataNotFoundException {
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        if (!categoryOptional.isPresent()) {
            throw new DataNotFoundException("Category's id " + id + " not found.");
        }
        return categoryMapper.toResponse(categoryOptional.get());
    }

    @Override
    //thêm mới category, nhớ check trùng tên
    public CategoryResponse save(CategoryRequest categoryRequest) {
        Optional<Category> categoryOptional = categoryRepository.findByCategoryName(categoryRequest.getCategoryName());
        if (categoryOptional.isPresent()) {
            throw new MyCustomRuntimeException("Category's name: " + categoryRequest.getCategoryName() + " is already existed");
        }
        Category category = categoryRepository.save(categoryMapper.toEntity(categoryRequest));
        return categoryMapper.toResponse(category);
    }

    @Override
    //cập nhâật category
    public CategoryResponse update(CategoryRequest categoryRequest, Long id) throws DataNotFoundException {
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        if (!categoryOptional.isPresent()) {
            throw new DataNotFoundException("Category's id " + id + " not found.");
        }
        Category category = categoryMapper.toEntity(categoryRequest);
        category.setId(id);
        return categoryMapper.toResponse(categoryRepository.save(category));
    }

    @Override
    //thay đổi trạng thái category
    public CategoryResponse deleteById(Long id) throws DataNotFoundException {
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        if (!categoryOptional.isPresent()) {
            throw new DataNotFoundException("Category's id " + id + " not found.");
        }
        categoryOptional.get().setStatus(!categoryOptional.get().isStatus());
        return categoryMapper.toResponse(categoryRepository.save(categoryOptional.get()));
    }
    @Override
    //thay đổi trạng thái category
    public CategoryResponse changeStatus(Long id) throws MyCustomRuntimeException {

        Category category = categoryRepository.findById(id).orElseThrow(()->new MyCustomRuntimeException("không tìm thấy category"));
        category.setStatus(!category.isStatus());
       category= categoryRepository.save(category);
        return  categoryMapper.toResponse(category);
    }
}
