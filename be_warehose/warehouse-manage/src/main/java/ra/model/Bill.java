package ra.model;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "bill")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Temporal(TemporalType.DATE)
    private Date created;

    private double total;

    private double priceShip;
    private String locationStart;
    private String locationEnd;

    private boolean status;


    @ManyToOne
    @JoinColumn(name = "ship_id")
    private Shipment shipment;

    @ManyToOne
    @JoinColumn(name = "storage_start")
    private Storage start;

    @ManyToOne
    @JoinColumn(name = "storage_end")
    private Storage end;

    @Enumerated(EnumType.STRING)
    private DeliveryName delivery;

}
