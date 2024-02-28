package ru.kata.spring.boot_security.until;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import ru.kata.spring.boot_security.models.Person;
import ru.kata.spring.boot_security.services.AdminService;
import ru.kata.spring.boot_security.services.PeopleService;

import java.util.Objects;
import java.util.Optional;

@Component
public class PersonValidator implements Validator {

    private final AdminService adminService;

    public PersonValidator(AdminService adminService) {
        this.adminService = adminService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;


        Optional<Person> existingPerson = adminService.findByEmail(person.getEmail());
        if (existingPerson.isPresent() && person.getId() != existingPerson.get().getId()) {
            String errMsg = String.format("Email %s is not unique", existingPerson.get().getEmail());
            errors.rejectValue("email", "duplicate.email", errMsg);
        }


        if (person.getAge() != null && person.getAge() < 0) {
            errors.rejectValue("age", "negative.number", "Age must be a non-negative number");
        }


        if (person.getPassword() != null && person.getPassword().length() <= 1) {
            errors.rejectValue("password", "short.Password", "Password must be at least 1 characters long");
        }

        if (person.getFirstName() == null || person.getFirstName().trim().isEmpty()) {
            errors.rejectValue("firstName", "NotEmpty", "FirstName should not be empty");
        }

        if (person.getLastName() == null || person.getLastName().trim().isEmpty()) {
            errors.rejectValue("lastName", "NotEmpty", "LastName should not be empty");
        }
    }
}
