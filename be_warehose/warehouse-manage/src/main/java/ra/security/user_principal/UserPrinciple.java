package ra.security.user_principal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ra.model.Users;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPrinciple implements UserDetails {
    private Long id;
    private String firstName;

    private String lastName;
    private String email;
    @JsonIgnore
    private String password;
    private String phone;
    private String address;
    private boolean sex;
    private Date dateOfBirth;
    private boolean status;
    private Collection<? extends GrantedAuthority> authorities;

    public static UserPrinciple build(Users user) {
        List<GrantedAuthority> list = new ArrayList<>();
        list.add(new SimpleGrantedAuthority(user.getRoles().getRoleName().name()));
        return UserPrinciple.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .password(user.getPassword())
                .phone(user.getPhone())
                .sex(user.isSex())
                .address(user.getAddress())
                .dateOfBirth(user.getDateOfBirth())
                .status(user.isStatus())
                .authorities(list)
                .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
