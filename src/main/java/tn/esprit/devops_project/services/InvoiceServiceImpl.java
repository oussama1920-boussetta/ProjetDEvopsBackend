package tn.esprit.devops_project.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.devops_project.entities.Invoice;
import tn.esprit.devops_project.repositories.InvoiceDetailRepository;
import tn.esprit.devops_project.repositories.InvoiceRepository;
import tn.esprit.devops_project.repositories.OperatorRepository;
import tn.esprit.devops_project.repositories.SupplierRepository;
import tn.esprit.devops_project.services.iservices.IInvoiceService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class InvoiceServiceImpl implements IInvoiceService {

    final InvoiceRepository invoiceRepository;
    final OperatorRepository operatorRepository;
    final InvoiceDetailRepository invoiceDetailRepository;
    final SupplierRepository supplierRepository;

    private static final String INVOICE_NOT_FOUND_MESSAGE = "Invoice not found";

    @Override
    public List<Invoice> retrieveAllInvoices() {
        return invoiceRepository.findAll();
    }

    @Override
    @Transactional
    public void cancelInvoice(Long invoiceId) {
        // method 01
        var invoice = invoiceRepository.findById(invoiceId).orElseThrow(() -> new NullPointerException(INVOICE_NOT_FOUND_MESSAGE));
        invoice.setArchived(true);
        invoiceRepository.save(invoice);
        // method 02 (Avec JPQL)
        invoiceRepository.updateInvoice(invoiceId);
    }

    @Override
    public Invoice retrieveInvoice(Long invoiceId) {

        return invoiceRepository.findById(invoiceId).orElseThrow(() -> new NullPointerException(INVOICE_NOT_FOUND_MESSAGE));
    }

    @Override
    public List<Invoice> getInvoicesBySupplier(Long idSupplier) {
        var supplier = supplierRepository.findById(idSupplier).orElseThrow(() -> new NullPointerException("Supplier not found"));
        return new ArrayList<>(supplier.getInvoices());

    }

    @Override
    public void assignOperatorToInvoice(Long idOperator, Long idInvoice) {
        var invoice = invoiceRepository.findById(idInvoice).orElseThrow(() -> new NullPointerException(INVOICE_NOT_FOUND_MESSAGE));
        var operator = operatorRepository.findById(idOperator).orElseThrow(() -> new NullPointerException("Operator not found"));
        operator.getInvoices().add(invoice);
        operatorRepository.save(operator);
    }

    @Override
    public float getTotalAmountInvoiceBetweenDates(Date startDate, Date endDate) {
        return invoiceRepository.getTotalAmountInvoiceBetweenDates(startDate, endDate);
    }
    @Override
    public float calculateAverageInvoiceAmountForSupplier(Long supplierId, Date startDate, Date endDate) {
        var totalAmount = invoiceRepository.getTotalAmountForSupplierBetweenDates(supplierId, startDate, endDate);
        var numberOfInvoices = invoiceRepository.countInvoicesForSupplierBetweenDates(supplierId, startDate, endDate);
        return (numberOfInvoices == 0) ? 0 : totalAmount / numberOfInvoices;
    }


}