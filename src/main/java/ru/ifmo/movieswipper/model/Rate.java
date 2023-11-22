package ru.ifmo.movieswipper.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Value;

@Value
public record Rate(@Min(0) @Max(5) float value) {

    @JsonCreator
    public Rate(float value) {
        this.value = value;
    }
}
