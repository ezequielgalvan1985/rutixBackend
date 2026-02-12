package elementary.Rutix.Viajes.dominio;

import com.fasterxml.jackson.annotation.JsonProperty;
import elementary.Rutix.PerfilRutix.dominio.PerfilRutix;
import elementary.Rutix.common.EstadoReserva;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

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

    private Integer cantidadLugares;


    @NotNull
    @Enumerated(EnumType.STRING)
    private EstadoReserva estado;

    @Column(updatable = false)
    private LocalDateTime fechaAlta;


    @Column(updatable = false)
    private LocalDateTime fechaPuntaje;

    private Integer puntaje;

    @PrePersist
    public void prePersist() {
        this.fechaAlta = LocalDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires"));
    }

    public void registrarPago(Pago p){
        this.listaPagos.add(p);
    }
    public void eliminarPago(Long id){
        this.listaPagos.removeIf(pago->pago.getId()==id);
    }
}
