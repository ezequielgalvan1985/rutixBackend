package elementary.Rutix.Security;

import elementary.Rutix.PerfilRutix.dominio.CuentaBancaria;
import elementary.Rutix.PerfilRutix.dominio.PerfilRutix;
import elementary.Rutix.PerfilRutix.dominio.Tarjeta;
import elementary.Rutix.PerfilRutix.repositorios.CuentaBancariaRepository;
import elementary.Rutix.PerfilRutix.repositorios.PerfilRutixRepository;
import elementary.Rutix.PerfilRutix.repositorios.TarjetaRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

/**
 * Autorizacion por recurso para CuentaBancaria y Tarjeta: compara el dueno
 * REAL guardado en la entidad contra el perfil autenticado, nunca el
 * perfilId que manda el cliente en el body (ese campo es solo para armar
 * la entidad, no para decidir permisos). Mismo patron estructural que ya
 * usan ActualizarVehiculoComando/EliminarVehiculoComando.
 * Cierra H18/H19 del backlog (ActualizarCuentaBancariaComando y
 * ActualizarTarjetaComando no validaban dueno).
 */
@Component("perfilRecursoSecurity")
public class PerfilRecursoSecurity {

    private final CuentaBancariaRepository repoCuenta;
    private final TarjetaRepository repoTarjeta;
    private final PerfilRutixRepository repoPerfil;

    public PerfilRecursoSecurity(CuentaBancariaRepository repoCuenta, TarjetaRepository repoTarjeta, PerfilRutixRepository repoPerfil) {
        this.repoCuenta = repoCuenta;
        this.repoTarjeta = repoTarjeta;
        this.repoPerfil = repoPerfil;
    }

    private PerfilRutix perfilAutenticado(Authentication auth) {
        if (auth == null) return null;
        Long uid = Long.parseLong(auth.getName());
        return repoPerfil.findByUsuarioId(uid).orElse(null);
    }

    public boolean esDuenoDeCuenta(Long cuentaId, Authentication auth) {
        if (cuentaId == null) return false;
        PerfilRutix p = perfilAutenticado(auth);
        if (p == null) return false;

        CuentaBancaria cuenta = repoCuenta.findById(cuentaId).orElse(null);
        if (cuenta == null) return false;

        return cuenta.getPerfil().getId().equals(p.getId());
    }

    public boolean esDuenoDeTarjeta(Long tarjetaId, Authentication auth) {
        if (tarjetaId == null) return false;
        PerfilRutix p = perfilAutenticado(auth);
        if (p == null) return false;

        Tarjeta tarjeta = repoTarjeta.findById(tarjetaId).orElse(null);
        if (tarjeta == null) return false;

        return tarjeta.getPerfil().getId().equals(p.getId());
    }
}
