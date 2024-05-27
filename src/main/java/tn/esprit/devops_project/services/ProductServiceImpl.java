package tn.esprit.devops_project.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.esprit.devops_project.dto.ProductSalesDto;
import tn.esprit.devops_project.dto.ProductSalesResultDto;
import tn.esprit.devops_project.entities.InvoiceDetail;
import tn.esprit.devops_project.entities.Product;
import tn.esprit.devops_project.entities.ProductCategory;
import tn.esprit.devops_project.repositories.InvoiceDetailRepository;
import tn.esprit.devops_project.repositories.InvoiceRepository;
import tn.esprit.devops_project.repositories.ProductRepository;
import tn.esprit.devops_project.repositories.StockRepository;
import tn.esprit.devops_project.services.iservices.IProductService;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class ProductServiceImpl implements IProductService {

    final ProductRepository productRepository;
    final StockRepository stockRepository;
    final InvoiceRepository invoiceRepository;
    final InvoiceDetailRepository invoiceDetailRepository;

    @Override
    public Product addProduct(Product product, Long idStock) {
        var stock = stockRepository.findById(idStock).orElseThrow(() -> new NullPointerException("stock not found"));
        product.setStock(stock);
        return productRepository.save(product);
    }

    @Override
    public Product retrieveProduct(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new NullPointerException("Product not found"));
    }

    @Override
    public List<Product> retreiveAllProduct() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> retrieveProductByCategory(ProductCategory category) {
        return productRepository.findByCategory(category);
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public List<Product> retreiveProductStock(Long id) {
        return productRepository.findByStockIdStock(id);
    }

    @Override
    public ProductSalesResultDto getTopAndLeastSoldProducts(Date startDate, Date endDate) {
        List<InvoiceDetail> invoiceDetails = invoiceDetailRepository.findByInvoiceDateCreationInvoiceBetween(startDate, endDate);

        Map<Product, Integer> productSales = invoiceDetails.stream()
                .collect(Collectors.groupingBy(
                        InvoiceDetail::getProduct,
                        Collectors.summingInt(InvoiceDetail::getQuantity)
                ));

        if (productSales.isEmpty()) {
            return null;
        }

        Map.Entry<Product, Integer> topProductEntry = productSales.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElseThrow();

        Map.Entry<Product, Integer> leastProductEntry = productSales.entrySet().stream()
                .min(Map.Entry.comparingByValue())
                .orElseThrow();

        var topProduct = ProductSalesDto.builder()
                .productId(topProductEntry.getKey().getIdProduct())
                .productTitle(topProductEntry.getKey().getTitle())
                .productCategory(topProductEntry.getKey().getCategory())
                .quantitySold(topProductEntry.getValue())
                .build();

        var leastProduct = ProductSalesDto.builder()
                .productId(leastProductEntry.getKey().getIdProduct())
                .productTitle(leastProductEntry.getKey().getTitle())
                .productCategory(leastProductEntry.getKey().getCategory())
                .quantitySold(leastProductEntry.getValue())
                .build();

        return ProductSalesResultDto.builder()
                .topSealedProduct(topProduct)
                .leastSealedProduct(leastProduct)
                .build();
    }
}
