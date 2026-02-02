package elementary.Rutix.PerfilRutix;

import elementary.Rutix.PerfilRutix.dto.CuentaBancariaDto;
import elementary.Rutix.PerfilRutix.comandos.cuentas.ActualizarCuentaBancariaComando;
import elementary.Rutix.PerfilRutix.comandos.cuentas.EliminarCuentaDePerfilComando;
import elementary.Rutix.PerfilRutix.comandos.cuentas.RegistrarCuentaEnPerfilComando;
import elementary.Rutix.PerfilRutix.consultas.cuentas.BuscarIdCuentaBancariaConsulta;
import elementary.Rutix.PerfilRutix.consultas.cuentas.ListarCuentaBancariaConsulta;
import elementary.Rutix.PerfilRutix.dto.EliminarCuentaDePerfilComandoDto;
import elementary.Rutix.common.dto.ConsultaListadoRequestDto;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;
import elementary.Rutix.PerfilRutix.dto.PerfilRutixDto;

@RestController
@RequestMapping("/v1/perfil/cuentas")
public class CuentaBancariaController {

    private RegistrarCuentaEnPerfilComando registrarCuentaEnPerfilComando;
    private ActualizarCuentaBancariaComando actualizar;
    private EliminarCuentaDePerfilComando eliminar;
    private BuscarIdCuentaBancariaConsulta buscar;
    private ListarCuentaBancariaConsulta listar;
    private static final Logger logger = LoggerFactory.getLogger(CuentaBancariaController.class);

    public CuentaBancariaController(RegistrarCuentaEnPerfilComando registrarCuentaEnPerfilComando, ActualizarCuentaBancariaComando actualizar, EliminarCuentaDePerfilComando eliminar, BuscarIdCuentaBancariaConsulta buscar, ListarCuentaBancariaConsulta listar) {
        this.registrarCuentaEnPerfilComando = registrarCuentaEnPerfilComando;
        this.actualizar = actualizar;
        this.eliminar = eliminar;
        this.buscar = buscar;
        this.listar = listar;
    }

    @GetMapping()
    public ResponseEntity<List<CuentaBancariaDto>> findAll (
            @RequestParam(value = "limit", required = false, defaultValue = "25") int limit,
            @RequestParam(value = "offset", required = false, defaultValue = "0") Long offset){
        List<CuentaBancariaDto> resultset = listar.execute(new ConsultaListadoRequestDto(offset,limit));
        if (resultset.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(resultset);
    }

    @GetMapping(value="/{id}")
    public ResponseEntity<CuentaBancariaDto> view(@PathVariable("id") Long id){
        CuentaBancariaDto registro =  buscar.execute(id);
        return ResponseEntity.ok(registro);

    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<PerfilRutixDto> add(@RequestBody @Valid CuentaBancariaDto m, @RequestHeader("X-User-Name") String username) throws IOException, TimeoutException, IOException, TimeoutException {
        return ResponseEntity.ok(registrarCuentaEnPerfilComando.execute(m));
    }

    @PutMapping()
    public void edit(@RequestBody @Valid CuentaBancariaDto dto){
        actualizar.execute(dto);
    }

    @DeleteMapping()
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@RequestBody @Valid EliminarCuentaDePerfilComandoDto request){
        eliminar.execute(request);
    }

}
