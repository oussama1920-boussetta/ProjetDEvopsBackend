package tn.esprit.devops_project.controllers;


import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.devops_project.entities.Stock;
import tn.esprit.devops_project.services.iservices.IStockService;
import java.util.List;

@CrossOrigin(origins = "http://localhost:8082")
@RestController
@AllArgsConstructor
public class StockController {

    IStockService stockService;

    @PostMapping("/stock")
    public ResponseEntity<Stock> addStock(@RequestBody Stock stock) {
        Stock savedStock = stockService.addStock(stock);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedStock);
    }

    @GetMapping("/stock/{id}")
    public Stock retrieveStock(@PathVariable Long id){
        return stockService.retrieveStock(id);
    }

    @GetMapping("/stock")
    public List<Stock> retrieveAllStock(){
        return stockService.retrieveAllStock();
    }

    @GetMapping("/stock/below-threshold")
    public List<Stock> findStocksBelowThreshold(@RequestParam int threshold) {
        return stockService.findStocksBelowThreshold(threshold);
    }


}
