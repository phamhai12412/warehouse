package ra.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.dto.request.CategoryRequest;
import ra.dto.response.CategoryResponse;
import ra.exception.DataNotFoundException;
import ra.service.ICategoryService;
import javax.validation.Valid;


@RestController
@RequestMapping("/api/v1/categories")
@CrossOrigin("*")
public class CategoryController {
    @Autowired
    private ICategoryService categoryService;

    @GetMapping("")
    //hiện thị tất cả các category có hỗ trợ tìm kiếm và phân trang
    public ResponseEntity<Page<CategoryResponse>> findAll(
            @RequestParam(defaultValue = "") String name,
            @PageableDefault(page = 0, size = 100, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return new ResponseEntity<>(categoryService.findAll(name,pageable), HttpStatus.OK);
    }

    @GetMapping("/search-by-name")
    //hiện thị tất cả các category được tìm kiếm theo tên category
    public ResponseEntity<?> searchByName(
            @RequestParam(defaultValue = "") String name,
            @PageableDefault(page = 0, size = 100, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) throws DataNotFoundException {
        return new ResponseEntity<>(categoryService.findByName(name, pageable), HttpStatus.OK);

    }

    @GetMapping("/{id}")
    //tìm kiếm category theo ìd
    public ResponseEntity<CategoryResponse> getOne(@PathVariable Long id) throws DataNotFoundException {
        return new ResponseEntity<>(categoryService.findById(id), HttpStatus.OK);

    }

    @PostMapping("")
    //thêm moi category
    public ResponseEntity<CategoryResponse> add(
            @Valid
            @RequestBody CategoryRequest categoryRequest) {
        return new ResponseEntity<>(categoryService.save(categoryRequest), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    //cập nhật category
    public ResponseEntity<CategoryResponse> edit(
            @Valid
            @RequestBody CategoryRequest categoryRequest,
            @PathVariable Long id) throws DataNotFoundException {
        return new ResponseEntity<>(categoryService.update(categoryRequest, id), HttpStatus.OK);
    }
    @PutMapping("/changeStatus/{id}")
    //thay đổi trạng thái category
    public ResponseEntity<CategoryResponse> changeCategoryStatus(@PathVariable Long id) {
        CategoryResponse categoryResponse = categoryService.changeStatus(id);
        return  new ResponseEntity<>(categoryResponse, HttpStatus.OK);
    }
}
