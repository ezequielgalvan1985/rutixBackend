package elementary.Rutix.PerfilRutix.comandos.tarjetas;

import elementary.Rutix.PerfilRutix.dominio.PerfilRutix;
import elementary.Rutix.PerfilRutix.dto.EliminarTarjetaDePerfilComandoDto;
import elementary.Rutix.PerfilRutix.repositorios.PerfilRutixRepository;
import elementary.Rutix.PerfilRutix.dto.PerfilRutixDto;
import elementary.Rutix.PerfilRutix.utlis.reglas.policy.TarjetaIdExistenteReglaNegocio;
import elementary.Rutix.common.excepciones.ReglaNegocioException;
import elementary.Rutix.common.interfaces.Comando;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class EliminarTarjetaDePerfilComando implements Comando<EliminarTarjetaDePerfilComandoDto,PerfilRutixDto> {

    private final PerfilRutixRepository repo;
    private final ModelMapper modelMapper;

    public EliminarTarjetaDePerfilComando(
            PerfilRutixRepository repo,
            ModelMapper modelMapper,
            TarjetaIdExistenteReglaNegocio tarjetaIdExistenteReglaNegocio
    ) {
        this.repo = repo;
        this.modelMapper = modelMapper;
    }


    @Override
    public PerfilRutixDto execute(EliminarTarjetaDePerfilComandoDto value) {
        PerfilRutix perfil = repo.findById(value.getPerfilId())
                .orElseThrow(() -> new ReglaNegocioException("Perfil no encontrado"));
        perfil.eliminarTarjeta(value.getId());
        repo.save(perfil);
        return null;
    }
}
