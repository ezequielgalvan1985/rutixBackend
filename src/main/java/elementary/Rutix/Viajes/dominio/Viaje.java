package elementary.Rutix.Viajes.dominio;

import elementary.Rutix.PerfilRutix.dominio.PerfilRutix;
import elementary.Rutix.PerfilRutix.dominio.Vehiculo;
import elementary.Rutix.common.EstadoViajeEnum;
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
    @JoinColumn(name = "perfil_creador_id",nullable = false, referencedColumnName ="id")
    private PerfilRutix perfilCreador;



    @OneToMany(mappedBy = "viaje",  cascade = CascadeType.ALL, fetch = FetchType.EAGER,orphanRemoval = true)
    private List<Reserva> listaReservas = new ArrayList<Reserva>();


    @Column(nullable = false)
    private Integer lugaresTotales;

    private Boolean pagaSenia;

    private Integer porcentajeSenia;


    private BigDecimal valor;

    @Enumerated(EnumType.STRING)
    private EstadoViajeEnum estado;

    public Integer getLugaresReservados(){
        return this.listaReservas.size();
    }
    public Integer getLugaresDisponibles(){
        return this.lugaresTotales - this.getLugaresReservados();
    }

    @Column(updatable = false)
    private LocalDateTime fechaAlta;

    @PrePersist
    public void prePersist() {
        this.fechaAlta = LocalDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires"));
    }

    public void registrarReserva(Reserva r ){
        this.listaReservas.add(r);
    }

    public void eliminarReserva(Long id){
        this.listaReservas.removeIf(reserva -> reserva.getId()==id);
    }
}
