package elementary.Rutix.Viajes;

import elementary.Rutix.Viajes.comandos.dto.ActualizarReservaDeViajeDto;
import elementary.Rutix.Viajes.comandos.dto.EliminarPagoDeReservaComandoDto;
import elementary.Rutix.Viajes.comandos.dto.EliminarReservaDeViajeComandoDto;
import elementary.Rutix.Viajes.comandos.dto.RegistrarReservaDto;
import elementary.Rutix.Viajes.comandos.pagos.EliminarPagoDeReservaComando;
import elementary.Rutix.Viajes.comandos.pagos.RegistrarPagoDeReservaComando;
import elementary.Rutix.Viajes.comandos.reservas.*;
import elementary.Rutix.Viajes.consultas.reservas.BuscarIdReservaConsulta;
import elementary.Rutix.Viajes.consultas.MisViajesDePasajeroConsulta;
import elementary.Rutix.Viajes.consultas.reservas.ListadoReservasConsulta;
import elementary.Rutix.Viajes.dto.ListadoByPasajeroIdRequestDto;
import elementary.Rutix.Viajes.dto.PagoDto;
import elementary.Rutix.Viajes.dto.ReservaDto;
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
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping("/v1/viajes/reservas")
public class ReservaController {

        private final RegistrarReservaEnViajeComando registrar;
        private final EliminarReservaDeViajeComando eliminar;
        private final ListadoReservasConsulta listar;
        private final BuscarIdReservaConsulta buscar;
        private final ActualizarReservaDeViajeComando actualizar;

        private final RegistrarPagoDeReservaComando registrarPago;
        private final EliminarPagoDeReservaComando eliminarPago;
        private final ConfirmarReservaDeViajeComando confirmarReserva;
        private final RechazarReservaDeViajeComando rechazarReserva;

        private static final Logger logger = LoggerFactory.getLogger(elementary.Rutix.Viajes.ViajeController.class);


    public ReservaController(RegistrarReservaEnViajeComando registrar,
                             EliminarReservaDeViajeComando eliminar,
                             ListadoReservasConsulta listar,
                             BuscarIdReservaConsulta buscar,
                             ActualizarReservaDeViajeComando actualizar,
                             RegistrarPagoDeReservaComando registrarPago,
                             EliminarPagoDeReservaComando eliminarPago,
                             ConfirmarReservaDeViajeComando confirmarReserva,
                             RechazarReservaDeViajeComando rechazarReserva
                             ) {
        this.registrar = registrar;
        this.eliminar = eliminar;
        this.listar = listar;
        this.buscar = buscar;
        this.actualizar = actualizar;
        this.registrarPago = registrarPago;
        this.eliminarPago = eliminarPago;
        this.confirmarReserva = confirmarReserva;
        this.rechazarReserva = rechazarReserva;
    }

    @GetMapping()
    public ResponseEntity<PageResponseDto<ReservaDto>> findAll (
            @RequestParam(value = "limit", required = false, defaultValue = "25") int limit,
            @RequestParam(value = "offset", required = false, defaultValue = "0") Long offset){
        PageResponseDto<ReservaDto> resultset = this.listar.execute(new ConsultaListadoRequestDto(offset,limit));

        return ResponseEntity.ok(resultset);
    }

    @GetMapping(value="/{id}")
    public ResponseEntity<ReservaDto> view(@PathVariable("id") Long id){
        ReservaDto registro =  buscar.execute(id);
        return ResponseEntity.ok(registro);

    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ReservaDto> add(@RequestBody @Valid RegistrarReservaDto m) throws IOException, TimeoutException, IOException, TimeoutException {
        return ResponseEntity.ok(registrar.execute(m));
    }

    @PutMapping("confirmar")
    public void confirmar( @RequestBody @Valid ActualizarReservaDeViajeDto dto){
        confirmarReserva.execute(dto);
    }

    @PutMapping("rechazar")
    public void rechazar( @RequestBody @Valid ActualizarReservaDeViajeDto dto){
        rechazarReserva.execute(dto);
    }


    @PutMapping()
    public void edit( @RequestBody @Valid ActualizarReservaDeViajeDto dto){
        actualizar.execute(dto);
    }

    @DeleteMapping()
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@RequestBody EliminarReservaDeViajeComandoDto dto){
        eliminar.execute(dto);
    }


    @PostMapping("/pagos")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ReservaDto> registrarPago(@RequestBody @Valid PagoDto m, @RequestHeader("X-User-Name") String username) throws IOException, TimeoutException, IOException, TimeoutException {
        return ResponseEntity.ok(registrarPago.execute(m));
    }

    @DeleteMapping("/pagos")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@RequestBody @Valid EliminarPagoDeReservaComandoDto dto){
        eliminarPago.execute(dto);
    }

}
