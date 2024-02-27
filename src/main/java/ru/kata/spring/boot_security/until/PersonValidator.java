package ru.kata.spring.boot_security.until;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import ru.kata.spring.boot_security.models.Person;
import ru.kata.spring.boot_security.services.AdminService;
import ru.kata.spring.boot_security.services.PeopleService;

import java.util.Optional;

@Component
public class PersonValidator implements Validator {

    private final AdminService adminService;

    public PersonValidator(PeopleService peopleService, AdminService adminService) {
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
        if (existingPerson.isPresent() && !existingPerson.get().equals(person)) {
            System.out.println();
            errors.rejectValue("email", "", "A user with that email already exists");
        }


        if (person.getAge() != null && person.getAge() < 0) {
            errors.rejectValue("age", "", "Age must be a non-negative number");
        }


        if (person.getPassword() != null && person.getPassword().length() <= 1) {
            errors.rejectValue("password", "", "Password must be at least 1 characters long");
        }
    }
}
