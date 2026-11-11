package elementary.Rutix.Viajes.comandos.pagos;

import elementary.Rutix.PerfilRutix.dominio.PerfilRutix;
import elementary.Rutix.PerfilRutix.repositorios.PerfilRutixRepository;
import elementary.Rutix.PerfilRutix.utlis.reglas.policy.PerfilExistenteReglaNegocio;
import elementary.Rutix.Viajes.IntegracionPagoExterno.GeneradorCobroFactory;
import elementary.Rutix.Viajes.IntegracionPagoExterno.GeneradorCobroStrategy;
import elementary.Rutix.Viajes.dominio.Pago;
import elementary.Rutix.Viajes.dominio.Reserva;
import elementary.Rutix.Viajes.dto.PagoDto;
import elementary.Rutix.Viajes.dto.ReservaDto;
import elementary.Rutix.Viajes.repositorios.ReservaRepository;
import elementary.Rutix.common.Enum.EstadoPagoEnum;
import elementary.Rutix.common.Enum.EstadoReserva;
import elementary.Rutix.common.excepciones.ReglaNegocioException;
import elementary.Rutix.common.interfaces.Comando;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Comparator;

@Service
public class RegistrarPagoDeReservaComando implements Comando<PagoDto, ReservaDto> {

    private static final Logger log = LoggerFactory.getLogger(RegistrarPagoDeReservaComando.class);

    private ModelMapper modelMapper;
    private ReservaRepository repo;
    private PerfilRutixRepository repoPerfil;
    private final GeneradorCobroFactory factory;

    public RegistrarPagoDeReservaComando(ModelMapper modelMapper,
                                         ReservaRepository repo,
                                         PerfilRutixRepository repoPerfil,
                                         GeneradorCobroFactory factory
                                          ) {
        this.modelMapper = modelMapper;
        this.repo = repo;
        this.repoPerfil = repoPerfil;
        this.factory = factory;
    }

    @Override
    public ReservaDto execute(PagoDto value) {
        Reserva reserva = this.repo.findById(value.getReservaId()).orElseThrow(()->new ReglaNegocioException("NO existe reserva"));

        //validaciones
        //la reserva debe estar confirmada por el conductor antes de poder pagarse

        if (reserva.getEstado()!= EstadoReserva.CONFIRMADA)
            throw new ReglaNegocioException("Reserva no se encuentra CONFIRMADA");


        //usuario que paga reserva debe ser el mismo que genero la reserva
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long uid = Long.parseLong(auth.getName());
        PerfilRutix usuarioLogueado = repoPerfil.findByUsuarioId(uid).orElseThrow(() -> new ReglaNegocioException("No se encontró el perfil"));

        // reserva.getPasajero() ya es el PerfilRutix del pasajero (lo trajo el
        // findById de la reserva), asi que comparar ids es suficiente: antes
        // esto volvia a buscar con findByUsuarioId(reserva.getPasajero().getId()),
        // pero ese metodo busca por el id del USUARIO (login), no por el id
        // del PERFIL, asi que ese id nunca iba a coincidir y siempre tiraba
        // "No se encontró el perfil".
        if (!usuarioLogueado.getId().equals(reserva.getPasajero().getId()))
            throw new ReglaNegocioException("Acceso Denegado para Pagar Reserva");

        Pago p = new Pago();
        p.setImporte(reserva.getValorTotalReserva());
        p.setEstado(EstadoPagoEnum.PENDIENTE);
        p.setForma(value.getForma());
        reserva.registrarPago(p);

        // La estrategia recibe los datos reales del pago (no un PagoDto
        // vacio): hoy EFECTIVO/MERCADOPAGO todavia no generan nada (ver
        // IntegracionPagoExterno), pero cuando se implemente la integracion
        // real de MercadoPago esto es lo que se manda a crear la preferencia
        // de cobro.
        GeneradorCobroStrategy strategy = factory.obtener(p.getForma());
        if (strategy == null) {
            throw new ReglaNegocioException("Forma de pago no soportada: " + p.getForma());
        }
        PagoDto solicitudCobro = modelMapper.map(p, PagoDto.class);
        solicitudCobro.setReservaId(reserva.getId());
        log.info("Generando cobro para reserva {} con forma {} (strategy={})",
                reserva.getId(), p.getForma(), strategy.getClass().getSimpleName());
        PagoDto cobro = strategy.generar(solicitudCobro);
        log.info("Resultado de la estrategia de cobro: cobro={}, checkoutUrl={}",
                cobro, cobro != null ? cobro.getCheckoutUrl() : null);
        if (cobro != null && cobro.getId() > 0) {
            p.setExternalId(String.valueOf(cobro.getId()));
        }

        // La reserva todavia no esta PAGADA: queda en un estado intermedio
        // segun la forma elegida, a la espera de que se confirme el pago
        // (el conductor confirma el efectivo recibido; Mercado Pago confirma
        // por webhook cuando se implemente la integracion real).
        reserva.iniciarPago(p.getForma());
        repo.save(reserva);
        log.info("Pago id={} de reserva {} guardado (externalId={})",
                p.getId(), reserva.getId(), p.getExternalId());

        ReservaDto resultado = modelMapper.map(reserva, ReservaDto.class);
        log.info("listaPagos del resultado mapeado: {}", resultado.getListaPagos());

        // El checkoutUrl (link de Mercado Pago) vive solo en el PagoDto que
        // devolvio la estrategia, nunca se persiste en el Pago -> hay que
        // "pegarlo" a mano en el pago recien creado dentro del ReservaDto
        // de salida para que el frontend pueda abrirlo.
        //
        // IMPORTANTE: no se puede matchear por p.getId(): como "reserva" ya
        // es una entidad managed (viene de findById en esta misma
        // transaccion), repo.save(reserva) termina haciendo
        // entityManager.merge(...) en vez de persist(...) para el Pago
        // nuevo colgado del cascade. merge() de una entidad transient crea
        // una COPIA managed con el id real asignado; esa copia no es la
        // misma instancia "p" que tenemos aca, asi que p.getId() se queda
        // en 0 para siempre aunque el INSERT ya se haya hecho en la base.
        // En cambio "resultado.getListaPagos()" si trae ids reales (se
        // volvio a consultar a la base), asi que alcanza con tomar el pago
        // de mayor id: por ser autoincremental, es siempre el recien
        // insertado.
        if (cobro != null && cobro.getCheckoutUrl() != null) {
            boolean matcheo = resultado.getListaPagos().stream()
                    .max(Comparator.comparingLong(PagoDto::getId))
                    .map(dto -> { dto.setCheckoutUrl(cobro.getCheckoutUrl()); return true; })
                    .orElse(false);
            log.info("Asignacion de checkoutUrl al pago de mayor id de la lista: {}", matcheo ? "OK" : "NO ENCONTRADO (lista vacia)");
        } else {
            log.info("No se adjunta checkoutUrl: cobro={}, checkoutUrl={}",
                    cobro != null, cobro != null ? cobro.getCheckoutUrl() : null);
        }

        return resultado;

    }

}
