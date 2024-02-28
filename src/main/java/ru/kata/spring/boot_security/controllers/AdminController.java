package ru.kata.spring.boot_security.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.models.Person;
import ru.kata.spring.boot_security.services.AdminService;
import ru.kata.spring.boot_security.validators.MyDataValidator;
import ru.kata.spring.boot_security.validators.PersonValidator;
import ru.kata.spring.boot_security.validators.RoleValidator;
import ru.kata.spring.boot_security.services.UserDataValidationService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

/**
 * Контроллер, отвечающий за управление пользователями администратором.
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;
    private final MyDataValidator myDataValidator;
    private final UserDataValidationService userDataValidationService;


    @Autowired
    public AdminController(AdminService adminService, MyDataValidator myDataValidator, UserDataValidationService userDataValidationService) {
        this.adminService = adminService;
        this.myDataValidator = myDataValidator;
        this.userDataValidationService = userDataValidationService;
    }


    @GetMapping()
    public String showAllUsers(Model model, Principal principal) {
        Person person = adminService.findByEmail(principal.getName());
        model.addAttribute("currentUser", person);
        List<Person> listOfUsers = adminService.getAllUsers();
        model.addAttribute("listOfUsers", listOfUsers);
        model.addAttribute("person", new Person());
        model.addAttribute("allErrors", myDataValidator.getAllErrorsAsString());
        return "admin/users";
    }


    @GetMapping("/new")
    public String showNewUserForm(Model model) {
        model.addAttribute("person", new Person());
        return "/admin/new";
    }


    @PostMapping("")
    public String createUser(@ModelAttribute @Valid Person user,
                             @RequestParam(value = "roles", required = false)
                             @Valid List<String> roles,
                             Model model) {

        String allErrors = userDataValidationService.validateUserData(user, roles, model);

        // Если есть ошибки, вернуть перенаправление на страницу администратора
        if (!allErrors.isEmpty()) {
            return "redirect:/admin";
        }
        adminService.create(user, roles);
        return "redirect:/admin";
    }


    /**
     * Обработка запроса на обновление данных пользователя.
     *
     * @param person   Объект типа {@link Person}, представляющий пользователя.
     * @param role   Список ролей пользователя.
     * @param model  Модель Spring, предоставляющая данные для отображения в представлении.
     * @return Строка с именем представления или перенаправление на страницу администратора.
     */
    @PostMapping("/user/edit")
    public String update(@ModelAttribute("person") @Valid Person person,
                         @RequestParam(value = "role", required = false) @Valid List<String> role,
                         Model model) {

        System.out.println(person.getPassword());
        System.out.println(person.getEmail());
        String allErrors = userDataValidationService.validateUserData(person, role, model);
        System.out.println();

        // Если есть ошибки, вернуть перенаправление на страницу администратора
        if (!allErrors.isEmpty()) {
            return "redirect:/admin";
        }

        // Обновление данных пользователя и перенаправление на страницу администратора
        adminService.updateUser(person, role);
        return "redirect:/admin";
    }

    @PostMapping("/user/delete")
    public String delete(@RequestParam Long id) {
        adminService.removeUser(id);
        return "redirect:/admin";
    }
}
