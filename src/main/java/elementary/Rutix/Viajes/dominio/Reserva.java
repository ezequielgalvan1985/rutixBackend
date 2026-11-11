package elementary.Rutix.Viajes.dominio;

import elementary.Rutix.PerfilRutix.dominio.PerfilRutix;
import elementary.Rutix.common.Enum.EstadoReserva;
import elementary.Rutix.common.Enum.MetodoPagoEnum;
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

    private String proveedor;
    private String externalId;
    private String paymentId;

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

    /**
     * Confirma el pago: pasa a PAGADA desde cualquiera de los dos estados
     * intermedios (iniciarPago). Hoy la dispara el webhook de Mercado Pago;
     * a futuro deberia poder dispararla tambien el conductor confirmando el
     * efectivo recibido. Idempotente (un webhook puede llegar mas de una
     * vez para el mismo pago).
     */
    public void confirmarPago(){
        if (this.getEstado() == EstadoReserva.PAGADA) return;
        if (this.getEstado() != EstadoReserva.PENDIENTE_CONFIRMACION_EFECTIVO
                && this.getEstado() != EstadoReserva.PENDIENTE_CONFIRMACION_MERCADOPAGO)
            throw new ReglaNegocioException("La reserva no tiene un pago pendiente de confirmacion");
        this.setEstado(EstadoReserva.PAGADA);
    }
    public void cancelar(){
        if(this.getEstado()!=EstadoReserva.PENDIENTE) throw new ReglaNegocioException("Solo se puede Cancelar una reserva pendiente");
        this.setEstado(EstadoReserva.CANCELADA);
    }

    /**
     * El pasajero elige la forma de pago: la reserva no pasa directo a
     * PAGADA, queda esperando que se confirme el pago (el conductor
     * confirma el efectivo recibido; Mercado Pago confirma por webhook).
     */
    public void iniciarPago(MetodoPagoEnum forma){
        if (this.getEstado() != EstadoReserva.CONFIRMADA)
            throw new ReglaNegocioException("Solo se puede pagar una reserva CONFIRMADA");

        switch (forma) {
            case EFECTIVO -> this.setEstado(EstadoReserva.PENDIENTE_CONFIRMACION_EFECTIVO);
            case MERCADOPAGO -> this.setEstado(EstadoReserva.PENDIENTE_CONFIRMACION_MERCADOPAGO);
            default -> throw new ReglaNegocioException("Forma de pago no soportada: " + forma);
        }
    }

    /**
     * Cancelacion en cascada por cancelacion del Viaje: a diferencia de cancelar(),
     * aqui la reserva puede venir de PENDIENTE, CONFIRMADA o PAGADA (el pasajero no
     * elige cancelar, el viaje ya no se va a realizar). Si ya estaba en un estado
     * terminal (CANCELADA/RECHAZADA) no hace nada.
     */
    public void cancelarPorViajeCancelado(){
        if (this.getEstado() == EstadoReserva.CANCELADA || this.getEstado() == EstadoReserva.RECHAZADA)
            return;
        this.setEstado(EstadoReserva.CANCELADA);
    }
}
