

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

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = DevopsProjectSpringBootApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class StockControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StockRepository stockRepository;

    @BeforeEach
    public void setUp() {
        var stock1 = Stock.builder().title("Stock 1").quantity(10).build();
        var stock2 = Stock.builder().title("Stock 2").quantity(20).build();
        stockRepository.saveAll(List.of(stock1, stock2));
    }

    @AfterEach
    public void tearDown() {
        stockRepository.deleteAll();
    }

    @Nested
    class AddStock {

        @Test
        void WHEN_add_stock_THEN_return_created() throws Exception {
            var stock = Stock.builder().title("New Stock").quantity(30).build();
            mockMvc.perform(post("/stock")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"title\":\"New Stock\",\"quantity\":30}"))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.title", is("New Stock")))
                    .andExpect(jsonPath("$.quantity", is(30)));
        }
    }

    @Nested
    class RetrieveStock {

        @Test
        void WHEN_retrieve_stock_WITH_correct_id_THEN_return_ok() throws Exception {
            var stockId = stockRepository.findAll().get(0).getIdStock();
            mockMvc.perform(get("/stock/{id}", stockId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.title", is("Stock 1")));
        }
    }

    @Nested
    class RetrieveAllStock {

        @Test
        void WHEN_retrieve_all_stock_THEN_return_ok() throws Exception {
            mockMvc.perform(get("/stock")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()", is(2)))
                    .andExpect(jsonPath("$[0].title", is("Stock 1")))
                    .andExpect(jsonPath("$[1].title", is("Stock 2")));
        }
    }

    @Nested
    class FindStocksBelowThreshold {

        @Test
        void WHEN_find_stocks_below_threshold_THEN_return_ok() throws Exception {
            mockMvc.perform(get("/stock/below-threshold?threshold=15")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()", is(1)))
                    .andExpect(jsonPath("$[0].title", is("Stock 1")));
        }
    }


}
