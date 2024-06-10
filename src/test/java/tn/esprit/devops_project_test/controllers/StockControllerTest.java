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
import tn.esprit.devops_project.entities.Stock;
import tn.esprit.devops_project.repositories.StockRepository;
import tn.esprit.devops_project.services.StockServiceImpl;
import java.util.List;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = DevopsProjectSpringBootApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class StockControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private StockServiceImpl stockService;

    @BeforeEach
    public void setUp() {
        // Add test data if needed
    }

    @AfterEach
    public void tearDown() {
        // Clean up test data if needed
    }

    @Nested
    class AddStock {

        @Test
        void WHEN_add_stock_THEN_return_created() throws Exception {
            String stockJson = "{\"title\":\"Test Stock\",\"quantity\":10}";

            mockMvc.perform(post("/stock")
                            .content(stockJson)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.title", is("Test Stock")))
                    .andExpect(jsonPath("$.quantity", is(10)));
        }
    }

    @Nested
    class RetrieveStock {

        @Test
        void WHEN_retrieve_stock_by_id_THEN_return_stock() throws Exception {
            // Add stock data
            Stock stock = new Stock();
            stock.setTitle("Test Stock");
            stock.setQuantity(10);
            stockRepository.save(stock);

            // Perform GET request
            mockMvc.perform(get("/stock/{id}", stock.getIdStock())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.title", is("Test Stock")))
                    .andExpect(jsonPath("$.quantity", is(10)));
        }
    }

    @Nested
    class RetrieveAllStocks {

        @Test
        void WHEN_retrieve_all_stocks_THEN_return_ok() throws Exception {
            mockMvc.perform(get("/stock")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(0))));
        }
    }

    @Nested
    class FindStocksBelowThreshold {

        @Test
        void WHEN_find_stocks_below_threshold_THEN_return_list_of_stocks() throws Exception {
            int threshold = 5;
            var stocks = List.of(new Stock(), new Stock());
            when(stockService.findStocksBelowThreshold(threshold)).thenReturn(stocks);

            mockMvc.perform(get("/stock/below-threshold?threshold=" + threshold)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)));
        }
    }


}