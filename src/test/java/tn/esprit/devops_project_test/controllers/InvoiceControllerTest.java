package tn.esprit.devops_project_test.controllers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import tn.esprit.devops_project.DevopsProjectSpringBootApplication;
import tn.esprit.devops_project.entities.Invoice;
import tn.esprit.devops_project.entities.Operator;
import tn.esprit.devops_project.entities.Supplier;
import tn.esprit.devops_project.entities.SupplierCategory;
import tn.esprit.devops_project.repositories.InvoiceRepository;
import tn.esprit.devops_project.repositories.OperatorRepository;
import tn.esprit.devops_project.repositories.SupplierRepository;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = DevopsProjectSpringBootApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class InvoiceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private OperatorRepository operatorRepository;

    @BeforeEach
    public void setUp() {

        var supplier = Supplier.builder()
                .code("SUP001")
                .label("Supplier 1")
                .supplierCategory(SupplierCategory.ORDINAIRE)
                .invoices(new HashSet<>())
                .build();

        supplierRepository.save(supplier);

        var calendar = Calendar.getInstance();
        calendar.set(2024, Calendar.MARCH, 1);
        var startDate = calendar.getTime();

        calendar.set(2024, Calendar.MAY, 1);
        var endDate = calendar.getTime();

        var invoice1 = Invoice.builder()
                .amountDiscount(10)
                .amountInvoice(100)
                .dateCreationInvoice(startDate)
                .dateLastModificationInvoice(new Date())
                .supplier(supplier)
                .archived(false)
                .build();

        var invoice2 = Invoice.builder()
                .amountDiscount(20)
                .amountInvoice(200)
                .dateCreationInvoice(endDate)
                .dateLastModificationInvoice(new Date())
                .archived(false)
                .build();

        invoiceRepository.save(invoice1);
        invoiceRepository.save(invoice2);

        var operator = Operator.builder()
                .fname("first name operator")
                .lname("last name operator")
                .password("test password")
                .invoices(new HashSet<>())
                .build();

        operatorRepository.save(operator);
    }

    @AfterEach
    public void tearDown() {
        operatorRepository.deleteAll();
        invoiceRepository.deleteAll();
        supplierRepository.deleteAll();
    }


    @Nested
    class RetrieveInvoices {

        @Test
        void WHEN_retrieve_invoices_THEN_ok() throws Exception {
            mockMvc.perform(get("/invoice")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()", is(2)))
                    .andExpect(jsonPath("$[0].amountInvoice", is(100.0)))
                    .andExpect(jsonPath("$[1].amountInvoice", is(200.0)));
        }
    }

    @Nested
    class RetrieveInvoice {

        @Test
        void WHEN_retrieve_invoice_WITH_correct_id_THEN_return_ok() throws Exception {
            var id = invoiceRepository.findAll().get(0).getIdInvoice();
            mockMvc.perform(get("/invoice/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.amountInvoice", is(100.0)));
        }

    }

    @Nested
    class CancelInvoice {

        @Test
        void WHEN_cancel_invoice_WITH_correct_id_THEN_return_ok() throws Exception {
            var id = invoiceRepository.findAll().get(0).getIdInvoice();
            mockMvc.perform(put("/invoice/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());

            var archivedInvoiceOptional = invoiceRepository.findById(id);
            assertTrue(archivedInvoiceOptional.isPresent());
            Invoice archivedInvoice = archivedInvoiceOptional.get();
            assertThat(archivedInvoice.getArchived(), is(true));
        }

    }

    @Nested
    class GetInvoicesBySupplier {

        @Test
        void WHEN_get_invoices_by_supplier_THEN_return_ok() throws Exception {
            var supplierId = supplierRepository.findAll().get(0).getIdSupplier();
            mockMvc.perform(get("/invoice/supplier/{supplierId}", supplierId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$").isNotEmpty())
                    .andExpect(jsonPath("$.length()", is(1)))
                    .andExpect(jsonPath("$[0].idInvoice").value(invoiceRepository.findAll().get(0).getIdInvoice()));
        }
    }

    @Nested
    class GetTotalAmountInvoiceBetweenDates {

        @Test
        void WHEN_get_total_amount_invoice_between_dates_THEN_ok() throws Exception {
            var startDate = invoiceRepository.findAll().get(0).getDateCreationInvoice();
            var endDate = invoiceRepository.findAll().get(1).getDateCreationInvoice();

            float expectedTotalAmount = calculateExpectedTotalAmount(startDate, endDate);

            mockMvc.perform(get("/invoice/price/{startDate}/{endDate}", startDate, endDate)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().string(String.valueOf(expectedTotalAmount)));
        }

        private float calculateExpectedTotalAmount(Date startDate, Date endDate) {
            return (float) invoiceRepository.findAll().stream()
                    .filter(invoice -> invoice.getDateCreationInvoice().compareTo(startDate) >= 0 && invoice.getDateCreationInvoice().compareTo(endDate) <= 0)
                    .mapToDouble(Invoice::getAmountInvoice)
                    .sum();
        }
    }

}

