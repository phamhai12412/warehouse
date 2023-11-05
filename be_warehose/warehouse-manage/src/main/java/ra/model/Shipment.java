package ra.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "shipment")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String shipName;

    private double price;

    private boolean status;
}
