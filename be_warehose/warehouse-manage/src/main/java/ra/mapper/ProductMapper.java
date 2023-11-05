package ra.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ra.dto.request.ProductRequest;
import ra.dto.response.ProductResponse;
import ra.exception.MyCustomRuntimeException;
import ra.model.Category;
import ra.model.Product;
import ra.repository.ICategoryRepository;

import java.util.Date;


@Component
public class ProductMapper implements IGenericMapper<Product, ProductRequest, ProductResponse> {
    @Autowired
    private ICategoryRepository categoryRepository;
    @Autowired
    private CategoryMapper categoryMapper;
    @Override
    public Product toEntity(ProductRequest productRequest) {
        Category category = categoryRepository.findById(productRequest.getCategoryId()).orElseThrow(() -> new MyCustomRuntimeException("không tồn tại category"));

        return Product.builder()
                .created(new Date())
                .quantity(productRequest.getQuantity())
                .productName(productRequest.getProductName().toUpperCase())
                .category(category)
                .created(productRequest.getCreated())
                .dueDate(productRequest.getDueDate())
                .price(productRequest.getPrice())
                .weight(productRequest.getWeight())
                .build();
    }

    @Override
    public ProductResponse toResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .productName(product.getProductName())
                .price(product.getPrice())
                .weight(product.getWeight())
                .code(product.getCode())
                .createdDate(product.getCreated().toString())
                .dueDate(product.getDueDate().toString())
                .category(categoryMapper.toResponse(product.getCategory()))
                .statusName(product.getStatusName().toString())
                .quantity(product.getQuantity())
                .storage(product.getStorage())
                .build();

    }

}
