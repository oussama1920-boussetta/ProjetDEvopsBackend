package tn.esprit.devops_project.services.iservices;

import tn.esprit.devops_project.dto.ProductSalesResultDto;
import tn.esprit.devops_project.entities.Product;
import tn.esprit.devops_project.entities.ProductCategory;

import java.util.Date;
import java.util.List;

public interface IProductService {

    Product addProduct(Product product, Long idStock);

    Product retrieveProduct(Long id);

    List<Product> retreiveAllProduct();

    List<Product> retrieveProductByCategory(ProductCategory category);

    void deleteProduct(Long id);

    List<Product> retreiveProductStock(Long id);

    ProductSalesResultDto getTopAndLeastSoldProducts(Date startDate, Date endDate);

}
