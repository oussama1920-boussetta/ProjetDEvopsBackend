package tn.esprit.devops_project_test.services;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.devops_project.entities.InvoiceDetail;
import tn.esprit.devops_project.entities.Product;
import tn.esprit.devops_project.entities.ProductCategory;
import tn.esprit.devops_project.repositories.InvoiceDetailRepository;
import tn.esprit.devops_project.services.ProductServiceImpl;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    private ProductServiceImpl productService;

    @Mock
    private InvoiceDetailRepository invoiceDetailRepository;

    @Nested
    class GetTopAndLeastSoldProducts {

        @Test
        void WHEN_no_invoice_details_THEN_return_null_products() {
            var startDate = new Date();
            var endDate = new Date();
            when(invoiceDetailRepository.findByInvoiceDateCreationInvoiceBetween(startDate, endDate)).thenReturn(List.of());

            var result = productService.getTopAndLeastSoldProducts(startDate, endDate);

            assertNull(result);
            verify(invoiceDetailRepository, times(1)).findByInvoiceDateCreationInvoiceBetween(startDate, endDate);
        }

        @Test
        void WHEN_invoice_details_present_THEN_return_top_and_least_sold_products() {
            var startDate = new Date();
            var endDate = new Date();

            var product1 = Product.builder()
                    .idProduct(1L)
                    .title("Product 1")
                    .category(ProductCategory.ELECTRONICS)
                    .build();

            var product2 = Product.builder()
                    .idProduct(2L)
                    .title("Product 2")
                    .category(ProductCategory.ELECTRONICS)
                    .build();

            var invoiceDetail1 = InvoiceDetail.builder()
                    .product(product1)
                    .quantity(10)
                    .build();

            var invoiceDetail2 = InvoiceDetail.builder()
                    .product(product2)
                    .quantity(5)
                    .build();

            when(invoiceDetailRepository.findByInvoiceDateCreationInvoiceBetween(startDate, endDate)).thenReturn(List.of(invoiceDetail1, invoiceDetail2));

            var result = productService.getTopAndLeastSoldProducts(startDate, endDate);

            assertNotNull(result.getTopSealedProduct());
            assertNotNull(result.getLeastSealedProduct());

            assertEquals(1L, result.getTopSealedProduct().getProductId());
            assertEquals("Product 1", result.getTopSealedProduct().getProductTitle());
            assertEquals(ProductCategory.ELECTRONICS, result.getTopSealedProduct().getProductCategory());
            assertEquals(10, result.getTopSealedProduct().getQuantitySold());

            assertEquals(2L, result.getLeastSealedProduct().getProductId());
            assertEquals("Product 2", result.getLeastSealedProduct().getProductTitle());
            assertEquals(ProductCategory.ELECTRONICS, result.getLeastSealedProduct().getProductCategory());
            assertEquals(5, result.getLeastSealedProduct().getQuantitySold());

            verify(invoiceDetailRepository, times(1)).findByInvoiceDateCreationInvoiceBetween(startDate, endDate);
        }

        @Test
        void WHEN_many_invoice_details_and_products_THEN_return_top_and_least_sold_products() {
            var startDate = new Date();
            var endDate = new Date();

            var product1 = Product.builder()
                    .idProduct(1L)
                    .title("Product 1")
                    .category(ProductCategory.ELECTRONICS)
                    .build();

            var product2 = Product.builder()
                    .idProduct(2L)
                    .title("Product 2")
                    .category(ProductCategory.ELECTRONICS)
                    .build();

            var product3 = Product.builder()
                    .idProduct(3L)
                    .title("Product 3")
                    .category(ProductCategory.ELECTRONICS)
                    .build();

            var invoiceDetail1 = InvoiceDetail.builder()
                    .product(product1)
                    .quantity(10)
                    .build();

            var invoiceDetail2 = InvoiceDetail.builder()
                    .product(product2)
                    .quantity(5)
                    .build();

            var invoiceDetail3 = InvoiceDetail.builder()
                    .product(product3)
                    .quantity(8)
                    .build();

            when(invoiceDetailRepository.findByInvoiceDateCreationInvoiceBetween(startDate, endDate))
                    .thenReturn(List.of(invoiceDetail1, invoiceDetail2, invoiceDetail3));

            var result = productService.getTopAndLeastSoldProducts(startDate, endDate);

            assertNotNull(result.getTopSealedProduct());
            assertNotNull(result.getLeastSealedProduct());

            assertEquals(1L, result.getTopSealedProduct().getProductId());
            assertEquals("Product 1", result.getTopSealedProduct().getProductTitle());
            assertEquals(ProductCategory.ELECTRONICS, result.getTopSealedProduct().getProductCategory());
            assertEquals(10, result.getTopSealedProduct().getQuantitySold());

            assertEquals(2L, result.getLeastSealedProduct().getProductId());
            assertEquals("Product 2", result.getLeastSealedProduct().getProductTitle());
            assertEquals(ProductCategory.ELECTRONICS, result.getLeastSealedProduct().getProductCategory());
            assertEquals(5, result.getLeastSealedProduct().getQuantitySold());

            verify(invoiceDetailRepository, times(1)).findByInvoiceDateCreationInvoiceBetween(startDate, endDate);
        }
    }
}
