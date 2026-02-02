package elementary.Rutix.PerfilRutix.comandos.cuentas;

import elementary.Rutix.PerfilRutix.dominio.PerfilRutix;
import elementary.Rutix.PerfilRutix.dto.CbuUnicoReglaNegocioDto;
import elementary.Rutix.PerfilRutix.dto.CuentaBancariaDto;
import elementary.Rutix.PerfilRutix.dominio.CuentaBancaria;
import elementary.Rutix.PerfilRutix.dto.PerfilRutixDto;
import elementary.Rutix.PerfilRutix.repositorios.PerfilRutixRepository;
import elementary.Rutix.PerfilRutix.utlis.reglas.policy.CbuUnicoReglaNegocio;
import elementary.Rutix.common.excepciones.ReglaNegocioException;
import elementary.Rutix.common.interfaces.Comando;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class RegistrarCuentaEnPerfilComando implements Comando<CuentaBancariaDto, PerfilRutixDto> {

    private final PerfilRutixRepository repo;
    private final ModelMapper modelMapper;
    private final CbuUnicoReglaNegocio cbuUnicoReglaNegocio;

    public RegistrarCuentaEnPerfilComando(
            PerfilRutixRepository repo,
            ModelMapper modelMapper,
            CbuUnicoReglaNegocio cbuUnicoReglaNegocio

    ) {
        this.repo = repo;
        this.modelMapper = modelMapper;
        this.cbuUnicoReglaNegocio = cbuUnicoReglaNegocio;
    }


    @Override
    public PerfilRutixDto execute(CuentaBancariaDto input) {
        PerfilRutix p = this.repo.findById(input.getPerfilId()).orElseThrow(()-> new ReglaNegocioException("Perfil inexistente"));
        this.cbuUnicoReglaNegocio.aplicar(new CbuUnicoReglaNegocioDto(input.getId(),input.getCbu()));

        CuentaBancaria r =modelMapper.map(input, CuentaBancaria.class);
        p.agregarCuenta(r);

        return  modelMapper.map(repo.save(p), PerfilRutixDto.class);

    }
}
