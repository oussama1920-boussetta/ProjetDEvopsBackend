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
class StockServiceImplTest {

    @InjectMocks
    private StockServiceImpl stockService;

    @Mock
    private StockRepository stockRepository;

    @Nested
    class AddStock {
        @Test
        void WHEN_add_stock_THEN_return_stock() {
            Stock stockToAdd = new Stock();
            stockToAdd.setTitle("Test Stock");
            stockToAdd.setQuantity(10);

            when(stockRepository.save(stockToAdd)).thenReturn(stockToAdd);

            Stock result = stockService.addStock(stockToAdd);

            assertNotNull(result);
            assertEquals("Test Stock", result.getTitle());
            assertEquals(10, result.getQuantity());
            verify(stockRepository, times(1)).save(stockToAdd);
        }
    }

    @Nested
    class RetrieveAllStocks {
        @Test
        void WHEN_retrieve_all_stocks_THEN_return_list_of_stocks() {
            var stocks = List.of(new Stock(), new Stock());
            when(stockRepository.findAll()).thenReturn(stocks);

            var result = stockService.retrieveAllStock();
            assertEquals(2, result.size());
            verify(stockRepository, times(1)).findAll();
        }
    }

    @Nested
    class RetrieveStock {
        @Test
        void WHEN_retrieve_stock_by_id_THEN_return_stock() {
            Stock stock = new Stock();
            stock.setIdStock(1L);
            when(stockRepository.findById(1L)).thenReturn(Optional.of(stock));

            var result = stockService.retrieveStock(1L);
            assertNotNull(result);
            assertEquals(1L, result.getIdStock());
            verify(stockRepository, times(1)).findById(1L);
        }

        @Test
        void WHEN_retrieve_stock_by_id_not_found_THEN_throw_exception() {
            when(stockRepository.findById(2L)).thenReturn(Optional.empty());

            assertThrows(NullPointerException.class, () -> stockService.retrieveStock(2L));
            verify(stockRepository, times(1)).findById(2L);
        }
    }

    @Nested
    class FindStocksBelowThreshold {
        @Test
        void WHEN_find_stocks_below_threshold_THEN_return_list_of_stocks() {
            int threshold = 5;
            var stocks = List.of(new Stock(), new Stock());
            when(stockRepository.findByQuantityLessThan(threshold)).thenReturn(stocks);

            var result = stockService.findStocksBelowThreshold(threshold);
            assertEquals(2, result.size());
            verify(stockRepository, times(1)).findByQuantityLessThan(threshold);
        }
    }


}