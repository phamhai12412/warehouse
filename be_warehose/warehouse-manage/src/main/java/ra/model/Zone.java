package ra.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "zone")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Zone {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String zoneName;

}

