package elementary.Rutix.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtTokenFilter jwtTokenFilter;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CorsConfigurationSource corsConfigurationSource;

    public SecurityConfig(JwtTokenFilter jwtTokenFilter,
                          CustomAuthenticationEntryPoint customAuthenticationEntryPoint,
                          CorsConfigurationSource corsConfigurationSource) {
        this.jwtTokenFilter = jwtTokenFilter;
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
        this.corsConfigurationSource = corsConfigurationSource;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                // Habilita CORS a nivel del filtro de Security (antes de cualquier
                // chequeo de autenticacion), usando la configuracion de CorsConfig.
                .cors(cors -> cors.configurationSource(corsConfigurationSource))

                // Rutix es stateless
                .csrf(csrf -> csrf.disable())

                // No usamos sesiones
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Desactivamos auth por defecto
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())

                // Toda request necesita un JWT valido; Rutix no confia solo en el Gateway.
                // Quedan afuera los endpoints que consultan herramientas de infraestructura
                // (Spring Boot Admin, Prometheus) y la documentacion, que no mandan token.
                .authorizeHttpRequests(auth -> auth
                        // El preflight CORS (OPTIONS) nunca manda Authorization: si no se
                        // permite explicitamente, cae en anyRequest().authenticated() y el
                        // navegador ve un 401 sin headers CORS (bloqueado por CORS, sin serlo).
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/actuator/**").permitAll()
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        // La busqueda/listado de viajes es publica: cualquiera puede explorar
                        // viajes sin loguearse. BuscarViajesConsulta ya maneja el caso anonimo
                        // (sin perfil logueado, accionesDisponibles queda vacio).
                        .requestMatchers(HttpMethod.POST, "/v1/viajes/buscar").permitAll()
                        // El autocompletado de ciudades (combo origen/destino) tambien es
                        // publico: hace falta antes incluso de loguearse, para armar la busqueda.
                        .requestMatchers(HttpMethod.GET, "/v1/ubicaciones/**").permitAll()
                        // Mercado Pago le pega directo al webhook (nunca manda un JWT de Rutix),
                        // y el navegador del pasajero vuelve a la pagina de retorno sin sesion.
                        .requestMatchers("/v1/reservas/pagar/mercadopago/**").permitAll()
                        .anyRequest().authenticated()
                )

                // Sin token valido -> 401 explicito, no el 403 vacio por defecto
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                )

                // Nuestro filtro JWT
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
