package tn.esprit.devops_project.dto;


import lombok.*;
import tn.esprit.devops_project.entities.ProductCategory;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ProductSalesDto {
    private Long productId;
    private String productTitle;
    private ProductCategory productCategory;
    private int quantitySold;
}
