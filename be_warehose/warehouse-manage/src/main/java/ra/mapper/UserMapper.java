package ra.mapper;

import org.omg.PortableServer.IdUniquenessPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ra.dto.request.FormSignUpDto;
import ra.dto.response.UserResponse;
import ra.model.RoleName;
import ra.model.Users;
import ra.repository.IStorageRepository;
import ra.repository.IUserRepository;

@Component
public class UserMapper implements IGenericMapper<Users, FormSignUpDto, UserResponse> {
    @Autowired
    private IStorageRepository storageRepository;
    @Override
    public Users toEntity(FormSignUpDto formSignUpDto) {
        return Users.builder()
                .email(formSignUpDto.getEmail())
                .firstName(formSignUpDto.getFirstName())
                .lastName(formSignUpDto.getLastName())
                .address(formSignUpDto.getAddress())
                .phone(formSignUpDto.getPhone())
                .dateOfBirth(formSignUpDto.getDateOfBirth())
                .sex(formSignUpDto.isSex())
                .build();
    }

    @Override
    public UserResponse toResponse(Users users) {
        return UserResponse.builder()
                .firstName(users.getFirstName())
                .lastName(users.getLastName())
                .id(users.getId())
                .roles(users.getRoles().getRoleName().toString())
                .address(users.getAddress())
                .phone(users.getPhone())
                .sex(users.isSex())
                .status(users.isStatus())
               .dateOfBirth(users.getDateOfBirth().toString())
                .storageName(users.getRoles().getRoleName().equals(RoleName.ROLE_ADMIN)?null: storageRepository.findByUsers_Id(users.getId()).getStorageName())
                .email(users.getEmail())
                .build();
    }
}
