package tn.esprit.devops_project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.devops_project.entities.Stock;

import java.util.List;


public interface StockRepository extends JpaRepository<Stock, Long> {
    List<Stock> findByQuantityLessThan(int threshold);
}

