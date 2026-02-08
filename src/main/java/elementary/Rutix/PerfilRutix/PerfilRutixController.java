package elementary.Rutix.PerfilRutix;

import elementary.Rutix.PerfilRutix.comandos.ActualizarPerfilRutixComando;
import elementary.Rutix.PerfilRutix.comandos.EliminarPerfilRutixComando;
import elementary.Rutix.PerfilRutix.comandos.tarjetas.EliminarTarjetaDePerfilComando;
import elementary.Rutix.PerfilRutix.comandos.RegistrarPerfilRutixComando;
import elementary.Rutix.PerfilRutix.consultas.BuscarIdPerfilRutixConsulta;
import elementary.Rutix.PerfilRutix.consultas.ListadoPerfilRutixConsulta;
import elementary.Rutix.PerfilRutix.consultas.ObtenerMiPerfilConsulta;
import elementary.Rutix.PerfilRutix.dto.PerfilRutixDto;
import elementary.Rutix.common.dto.ConsultaListadoRequestDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

@RestController

@RequestMapping("/v1/perfil")
public class PerfilRutixController {


    @Autowired
    private RegistrarPerfilRutixComando registrar;

    @Autowired
    private ActualizarPerfilRutixComando actualizar;
    @Autowired
    private EliminarPerfilRutixComando eliminar;

    @Autowired
    private BuscarIdPerfilRutixConsulta buscar;

    @Autowired
    private ListadoPerfilRutixConsulta listar;

    @Autowired
    private EliminarTarjetaDePerfilComando eliminarTarjeta;

    @Autowired
    private ObtenerMiPerfilConsulta obtenerMiPerfilConsulta;

    private static final Logger logger = LoggerFactory.getLogger(PerfilRutixController.class);


    @GetMapping()
    public ResponseEntity<List<PerfilRutixDto>> findAll (
            @RequestParam(value = "limit", required = false, defaultValue = "25") int limit,
            @RequestParam(value = "offset", required = false, defaultValue = "0") Long offset){
        List<PerfilRutixDto> resultset = listar.execute(new ConsultaListadoRequestDto(offset,limit));
        if (resultset.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(resultset);
    }

    @GetMapping(value="/{id}")
    public ResponseEntity<PerfilRutixDto> view(@PathVariable("id") Long id){
        PerfilRutixDto registro =  buscar.execute(id);
        return ResponseEntity.ok(registro);

    }
    @GetMapping(value="/me")
    public ResponseEntity<PerfilRutixDto> getMiPerfil(){

        PerfilRutixDto registro =  obtenerMiPerfilConsulta.execute(null);

        return ResponseEntity.ok(registro);

    }
    @GetMapping("/me2")
    public String me() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<PerfilRutixDto> add(@RequestBody PerfilRutixDto m, @RequestHeader("X-User-Name") String username) throws IOException, TimeoutException {
        return ResponseEntity.ok(registrar.execute(m));
    }

    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    public void edit(@RequestBody PerfilRutixDto dto){
        actualizar.execute(dto);
    }

    @DeleteMapping(value="/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id){
        eliminar.execute(id);
    }


}
