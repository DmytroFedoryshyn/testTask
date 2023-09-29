import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import test.exception.RegistrationException;
import test.exception.DataProcessingException;
import test.exception.GlobalExceptionHandler;

public class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
    }


    @Test
    void handleDataProcessingException() {
        DataProcessingException ex = new DataProcessingException("Data Processing Error");

        ResponseEntity<Object> responseEntity = globalExceptionHandler.handleDataProcessingException(ex);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void handleAgeRestrictionException() {
        RegistrationException ex = new RegistrationException("Age Restriction Error");

        ResponseEntity<Object> responseEntity = globalExceptionHandler.handleAgeRestrictionException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

    }

    @Test
    void handleException() {
        Exception ex = new Exception("Generic Error");

        ResponseEntity<Object> responseEntity = globalExceptionHandler.handleException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }
}
