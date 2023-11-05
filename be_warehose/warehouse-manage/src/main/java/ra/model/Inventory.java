package ra.model;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "inventory")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String note;
    @Temporal(TemporalType.DATE)
    private Date created;

    @ManyToOne
    @JoinColumn(name = "storage_id")
    private Storage storage;

}
