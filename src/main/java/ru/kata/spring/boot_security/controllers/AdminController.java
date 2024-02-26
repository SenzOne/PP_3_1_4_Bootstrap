package ru.kata.spring.boot_security.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.models.Person;
import ru.kata.spring.boot_security.services.AdminService;
import ru.kata.spring.boot_security.services.RoleService;
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

        /**
         * Конструктор контроллера.
         *
         * @param adminService    Сервис для работы с пользователями.
         * @param roleService     Сервис для работы с ролями.
         * @param personValidator Валидатор для объектов Person.
         * @param roleValidator
         */
        @Autowired
        public AdminController(AdminService adminService, RoleService roleService, PersonValidator personValidator, RoleValidator roleValidator) {
            this.adminService = adminService;
            this.roleService = roleService;
            this.personValidator = personValidator;
            this.roleValidator = roleValidator;
        }


        @GetMapping()
        public String showAllUsers(Model model, Principal principal) {
            Person person = adminService.findUserByUserName(principal.getName());
            model.addAttribute("currentUser", person);
            List<Person> listOfUsers = adminService.getAllUsers();
            model.addAttribute("listOfUsers", listOfUsers);
            model.addAttribute("person", new Person());
            return "admin/users";
        }


        @GetMapping("/new")
        public String newUser(Model model) {
            model.addAttribute("person", new Person());
            return "/admin/new";
        }


        //точно работает
        @PostMapping("")
        public String createUser(@ModelAttribute Person user, BindingResult bindingResult
                , @RequestParam(value = "roles", required = false) List<String> roles) {
            if (bindingResult.hasErrors()) {
                return "/admin/new";
            }
            adminService.create(user, roles);
            return "redirect:/admin";
        }


        // точно работает
        @PostMapping("/user/edit")
        public String update(@RequestParam Long id, @ModelAttribute("user") @Valid Person user, BindingResult bindingResult,
                             @RequestParam List<String> role) {

            if (bindingResult.hasErrors()) {
                return "redirect:/admin/edit?" + id;
            }

            adminService.updateUser(user, role); // еще при создании юзера исправить баг с ролями
            return "redirect:/admin";
        }


        // работает
        @PostMapping("/user/delete")
        public String delete(@RequestParam Long id) {
            adminService.removeUser(id);
            return "redirect:/admin";
        }
    }

