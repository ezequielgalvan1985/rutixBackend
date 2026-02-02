package elementary.Rutix.PerfilRutix.dominio;

import elementary.Rutix.PerfilRutix.comandos.dto.ActualizarVehiculoComandoDto;
import elementary.Rutix.PerfilRutix.dto.VehiculoDto;
import elementary.Rutix.Viajes.dominio.Viaje;
import elementary.Rutix.common.excepciones.ReglaNegocioException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class PerfilRutix {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    private Long usuarioId;

    @Column(nullable = true)
    private Integer valoracion;

    @Column(length = 100)
    private String direccion;

    @Column(length = 20)
    private String telefono;

    @Column(length = 50)
    private String email;

    @Column(length = 50)
    private String ciudad;

    @OneToMany(mappedBy = "perfil",  cascade = CascadeType.ALL, fetch = FetchType.EAGER,orphanRemoval = true)
    private List<Vehiculo> listaVehiculos = new ArrayList<Vehiculo>();; //debito o credito

    @OneToMany(mappedBy = "perfil",  cascade = CascadeType.ALL, fetch = FetchType.EAGER,orphanRemoval = true)
    private List<Tarjeta> listaTarjetas = new ArrayList<Tarjeta>();; //debito o credito

    @OneToMany(mappedBy = "perfil",  cascade = CascadeType.ALL, fetch = FetchType.EAGER,orphanRemoval = true)
    private List<CuentaBancaria> listaCuentas = new ArrayList<CuentaBancaria>();; //debito o credito



    @Column(updatable = false)
    private LocalDateTime fechaAlta;


    @PrePersist
    public void prePersist() {
        this.fechaAlta = LocalDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires"));
    }

    public void agregarTarjeta(Tarjeta t){
        this.listaTarjetas.add(t);
    }
    public void eliminarTarjeta(Long tarjetaId) {
        listaTarjetas.removeIf(t -> t.getId()==tarjetaId);
    }


    public void agregarVehiculo(Vehiculo v ){
        this.listaVehiculos.add(v);
    }
    public void actualizarVehiculo(ActualizarVehiculoComandoDto cmd){
        Vehiculo v = this.listaVehiculos.stream()
                .filter(x -> x.getId().equals(cmd.getId()))
                .findFirst()
                .orElseThrow(() -> new ReglaNegocioException("Vehiculo inexistente"));

        v.setMarca(cmd.getMarca());
        v.setModelo(cmd.getModelo());
        v.setPatente(cmd.getPatente());
        v.setAnio(cmd.getAnio());
        v.setDescripcion(cmd.getDescripcion());
        v.setActivo(cmd.getActivo());
        v.setTieneAire(cmd.getTieneAire());


    }
    public void eliminarVehiculo(Long id){
        listaVehiculos.removeIf(vehiculo -> vehiculo.getId()==id);
    }


    public void agregarCuenta(CuentaBancaria c){
        this.listaCuentas.add(c);
    }

    public void eliminarCuenta(Long id){
        this.listaCuentas.removeIf(c->c.getId()==id);
    }


}
