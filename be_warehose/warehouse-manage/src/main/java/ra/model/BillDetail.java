package ra.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "bill_detail")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class BillDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private int quantity;

    @ManyToOne
    @JoinColumn(name = "bill_id")
    private Bill bill;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

}
