package tn.esprit.devops_project.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InvoiceDetail implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idInvoiceDetail;
    int quantity;
    float price;
    @ManyToOne
    Product product;
    @ManyToOne
    @JsonIgnore
    Invoice invoice;

}
