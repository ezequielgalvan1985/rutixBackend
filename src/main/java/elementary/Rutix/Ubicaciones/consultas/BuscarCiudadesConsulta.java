package elementary.Rutix.Ubicaciones.consultas;

import elementary.Rutix.Ubicaciones.dominio.Ciudad;
import elementary.Rutix.Ubicaciones.dto.CiudadAutocompleteDto;
import elementary.Rutix.Ubicaciones.dto.CiudadBuscarRequestDto;
import elementary.Rutix.Ubicaciones.repositorios.CiudadRepository;
import elementary.Rutix.common.interfaces.Consulta;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Autocompletado de ciudades para el combo de origen/destino (tipo Despegar).
 * Publica (ver SecurityConfig): no requiere login, es solo para armar el combo.
 */
@Service
public class BuscarCiudadesConsulta implements Consulta<CiudadBuscarRequestDto, List<CiudadAutocompleteDto>> {

    // Con menos de 2 caracteres el LIKE '%texto%' escanea la tabla entera para
    // devolver un resultado inutil (demasiadas coincidencias); se corta antes.
    private static final int TEXTO_MINIMO = 2;
    private static final int LIMITE_DEFAULT = 10;
    private static final int LIMITE_MAXIMO = 20;

    private final CiudadRepository repo;

    public BuscarCiudadesConsulta(CiudadRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<CiudadAutocompleteDto> execute(CiudadBuscarRequestDto input) {
        String texto = input.getTexto() == null ? "" : input.getTexto().trim();
        if (texto.length() < TEXTO_MINIMO) {
            return new ArrayList<>();
        }

        int limit = (input.getLimit() == null || input.getLimit() < 1)
                ? LIMITE_DEFAULT
                : Math.min(input.getLimit(), LIMITE_MAXIMO);

        return repo.buscarPorTexto(texto).stream()
                .limit(limit)
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private CiudadAutocompleteDto toDto(Ciudad c) {
        return CiudadAutocompleteDto.builder()
                .id(c.getId())
                .ciudad(c.getNombre())
                .provincia(c.getProvincia().getNombre())
                .pais(c.getProvincia().getPais().getNombre())
                .build();
    }
}
