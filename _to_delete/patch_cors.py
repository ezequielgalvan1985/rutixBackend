import io

def read(p):
    with io.open(p, "r", encoding="utf-8") as f:
        return f.read()

def write(p, c):
    with io.open(p, "w", encoding="utf-8") as f:
        f.write(c)

base = "src/main/java/elementary/Rutix/"

# 1) CorsConfig.java: expose a CorsConfigurationSource bean (consumed by Security), instead of
# only a WebMvcConfigurer mapping that runs too late (after Spring Security already rejected
# the unauthenticated OPTIONS preflight).
p = base + "common/config/CorsConfig.java"
new_cors_config = '''package elementary.Rutix.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Fuente de configuracion CORS usada por SecurityConfig (http.cors(...)).
 * Antes esto vivia solo como WebMvcConfigurer.addCorsMappings, que corre en
 * la capa de MVC: Spring Security ya rechazaba el preflight OPTIONS (sin
 * Authorization) con anyRequest().authenticated() antes de que la request
 * llegara ahi, asi que el navegador nunca veia Access-Control-Allow-Origin.
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of(
                "http://localhost:8100",
                "http://localhost:4200"
        ));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
'''
write(p, new_cors_config)
print("1) CorsConfig.java OK")

# 2) SecurityConfig.java: wire .cors(...) into the filter chain + permit OPTIONS preflight
p = base + "Security/SecurityConfig.java"
c = read(p)

old_imports = """import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
"""
assert c.count(old_imports) == 1, ("imports", c.count(old_imports))
new_imports = """import org.springframework.http.HttpMethod;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;
"""
c = c.replace(old_imports, new_imports)

old_fields = """    private final JwtTokenFilter jwtTokenFilter;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    public SecurityConfig(JwtTokenFilter jwtTokenFilter, CustomAuthenticationEntryPoint customAuthenticationEntryPoint) {
        this.jwtTokenFilter = jwtTokenFilter;
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
    }
"""
assert c.count(old_fields) == 1, ("fields/ctor", c.count(old_fields))
new_fields = """    private final JwtTokenFilter jwtTokenFilter;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CorsConfigurationSource corsConfigurationSource;

    public SecurityConfig(JwtTokenFilter jwtTokenFilter,
                          CustomAuthenticationEntryPoint customAuthenticationEntryPoint,
                          CorsConfigurationSource corsConfigurationSource) {
        this.jwtTokenFilter = jwtTokenFilter;
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
        this.corsConfigurationSource = corsConfigurationSource;
    }
"""
c = c.replace(old_fields, new_fields)

old_chain_head = """        http
                // Rutix es stateless
                .csrf(csrf -> csrf.disable())
"""
assert c.count(old_chain_head) == 1, ("chain head", c.count(old_chain_head))
new_chain_head = """        http
                // Habilita CORS a nivel del filtro de Security (antes de cualquier
                // chequeo de autenticacion), usando la configuracion de CorsConfig.
                .cors(cors -> cors.configurationSource(corsConfigurationSource))

                // Rutix es stateless
                .csrf(csrf -> csrf.disable())
"""
c = c.replace(old_chain_head, new_chain_head)

old_auth = """                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/actuator/**").permitAll()
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        .anyRequest().authenticated()
                )
"""
assert c.count(old_auth) == 1, ("authorizeHttpRequests", c.count(old_auth))
new_auth = """                .authorizeHttpRequests(auth -> auth
                        // El preflight CORS (OPTIONS) nunca manda Authorization: si no se
                        // permite explicitamente, cae en anyRequest().authenticated() y el
                        // navegador ve un 401 sin headers CORS (bloqueado por CORS, sin serlo).
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/actuator/**").permitAll()
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        .anyRequest().authenticated()
                )
"""
c = c.replace(old_auth, new_auth)

write(p, c)
print("2) SecurityConfig.java OK")
