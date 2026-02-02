package elementary.Rutix.PerfilRutix.utlis.reglas.policy;

import elementary.Rutix.PerfilRutix.dominio.Vehiculo;
import elementary.Rutix.PerfilRutix.repositorios.VehiculoRepository;
import elementary.Rutix.PerfilRutix.dto.VehiculoDto;
import elementary.Rutix.common.excepciones.ReglaNegocioException;
import elementary.Rutix.common.interfaces.ReglaNegocio;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class VehiculoExistenteReglaNegocio implements ReglaNegocio<VehiculoDto, VehiculoDto> {
    private final VehiculoRepository repo;
    private final ModelMapper mapper;

    public VehiculoExistenteReglaNegocio(
            VehiculoRepository repo,
            ModelMapper mapper
    ) {
        this.repo = repo;
        this.mapper = mapper;
    }


    @Override
    public VehiculoDto aplicar(VehiculoDto dto) {
        if (dto == null || dto.getId() <= 0) {
            throw new ReglaNegocioException("Vehículo inválido");
        }
        Vehiculo entity = repo.findById(dto.getId())
                .orElseThrow(() ->
                        new ReglaNegocioException("El vehículo no existe")
                );

        return mapper.map(entity, VehiculoDto.class);
    }

}
