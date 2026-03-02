package elementary.Rutix.common.excepciones;

import brave.Response;
import elementary.Rutix.common.dto.ResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeoutException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);



    @ExceptionHandler(SinContenidoException.class)
    public ResponseEntity<ResponseDto> handleNoContent(SinContenidoException ex){

        ex.printStackTrace();
        ResponseDto r = ResponseDto.builder()
                .status(false)
                .error("No existen datos")
                .message(ex.getMessage())
                .timestamp(new Date())
                .data(null)
                .build();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(r);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDto> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ex.printStackTrace();
        ResponseDto r = ResponseDto.builder()
                .status(false)
                .error("Solicitud incorrecta")
                .message("Revisar lista de errores")
                .listValidationErrors(errors)
                .timestamp(new Date())
                .data(null)
                .build();
        return ResponseEntity.badRequest().body(r);
    }

    @ExceptionHandler(ReglaNegocioException.class)
    public ResponseEntity<ResponseDto> handleBadRequest(ReglaNegocioException ex) {
        ex.printStackTrace();
        ResponseDto r = ResponseDto.builder()
                .status(false)
                .error("Regla de Negocio")
                .message(ex.getMessage())
                .timestamp(new Date())
                .data(null)
                .build();
        return ResponseEntity.badRequest().body(r);
    }
    @ExceptionHandler(CorruptoRequestException.class)
    public ResponseEntity<ResponseDto> handleBadRequest(CorruptoRequestException ex) {
        ex.printStackTrace();
        ResponseDto r = ResponseDto.builder()
                .status(false)
                .error("Solicitud incorrecta")
                .message(ex.getMessage())
                .timestamp(new Date())
                .data(null)
                .build();
        return ResponseEntity.badRequest().body(r);
    }

    @ExceptionHandler(NoEncontradoException.class)
    public ResponseEntity<ResponseDto> handleNotFound(NoEncontradoException ex) {
        ex.printStackTrace();
        ResponseDto r = ResponseDto.builder()
                .status(false)
                .error("Recurso no encontrado")
                .message(ex.getMessage())
                .timestamp(new Date())
                .data(null)
                .build();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(r);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDto> handleGenericException(Exception ex, HttpServletRequest request) throws NoResourceFoundException {
        if (ex instanceof NoResourceFoundException) {
            // Dejar que Spring maneje esta excepción normalmente
            throw (NoResourceFoundException) ex;
        }

        ex.printStackTrace();
        ResponseDto r = ResponseDto.builder()
                .status(false)
                .error("Error inesperado")
                .message(ex.getMessage())
                .timestamp(new Date())
                .data(null)
                .build();
        return ResponseEntity.internalServerError().body(r);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ResponseDto> handleIOException(IOException ex, HttpServletRequest request)  {
        ex.printStackTrace();
        ResponseDto r = ResponseDto.builder()
                .status(false)
                .error("IOException")
                .message(ex.getMessage())
                .timestamp(new Date())
                .data(null)
                .build();
        return ResponseEntity.internalServerError().body(r);
    }

    @ExceptionHandler(TimeoutException.class)
    public ResponseEntity<ResponseDto> handleTimeoutException(TimeoutException ex, HttpServletRequest request)  {
        ex.printStackTrace();
        ResponseDto r = ResponseDto.builder()
                .status(false)
                .error("TimeoutException")
                .message(ex.getMessage())
                .timestamp(new Date())
                .data(null)
                .build();
        return ResponseEntity.internalServerError().body(r);
    }

    @ExceptionHandler(ParametroInvalidoException.class)
    public ResponseEntity<ResponseDto> handleParametroInvalido(ParametroInvalidoException ex) {
        ex.printStackTrace();
        ResponseDto r = ResponseDto.builder()
                .status(false)
                .error("Parametro Invalido")
                .message(ex.getMessage())
                .timestamp(new Date())
                .data(null)
                .build();
        return ResponseEntity.badRequest().body(r);
    }

    @ExceptionHandler(ParametroObligatorioException.class)
    public ResponseEntity<ResponseDto> handleParametroObligatorio(ParametroObligatorioException ex) {
        ex.printStackTrace();
        ResponseDto r = ResponseDto.builder()
                .status(false)
                .error("Parametro Obligatorio Exception")
                .message(ex.getMessage())
                .timestamp(new Date())
                .data(null)
                .build();
        return ResponseEntity.badRequest().body(r);
    }


}