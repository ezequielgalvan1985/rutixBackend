package elementary.Rutix.Viajes.comandos.pagos;

import elementary.Rutix.PerfilRutix.dominio.PerfilRutix;
import elementary.Rutix.PerfilRutix.repositorios.PerfilRutixRepository;
import elementary.Rutix.PerfilRutix.utlis.reglas.policy.PerfilExistenteReglaNegocio;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class RegistrarPagoDeReservaComando implements Comando<PagoDto, ReservaDto> {

    private ModelMapper modelMapper;
    private ReservaRepository repo;
    private PerfilRutixRepository repoPerfil;

    public RegistrarPagoDeReservaComando(ModelMapper modelMapper,
                                         ReservaRepository repo,
                                         PerfilRutixRepository repoPerfil
                                          ) {
        this.modelMapper = modelMapper;
        this.repo = repo;
        this.repoPerfil = repoPerfil;
    }

    @Override
    public ReservaDto execute(PagoDto value) {
        Reserva reserva = this.repo.findById(value.getReservaId()).orElseThrow(()->new ReglaNegocioException("NO existe reserva"));

        //validaciones
        //estado pendiente

        if (reserva.getEstado()!= EstadoReserva.PENDIENTE) throw new ReglaNegocioException("Reserva no se encuentra PENDIENTE");

        //usuario que paga reserva debe ser el mismo que genero la reserva
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long uid = Long.parseLong(auth.getName());
        PerfilRutix usuarioLogueado = repoPerfil.findByUsuarioId(uid).orElseThrow(() -> new ReglaNegocioException("No se encontró el perfil"));

        PerfilRutix usuarioReserva = repoPerfil.findByUsuarioId(reserva.getPasajero().getId())
                                        .orElseThrow(() -> new ReglaNegocioException("No se encontró el perfil"));

        if (usuarioLogueado.getId()!= usuarioReserva.getId()) throw new ReglaNegocioException("Acceso Denegado para Pagar Reserva");

        Pago p = new Pago();
        p.setImporte(reserva.getValorTotalReserva());
        p.setEstado(EstadoPagoEnum.CONFIRMADO);
        p.setForma(value.getForma());
        reserva.registrarPago(p);
        reserva.setEstado(EstadoReserva.PAGADA);
        repo.save(reserva);
        return modelMapper.map(reserva, ReservaDto.class);

    }

}
