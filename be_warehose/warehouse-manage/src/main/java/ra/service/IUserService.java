package ra.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import ra.dto.request.FormSignUpDto;
import ra.dto.request.StorageRequest;
import ra.dto.response.PasswordResponse;
import ra.dto.response.UserResponse;
import ra.exception.LoginException;
import ra.exception.MyCustomRuntimeException;
import ra.model.RoleName;
import ra.model.Users;

import java.io.IOException;
import java.util.Optional;

public interface IUserService {


    Optional<Users> findByUserName(String username);
    UserResponse save(FormSignUpDto form) throws MyCustomRuntimeException;
    byte[] exportToExcel(RoleName roleName) throws IOException, MyCustomRuntimeException;
    Page<UserResponse> findAllByEmailContainingIgnoreCase(String email, Pageable pageable);
    UserResponse changeStatus(Long userId);
    UserResponse updateUser(FormSignUpDto userUpdate, Long userId);
    PasswordResponse updatePasswordUser(Long userId);
    UserResponse editPasswordUser(Authentication authentication, String passwordUser, String passwordUserEdit);

}