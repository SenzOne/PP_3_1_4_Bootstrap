package ru.kata.spring.boot_security.until;

import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.List;

@Component
public class MyDataValidator {

    private List<BindingResult> bindingResultList = new ArrayList<>();
    private String errors = "";

    public void validate(BindingResult bindingResult) {
        bindingResultList.add(bindingResult);
    }

    public void addAllErrorsAsString() {
        StringBuilder errorString = new StringBuilder();

        for (BindingResult bindingResult : bindingResultList) {
            for (ObjectError error : bindingResult.getAllErrors()) {
                String fieldName = error.getObjectName();
                String errorMessage = error.getDefaultMessage();

                errorString.append(fieldName.toUpperCase()).append(": ").append(errorMessage).append("\n");
            }
        }
        errors = errorString.toString();
    }

    public String getAllErrorsAsString() {
        addAllErrorsAsString();
        return errors;
    }

    public void dataClean() {
        bindingResultList = new ArrayList<>();
        errors = "";
    }
}

