package ra.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ra.dto.request.CategoryRequest;
import ra.dto.response.CategoryResponse;
import ra.model.Category;
import ra.repository.ICategoryRepository;

import java.util.Collections;
import java.util.stream.Collectors;

@Component
public class CategoryMapper implements IGenericMapper<Category, CategoryRequest, CategoryResponse> {
    @Autowired
    ICategoryRepository categoryRepository;

    @Override
    public Category toEntity(CategoryRequest categoryRequest) {
        return Category.builder()
                .categoryName(categoryRequest.getCategoryName())
                .status(true)
                .build();
    }

    @Override
    public CategoryResponse toResponse(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .categoryName(category.getCategoryName())
                .status(category.isStatus())
                .build();
    }
}
