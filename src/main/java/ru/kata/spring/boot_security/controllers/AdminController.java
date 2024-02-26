//package ru.kata.spring.boot_security.controllers;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.ui.ModelMap;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//import ru.kata.spring.boot_security.models.Person;
//import ru.kata.spring.boot_security.models.Role;
//import ru.kata.spring.boot_security.security.PersonDetails;
//import ru.kata.spring.boot_security.services.AdminService;
//import ru.kata.spring.boot_security.services.RoleService;
//import ru.kata.spring.boot_security.until.PersonValidator;
//import ru.kata.spring.boot_security.until.RoleValidator;
//
//import javax.validation.Valid;
//import java.security.Principal;
//import java.util.Collection;
//import java.util.List;
//
///**
// * Контроллер, отвечающий за управление пользователями администратором.
// */
//@Controller
//@RequestMapping("/admin")
//public class AdminController {
//
//    private final AdminService adminService;
//    private final RoleService roleService;
//    private final PersonValidator personValidator;
//    private final RoleValidator roleValidator;
//
//    /**
//     * Конструктор контроллера.
//     *
//     * @param adminService    Сервис для работы с пользователями.
//     * @param roleService     Сервис для работы с ролями.
//     * @param personValidator Валидатор для объектов Person.
//     * @param roleValidator
//     */
//    @Autowired
//    public AdminController(AdminService adminService, RoleService roleService, PersonValidator personValidator, RoleValidator roleValidator) {
//        this.adminService = adminService;
//        this.roleService = roleService;
//        this.personValidator = personValidator;
//        this.roleValidator = roleValidator;
//    }


//
//    @GetMapping()
//    public String showAllUsers(ModelMap model, Principal principal) {
//        Person person = adminService.findUserByUserName(principal.getName());
//        model.addAttribute("currentUser", person);
//        List<Person> listOfUsers = adminService.getAllUsers();
//        model.addAttribute("listOfUsers", listOfUsers);
//        model.addAttribute("person", new Person());
//        return "admin/users";
//    }
//
//
//
//    @GetMapping("/new") public String newUser(Model model) {
//        model.addAttribute("person", new Person());
////        model.addAttribute("person", new Person());
//        return "/admin/new";
//    }
//
//
//    @PostMapping("")
//    public String createUser(@ModelAttribute Person user, BindingResult bindingResult
//            , @RequestParam(value = "roles", required = false) List<String> roles) {
//        if (bindingResult.hasErrors()) {
//            return "/admin/new";
//        }
//        adminService.create(user, roles);
//        return "redirect:/admin";
//    }
//
//
//    @GetMapping("/{id}/update")
//    public String getEditUserForm(ModelMap model, @PathVariable("id") Long id) {
//        model.addAttribute("user", adminService.findOneById(id));
//        return "admin/userUpdate";
//    }
//
//
//    @PatchMapping("/{id}")
//    public String saveUpdateUser(@ModelAttribute("person") Person person, @PathVariable("id") Long id) {
//        // adminService.updateUser(person, );
//        return "redirect:/admin/";
//    }
//
//    /**
//     * Удаляет пользователя по ID и перенаправляет на страницу со списком пользователей.
//     *
//     * @param id ID пользователя для удаления.
//     * @return Строка с адресом перенаправления "/admin/users".
//     */
//    @DeleteMapping("/{id}")
//    public String removeUser(@PathVariable("id") Long id) {
//        adminService.removeUser(id);
//        return "redirect:/admin/";
//    }
//
//    /**
//     * Получает форму редактирования пользователя по ID.
//     *
//     * @param model Модель для передачи данных в представление.
//     * @param id    ID пользователя для редактирования.
//     * @return Строка с именем представления "admin/userUpdate".
//     */
//    @GetMapping("/updateUser")
//    public String getEditUserForm(Model model, @RequestParam("id") Long id) {
//        model.addAttribute("person", adminService.findOneById(id));
//        model.addAttribute("roles", roleService.getRoles());
//        return "admin/userUpdate";
//    }
//
//
//    @PostMapping("/user/edit")
//    public String update(@RequestParam Long id, @ModelAttribute Person user,
//                         @RequestParam(value = "roles", required = false) List<String> roles) {
//        adminService.updateUser(user, roles);
//        return "redirect:/admin";
//    }
//
//    @PostMapping("/user/delete")
//    public String delete(@RequestParam Long id) {
//        adminService.removeUser(id);
//        return "redirect:/admin";
//    }
//}


package ru.kata.spring.boot_security.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.kata.spring.boot_security.models.Person;
import ru.kata.spring.boot_security.models.Role;
import ru.kata.spring.boot_security.security.PersonDetails;
import ru.kata.spring.boot_security.services.AdminService;
import ru.kata.spring.boot_security.services.RoleService;
import ru.kata.spring.boot_security.until.PersonValidator;
import ru.kata.spring.boot_security.until.RoleValidator;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
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


        @PostMapping("")
        public String createUser(@ModelAttribute Person user, BindingResult bindingResult
                , @RequestParam(value = "roles", required = false) List<String> roles) {
            if (bindingResult.hasErrors()) {
                return "/admin/new";
            }
            adminService.create(user, roles);
            return "redirect:/admin";
        }


        @GetMapping("/{id}/update")
        public String getEditUserForm(ModelMap model, @PathVariable("id") Long id) {
            model.addAttribute("user", adminService.findOneById(id));
            return "admin/userUpdate";
        }


        @PatchMapping("/{id}")
        public String saveUpdateUser(@ModelAttribute("person") Person person, @PathVariable("id") Long id) {
            // adminService.updateUser(person, );
            return "redirect:/admin/";
        }

        /**
         * Удаляет пользователя по ID и перенаправляет на страницу со списком пользователей.
         *
         * @param id ID пользователя для удаления.
         * @return Строка с адресом перенаправления "/admin/users".
         */
        @DeleteMapping("/{id}")
        public String removeUser(@PathVariable("id") Long id) {
            adminService.removeUser(id);
            return "redirect:/admin/";
        }

        /**
         * Получает форму редактирования пользователя по ID.
         *
         * @param model Модель для передачи данных в представление.
         * @param id    ID пользователя для редактирования.
         * @return Строка с именем представления "admin/userUpdate".
         */
        @GetMapping("/updateUser")
        public String getEditUserForm(Model model, @RequestParam("id") Long id) {
            model.addAttribute("person", adminService.findOneById(id));
            model.addAttribute("roles", roleService.getRoles());
            return "admin/userUpdate";
        }


        @PostMapping("/user/edit")
        public String update(@RequestParam Long id, @ModelAttribute Person user,
                             @RequestParam List<String> role) {
//            List<String> list = new ArrayList<>(); // доработать, с формы получается либо 1 либо 2
            // list.add(role); // прописать логику того что если получается 1, то юзеру присваивается 2 роли - юзер и админ. если 2 - только юзер
            adminService.updateUser(user, role); // еще при создании юзера исправить баг с ролями
            return "redirect:/admin";
        }

        @PostMapping("/user/delete")
        public String delete(@RequestParam Long id) {
            adminService.removeUser(id);
            return "redirect:/admin";
        }
    }

//package ru.kata.spring.boot_security.controllers;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//import ru.kata.spring.boot_security.models.Person;
//import ru.kata.spring.boot_security.security.PersonDetails;
//import ru.kata.spring.boot_security.services.AdminService;
//import ru.kata.spring.boot_security.services.RoleService;
//import ru.kata.spring.boot_security.until.PersonValidator;
//import ru.kata.spring.boot_security.until.RoleValidator;
//
//import javax.validation.Valid;
//import java.security.Principal;
//import java.util.List;
//
///**
// * Контроллер, отвечающий за управление пользователями администратором.
// */
//@Controller
//@RequestMapping("/admin")
//public class AdminController {
//
//    private final AdminService adminService;
//    private final RoleService roleService;
//    private final PersonValidator personValidator;
//    private final RoleValidator roleValidator;
//
//    /**
//     * Конструктор контроллера.
//     *
//     * @param adminService    Сервис для работы с пользователями.
//     * @param roleService     Сервис для работы с ролями.
//     * @param personValidator Валидатор для объектов Person.
//     * @param roleValidator
//     */
//    @Autowired
//    public AdminController(AdminService adminService, RoleService roleService, PersonValidator personValidator, RoleValidator roleValidator) {
//        this.adminService = adminService;
//        this.roleService = roleService;
//        this.personValidator = personValidator;
//        this.roleValidator = roleValidator;
//    }
//
//    /**
//     * Получает страницу со списком всех пользователей.
//     *
//     * @param model     Модель для передачи данных в представление.
//     * @param principal Объект Principal для получения информации об аутентифицированном пользователе.
//     * @return Строка с именем представления "admin/users".
//     */
//    @GetMapping("/")
//    public String getAllUsers(Model model, Principal principal) {
////        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
////        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
////        model.addAttribute("personDetails", personDetails);
////        Person person = adminService.findUserByUserName(principal.getName());
////        model.addAttribute("person", person);
////        List<Person> personList = adminService.getAllUsers();
////        model.addAttribute("personList", personList);
//        Person admin =  adminService.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
//        model.addAttribute("admin", admin);
//        model.addAttribute("user", new Person());
//        model.addAttribute("users", adminService.getAllUsers());
//        return "admin/users";
//    }
//
//    /**
//     * Удаляет пользователя по ID и перенаправляет на страницу со списком пользователей.
//     *
//     * @param id ID пользователя для удаления.
//     * @return Строка с адресом перенаправления "/admin/users".
//     */
//    @GetMapping("admin/removeUser")
//    public String removeUser(@RequestParam("id") Long id) {
//        adminService.removeUser(id);
//        return "redirect:/admin/users";
//    }
//
//    /**
//     * Получает форму редактирования пользователя по ID.
//     *
//     * @param model Модель для передачи данных в представление.
//     * @param id    ID пользователя для редактирования.
//     * @return Строка с именем представления "admin/userUpdate".
//     */
//    @GetMapping("/admin/updateUser")
//    public String getEditUserForm(Model model, @RequestParam("id") Long id) {
//        model.addAttribute("person", adminService.findOneById(id));
//        model.addAttribute("roles", roleService.getRoles());
//        return "admin/userUpdate";
//    }
//
//    /**
//     * Обрабатывает форму редактирования пользователя и перенаправляет на страницу со списком пользователей.
//     *
//     * @param person        Объект Person с данными пользователя.
//     * @param roles         Список ролей пользователя.
//     * @param personBindingResult Результат валидации данных пользователя.
//     * @param rolesBindingResult Результат валидации данных роли.
//     * @return Строка с адресом перенаправления "/admin/users".
//     */
//    @PostMapping("/updateUser")
//    public String postEditUserForm(@ModelAttribute("person") @Valid Person person,
//                                   BindingResult personBindingResult,
//                                   @RequestParam(value = "roles", required = false) @Valid List<String> roles,
//                                   BindingResult rolesBindingResult,
//                                   RedirectAttributes redirectAttributes) {
//
//        System.out.println();
//        personValidator.validate(person, personBindingResult);
//        if (personBindingResult.hasErrors()) {
//            redirectAttributes.addFlashAttribute("errorsPerson", personBindingResult.getAllErrors());
//            return "/admin/userUpdate";
//        }
//
//
//        roleValidator.validate(roles, rolesBindingResult);
//        if (rolesBindingResult.hasErrors()) {
//            redirectAttributes.addFlashAttribute("errorsRoles", rolesBindingResult.getAllErrors());
//            return "/admin/userUpdate";
//        }
//
//        adminService.updateUser(person, roles);
//        return "redirect:/admin/users";
//    }
//
//}







//package ru.kata.spring.boot_security.controllers;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//import ru.kata.spring.boot_security.models.Person;
//import ru.kata.spring.boot_security.security.PersonDetails;
//import ru.kata.spring.boot_security.services.AdminService;
//import ru.kata.spring.boot_security.services.RoleService;
//import ru.kata.spring.boot_security.until.PersonValidator;
//import ru.kata.spring.boot_security.until.RoleValidator;
//
//import javax.validation.Valid;
//import java.security.Principal;
//import java.util.List;
//
///**
// * Контроллер, отвечающий за управление пользователями администратором.
// */
//@Controller
//@RequestMapping("/admin")
//public class AdminController {
//
//    private final AdminService adminService;
//    private final RoleService roleService;
//    private final PersonValidator personValidator;
//    private final RoleValidator roleValidator;
//
//    /**
//     * Конструктор контроллера.
//     *
//     * @param adminService    Сервис для работы с пользователями.
//     * @param roleService     Сервис для работы с ролями.
//     * @param personValidator Валидатор для объектов Person.
//     * @param roleValidator
//     */
//    @Autowired
//    public AdminController(AdminService adminService, RoleService roleService, PersonValidator personValidator, RoleValidator roleValidator) {
//        this.adminService = adminService;
//        this.roleService = roleService;
//        this.personValidator = personValidator;
//        this.roleValidator = roleValidator;
//    }
//
//    /**
//     * Получает страницу со списком всех пользователей.
//     *
//     * @param model     Модель для передачи данных в представление.
//     * @param principal Объект Principal для получения информации об аутентифицированном пользователе.
//     * @return Строка с именем представления "admin/users".
//     */
//    @GetMapping("/")
//    public String getAllUsers(Model model, Principal principal) {
////        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
////        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
////        model.addAttribute("personDetails", personDetails);
////        Person person = adminService.findUserByUserName(principal.getName());
////        model.addAttribute("person", person);
////        List<Person> personList = adminService.getAllUsers();
////        model.addAttribute("personList", personList);
//        Person admin =  adminService.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
//        model.addAttribute("admin", admin);
//        model.addAttribute("user", new Person());
//        model.addAttribute("users", adminService.getAllUsers());
//        return "admin/users";
//    }
//
//    /**
//     * Удаляет пользователя по ID и перенаправляет на страницу со списком пользователей.
//     *
//     * @param id ID пользователя для удаления.
//     * @return Строка с адресом перенаправления "/admin/users".
//     */
//    @GetMapping("admin/removeUser")
//    public String removeUser(@RequestParam("id") Long id) {
//        adminService.removeUser(id);
//        return "redirect:/admin/users";
//    }
//
//    /**
//     * Получает форму редактирования пользователя по ID.
//     *
//     * @param model Модель для передачи данных в представление.
//     * @param id    ID пользователя для редактирования.
//     * @return Строка с именем представления "admin/userUpdate".
//     */
//    @GetMapping("/admin/updateUser")
//    public String getEditUserForm(Model model, @RequestParam("id") Long id) {
//        model.addAttribute("person", adminService.findOneById(id));
//        model.addAttribute("roles", roleService.getRoles());
//        return "admin/userUpdate";
//    }
//
//    /**
//     * Обрабатывает форму редактирования пользователя и перенаправляет на страницу со списком пользователей.
//     *
//     * @param person        Объект Person с данными пользователя.
//     * @param roles         Список ролей пользователя.
//     * @param personBindingResult Результат валидации данных пользователя.
//     * @param rolesBindingResult Результат валидации данных роли.
//     * @return Строка с адресом перенаправления "/admin/users".
//     */
//    @PostMapping("/updateUser")
//    public String postEditUserForm(@ModelAttribute("person") @Valid Person person,
//                                   BindingResult personBindingResult,
//                                   @RequestParam(value = "roles", required = false) @Valid List<String> roles,
//                                   BindingResult rolesBindingResult,
//                                   RedirectAttributes redirectAttributes) {
//
//        System.out.println();
//        personValidator.validate(person, personBindingResult);
//        if (personBindingResult.hasErrors()) {
//            redirectAttributes.addFlashAttribute("errorsPerson", personBindingResult.getAllErrors());
//            return "/admin/userUpdate";
//        }
//
//
//        roleValidator.validate(roles, rolesBindingResult);
//        if (rolesBindingResult.hasErrors()) {
//            redirectAttributes.addFlashAttribute("errorsRoles", rolesBindingResult.getAllErrors());
//            return "/admin/userUpdate";
//        }
//
//        adminService.updateUser(person, roles);
//        return "redirect:/admin/users";
//    }
//
//}

