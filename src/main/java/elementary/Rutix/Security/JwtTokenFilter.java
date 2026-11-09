package elementary.Rutix.Security;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import elementary.Rutix.common.dto.ResponseDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;

/**
 * captura header authorization
 * obtiene el token
 * decodifica el jwt
 * extrae el username con jwtUtils
 * Si viene un JWT válido, reconstruir la identidad del usuario dentro de Spring Security para esta request
 * esta clase le dice a spring security que la request pertence a un usuario con x permisos
 * en resumen traduce el jwt al lenguaje que entiene el spring security para poder usar sus funciones
 *
 * Si no viene token, no setea Authentication y deja pasar la request: es
 * CustomAuthenticationEntryPoint (via authenticated() en SecurityConfig) quien
 * la rechaza con 401 mas adelante en la cadena.
 * Si el token viene pero es invalido, corta aca mismo con un 401 explicito en
 * vez de dejar que la excepcion se propague como un 500.
 * */
@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private JwtUtils jwtUtils;
    private ObjectMapper objectMapper;

    Logger logger = LoggerFactory.getLogger(JwtTokenFilter.class);

    public JwtTokenFilter(JwtUtils jwtUtils, ObjectMapper objectMapper) {
        this.jwtUtils = jwtUtils;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        String jwtToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        logger.info("JwtTokenValidator.doFilterInternal.jwtToken: " + jwtToken);
        if (jwtToken != null) {
            jwtToken = jwtToken.substring(7);

            try {
                DecodedJWT decodedJWT = jwtUtils.validateToken(jwtToken);
                String userId = decodedJWT.getSubject();
                String username = jwtUtils.getSpecificClaim(decodedJWT, "username").asString();

                String stringAuthorities = jwtUtils.getSpecificClaim(decodedJWT, "authorities").asString();

                Collection<? extends GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(stringAuthorities);

                SecurityContext context = SecurityContextHolder.createEmptyContext();
                Authentication authenticationToken = new UsernamePasswordAuthenticationToken(Long.valueOf(userId), null, authorities);
                context.setAuthentication(authenticationToken);
                SecurityContextHolder.setContext(context);
            } catch (Exception ex) {
                logger.warn("JwtTokenValidator.doFilterInternal: token invalido o malformado - " + ex.getMessage());
                SecurityContextHolder.clearContext();
                escribirNoAutorizado(response, "Token invalido o expirado");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private void escribirNoAutorizado(HttpServletResponse response, String mensaje) throws IOException {
        ResponseDto r = ResponseDto.builder()
                .status(false)
                .error("No autorizado")
                .message(mensaje)
                .timestamp(new Date())
                .data(null)
                .build();

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(r));
    }
}