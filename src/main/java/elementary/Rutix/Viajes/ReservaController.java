package elementary.Rutix.Viajes;

import elementary.Rutix.Viajes.comandos.dto.*;
import elementary.Rutix.Viajes.comandos.pagos.EliminarPagoDeReservaComando;
import elementary.Rutix.Viajes.comandos.pagos.RegistrarPagoDeReservaComando;
import elementary.Rutix.Viajes.comandos.pagos.ConfirmarPagoMercadoPagoWebhookComando;
import elementary.Rutix.Viajes.comandos.pagos.DevolucionPagoDeReservaComando;
import elementary.Rutix.Viajes.comandos.reservas.*;
import elementary.Rutix.Viajes.consultas.reservas.BuscarIdReservaConsulta;
import elementary.Rutix.Viajes.consultas.MisViajesDePasajeroConsulta;
import elementary.Rutix.Viajes.consultas.reservas.BuscarReservaPorViajeIdConsulta;
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
import org.springframework.http.MediaType;
import java.util.Map;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping("/v1/reservas")
public class ReservaController {

        //COMANDOS
        private final RegistrarReservaEnViajeComando registrar;
        private final EliminarReservaDeViajeComando eliminar;
        private final ActualizarReservaDeViajeComando actualizar;
        private final RegistrarPagoDeReservaComando registrarPago;
        private final EliminarPagoDeReservaComando eliminarPago;
        private final DevolucionPagoDeReservaComando devolucionPago;
        private final ConfirmarPagoMercadoPagoWebhookComando confirmarPagoMercadoPago;
        private final ConfirmarReservaDeViajeComando confirmarReserva;
        private final RechazarReservaDeViajeComando rechazarReserva;
        private final CancelarReservaDeViajeComando cancelarReserva;

         private final ListadoReservasConsulta listar;
        private final BuscarIdReservaConsulta buscar;

        private final BuscarReservaPorViajeIdConsulta buscarReservaPorViajeIdConsulta;

        private static final Logger logger = LoggerFactory.getLogger(elementary.Rutix.Viajes.ViajeController.class);


    public ReservaController(RegistrarReservaEnViajeComando registrar,
                             EliminarReservaDeViajeComando eliminar,
                             ListadoReservasConsulta listar,
                             BuscarIdReservaConsulta buscar,
                             ActualizarReservaDeViajeComando actualizar,
                             RegistrarPagoDeReservaComando registrarPago,
                             EliminarPagoDeReservaComando eliminarPago,
                             DevolucionPagoDeReservaComando devolucionPago,
                             ConfirmarPagoMercadoPagoWebhookComando confirmarPagoMercadoPago,
                             ConfirmarReservaDeViajeComando confirmarReserva,
                             RechazarReservaDeViajeComando rechazarReserva,
                             CancelarReservaDeViajeComando cancelarReserva,
                             BuscarReservaPorViajeIdConsulta buscarReservaPorViajeIdConsulta
                             ) {
        this.registrar = registrar;
        this.eliminar = eliminar;
        this.listar = listar;
        this.buscar = buscar;
        this.actualizar = actualizar;
        this.registrarPago = registrarPago;
        this.eliminarPago = eliminarPago;
        this.devolucionPago = devolucionPago;
        this.confirmarPagoMercadoPago = confirmarPagoMercadoPago;
        this.confirmarReserva = confirmarReserva;
        this.rechazarReserva = rechazarReserva;
        this.buscarReservaPorViajeIdConsulta = buscarReservaPorViajeIdConsulta;
        this.cancelarReserva = cancelarReserva;
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

    @GetMapping(value="/consulta/viaje/{id}")
    public ResponseEntity<ReservaDto> buscarPorViajeId(@PathVariable("id") Long id){
        ReservaDto registro =  buscarReservaPorViajeIdConsulta.execute(id);
        return ResponseEntity.ok(registro);
    }


    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ReservaDto> add(@RequestBody @Valid RegistrarReservaDto m) throws IOException, TimeoutException, IOException, TimeoutException {
        return ResponseEntity.ok(registrar.execute(m));
    }

    @PutMapping("confirmar")
    public void confirmar( @RequestBody @Valid ActualizarEstadoReservaDto dto){
        confirmarReserva.execute(dto);
    }

    @PutMapping("rechazar")
    public void rechazar( @RequestBody @Valid ActualizarEstadoReservaDto dto){
        rechazarReserva.execute(dto);
    }

    @PutMapping("cancelar")
    public void cancelar( @RequestBody @Valid ActualizarEstadoReservaDto dto){
        cancelarReserva.execute(dto);
    }

    @PutMapping()
    public void edit( @RequestBody @Valid ActualizarReservaDeViajeDto dto){
        actualizar.execute(dto);
    }

    @DeleteMapping()
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN') or @reservaSecurity.esPasajeroDueno(#dto.id, authentication)")
    public void delete(@RequestBody EliminarReservaDeViajeComandoDto dto){
        eliminar.execute(dto);
    }


    @PostMapping("/pagar")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ReservaDto> registrarPago(@RequestBody @Valid PagoDto m) throws IOException, TimeoutException, IOException, TimeoutException {
        return ResponseEntity.ok(registrarPago.execute(m));
    }

    @DeleteMapping("/eliminar-pago")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@RequestBody @Valid EliminarPagoDeReservaComandoDto dto){
        eliminarPago.execute(dto);
    }

    @PutMapping("/devolver-pago")
    @PreAuthorize("hasRole('ADMIN')")
    public void devolverPago(@RequestBody @Valid DevolucionPagoDeReservaComandoDto dto){
        devolucionPago.execute(dto);
    }

    // --- Mercado Pago (Checkout Pro) ---
    // Estos dos endpoints los pega directo Mercado Pago o el navegador del
    // pasajero (nunca el frontend de Rutix con su JWT), asi que estan
    // permitAll en SecurityConfig.

    /**
     * Webhook/IPN: Mercado Pago avisa que algo paso con un pago. Los
     * parametros llegan por query string incluso siendo POST ("data.id" y
     * "type=payment" en el formato nuevo; "id" y "topic=payment" en el IPN
     * viejo), por eso se leen todos como Map en vez de @RequestParam fijos.
     * Siempre responde 200: Mercado Pago no necesita saber si hubo un
     * problema de nuestro lado, y reintentar no ayuda si el problema es
     * nuestro.
     */
    @RequestMapping(value = "/pagar/mercadopago/webhook", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<Void> webhookMercadoPago(@RequestParam Map<String, String> params) {
        String type = params.getOrDefault("type", params.get("topic"));
        String dataId = params.getOrDefault("data.id", params.get("id"));

        if ("payment".equals(type) && dataId != null) {
            try {
                confirmarPagoMercadoPago.execute(Long.parseLong(dataId));
            } catch (NumberFormatException e) {
                logger.warn("Webhook de Mercado Pago con data.id/id no numerico: {}", dataId);
            }
        }
        return ResponseEntity.ok().build();
    }

    /**
     * back_urls de la preferencia: a esto vuelve el navegador del pasajero
     * despues de pagar en Mercado Pago. Es solo informativo (la confirmacion
     * real de la reserva la hace el webhook, no esta pantalla) asi que por
     * ahora es una pagina simple pidiendo volver a la app.
     */
    @GetMapping(value = "/pagar/mercadopago/retorno", produces = MediaType.TEXT_HTML_VALUE)
    public String retornoMercadoPago(@RequestParam(required = false) String estado) {
        String mensaje = switch (estado == null ? "" : estado) {
            case "success" -> "¡Gracias! Tu pago está siendo confirmado.";
            case "pending" -> "Tu pago está pendiente de confirmación.";
            case "failure" -> "No se pudo completar el pago. Podés intentarlo de nuevo desde la app.";
            default -> "Ya podés volver a la app Rutix.";
        };
        return "<html><body style=\"font-family:sans-serif;text-align:center;padding:40px\">"
                + "<h2>" + mensaje + "</h2>"
                + "<p>Volvé a la app Rutix para ver el estado de tu reserva.</p>"
                + "</body></html>";
    }

}
