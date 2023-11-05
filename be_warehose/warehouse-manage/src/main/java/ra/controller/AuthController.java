package ra.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;
import ra.dto.request.FormSignInDto;
import ra.dto.request.FormSignUpDto;
import ra.dto.response.JwtResponse;
import ra.dto.response.StorageResponse;
import ra.dto.response.UserResponse;
import ra.model.RoleName;
import ra.model.Storage;
import ra.model.Users;
import ra.repository.IStorageRepository;
import ra.repository.IUserRepository;
import ra.security.jwt.JwtProvider;
import ra.security.user_principal.UserPrinciple;
import ra.service.IStorageService;
import ra.service.IUserService;
import javax.security.auth.login.LoginException;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/sign")
@CrossOrigin("*")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private IStorageRepository storageRepository;
    @Autowired
    private IUserService userService;
    @GetMapping
    public ResponseEntity<String> home() {
        return ResponseEntity.ok("xác thực thành công");
    }

    @PostMapping("/sign-in")
    //đăng nhập
    public ResponseEntity<JwtResponse> signin(@RequestBody @Valid FormSignInDto formSignInDto) throws LoginException {
        Authentication authentication = null;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(formSignInDto.getUsername(), formSignInDto.getPassword())
            );
        } catch (AuthenticationException e) {
            throw new LoginException("Tài khoản hoặc mật khẩu không chính xác!");
        }
        UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
        if(!userPrinciple.isStatus()){
            throw new LoginException("Tài khoản bị khóa!");
        }
        String token = jwtProvider.generateToken(userPrinciple);
        List<String> roles = userPrinciple.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        Long storageId=-1L;
        String storage=null;
        if(!(roles.get(0).equals("ROLE_ADMIN"))){
            storageId =storageRepository.findByUsers_Id(userPrinciple.getId()).getId();
          storage =storageRepository.findByUsers_Id(userPrinciple.getId()).getStorageName();
        }

        return ResponseEntity.ok(JwtResponse.builder().token(token)
                .lastName(userPrinciple.getLastName())
                .firstName(userPrinciple.getFirstName())
                .username(userPrinciple.getUsername())
                        .dateOfBirth(userPrinciple.getDateOfBirth().toString())
                        .sex(userPrinciple.isSex())
                        .address(userPrinciple.getAddress())
                .storageId(storageId)
                .phone(userPrinciple.getPhone())
                .id(userPrinciple.getId())
                .roles(roles)
                .type("Bearer")
                .build());
    }

    @PostMapping("/sign-up")
    //đăng kí
    public ResponseEntity<UserResponse> signup(@RequestBody @Valid FormSignUpDto formSignUpDto) {



        return new ResponseEntity<>(userService.save(formSignUpDto), HttpStatus.CREATED);

    }

}
