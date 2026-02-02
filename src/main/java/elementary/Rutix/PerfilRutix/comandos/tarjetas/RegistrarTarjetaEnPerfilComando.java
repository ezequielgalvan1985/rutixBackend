package elementary.Rutix.PerfilRutix.comandos.tarjetas;

import elementary.Rutix.PerfilRutix.dominio.PerfilRutix;
import elementary.Rutix.PerfilRutix.dto.PerfilRutixDto;
import elementary.Rutix.PerfilRutix.repositorios.PerfilRutixRepository;
import elementary.Rutix.PerfilRutix.dominio.Tarjeta;
import elementary.Rutix.PerfilRutix.utlis.reglas.policy.TokenUnicoReglaNegocio;
import elementary.Rutix.PerfilRutix.dto.TarjetaDto;
import elementary.Rutix.common.excepciones.ReglaNegocioException;
import elementary.Rutix.common.interfaces.Comando;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class RegistrarTarjetaEnPerfilComando implements Comando<TarjetaDto , PerfilRutixDto > {
    private PerfilRutixRepository repo;
    private final ModelMapper modelMapper;
    private final TokenUnicoReglaNegocio tokenUnicoReglaNegocio;

    public RegistrarTarjetaEnPerfilComando(
            PerfilRutixRepository repo,
            ModelMapper modelMapper,
            TokenUnicoReglaNegocio tokenUnicoReglaNegocio

    ) {
        this.repo = repo;
        this.modelMapper = modelMapper;
        this.tokenUnicoReglaNegocio = tokenUnicoReglaNegocio;
    }


    @Override
    public PerfilRutixDto execute(TarjetaDto input) {
        PerfilRutix p = this.repo.findById(input.getPerfilId()).orElseThrow(()-> new ReglaNegocioException("Perfil inexistente"));
        this.tokenUnicoReglaNegocio.aplicar(input);
        Tarjeta model = modelMapper.map(input, Tarjeta.class);
        p.agregarTarjeta(model);
        return modelMapper.map(this.repo.save(p),PerfilRutixDto.class);

    }
}
