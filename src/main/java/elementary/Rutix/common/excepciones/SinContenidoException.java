package elementary.Rutix.common.excepciones;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NO_CONTENT)
public class SinContenidoException extends RuntimeException {
    public SinContenidoException(String message) {
        super(message);
    }
}
