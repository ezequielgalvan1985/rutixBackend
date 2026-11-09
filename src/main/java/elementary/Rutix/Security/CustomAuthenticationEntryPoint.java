package elementary.Rutix.Security;

import com.fasterxml.jackson.databind.ObjectMapper;
import elementary.Rutix.common.dto.ResponseDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

/**
 * Se dispara cuando SecurityConfig exige authenticated() y la request llego
 * sin Authentication en el contexto (no vino token, o JwtTokenFilter no pudo
 * armar uno). Reemplaza el 403 vacio por defecto de Spring Security por un
 * 401 con el mismo formato de respuesta que usa GlobalExceptionHandler.
 */
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    public CustomAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                          AuthenticationException authException) throws IOException, ServletException {

        ResponseDto r = ResponseDto.builder()
                .status(false)
                .error("No autorizado")
                .message("Se requiere un token valido para acceder a este recurso")
                .timestamp(new Date())
                .data(null)
                .build();

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(r));
    }
}
