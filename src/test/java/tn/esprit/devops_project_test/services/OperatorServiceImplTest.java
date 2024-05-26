package tn.esprit.devops_project_test.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.devops_project.entities.Operator;
import tn.esprit.devops_project.repositories.OperatorRepository;
import tn.esprit.devops_project.services.OperatorServiceImpl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OperatorServiceImplTest {

    @InjectMocks
    OperatorServiceImpl operatorService;
    @Mock
    OperatorRepository operatorRepository;

    private Operator operator;

    @BeforeEach
    void setUp() {
        operator = Operator.builder()
                .idOperateur(1L)
                .fname("firstName operateur")
                .lname("lastName operateur")
                .password("password operateur")
                .build();
        reset(operatorRepository);
    }

    @Nested
    class RetrieveAllOperators {

        @Test
        void WHEN_retrieve_all_operators_THEN_ok() {
            when(operatorRepository.findAll()).thenReturn(Collections.singletonList(operator));
            List<Operator> result = operatorService.retrieveAllOperators();
            assertEquals(1, result.size());
            assertEquals(operator, result.get(0));
        }

    }

    @Nested
    class AddOperator {

        @Test
        void WHEN_add_operator_THEN_ok() {
            when(operatorRepository.save(operator)).thenReturn(operator);
            var result = operatorService.addOperator(operator);
            assertEquals(operator, result);
            assertEquals(operator.getIdOperateur(), result.getIdOperateur());
            assertEquals(operator.getFname(), result.getFname());
            assertEquals(operator.getLname(), result.getLname());
            assertEquals(operator.getPassword(), result.getPassword());
        }
    }

    @Nested
    class DeleteOperator {

        @Test
        void WHEN_delete_operator_THEN_ok() {
            operatorService.deleteOperator(operator.getIdOperateur());
            verify(operatorRepository, times(1)).deleteById(operator.getIdOperateur());
        }

    }

    @Nested
    class UpdateOperator {

        @Test
        void WHEN_update_operator_THEN_ok() {
            when(operatorRepository.save(operator)).thenReturn(operator);
            var result = operatorService.updateOperator(operator);
            assertEquals(operator, result);
            assertEquals(operator.getIdOperateur(), result.getIdOperateur());
            assertEquals(operator.getFname(), result.getFname());
            assertEquals(operator.getLname(), result.getLname());
            assertEquals(operator.getPassword(), result.getPassword());
        }
    }

    @Nested
    class RetrieveOperator {

        @Nested
        class OK {

            @Test
            void WHEN_retrieve_operator_WITH_correct_id_THEN_ok() {
                when(operatorRepository.findById(operator.getIdOperateur())).thenReturn(Optional.of(operator));
                var result = operatorService.retrieveOperator(operator.getIdOperateur());
                assertEquals(operator, result);
            }
        }

        @Nested
        class KO {

            @Test
            void WHEN_retrieve_operator_WITH_id_not_found_THEN_throw_exception() {
                when(operatorRepository.findById(2L)).thenReturn(Optional.empty());
                var exception = assertThrows(NullPointerException.class, () -> {
                    operatorService.retrieveOperator(2L);
                });
                assertEquals("Operator not found", exception.getMessage());
                verify(operatorRepository, times(1)).findById(2L);
            }
        }
    }
}
