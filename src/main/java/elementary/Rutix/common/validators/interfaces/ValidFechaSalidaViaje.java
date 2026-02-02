package elementary.Rutix.common.validators.interfaces;

import elementary.Rutix.common.validators.FechaSalidaViajeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FechaSalidaViajeValidator.class)
public @interface ValidFechaSalidaViaje {
    String message() default "Fecha no puede ser menor a la fecha del dia";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
