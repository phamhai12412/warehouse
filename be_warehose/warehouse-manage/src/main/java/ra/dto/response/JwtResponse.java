package ra.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {
    private Long id;
    private String token;
    private String type = "Bearer";
    private String firstName;
    private String lastName;
    private String username;
    private boolean status;
    private String phone;
    private List<String> roles = new ArrayList<>();
    private Long storageId;
    private String address;
    private String dateOfBirth;
    private boolean sex;
}