package ru.kata.spring.boot_security.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.kata.spring.boot_security.models.Person;
import ru.kata.spring.boot_security.security.PersonDetails;
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

    /**
     * Получает страницу со списком всех пользователей.
     *
     * @param model     Модель для передачи данных в представление.
     * @param principal Объект Principal для получения информации об аутентифицированном пользователе.
     * @return Строка с именем представления "admin/users".
     */
    @GetMapping("/users")
    public String getAllUsers(Model model, Principal principal) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        model.addAttribute("personDetails", personDetails);
        Person person = adminService.findUserByUserName(principal.getName());
        model.addAttribute("person", person);
        List<Person> personList = adminService.getAllUsers();
        model.addAttribute("personList", personList);
        return "admin/users";
    }

    /**
     * Удаляет пользователя по ID и перенаправляет на страницу со списком пользователей.
     *
     * @param id ID пользователя для удаления.
     * @return Строка с адресом перенаправления "/admin/users".
     */
    @GetMapping("admin/removeUser")
    public String removeUser(@RequestParam("id") Long id) {
        adminService.removeUser(id);
        return "redirect:/admin/users";
    }

    /**
     * Получает форму редактирования пользователя по ID.
     *
     * @param model Модель для передачи данных в представление.
     * @param id    ID пользователя для редактирования.
     * @return Строка с именем представления "admin/userUpdate".
     */
    @GetMapping("/admin/updateUser")
    public String getEditUserForm(Model model, @RequestParam("id") Long id) {
        model.addAttribute("person", adminService.findOneById(id));
        model.addAttribute("roles", roleService.getRoles());
        return "admin/userUpdate";
    }

    /**
     * Обрабатывает форму редактирования пользователя и перенаправляет на страницу со списком пользователей.
     *
     * @param person        Объект Person с данными пользователя.
     * @param roles         Список ролей пользователя.
     * @param personBindingResult Результат валидации данных пользователя.
     * @param rolesBindingResult Результат валидации данных роли.
     * @return Строка с адресом перенаправления "/admin/users".
     */
    @PostMapping("/updateUser")
    public String postEditUserForm(@ModelAttribute("person") @Valid Person person,
                                   BindingResult personBindingResult,
                                   @RequestParam(value = "roles", required = false) @Valid List<String> roles,
                                   BindingResult rolesBindingResult,
                                   RedirectAttributes redirectAttributes) {

        System.out.println();
        personValidator.validate(person, personBindingResult);
        if (personBindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorsPerson", personBindingResult.getAllErrors());
            return "/admin/userUpdate";
        }


        roleValidator.validate(roles, rolesBindingResult);
        if (rolesBindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorsRoles", rolesBindingResult.getAllErrors());
            return "/admin/userUpdate";
        }

        adminService.updateUser(person, roles);
        return "redirect:/admin/users";
    }

}
