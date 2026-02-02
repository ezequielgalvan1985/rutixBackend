package elementary.Rutix.common.validators;

import elementary.Rutix.Viajes.dto.ViajeDto;
import elementary.Rutix.Viajes.repositorios.ViajeRepository;
import elementary.Rutix.common.validators.interfaces.ValidViajeId;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class ViajeIdValidator implements ConstraintValidator<ValidViajeId, ViajeDto> {

    @Autowired
    private ViajeRepository repo;
    /**
     * Implements the validation logic.
     * The state of {@code value} must not be altered.
     * <p>
     * This method can be accessed concurrently, thread-safety must be ensured
     * by the implementation.
     *
     * @param value   object to validate
     * @param context context in which the constraint is evaluated
     * @return {@code false} if {@code value} does not pass the constraint
     */
    @Override
    public boolean isValid(ViajeDto value, ConstraintValidatorContext context) {
        if (value==null) return false;
        return this.repo.existsById(value.getId());
    }
}
