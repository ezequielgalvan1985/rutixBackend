package elementary.Rutix.common.validators.interfaces;

import elementary.Rutix.common.validators.PerfilRutixIdValidator;
import elementary.Rutix.common.validators.ViajeIdValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ViajeIdValidator.class)
public @interface ValidViajeId {
    String message() default "Codigo de Viaje Invalido";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
