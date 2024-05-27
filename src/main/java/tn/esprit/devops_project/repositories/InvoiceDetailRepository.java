package tn.esprit.devops_project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tn.esprit.devops_project.entities.InvoiceDetail;

import java.util.Date;
import java.util.List;

public interface InvoiceDetailRepository extends JpaRepository<InvoiceDetail, Long> {

    @Query("SELECT i FROM InvoiceDetail i WHERE i.invoice.dateCreationInvoice BETWEEN :startDate AND :endDate")
    List<InvoiceDetail> findByInvoiceDateCreationInvoiceBetween(Date startDate, Date endDate);
}
