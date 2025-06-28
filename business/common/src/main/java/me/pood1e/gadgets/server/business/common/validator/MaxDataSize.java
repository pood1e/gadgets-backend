package me.pood1e.gadgets.server.business.common.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.intellij.lang.annotations.Language;

import java.lang.annotation.*;

/**
 * @author pood1e
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {DataSizeValidator.class})
@Documented
public @interface MaxDataSize {
    String message() default "数据大小超出限制";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Language("spel")
    String value();
}
