package tn.esprit.devops_project.controllers;

import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import tn.esprit.devops_project.dto.ProductSalesResultDto;
import tn.esprit.devops_project.entities.Product;
import tn.esprit.devops_project.entities.ProductCategory;
import tn.esprit.devops_project.services.iservices.IProductService;

import java.util.Date;
import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:8082")
public class ProductController {

    private final IProductService productService;

    @PostMapping("/product/{idStock}")
    public Product addProduct(@RequestBody Product product, @PathVariable Long idStock) {
        return productService.addProduct(product, idStock);
    }

    @GetMapping("/product/{id}")
    public Product retrieveProduct(@PathVariable Long id) {
        return productService.retrieveProduct(id);
    }

    @GetMapping("/product")
    public List<Product> retrieveAllProduct() {
        return productService.retreiveAllProduct();
    }

    @GetMapping("/product/stock/{id}")
    public List<Product> retrieveProductStock(@PathVariable Long id) {
        return productService.retreiveProductStock(id);
    }

    @GetMapping("/productCategoy/{category}")
    public List<Product> retrieveProductByCategory(@PathVariable ProductCategory category) {
        return productService.retrieveProductByCategory(category);
    }

    @DeleteMapping("/product/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }

    @GetMapping("/products/sales")
    public ProductSalesResultDto getTopAndLeastSoldProducts(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        return productService.getTopAndLeastSoldProducts(startDate, endDate);
    }
}
