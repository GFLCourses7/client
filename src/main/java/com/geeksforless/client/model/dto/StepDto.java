package com.geeksforless.client.model.dto;

import java.util.Objects;

public class StepDto {

    private String action;
    private String value;

    public StepDto() {
    }

    public StepDto(String action, String value) {
        this.action = action;
        this.value = value;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepDto stepDto = (StepDto) o;
        return Objects.equals(action, stepDto.action) && Objects.equals(value, stepDto.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(action, value);
    }

    @Override
    public String toString() {
        return "StepDto{" +
                "action='" + action + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
