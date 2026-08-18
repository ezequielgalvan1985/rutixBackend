package elementary.Rutix.Viajes.dominio;

import elementary.Rutix.PerfilRutix.dominio.PerfilRutix;
import elementary.Rutix.common.Enum.EstadoReserva;
import elementary.Rutix.common.excepciones.ReglaNegocioException;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="reservas")
public class Reserva {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="perfil_id", nullable=false)
    private PerfilRutix pasajero;

    @ManyToOne
    @JoinColumn(name="viaje_id", nullable = false)
    private Viaje viaje;


    @OneToMany(mappedBy = "reserva",  cascade = CascadeType.ALL, fetch = FetchType.LAZY,orphanRemoval = true)
    private List<Pago> listaPagos = new ArrayList<Pago>();

    private Integer asientos;


    @NotNull
    @Enumerated(EnumType.STRING)
    private EstadoReserva estado;

    @Column(updatable = false)
    private LocalDateTime fechaAlta;

    @Column(updatable = true)
    private LocalDateTime fechaActualizacion;

    @Column(updatable = false)
    private LocalDateTime fechaPuntaje;

    private Integer puntaje;

    public BigDecimal getValorTotalReserva(){
        return this.viaje.getValor().multiply(BigDecimal.valueOf(this.getAsientos().longValue()));
    }

    @PrePersist
    public void prePersist() {
        this.fechaAlta = LocalDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires"));
    }

    public void registrarPago(Pago p){
        p.setReserva(this);
        this.listaPagos.add(p);

    }
    public void eliminarPago(Long id){
        this.listaPagos.removeIf(pago->pago.getId()==id);
    }

    public void confirmar(){
        if(this.getEstado()!=EstadoReserva.PENDIENTE) throw new ReglaNegocioException("Solo se puede Confirmar una reserva pendiente");
        this.setEstado(EstadoReserva.CONFIRMADA);
    }
    public void rechazar(){
        if(this.getEstado()!=EstadoReserva.PENDIENTE) throw new ReglaNegocioException("Solo se puede Rechazar una reserva pendiente");
        this.setEstado(EstadoReserva.RECHAZADA);
    }
    public void cancelar(){
        if(this.getEstado()!=EstadoReserva.PENDIENTE) throw new ReglaNegocioException("Solo se puede Cancelar una reserva pendiente");
        this.setEstado(EstadoReserva.CANCELADA);
    }
}
