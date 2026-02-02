package elementary.Rutix.common.validators;

import elementary.Rutix.PerfilRutix.dto.PerfilRutixDto;
import elementary.Rutix.PerfilRutix.repositorios.PerfilRutixRepository;
import elementary.Rutix.common.validators.interfaces.ValidPerfilRutixId;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PerfilRutixIdValidator implements ConstraintValidator<ValidPerfilRutixId, PerfilRutixDto> {
    @Autowired
    private PerfilRutixRepository repoPerfil;

    @Override
    public boolean isValid(PerfilRutixDto dto, ConstraintValidatorContext context) {
        System.out.println("Ingreso a validatorPerfil");
        // Your validation logic
        if (dto == null || dto.getId() == 0) return false;


        return this.repoPerfil.existsById(dto.getId());
    }
}