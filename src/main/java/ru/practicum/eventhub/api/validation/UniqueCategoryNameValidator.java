package ru.practicum.eventhub.api.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.eventhub.domain.repository.CategoryRepository;

@Component
@RequiredArgsConstructor
public class UniqueCategoryNameValidator implements ConstraintValidator<UniqueCategoryName, String> {
    private final CategoryRepository categoryRepository;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true;
        }
        return !categoryRepository.existsByNameIgnoreCase(value.trim());
    }
}
