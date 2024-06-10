package tn.esprit.devops_project_test.services;


import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.devops_project.entities.Stock;
import tn.esprit.devops_project.repositories.StockRepository;
import tn.esprit.devops_project.services.StockServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockServiceTest {

    @InjectMocks
    private StockServiceImpl stockService;

    @Mock
    private StockRepository stockRepository;

    @Nested
    class AddStock {

        @Test
        void WHEN_add_stock_THEN_return_stock() {
            var stock = new Stock();
            when(stockRepository.save(stock)).thenReturn(stock);

            var result = stockService.addStock(stock);
            assertNotNull(result);
            verify(stockRepository, times(1)).save(stock);
        }
    }

    @Nested
    class RetrieveStock {

        @Test
        void WHEN_retrieve_stock_WITH_correct_id_THEN_return_stock() {
            var stock = new Stock();
            when(stockRepository.findById(1L)).thenReturn(Optional.of(stock));

            var result = stockService.retrieveStock(1L);
            assertNotNull(result);
            verify(stockRepository, times(1)).findById(1L);
        }

        @Test
        void WHEN_retrieve_stock_WITH_incorrect_id_THEN_throw_exception() {
            when(stockRepository.findById(2L)).thenReturn(Optional.empty());

            var exception = assertThrows(NullPointerException.class, () -> stockService.retrieveStock(2L));

            assertEquals("Stock not found", exception.getMessage());
            verify(stockRepository, times(1)).findById(2L);
        }
    }

    @Nested
    class RetrieveAllStock {

        @Test
        void WHEN_retrieve_all_stock_THEN_return_stock_list() {
            var stocks = List.of(new Stock(), new Stock());
            when(stockRepository.findAll()).thenReturn(stocks);

            var result = stockService.retrieveAllStock();
            assertEquals(2, result.size());
            verify(stockRepository, times(1)).findAll();
        }
    }

    @Nested
    class FindStocksBelowThreshold {

        @Test
        void WHEN_find_stocks_below_threshold_THEN_return_stock_list() {
            var stocks = List.of(new Stock(), new Stock());
            when(stockRepository.findByQuantityLessThan(15)).thenReturn(stocks);

            var result = stockService.findStocksBelowThreshold(15);
            assertEquals(2, result.size());
            verify(stockRepository, times(1)).findByQuantityLessThan(15);
        }
    }

    @Nested
    class AdvancedTest {

        @Test
        void WHEN_find_stocks_with_quantity_above_threshold_THEN_return_stock_list() {
            var stocks = List.of(new Stock(), new Stock());
            when(stockRepository.findByQuantityLessThan(30)).thenReturn(stocks);

            var result = stockService.findStocksBelowThreshold(30);
            assertEquals(2, result.size());
            verify(stockRepository, times(1)).findByQuantityLessThan(30);
        }
    }
}
