package me.pood1e.gadgets.server.business.common.validator;

import jakarta.validation.ConstraintValidatorContext;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.util.unit.DataSize;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DataSizeValidatorTest {

    @Nested
    class Initialize {
        @Test
        void testDataSizeStringSuccess() {
            // Arrange
            DataSizeValidator validator = new DataSizeValidator();
            MaxDataSize annotation = mock(MaxDataSize.class);
            when(annotation.value()).thenReturn("5MB");

            // Act
            validator.initialize(annotation);

            // Assert
            assertEquals(DataSize.ofMegabytes(5).toBytes(), validator.getMaxBytes());
        }

        @Test
        void testSpELExpressionSuccess() {
            // Arrange
            DataSizeValidator validator = new DataSizeValidator();
            MaxDataSize annotation = mock(MaxDataSize.class);
            when(annotation.value()).thenReturn("@properties.size");
            ApplicationContext applicationContext = mock(ApplicationContext.class);
            TestProperties testProperties = new TestProperties();
            testProperties.setSize(DataSize.ofBytes(100));
            when(applicationContext.getBean("properties")).thenReturn(testProperties);
            validator.setApplicationContext(applicationContext);

            // Act
            validator.initialize(annotation);

            // Assert
            assertEquals(DataSize.ofBytes(100).toBytes(), validator.getMaxBytes());
        }

        @Test
        void testDataSizeStringFailed() {
            // Arrange
            DataSizeValidator validator = new DataSizeValidator();
            MaxDataSize annotation = mock(MaxDataSize.class);
            when(annotation.value()).thenReturn("5MBx");

            // Act & Assert
            assertThrows(IllegalArgumentException.class, () -> validator.initialize(annotation));
        }

        @Test
        void testSpELExpressionFailed() {
            // Arrange
            DataSizeValidator validator = new DataSizeValidator();
            MaxDataSize annotation = mock(MaxDataSize.class);
            // not found bean
            when(annotation.value()).thenReturn("@properties.size");

            // Act & Assert
            assertThrows(IllegalArgumentException.class, () -> validator.initialize(annotation));
        }
    }

    @Nested
    class Valid {
        @Test
        void testValidSuccess() {
            // Arrange
            DataSizeValidator validator = new DataSizeValidator();
            validator.setMaxBytes(DataSize.ofBytes(10).toBytes());

            // Act
            boolean result = validator.isValid("test", null);

            // Assert
            assertTrue(result);
        }

        @Test
        void testValidNullSuccess() {
            // Arrange
            DataSizeValidator validator = new DataSizeValidator();
            validator.setMaxBytes(DataSize.ofBytes(10).toBytes());

            // Act
            boolean result = validator.isValid(null, null);

            // Assert
            assertTrue(result);
        }

        @Test
        void testValidFailed() {
            // Arrange
            DataSizeValidator validator = new DataSizeValidator();
            validator.setMaxBytes(DataSize.ofBytes(3).toBytes());
            ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);
            ConstraintValidatorContext.ConstraintViolationBuilder violationBuilder = mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);
            when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(violationBuilder);

            // Act
            boolean result = validator.isValid("test", context);

            // Assert
            assertFalse(result);
            verify(context).disableDefaultConstraintViolation();
            verify(context).buildConstraintViolationWithTemplate(
                    contains("exceed limit")
            );
        }
    }

    @Getter
    @Setter
    static class TestProperties {
        private DataSize size;
    }
}