package elementary.Rutix.Viajes.dominio;

import elementary.Rutix.PerfilRutix.dominio.PerfilRutix;
import elementary.Rutix.PerfilRutix.dominio.Vehiculo;
import elementary.Rutix.common.Enum.EstadoReserva;
import elementary.Rutix.common.Enum.EstadoViajeEnum;
import elementary.Rutix.common.excepciones.ReglaNegocioException;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "viajes")
public class Viaje {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private LocalDate fechaSalida;

    @Column(nullable = false)
    private LocalTime horaSalida;

    @Column(length = 100, nullable = false)
    private String ciudadPartida;

    @Column(length = 100, nullable = false)
    private String ciudadDestino;

    @Column(nullable = false)
    private LocalTime horaLlegada;

    @ManyToOne
    @JoinColumn(name = "perfil_id",nullable = false, referencedColumnName ="id")
    private PerfilRutix conductor;

    @ManyToOne
    @JoinColumn(name = "vehiculo_id", nullable = false)
    private Vehiculo vehiculo;

    @OneToMany(mappedBy = "viaje",  cascade = CascadeType.ALL, fetch = FetchType.EAGER,orphanRemoval = true)
    private List<Reserva> listaReservas = new ArrayList<Reserva>();


    @Column(nullable = false)
    private Integer asientos;

    private Boolean pagaSenia;

    private Integer porcentajeSenia;


    private BigDecimal valor;

    @Enumerated(EnumType.STRING)
    private EstadoViajeEnum estado;


    private Integer asientosDisponibles;

    private Integer asientosReservados;

    @Column(updatable = false)
    private LocalDateTime fechaAlta;

    @PrePersist
    public void prePersist() {
        this.fechaAlta = LocalDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires"));
    }

    public void registrarReserva(Reserva r ){
        if (r == null) {
            throw new ReglaNegocioException("La reserva no puede ser nula");
        }
        this.listaReservas.add(r);
    }

    public void eliminarReserva(Long id){
        Reserva reserva = this.buscarReservaPorId(id);
        if (reserva.getEstado() == EstadoReserva.CONFIRMADA) {
            throw new ReglaNegocioException("No se puede eliminar una reserva confirmada");
        }
        this.listaReservas.remove(reserva);
    }

    public void confirmarReserva(Long id){
        Reserva r = this.buscarReservaPorId(id);
        r.confirmar();
    }

    public void rechazarReserva(Long id){
        Reserva r = this.buscarReservaPorId(id);
        r.rechazar();
        this.setAsientosReservados(this.getAsientosReservados()- r.getAsientos());
        this.setAsientosDisponibles(this.getAsientosDisponibles()+r.getAsientos());
    }
    private Reserva buscarReservaPorId(Long id) {
        return this.listaReservas.stream()
                .filter(reserva -> reserva.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ReglaNegocioException("No existe la reserva"));
    }
}
