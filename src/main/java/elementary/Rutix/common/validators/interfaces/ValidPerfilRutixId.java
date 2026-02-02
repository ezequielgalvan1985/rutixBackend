package elementary.Rutix.common.validators.interfaces;

import elementary.Rutix.common.validators.PerfilRutixIdValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PerfilRutixIdValidator.class)
public @interface ValidPerfilRutixId {
    String message() default "Codigo de Perfil Invalido";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
