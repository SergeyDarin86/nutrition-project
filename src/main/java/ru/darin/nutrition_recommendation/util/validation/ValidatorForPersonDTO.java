package ru.darin.nutrition_recommendation.util.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.darin.nutrition_recommendation.dto.PersonDTO;

public class ValidatorForPersonDTO implements ConstraintValidator<NutritionValidation, PersonDTO> {

    private static final String REGEX_VALUE = "[А-ЯЁ][а-яё]+ [А-ЯЁ][а-яё]+ [А-ЯЁ][а-яё]+";

    private static final String MSG_NOT_EMPTY = "Поле не может быть пустым";

    private static final String MSG_WRONG_FORMAT = "Неверный формат ввода данных. ФИО должно быть следующего формата: Фамилия Имя Отчество";

    private static final String MSG_WRONG_LENGTH = "ФИО должно содержать от 9 до 50 символов";

    @Override
    public boolean isValid(PersonDTO dto, ConstraintValidatorContext context) {
        boolean isValid = true;
        if (dto.getFullName() == null || dto.getFullName().isEmpty()) {
            context.disableDefaultConstraintViolation();
            context
                    .buildConstraintViolationWithTemplate(MSG_NOT_EMPTY)
                    .addPropertyNode("fullName")
                    .addConstraintViolation();
            isValid = false;
        } else if (!dto.getFullName().matches(REGEX_VALUE)) {
            context.disableDefaultConstraintViolation();
            context
                    .buildConstraintViolationWithTemplate(MSG_WRONG_FORMAT)
                    .addPropertyNode("fullName")
                    .addConstraintViolation();
            isValid = false;
        } else if (dto.getFullName().length() < 9 || dto.getFullName().length() > 50) {
            context.disableDefaultConstraintViolation();
            context
                    .buildConstraintViolationWithTemplate(MSG_WRONG_LENGTH)
                    .addPropertyNode("fullName")
                    .addConstraintViolation();
            isValid = false;
        }
        return isValid;
    }

}