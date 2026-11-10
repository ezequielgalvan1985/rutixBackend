package elementary.Rutix.Ubicaciones;

import elementary.Rutix.Ubicaciones.dominio.Ciudad;
import elementary.Rutix.Ubicaciones.dto.CiudadAutocompleteDto;

// Mapeo manual Ciudad -> CiudadAutocompleteDto, usado donde se arma la
// respuesta de un Viaje (ciudadPartida/ciudadDestino). No se delega en
// ModelMapper porque los nombres de propiedades no coinciden 1 a 1
// (Ciudad.nombre vs CiudadAutocompleteDto.ciudad, Ciudad.provincia es una
// entidad vs CiudadAutocompleteDto.provincia que es un String), asi que el
// mapeo automatico podria quedar incompleto en silencio.
public final class CiudadMapper {

    private CiudadMapper() {
    }

    public static CiudadAutocompleteDto toDto(Ciudad c) {
        if (c == null) {
            return null;
        }
        return CiudadAutocompleteDto.builder()
                .id(c.getId())
                .ciudad(c.getNombre())
                .provincia(c.getProvincia() != null ? c.getProvincia().getNombre() : null)
                .pais(c.getProvincia() != null && c.getProvincia().getPais() != null
                        ? c.getProvincia().getPais().getNombre() : null)
                .build();
    }
}
