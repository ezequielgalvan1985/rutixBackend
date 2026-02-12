package elementary.Rutix.Viajes;

import elementary.Rutix.Viajes.comandos.reservas.EliminarReservaDeViajeComando;
import elementary.Rutix.Viajes.comandos.reservas.RegistrarReservaEnViajeComando;
import elementary.Rutix.Viajes.comandos.ActualizarViajeComando;
import elementary.Rutix.Viajes.comandos.EliminarViajeComando;
import elementary.Rutix.Viajes.comandos.RegistrarViajeComando;
import elementary.Rutix.Viajes.consultas.BuscarIdViajeConsulta;
import elementary.Rutix.Viajes.consultas.BuscarViajesConsulta;
import elementary.Rutix.Viajes.consultas.ListadoMisViajesConductorConsulta;
import elementary.Rutix.Viajes.consultas.ListadoViajeConsulta;
import elementary.Rutix.Viajes.dto.*;
import elementary.Rutix.common.dto.ConsultaListadoRequestDto;
import elementary.Rutix.common.dto.PageResponseDto;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
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
    private final ListadoMisViajesConductorConsulta listadoMisViajesConductorConsulta;


    private static final Logger logger = LoggerFactory.getLogger(ViajeController.class);

    public ViajeController(RegistrarViajeComando registrarViajeUseCase,
                           ActualizarViajeComando actualizarViajeUseCase,
                           EliminarViajeComando eliminarViajeUseCase,
                           BuscarIdViajeConsulta buscarViajeUseCase,
                           ListadoViajeConsulta listadoViajeUseCase,
                           RegistrarReservaEnViajeComando registrarReservaEnViajeUseCase,
                           EliminarReservaDeViajeComando eliminarReservaDeViajeUseCase,
                           BuscarViajesConsulta buscarViajesConsulta,
                           ListadoMisViajesConductorConsulta listadoMisViajesConductorConsulta
                           ){
        this.registrarViajeUseCase = registrarViajeUseCase;
        this.actualizarViajeUseCase = actualizarViajeUseCase;
        this.eliminarViajeUseCase = eliminarViajeUseCase;
        this.buscarIdViajeUseCase = buscarViajeUseCase;
        this.listadoViajeUseCase = listadoViajeUseCase;
        this.buscarViajesConsulta = buscarViajesConsulta;
        this.listadoMisViajesConductorConsulta = listadoMisViajesConductorConsulta;
    }

    @GetMapping("/listado/misviajes/conductor")
    public ResponseEntity<PageResponseDto<ViajeResumidoDto>> listadoMisViajesConductorConsulta (
            @RequestParam(value = "limit", required = false, defaultValue = "25") int limit,
            @RequestParam(value = "offset", required = false, defaultValue = "0") Long offset){
        PageResponseDto<ViajeResumidoDto> resultset = this.listadoMisViajesConductorConsulta.execute(new ConsultaListadoRequestDto(offset,limit));
        return ResponseEntity.ok(resultset);
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
    public ResponseEntity<PageResponseDto<ViajeResumidoDto>> buscarViajesConsulta (@RequestBody BuscarViajesRequestConsultaDto request){
        PageResponseDto<ViajeResumidoDto> resultset = this.buscarViajesConsulta.execute(request);
        return ResponseEntity.ok(resultset);
    }

    @GetMapping(value="/{id}")
    public ResponseEntity<ViajeDto> view(@PathVariable("id") Long id){
        ViajeDto registro =  buscarIdViajeUseCase.execute(id);
        return ResponseEntity.ok(registro);

    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ViajeResumidoDto> add(@RequestBody @Valid RegistrarViajeRequestComandoDto m) throws IOException, TimeoutException, IOException, TimeoutException {
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
