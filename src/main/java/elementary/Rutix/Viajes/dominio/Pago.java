package elementary.Rutix.Viajes.dominio;

import elementary.Rutix.PerfilRutix.dominio.PerfilRutix;
import elementary.Rutix.common.EstadoPagoEnum;
import elementary.Rutix.common.MetodoPagoEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pagos")
public class Pago {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long id;

    @ManyToOne
    @JoinColumn(name="reserva_id", nullable = false)
    private Reserva reserva;

    private BigDecimal importe;

    @Enumerated(EnumType.STRING)
    private EstadoPagoEnum estado;

    @NotNull
    @Enumerated(EnumType.STRING)
    private MetodoPagoEnum forma;


    @Column(updatable = false)
    private LocalDateTime fechaAlta;

    @PrePersist
    public void prePersist() {
        this.fechaAlta = LocalDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires"));
    }

}
