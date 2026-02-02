package elementary.Rutix.PerfilRutix.comandos.tarjetas;

import elementary.Rutix.PerfilRutix.dominio.PerfilRutix;
import elementary.Rutix.PerfilRutix.dto.PerfilRutixDto;
import elementary.Rutix.PerfilRutix.utlis.reglas.policy.PerfilExistenteReglaNegocio;
import elementary.Rutix.PerfilRutix.dominio.Tarjeta;
import elementary.Rutix.PerfilRutix.repositorios.TarjetaRepository;
import elementary.Rutix.PerfilRutix.utlis.reglas.policy.TarjetaIdExistenteReglaNegocio;
import elementary.Rutix.PerfilRutix.utlis.reglas.policy.TokenUnicoReglaNegocio;
import elementary.Rutix.PerfilRutix.dto.TarjetaDto;
import elementary.Rutix.common.interfaces.Comando;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;


@Service
public class ActualizarTarjetaComando implements Comando<TarjetaDto , TarjetaDto > {

    private final TarjetaRepository repo;
    private final ModelMapper modelMapper;
    private final TokenUnicoReglaNegocio tokenUnicoReglaNegocio;
    private final PerfilExistenteReglaNegocio perfilExistenteReglaNegocio;
    private final TarjetaIdExistenteReglaNegocio tarjetaIdExistenteReglaNegocio;

    public ActualizarTarjetaComando(
            TarjetaRepository repo,
            ModelMapper modelMapper,
            TokenUnicoReglaNegocio tokenUnicoReglaNegocio,
            TarjetaIdExistenteReglaNegocio tarjetaIdExistenteReglaNegocio,
            PerfilExistenteReglaNegocio perfilExistenteReglaNegocio

    ) {
        this.repo = repo;
        this.modelMapper = modelMapper;
        this.tokenUnicoReglaNegocio = tokenUnicoReglaNegocio;
        this.perfilExistenteReglaNegocio = perfilExistenteReglaNegocio;
        this.tarjetaIdExistenteReglaNegocio = tarjetaIdExistenteReglaNegocio;

    }




    @Override
    public TarjetaDto execute(TarjetaDto input) {

        //validaciones
        this.tarjetaIdExistenteReglaNegocio.aplicar(input);
        PerfilRutix perfil = this.perfilExistenteReglaNegocio.aplicar(input.getPerfilId());
        this.tokenUnicoReglaNegocio.aplicar(input);


        //Mappeo
        Tarjeta t = modelMapper.map(input, Tarjeta.class);
        Tarjeta entity =this.repo.save(t);

        //Guardar
        return   modelMapper.map(entity, TarjetaDto.class);

    }
}
