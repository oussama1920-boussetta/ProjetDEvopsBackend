package tn.esprit.devops_project_test.services;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.devops_project.entities.Invoice;
import tn.esprit.devops_project.entities.Operator;
import tn.esprit.devops_project.entities.Supplier;
import tn.esprit.devops_project.repositories.InvoiceRepository;
import tn.esprit.devops_project.repositories.OperatorRepository;
import tn.esprit.devops_project.repositories.SupplierRepository;
import tn.esprit.devops_project.services.InvoiceServiceImpl;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InvoiceServiceImplTest {

    @InjectMocks
    private InvoiceServiceImpl invoiceService;

    @Mock
    private InvoiceRepository invoiceRepository;

    @Mock
    private OperatorRepository operatorRepository;

    @Mock
    private SupplierRepository supplierRepository;

    @Nested
    class RetrieveAllInvoices {
        @Test
        void WHEN_retrieve_all_invoices_THEN_return_ok() {
            var invoices = List.of(new Invoice(), new Invoice());
            when(invoiceRepository.findAll()).thenReturn(invoices);

            var result = invoiceService.retrieveAllInvoices();
            assertEquals(2, result.size());
            verify(invoiceRepository, times(1)).findAll();
        }
    }

    @Nested
    class RetrieveInvoice {

        @Nested
        class OK {

            @Test
            void WHEN_retrieve_invoice_WITH_correct_id_THEN_ok() {
                var invoice = new Invoice();
                when(invoiceRepository.findById(1L)).thenReturn(Optional.of(invoice));

                var result = invoiceService.retrieveInvoice(1L);
                assertNotNull(result);
                verify(invoiceRepository, times(1)).findById(1L);
            }
        }

        @Nested
        class KO {

            @Test
            void WHEN_retrieve_invoice_WITH_id_not_found_THEN_throw_exception() {
                when(invoiceRepository.findById(2L)).thenReturn(Optional.empty());

                var exception = assertThrows(NullPointerException.class, () -> {
                    invoiceService.retrieveInvoice(2L);
                });

                assertEquals("Invoice not found", exception.getMessage());
                verify(invoiceRepository, times(1)).findById(2L);
            }
        }
    }

    @Nested
    class CancelInvoice {

        @Nested
        class OK {

            @Test
            void WHEN_cancel_invoice_WITH_correct_id_THEN_ok() {
                var invoice = new Invoice();
                when(invoiceRepository.findById(1L)).thenReturn(Optional.of(invoice));

                invoiceService.cancelInvoice(1L);

                verify(invoiceRepository, times(1)).findById(1L);
                verify(invoiceRepository, times(1)).save(invoice);
                assertTrue(invoice.getArchived());
            }
        }

        @Nested
        class KO {

            @Test
            void WHEN_cancel_invoice_WITH_id_not_found_THEN_throw_exception() {
                when(invoiceRepository.findById(2L)).thenReturn(Optional.empty());

                var exception = assertThrows(NullPointerException.class, () -> {
                    invoiceService.cancelInvoice(2L);
                });

                assertEquals("Invoice not found", exception.getMessage());
                verify(invoiceRepository, times(1)).findById(2L);
                verify(invoiceRepository, never()).save(any(Invoice.class));
            }
        }
    }

    @Nested
    class GetInvoicesBySupplier {

        @Nested
        class OK {
            @Test
            void WHEN_getInvoicesBySupplier_WITH_correct_id_THEN_ok() {
                var supplier = new Supplier();
                var invoice1 = new Invoice();
                var invoice2 = new Invoice();
                supplier.setInvoices(Set.of(invoice1, invoice2));

                when(supplierRepository.findById(1L)).thenReturn(Optional.of(supplier));

                List<Invoice> result = invoiceService.getInvoicesBySupplier(1L);
                assertNotNull(result);
                assertEquals(2, result.size());
                assertTrue(result.contains(invoice1));
                assertTrue(result.contains(invoice2));
                verify(supplierRepository, times(1)).findById(1L);
            }
        }

        @Nested
        class KO {

            @Test
            void WHEN_getInvoicesBySupplier_WITH_id_not_found_THEN_throw_exception() {
                when(supplierRepository.findById(2L)).thenReturn(Optional.empty());

                var exception = assertThrows(NullPointerException.class, () -> {
                    invoiceService.getInvoicesBySupplier(2L);
                });

                assertEquals("Supplier not found", exception.getMessage());
                verify(supplierRepository, times(1)).findById(2L);
            }
        }
    }


    @Nested
    class AssignOperatorToInvoice {

        @Nested
        class OK {

            @Test
            void WHEN_assignOperatorToInvoice_WITH_correct_ids_THEN_ok() {
                var invoice = new Invoice();
                var operator = new Operator();

                when(invoiceRepository.findById(1L)).thenReturn(Optional.of(invoice));
                when(operatorRepository.findById(1L)).thenReturn(Optional.of(operator));

                invoiceService.assignOperatorToInvoice(1L, 1L);

                assertTrue(operator.getInvoices().contains(invoice));
                verify(invoiceRepository, times(1)).findById(1L);
                verify(operatorRepository, times(1)).findById(1L);
                verify(operatorRepository, times(1)).save(operator);
            }

        }

        @Nested
        class KO {

            @Test
            void WHEN_assignOperatorToInvoice_WITH_invoice_id_not_found_THEN_throw_exception() {
                when(invoiceRepository.findById(1L)).thenReturn(Optional.empty());

                var exception = assertThrows(NullPointerException.class, () -> {
                    invoiceService.assignOperatorToInvoice(1L, 1L);
                });

                assertEquals("Invoice not found", exception.getMessage());
                verify(invoiceRepository, times(1)).findById(1L);
                verify(operatorRepository, never()).findById(anyLong());
                verify(operatorRepository, never()).save(any(Operator.class));
            }

            @Test
            void WHEN_assignOperatorToInvoice_WITH_operator_id_not_found_THEN_throw_exception() {
                var invoice = new Invoice();

                when(invoiceRepository.findById(1L)).thenReturn(Optional.of(invoice));
                when(operatorRepository.findById(1L)).thenReturn(Optional.empty());

                var exception = assertThrows(NullPointerException.class, () -> {
                    invoiceService.assignOperatorToInvoice(1L, 1L);
                });

                assertEquals("Operator not found", exception.getMessage());
                verify(invoiceRepository, times(1)).findById(1L);
                verify(operatorRepository, times(1)).findById(1L);
                verify(operatorRepository, never()).save(any(Operator.class));
            }

        }

    }

    @Nested
    class GetTotalAmountInvoicesBetweenDates {

        @Test
        void WHEN_get_total_amount_invoices_between_dates_THEN_ok() {
            var startDate = new Date();
            var endDate = new Date();
            when(invoiceRepository.getTotalAmountInvoiceBetweenDates(startDate, endDate)).thenReturn(300f);

            var result = invoiceService.getTotalAmountInvoiceBetweenDates(startDate, endDate);
            assertEquals(300f, result, 0);
            verify(invoiceRepository, times(1)).getTotalAmountInvoiceBetweenDates(startDate, endDate);
        }
    }
}

