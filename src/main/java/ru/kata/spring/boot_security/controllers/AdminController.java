package ru.kata.spring.boot_security.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.models.Person;
import ru.kata.spring.boot_security.services.AdminService;
import ru.kata.spring.boot_security.services.RoleService;
import ru.kata.spring.boot_security.until.MyDataValidator;
import ru.kata.spring.boot_security.until.PersonValidator;
import ru.kata.spring.boot_security.until.RoleValidator;

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
    private final RoleService roleService;
    private final PersonValidator personValidator;
    private final RoleValidator roleValidator;
    private final MyDataValidator myDataValidator;



    @Autowired
    public AdminController(AdminService adminService, RoleService roleService, PersonValidator personValidator, RoleValidator roleValidator, MyDataValidator myDataValidator) {
        this.adminService = adminService;
        this.roleService = roleService;
        this.personValidator = personValidator;
        this.roleValidator = roleValidator;
        this.myDataValidator = myDataValidator;
    }


    @GetMapping()
    public String showAllUsers(Model model, Principal principal) {
        Person person = adminService.findUserByUserName(principal.getName());
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
    public String createUser(@ModelAttribute @Valid Person user, BindingResult bindingResult
            , @RequestParam(value = "roles", required = false) @Valid List<String> roles, BindingResult roleBindingResult) {

        personValidator.validate(user, bindingResult);
        roleValidator.validate(roles, roleBindingResult);

        if (bindingResult.hasErrors() || roleBindingResult.hasErrors()) {
            return "redirect:/admin";
        }
        adminService.create(user, roles);
        return "redirect:/admin";
    }


    @PostMapping("/user/edit")
    public String update(@ModelAttribute("user") @Valid Person user, BindingResult bindingResult,
                         @RequestParam(value = "role", required = false) @Valid List<String> role, BindingResult roleBindingResult,
                         Model model) {

        myDataValidator.dataClean();
        personValidator.validate(user, bindingResult);
        roleValidator.validate(role, roleBindingResult);

        myDataValidator.validate(bindingResult);
        myDataValidator.validate(roleBindingResult);


        String allErrors = myDataValidator.getAllErrorsAsString();

        if (!allErrors.isEmpty()) {
            model.addAttribute("allErrors", allErrors);
            return "redirect:/admin";
        }

        adminService.updateUser(user, role);
        return "redirect:/admin";
    }


    @PostMapping("/user/delete")
    public String delete(@RequestParam Long id) {
        adminService.removeUser(id);
        return "redirect:/admin";
    }
}

