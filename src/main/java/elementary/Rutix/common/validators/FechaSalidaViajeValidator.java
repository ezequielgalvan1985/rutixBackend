package elementary.Rutix.common.validators;

import elementary.Rutix.common.validators.interfaces.ValidFechaSalidaViaje;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Component
public class FechaSalidaViajeValidator implements ConstraintValidator<ValidFechaSalidaViaje, LocalDate> {
    @Override
    public boolean isValid(LocalDate fecha, ConstraintValidatorContext context) {

        if (fecha.isBefore(LocalDateTime.now().toLocalDate()))
            return false;
        return true;
    }
}
