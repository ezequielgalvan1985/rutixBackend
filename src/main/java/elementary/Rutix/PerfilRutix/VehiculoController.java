package elementary.Rutix.PerfilRutix;

import elementary.Rutix.PerfilRutix.comandos.dto.ActualizarVehiculoComandoDto;
import elementary.Rutix.PerfilRutix.comandos.vehiculos.ActualizarVehiculoComando;
import elementary.Rutix.PerfilRutix.comandos.vehiculos.EliminarVehiculoComando;
import elementary.Rutix.PerfilRutix.comandos.vehiculos.RegistrarVehiculoComando;
import elementary.Rutix.PerfilRutix.consultas.vehiculos.BuscarIdVehiculoConsulta;
import elementary.Rutix.PerfilRutix.consultas.vehiculos.ListadoVehiculoConsulta;
import elementary.Rutix.PerfilRutix.dto.EliminarVehiculoDePerfilComandoDto;
import elementary.Rutix.PerfilRutix.dto.PerfilRutixDto;
import elementary.Rutix.PerfilRutix.dto.VehiculoDto;
import elementary.Rutix.common.dto.ConsultaListadoRequestDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping("/v1/perfil/vehiculos")
public class VehiculoController {

    private static final Logger logger = LoggerFactory.getLogger(VehiculoController.class);

    private final RegistrarVehiculoComando registrar;
    private final ActualizarVehiculoComando actualizar;
    private final EliminarVehiculoComando eliminar;
    private final ListadoVehiculoConsulta listar;
    private final BuscarIdVehiculoConsulta buscar;

    public VehiculoController(RegistrarVehiculoComando registrar, ActualizarVehiculoComando actualizar, EliminarVehiculoComando eliminar, ListadoVehiculoConsulta listar, BuscarIdVehiculoConsulta buscar) {
        this.registrar = registrar;
        this.actualizar = actualizar;
        this.eliminar = eliminar;
        this.listar = listar;
        this.buscar = buscar;
    }


    @GetMapping()
    public ResponseEntity<List<VehiculoDto>> findAll (
            @RequestParam(value = "limit", required = false, defaultValue = "25") int limit,
            @RequestParam(value = "offset", required = false, defaultValue = "0") Long offset){
        List<VehiculoDto> resultset = listar.execute(new ConsultaListadoRequestDto(offset,limit));
        if (resultset.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(resultset);
    }

    @GetMapping(value="/{id}")
    public ResponseEntity<VehiculoDto> view(@PathVariable("id") Long id){
        VehiculoDto registro =  buscar.execute(id);
        return ResponseEntity.ok(registro);

    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<PerfilRutixDto> add(@RequestBody VehiculoDto m) throws IOException, TimeoutException, IOException, TimeoutException {
        return ResponseEntity.ok(registrar.execute(m));
    }

    @PutMapping()
    public void edit(@RequestBody ActualizarVehiculoComandoDto dto){
        actualizar.execute(dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id){
        eliminar.execute(id);
    }



}
