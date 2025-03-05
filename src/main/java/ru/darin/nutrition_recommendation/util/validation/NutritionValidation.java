package ru.darin.nutrition_recommendation.util.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ValidatorForPersonDTO.class})
@Target({ElementType.TYPE})
public @interface NutritionValidation{

    String message() default "Ошибка валидации";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
