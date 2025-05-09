package ru.darin.nutrition_recommendation.util.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.darin.nutrition_recommendation.dto.AllergenTypeDTO;

public class ValidatorForAllergenTypeDTO implements ConstraintValidator<NutritionValidation, AllergenTypeDTO> {

    private static final String REGEX_VALUE = "[А-Я].+";

    private static final String MSG_NOT_EMPTY = "Поле не может быть пустым";

    private static final String MSG_WRONG_FORMAT = "Неверный формат ввода данных. Название типа аллергена должно начинаться с заглавной буквы";

    private static final String MSG_WRONG_LENGTH = "Тип аллергена должен содержать от 3 до 50 символов";

    @Override
    public boolean isValid(AllergenTypeDTO dto, ConstraintValidatorContext context) {
        boolean isValid = true;
        if (dto.getAllergenTitle() == null || dto.getAllergenTitle().isEmpty()) {
            context.disableDefaultConstraintViolation();
            context
                    .buildConstraintViolationWithTemplate(MSG_NOT_EMPTY)
                    .addPropertyNode("allergenTitle")
                    .addConstraintViolation();
            isValid = false;
        } else if (!dto.getAllergenTitle().matches(REGEX_VALUE)) {
            context.disableDefaultConstraintViolation();
            context
                    .buildConstraintViolationWithTemplate(MSG_WRONG_FORMAT)
                    .addPropertyNode("allergenTitle")
                    .addConstraintViolation();
            isValid = false;
        } else if (dto.getAllergenTitle().length() < 3 || dto.getAllergenTitle().length() > 50) {
            context.disableDefaultConstraintViolation();
            context
                    .buildConstraintViolationWithTemplate(MSG_WRONG_LENGTH)
                    .addPropertyNode("allergenTitle")
                    .addConstraintViolation();
            isValid = false;
        }
        return isValid;
    }

}