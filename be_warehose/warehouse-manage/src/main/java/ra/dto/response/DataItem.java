package ra.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataItem {
   private List<Integer> data;
  private List<String> backgroundColor;
   private List<String> borderColor;
   private Long borderWidth;
}
