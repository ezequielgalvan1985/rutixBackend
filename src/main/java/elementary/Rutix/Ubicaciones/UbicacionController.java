package elementary.Rutix.Ubicaciones;

import elementary.Rutix.Ubicaciones.consultas.BuscarCiudadesConsulta;
import elementary.Rutix.Ubicaciones.dto.CiudadAutocompleteDto;
import elementary.Rutix.Ubicaciones.dto.CiudadBuscarRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/ubicaciones")
public class UbicacionController {

    private final BuscarCiudadesConsulta buscarCiudadesUseCase;

    public UbicacionController(BuscarCiudadesConsulta buscarCiudadesUseCase) {
        this.buscarCiudadesUseCase = buscarCiudadesUseCase;
    }

    // Publica (ver SecurityConfig): el combo de origen/destino tiene que poder
    // autocompletar sin que el usuario este logueado.
    @GetMapping("/ciudades/buscar")
    public ResponseEntity<List<CiudadAutocompleteDto>> buscarCiudades(
            @RequestParam(value = "texto", required = false) String texto,
            @RequestParam(value = "limit", required = false) Integer limit) {
        List<CiudadAutocompleteDto> resultado = buscarCiudadesUseCase.execute(new CiudadBuscarRequestDto(texto, limit));
        return ResponseEntity.ok(resultado);
    }
}
