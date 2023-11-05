package ra.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "inventory_detail")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class InventoryDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private int quantity;
    private int quantityToday;
    private String reason;
    @ManyToOne
    @JoinColumn(name = "inventory_id")
    private Inventory inventory;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

}
