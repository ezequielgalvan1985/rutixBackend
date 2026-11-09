package elementary.Rutix.Security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

/**
 * Chequeo de rol reutilizable para los Comando que hoy validan dueño
 * "a mano" (via SecurityContextHolder), para que un admin de sistema
 * (ROLE_ADMIN, emitido por ms-security) pueda operar tambien sobre
 * recursos ajenos sin pasar por el chequeo de dueño.
 */
public final class RolUtils {

    private static final String ROLE_ADMIN = "ROLE_ADMIN";

    private RolUtils() {
    }

    public static boolean esAdmin(Authentication authentication) {
        if (authentication == null) {
            return false;
        }
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(ROLE_ADMIN::equals);
    }
}
