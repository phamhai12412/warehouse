package ra.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ra.model.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BillResponse {
    @JsonIgnoreProperties({"status", "delivery", "start", "end"})
    private Bill bill;
    private String delivery;
    @JsonIgnoreProperties({"email", "password", "address", "sex", "dateOfBirth", "roles", "status"})
    private Users userCreate;
    @JsonIgnoreProperties({"created", "statusName"})
    private Storage start;
    @JsonIgnoreProperties({"created", "statusName"})
    private Storage end;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private String created;
    private List<BillDetailResponse> billDetais;
}
