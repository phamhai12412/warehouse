package ra.model;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "storage")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Storage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String storageName;

    private Date created;

    private String address;

    @Enumerated(EnumType.STRING)
    private TypeStorage typeStorage;

    @OneToOne
    @JoinColumn(name = "user_id")
    private Users users;

    @Enumerated(EnumType.STRING)
    private StatusName statusName;

    @ManyToOne
    @JoinColumn(name = "zone_id")
    private Zone zone;

}
