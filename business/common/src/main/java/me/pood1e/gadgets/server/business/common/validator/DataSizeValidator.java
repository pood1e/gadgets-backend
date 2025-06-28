package me.pood1e.gadgets.server.business.common.validator;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.unit.DataSize;

import java.nio.charset.StandardCharsets;

/**
 * @author pood1e
 */
@Slf4j
public class DataSizeValidator implements ConstraintValidator<MaxDataSize, String> {

    @Autowired
    @Setter
    private ApplicationContext applicationContext;
    @Getter
    @Setter
    private long maxBytes;

    private DataSize tryParseAsDataSize(String value) {
        try {
            return DataSize.parse(value);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private DataSize tryParseAsSpEl(String value) {
        try {
            SpelExpressionParser parser = new SpelExpressionParser();
            Expression expression = parser.parseExpression(value);
            StandardEvaluationContext context = new StandardEvaluationContext();
            context.setBeanResolver(new BeanFactoryResolver(applicationContext));
            return (DataSize) expression.getValue(context);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void initialize(MaxDataSize constraintAnnotation) {
        DataSize dataSize = tryParseAsDataSize(constraintAnnotation.value());
        if (dataSize == null) {
            dataSize = tryParseAsSpEl(constraintAnnotation.value());
        }
        if (dataSize != null) {
            this.maxBytes = dataSize.toBytes();
        } else {
            throw new IllegalArgumentException("cannot parse data size");
        }
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        long actualBytes = value.getBytes(StandardCharsets.UTF_8).length;
        boolean valid = actualBytes <= maxBytes;

        if (!valid) {
            String message = String.format("actual data size:%d exceed limit:%d", actualBytes, maxBytes);
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message)
                    .addConstraintViolation();
        }

        return valid;
    }
}