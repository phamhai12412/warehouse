package ra.controller;


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
import ra.dto.request.EditPassRequest;
import ra.dto.request.FormSignUpDto;
import ra.dto.response.PasswordResponse;
import ra.dto.response.UserResponse;
import ra.exception.MyCustomRuntimeException;
import ra.model.RoleName;
import ra.service.IUserService;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin("*")
public class UserController {
    @Autowired
    private IUserService userService;

    @GetMapping()
    //lấu ra tất cả các use có hỗ trợ tìm kiếm theo email và phân trang
    public ResponseEntity<Page<UserResponse>> findAll(
            @RequestParam(defaultValue = "") String name,
            @PageableDefault(page = 0, size = 100, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {

        return new ResponseEntity<>(userService.findAllByEmailContainingIgnoreCase(name, pageable), HttpStatus.OK);
    }

    @PutMapping("/status/{userId}")
    //thay đổi trạng thái của user
    public ResponseEntity<UserResponse> changeStatus(@PathVariable Long userId) {
        return new ResponseEntity<>(userService.changeStatus(userId), HttpStatus.OK);
    }

    @PutMapping("/update_user/{userId}")
    //cập nhật thông tin cho user
    public ResponseEntity<UserResponse> updateUser(@RequestBody @Valid FormSignUpDto userUpdate, @PathVariable Long userId) {
        return new ResponseEntity<>(userService.updateUser(userUpdate, userId), HttpStatus.OK);
    }

    @GetMapping("/export/manager")
    //lấy ra danh sách các nhân viên có chức vụ quản lý kho xuất excel
    public ResponseEntity<byte[]> exportToExcel() throws MyCustomRuntimeException {
        try {
            byte[] excelBytes = userService.exportToExcel(RoleName.ROLE_MANAGER);
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
    @GetMapping("/export/smk")
    //lấy ra danh sách các nhân viên có chức vụ quản lý siêu thị xuất excel
    public ResponseEntity<byte[]> exportToExcelSMK() throws MyCustomRuntimeException {
        try {
            byte[] excelBytes = userService.exportToExcel(RoleName.ROLE_SUPERMARKET);
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

    @PutMapping("/update-password/{userId}")
    //chức năng tạo mật khẩu mới khi quyên mâật khẩu
    public ResponseEntity<PasswordResponse> updatePasswordUser(@PathVariable Long userId) {
        return new ResponseEntity<>(userService.updatePasswordUser(userId), HttpStatus.OK);
    }
    @PutMapping("/edit-password")
    //chức năng thay đổi mật khẩu
    public ResponseEntity<UserResponse> editPassword(
            Authentication authentication,
            @RequestBody EditPassRequest pass) {
        return new ResponseEntity<>(userService.editPasswordUser(authentication, pass.getPasswordUser(), pass.getPasswordUserEdit()),HttpStatus.OK);
    }
}
 