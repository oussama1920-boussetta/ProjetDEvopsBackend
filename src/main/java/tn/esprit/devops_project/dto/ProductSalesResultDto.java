package tn.esprit.devops_project.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ProductSalesResultDto {
    private ProductSalesDto topSealedProduct;
    private ProductSalesDto leastSealedProduct;
}
