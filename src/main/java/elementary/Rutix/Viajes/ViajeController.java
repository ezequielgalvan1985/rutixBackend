package elementary.Rutix.Viajes;

import elementary.Rutix.Viajes.comandos.reservas.EliminarReservaDeViajeComando;
import elementary.Rutix.Viajes.comandos.reservas.RegistrarReservaEnViajeComando;
import elementary.Rutix.Viajes.comandos.ActualizarViajeComando;
import elementary.Rutix.Viajes.comandos.EliminarViajeComando;
import elementary.Rutix.Viajes.comandos.RegistrarViajeComando;
import elementary.Rutix.Viajes.consultas.BuscarIdViajeConsulta;
import elementary.Rutix.Viajes.consultas.BuscarViajesConsulta;
import elementary.Rutix.Viajes.consultas.ListadoViajeConsulta;
import elementary.Rutix.Viajes.dto.BuscarViajesRequestConsultaDto;
import elementary.Rutix.Viajes.dto.ViajeDto;
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
@RequestMapping("/v1/viajes")
public class ViajeController {

    private final RegistrarViajeComando registrarViajeUseCase;
    private final ActualizarViajeComando actualizarViajeUseCase;
    private final ListadoViajeConsulta listadoViajeUseCase;
    private final EliminarViajeComando eliminarViajeUseCase;
    private final BuscarIdViajeConsulta buscarIdViajeUseCase;
    private final BuscarViajesConsulta buscarViajesConsulta;

    private static final Logger logger = LoggerFactory.getLogger(ViajeController.class);

    public ViajeController(RegistrarViajeComando registrarViajeUseCase,
                           ActualizarViajeComando actualizarViajeUseCase,
                           EliminarViajeComando eliminarViajeUseCase,
                           BuscarIdViajeConsulta buscarViajeUseCase,
                           ListadoViajeConsulta listadoViajeUseCase,
                           RegistrarReservaEnViajeComando registrarReservaEnViajeUseCase,
                           EliminarReservaDeViajeComando eliminarReservaDeViajeUseCase,
                           BuscarViajesConsulta buscarViajesConsulta
                           ){
        this.registrarViajeUseCase = registrarViajeUseCase;
        this.actualizarViajeUseCase = actualizarViajeUseCase;
        this.eliminarViajeUseCase = eliminarViajeUseCase;
        this.buscarIdViajeUseCase = buscarViajeUseCase;
        this.listadoViajeUseCase = listadoViajeUseCase;
        this.buscarViajesConsulta = buscarViajesConsulta;

    }

    @GetMapping()
    public ResponseEntity<List<ViajeDto>> findAll (
            @RequestParam(value = "limit", required = false, defaultValue = "25") int limit,
            @RequestParam(value = "offset", required = false, defaultValue = "0") Long offset){
        List<ViajeDto> resultset = this.listadoViajeUseCase.execute(new ConsultaListadoRequestDto(offset,limit));
        if (resultset.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(resultset);
    }

    @PostMapping("/buscar")
    public ResponseEntity<List<ViajeDto>> buscarViajesConsulta (@RequestBody BuscarViajesRequestConsultaDto request){
        List<ViajeDto> resultset = this.buscarViajesConsulta.execute(request);
        if (resultset.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(resultset);
    }
    @GetMapping(value="/{id}")
    public ResponseEntity<ViajeDto> view(@PathVariable("id") Long id){
        ViajeDto registro =  buscarIdViajeUseCase.execute(id);
        return ResponseEntity.ok(registro);

    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ViajeDto> add(@RequestBody @Valid ViajeDto m, @RequestHeader("X-User-Name") String username) throws IOException, TimeoutException, IOException, TimeoutException {
        return ResponseEntity.ok(registrarViajeUseCase.execute(m));
    }

    @PutMapping()
    public void edit( @RequestBody @Valid  ViajeDto dto){
        actualizarViajeUseCase.execute(dto);
    }

    @DeleteMapping(value="/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id){
        eliminarViajeUseCase.execute(id);
    }


}
