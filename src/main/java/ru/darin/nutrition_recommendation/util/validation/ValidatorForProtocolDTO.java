package ru.darin.nutrition_recommendation.util.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.darin.nutrition_recommendation.dto.ProtocolDTO;

public class ValidatorForProtocolDTO implements ConstraintValidator<NutritionValidation, ProtocolDTO> {

    private static final String MSG_NOT_EMPTY = "Поле не может быть пустым";

    private static final String MSG_WRONG_LENGTH = "Название протокола должно содержать от 3 до 50 символов";

    private static final String MSG_WRONG_FORMAT = "Название протокола должно начинаться с заглавной буквы";

    private static final String REGEX_VALUE = "[А-Я].+";

    @Override
    public boolean isValid(ProtocolDTO dto, ConstraintValidatorContext context) {
        boolean isValid = true;
        if (dto.getProtocolTitle() == null || dto.getProtocolTitle().isEmpty()) {
            context.disableDefaultConstraintViolation();
            context
                    .buildConstraintViolationWithTemplate(MSG_NOT_EMPTY)
                    .addPropertyNode("protocolTitle")
                    .addConstraintViolation();
            isValid = false;
        } else if (dto.getProtocolTitle().length() < 3 || dto.getProtocolTitle().length() > 50) {
            context.disableDefaultConstraintViolation();
            context
                    .buildConstraintViolationWithTemplate(MSG_WRONG_LENGTH)
                    .addPropertyNode("protocolTitle")
                    .addConstraintViolation();
            isValid = false;
        } else if (!dto.getProtocolTitle().matches(REGEX_VALUE)) {
            context.disableDefaultConstraintViolation();
            context
                    .buildConstraintViolationWithTemplate(MSG_WRONG_FORMAT)
                    .addPropertyNode("protocolTitle")
                    .addConstraintViolation();
            isValid = false;
        }
        return isValid;
    }

}