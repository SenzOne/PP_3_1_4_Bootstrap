package ru.kata.spring.boot_security.services;

import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import ru.kata.spring.boot_security.models.Person;
import ru.kata.spring.boot_security.validators.MyDataValidator;
import ru.kata.spring.boot_security.validators.PersonValidator;
import ru.kata.spring.boot_security.validators.RoleValidator;

import java.util.List;

// Класс, выполняющий валидацию пользователя и ролей
@Component
public class UserDataValidationService {

    private final PersonValidator personValidator;
    private final RoleValidator roleValidator;
    private final MyDataValidator myDataValidator;

    public UserDataValidationService(
            PersonValidator personValidator,
            RoleValidator roleValidator,
            MyDataValidator myDataValidator) {
        this.personValidator = personValidator;
        this.roleValidator = roleValidator;
        this.myDataValidator = myDataValidator;
    }

    // Метод для валидации пользователя и ролей
    public String validateUserData(Person user, List<String> role, Model model) {
        // Создание объекта BindingResult для пользователя и списка ролей
        BindingResult userBindingResult = new BeanPropertyBindingResult(user, "user");
        BindingResult roleBindingResult = new BeanPropertyBindingResult(role, "role");

        // Валидация пользователя и списка ролей
        personValidator.validate(user, userBindingResult);
        roleValidator.validate(role, roleBindingResult);

        // Очистка предыдущих результатов валидации
        myDataValidator.dataClean();

        // Валидация результатов валидации пользователя и списка ролей
        myDataValidator.validate(userBindingResult);
        myDataValidator.validate(roleBindingResult);

        // Получение строкового представления ошибок
        String allErrors = myDataValidator.getAllErrorsAsString();

        // Если есть ошибки, добавить их в модель
        if (!allErrors.isEmpty()) {
            model.addAttribute("allErrors", allErrors);
        }

        return allErrors;
    }
}
