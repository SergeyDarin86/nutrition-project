package ru.darin.nutrition_recommendation.util.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.darin.nutrition_recommendation.dto.IllnessDTO;

public class ValidatorForIllnessDTO implements ConstraintValidator<NutritionValidation, IllnessDTO> {

    private static final String MSG_NOT_EMPTY = "Поле не может быть пустым";

    private static final String MSG_WRONG_LENGTH = "Название заболевания должно содержать от 3 до 50 символов";

    private static final String MSG_WRONG_FORMAT = "Название заболевания должно начинаться с заглавной буквы";

    private static final String REGEX_VALUE = "[А-Я].+";

    @Override
    public boolean isValid(IllnessDTO dto, ConstraintValidatorContext context) {
        boolean isValid = true;
        if (dto.getIllnessTitle() == null || dto.getIllnessTitle().isEmpty()) {
            context.disableDefaultConstraintViolation();
            context
                    .buildConstraintViolationWithTemplate(MSG_NOT_EMPTY)
                    .addPropertyNode("illnessTitle")
                    .addConstraintViolation();
            isValid = false;
        } else if (dto.getIllnessTitle().length() < 3 || dto.getIllnessTitle().length() > 50) {
            context.disableDefaultConstraintViolation();
            context
                    .buildConstraintViolationWithTemplate(MSG_WRONG_LENGTH)
                    .addPropertyNode("illnessTitle")
                    .addConstraintViolation();
            isValid = false;
        } else if (!dto.getIllnessTitle().matches(REGEX_VALUE)) {
            context.disableDefaultConstraintViolation();
            context
                    .buildConstraintViolationWithTemplate(MSG_WRONG_FORMAT)
                    .addPropertyNode("illnessTitle")
                    .addConstraintViolation();
            isValid = false;
        }
        return isValid;
    }

}