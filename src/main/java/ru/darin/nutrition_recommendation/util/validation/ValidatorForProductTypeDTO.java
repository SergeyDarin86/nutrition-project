package ru.darin.nutrition_recommendation.util.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.darin.nutrition_recommendation.dto.ProductTypeDTO;

public class ValidatorForProductTypeDTO implements ConstraintValidator<NutritionValidation, ProductTypeDTO> {

    private static final String MSG_NOT_EMPTY = "Поле не может быть пустым";

    private static final String MSG_WRONG_LENGTH = "Название типа продукта должно содержать от 5 до 50 символов";

    @Override
    public boolean isValid(ProductTypeDTO dto, ConstraintValidatorContext context) {
        boolean isValid = true;
        if (dto.getProductType() == null || dto.getProductType().isEmpty()) {
            context.disableDefaultConstraintViolation();
            context
                    .buildConstraintViolationWithTemplate(MSG_NOT_EMPTY)
                    .addPropertyNode("productType")
                    .addConstraintViolation();
            isValid = false;
        } else if (dto.getProductType().length() < 5 || dto.getProductType().length() > 50) {
            context.disableDefaultConstraintViolation();
            context
                    .buildConstraintViolationWithTemplate(MSG_WRONG_LENGTH)
                    .addPropertyNode("productType")
                    .addConstraintViolation();
            isValid = false;
        }
        return isValid;
    }

}