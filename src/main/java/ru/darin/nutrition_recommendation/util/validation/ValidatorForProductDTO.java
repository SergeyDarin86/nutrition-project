package ru.darin.nutrition_recommendation.util.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.darin.nutrition_recommendation.dto.ProductDTO;

public class ValidatorForProductDTO implements ConstraintValidator<NutritionValidation, ProductDTO> {

    private static final String MSG_NOT_EMPTY = "Поле не может быть пустым";

    private static final String MSG_WRONG_LENGTH = "Название продукта должно содержать от 3 до 50 символов";

    private static final String REGEX_VALUE = "[А-Я].+";

    private static final String MSG_WRONG_FORMAT = "Название продукта должно начинаться с заглавной буквы";

    private static final String MSG_WRONG_COUNT_OF_ALLERGEN_TYPES = "Продукт одновременно может относиться не более, чем к двум типам аллергенов";

    @Override
    public boolean isValid(ProductDTO dto, ConstraintValidatorContext context) {
        boolean isValid = true;
        if (dto.getProduct() == null || dto.getProduct().isEmpty()) {
            context.disableDefaultConstraintViolation();
            context
                    .buildConstraintViolationWithTemplate(MSG_NOT_EMPTY)
                    .addPropertyNode("product")
                    .addConstraintViolation();
            isValid = false;
        } else if (dto.getProduct().length() < 3 || dto.getProduct().length() > 50) {
            context.disableDefaultConstraintViolation();
            context
                    .buildConstraintViolationWithTemplate(MSG_WRONG_LENGTH)
                    .addPropertyNode("product")
                    .addConstraintViolation();
            isValid = false;
        } else if (!dto.getProduct().matches(REGEX_VALUE)) {
            context.disableDefaultConstraintViolation();
            context
                    .buildConstraintViolationWithTemplate(MSG_WRONG_FORMAT)
                    .addPropertyNode("product")
                    .addConstraintViolation();
            isValid = false;
        }else if (dto == null) {
            context.disableDefaultConstraintViolation();
            context
                    .buildConstraintViolationWithTemplate(MSG_WRONG_COUNT_OF_ALLERGEN_TYPES)
                    .addPropertyNode("product")
                    .addConstraintViolation();
            isValid = false;
        }
        return isValid;
    }

}