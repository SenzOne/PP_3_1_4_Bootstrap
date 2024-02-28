package ru.kata.spring.boot_security.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.models.Person;
import ru.kata.spring.boot_security.models.Role;
import ru.kata.spring.boot_security.repositories.PeopleRepository;
import ru.kata.spring.boot_security.repositories.RoleRepository;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Реализация сервиса для администраторских операций с пользователями.
 */
@Service
@Transactional
public class AdminServiceImpl implements AdminService {

    private final PeopleRepository peopleRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Конструктор сервиса.
     *
     * @param peopleRepository Репозиторий для работы с пользователями.
     * @param roleRepository   Репозиторий для работы с ролями.
     * @param passwordEncoder  кодировка пароля.
     */
    @Autowired
    public AdminServiceImpl(PeopleRepository peopleRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.peopleRepository = peopleRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Получает список всех пользователей.
     *
     * @return Список объектов Person.
     */
    @Override
    public List<Person> getAllUsers() {
        return peopleRepository.findAllWithRoles();
    }

    /**
     * Находит пользователя по имени.
     *
     * @param name Имя пользователя.
     * @return Объект Person, представляющий пользователя.
     * @throws UsernameNotFoundException если пользователь не найден.
     */
    @Override
    public Person findUserByFirstName(String name) {
        Optional<Person> user = peopleRepository.findByFirstNameWithRoles(name);
        if (user.isEmpty())
            throw new UsernameNotFoundException("User " + name + " not found");
        return user.get();
    }

    public Optional<Person> doesPersonExist(String email) {
        return peopleRepository.findByEmailWithRoles(email);
    }


    /**
     * Удаляет пользователя по ID.
     *
     * @param id ID пользователя для удаления.
     */
    @Override
    public void removeUser(Long id) {
        peopleRepository.delete(peopleRepository.getById(id));
    }

    /**
     * Находит пользователя по ID.
     *
     * @param id ID пользователя.
     * @return Объект Person, представляющий пользователя.
     * @throws UsernameNotFoundException если пользователь не найден.
     */
    @Override
    public Person findOneById(Long id) {
        Optional<Person> user = peopleRepository.findByPersonIdWithRoles(id);
        if (user.isEmpty())
            throw new UsernameNotFoundException("User not found");
        return user.get();
    }

    @Override
    public Person findByEmail(String email) {
        Optional<Person> user = peopleRepository.findByEmailWithRoles(email);
        if (user.isEmpty())
            throw new UsernameNotFoundException("User not found");
        return user.get();
    }

    public void create(Person person, List<String> roles) {
        Set<Role> roleSet = roles.stream()
                .map(Long::valueOf)
                .map(roleRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
        person.setRoles(roleSet);
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        peopleRepository.save(person);
    }

    /**
     * Обновляет информацию о пользователе и его ролях.
     *
     * @param person Обновленный объект Person.
     * @param roles  Список строковых идентификаторов ролей.
     */
    @Override
    public void updateUser(Person person, List<String> roles) {
        Person beforeUpdate = peopleRepository.getById(person.getId());

        if (person.getPassword().isEmpty()){
            person.setPassword(passwordEncoder.encode(beforeUpdate.getPassword()));
        }
        Set<Role> roleSet = roles.stream()
                .map(Long::valueOf)
                .map(roleRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
        person.setRoles(roleSet);
        peopleRepository.save(person);
    }
}
