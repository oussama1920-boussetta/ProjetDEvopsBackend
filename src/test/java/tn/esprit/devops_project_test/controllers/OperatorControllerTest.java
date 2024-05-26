package tn.esprit.devops_project_test.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import tn.esprit.devops_project.DevopsProjectSpringBootApplication;
import tn.esprit.devops_project.entities.Operator;
import tn.esprit.devops_project.repositories.OperatorRepository;

import java.util.HashSet;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = DevopsProjectSpringBootApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class}, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
class OperatorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OperatorRepository operatorRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @AfterEach
    void tearDown() {
        operatorRepository.deleteAll();
    }

    @Nested
    class RetrieveOperators {

        @Test
        void WHEN_retrieve_operators_THEN_ok() throws Exception {

            var operator1 = Operator.builder()
                    .fname("test fname 1")
                    .lname("test lname 1")
                    .invoices(new HashSet<>())
                    .password("test password")
                    .build();

            var operator2 = Operator.builder()
                    .fname("test fname 2")
                    .lname("test lname 2")
                    .invoices(new HashSet<>())
                    .password("test password")
                    .build();

            operatorRepository.save(operator1);
            operatorRepository.save(operator2);

            mockMvc.perform(get("/operator")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()", is(2)))
                    .andExpect(jsonPath("$[0].idOperateur").value(operator1.getIdOperateur()))
                    .andExpect(jsonPath("$[0].fname").value(operator1.getFname()))
                    .andExpect(jsonPath("$[1].idOperateur").value(operator2.getIdOperateur()))
                    .andExpect(jsonPath("$[1].fname").value(operator2.getFname()));
        }
    }

    @Nested
    class RetrieveOperator {

        @Test
        void WHEN_retrieve_operator_WITH_correct_id_THEN_ok() throws Exception {

            var operator = Operator.builder()
                    .fname("test fname")
                    .lname("test lname")
                    .invoices(new HashSet<>())
                    .password("test password")
                    .build();
            operatorRepository.save(operator);

            mockMvc.perform(get("/operator/{operatorId}", operator.getIdOperateur())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.idOperateur").value(operator.getIdOperateur()))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.fname").value("test fname"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.lname").value("test lname"));
        }
    }

    @Nested
    class CreateOperator {

        @Test
        void WHEN_create_operator_THEN_ok() throws Exception {

            var operator = Operator.builder()
                    .idOperateur(1L)
                    .fname("test fname")
                    .lname("test lname")
                    .invoices(new HashSet<>())
                    .password("test password")
                    .build();

            mockMvc.perform(post("/operator")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(operator)))
                    .andExpect(status().isOk());

            assertNotNull(operatorRepository.findById(operator.getIdOperateur()));
        }
    }

    @Nested
    class DeleteOperator {

        @Test
        void WHEN_delete_operator_THEN_ok() throws Exception {
            var operator = Operator.builder()
                    .fname("test fname")
                    .lname("test lname")
                    .invoices(new HashSet<>())
                    .password("test password")
                    .build();
            operatorRepository.save(operator);

            mockMvc.perform(delete("/operator/{id}", operator.getIdOperateur())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());

            assertEquals(Optional.empty(), operatorRepository.findById(operator.getIdOperateur()));
        }
    }

    @Nested
    class UpdateOperator {

        @Test
        void WHEN_update_operator_THEN_return_ok() throws Exception {

            var operator = Operator.builder()
                    .fname("test fname")
                    .lname("test lname")
                    .build();
            operatorRepository.save(operator);

            mockMvc.perform(put("/operator")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{ \"fname\": \"John\", \"lname\": \"Doe\" }"))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.idOperateur").value(operator.getIdOperateur() + 1))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.fname").value("John"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.lname").value("Doe"));
        }
    }

}
