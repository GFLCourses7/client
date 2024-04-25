package com.geeksforless.client.model.dto;

import java.util.Objects;

public class StepDtoInternal {

    private Long id;

    private String action;

    private String value;

    public StepDtoInternal() {
    }

    public StepDtoInternal(Long id, String action, String value) {
        this.id = id;
        this.action = action;
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        StepDtoInternal that = (StepDtoInternal) o;
        return Objects.equals(id, that.id) && Objects.equals(action, that.action) && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, action, value);
    }

    @Override
    public String toString() {
        return "StepDtoInternal{" +
                "id=" + id +
                ", action='" + action + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
