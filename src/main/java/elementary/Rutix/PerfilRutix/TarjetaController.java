package elementary.Rutix.PerfilRutix;

import elementary.Rutix.PerfilRutix.comandos.tarjetas.ActualizarTarjetaComando;
import elementary.Rutix.PerfilRutix.comandos.tarjetas.EliminarTarjetaDePerfilComando;
import elementary.Rutix.PerfilRutix.comandos.tarjetas.RegistrarTarjetaEnPerfilComando;
import elementary.Rutix.PerfilRutix.consultas.tarjetas.BuscarIdTarjetaConsulta;
import elementary.Rutix.PerfilRutix.consultas.tarjetas.ListadoTarjetaConsulta;
import elementary.Rutix.PerfilRutix.dto.EliminarTarjetaDePerfilComandoDto;
import elementary.Rutix.PerfilRutix.dto.PerfilRutixDto;
import elementary.Rutix.PerfilRutix.dto.TarjetaDto;
import elementary.Rutix.common.dto.ConsultaListadoRequestDto;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping("/v1/perfil/tarjetas")
public class TarjetaController {

    private static final Logger logger = LoggerFactory.getLogger(TarjetaController.class);

    private RegistrarTarjetaEnPerfilComando registrar;
    private ActualizarTarjetaComando actualizar;
    private ListadoTarjetaConsulta listar;
    private BuscarIdTarjetaConsulta buscar;
    private final EliminarTarjetaDePerfilComando eliminar;

    public TarjetaController(RegistrarTarjetaEnPerfilComando registrar,
                             ActualizarTarjetaComando actualizar,
                             ListadoTarjetaConsulta listar,
                             BuscarIdTarjetaConsulta buscar,
                             EliminarTarjetaDePerfilComando eliminar) {
        this.registrar = registrar;
        this.actualizar = actualizar;
        this.listar = listar;
        this.buscar = buscar;
        this.eliminar = eliminar;
    }

    @GetMapping()
    public ResponseEntity<List<TarjetaDto>> findAll (
            @RequestParam(value = "limit", required = false, defaultValue = "25") Integer limit,
            @RequestParam(value = "offset", required = false, defaultValue = "0") Long offset){
        List<TarjetaDto> resultset = listar.execute(new ConsultaListadoRequestDto( offset,limit));
        if (resultset.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(resultset);
    }

    @GetMapping(value="/{id}")
    public ResponseEntity<TarjetaDto> view(@PathVariable("id") Long id){
        TarjetaDto registro =  buscar.execute(id);
        return ResponseEntity.ok(registro);

    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<PerfilRutixDto> add(@RequestBody @Valid TarjetaDto m, @RequestHeader("X-User-Name") String username) throws IOException, TimeoutException, IOException, TimeoutException {
        return ResponseEntity.ok(registrar.execute(m));
    }

    @PutMapping()
    public void edit(@RequestBody @Valid TarjetaDto dto){
        actualizar.execute(dto);
    }


    @DeleteMapping()
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@RequestBody @Valid EliminarTarjetaDePerfilComandoDto dto){
        eliminar.execute(dto);
    }
}
