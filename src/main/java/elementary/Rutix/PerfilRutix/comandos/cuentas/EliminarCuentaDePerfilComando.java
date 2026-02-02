package elementary.Rutix.PerfilRutix.comandos.cuentas;

import elementary.Rutix.PerfilRutix.dominio.PerfilRutix;
import elementary.Rutix.PerfilRutix.dto.EliminarCuentaDePerfilComandoDto;
import elementary.Rutix.PerfilRutix.repositorios.CuentaBancariaRepository;
import elementary.Rutix.PerfilRutix.repositorios.PerfilRutixRepository;
import elementary.Rutix.common.excepciones.ReglaNegocioException;
import elementary.Rutix.common.interfaces.Comando;
import org.springframework.stereotype.Service;

@Service
public class EliminarCuentaDePerfilComando implements Comando<EliminarCuentaDePerfilComandoDto, Void> {

    private final PerfilRutixRepository repo;

    public EliminarCuentaDePerfilComando(PerfilRutixRepository repo) {
        this.repo = repo;
    }


    @Override
    public Void execute(EliminarCuentaDePerfilComandoDto input) {
        PerfilRutix p = this.repo.findById(input.getPerfilId()).orElseThrow(()-> new ReglaNegocioException("Perfil inexistente"));
        p.eliminarCuenta(input.getId());
        repo.save(p);
        return null;
    }
}
