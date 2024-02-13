package ru.ifmo.authservice.error;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;


@Component
public class CustomErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
        Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, options);
        if (getError(webRequest) instanceof MethodArgumentNotValidException e) {
            /* Добавляем адекватный вывод ошибок валидации */
            Map<String, List<String>> errors = new HashMap<>();
            e.getFieldErrors().forEach(fieldError -> {
                String fieldName = fieldError.getField();
                String defaultMessage = fieldError.getDefaultMessage();
                errors.put(fieldName, Arrays.stream(Objects.requireNonNull(defaultMessage).split(","))
                        .map(StringUtils::capitalize)
                        .map(s -> s.endsWith(".") ? s : s + ".")
                        .toList());
            });
            errorAttributes.put("message", "Error during validation of request body");
            errorAttributes.put("errors", errors);
        }

        return errorAttributes;
    }

}
