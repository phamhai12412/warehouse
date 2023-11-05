package ra.model;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "product")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String productName;

    private double price;

    private double weight;

    private String code;

    @Temporal(TemporalType.DATE)
    private Date created;

    @Temporal(TemporalType.DATE)
    private Date dueDate;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "storage_id")
    private Storage storage;

    private int quantity;

    @Enumerated(EnumType.STRING)
    private StatusName statusName;

}
