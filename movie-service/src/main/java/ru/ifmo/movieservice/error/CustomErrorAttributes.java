package ru.ifmo.movieservice.error;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.reactive.function.server.ServerRequest;

@Component
public class CustomErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest webRequest, ErrorAttributeOptions options) {
        Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, options);
        if (getError(webRequest) instanceof WebExchangeBindException e) {
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
        } else {
            errorAttributes.remove("message");
        }

        return errorAttributes;
    }
}
