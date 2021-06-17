package ru.vlsu.vetclinic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.vlsu.vetclinic.persistence.Role;
import ru.vlsu.vetclinic.persistence.User;
import ru.vlsu.vetclinic.persistence.UserRepository;

import java.util.Collections;
import java.util.List;

@Service
public class UserService {

    private final UserRepository repository;

    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository repository, BCryptPasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public void create(UserRepr userRepr){
        User user = new User();
        user.setUsername(userRepr.getUsername());
        user.setPassword(passwordEncoder.encode(userRepr.getPassword()));
        //чтобы зарегистрированному пользователю автоматически давалась роль юзера
        user.setRoles(Collections.singleton(new Role(2L, "USER")));
        repository.save(user);
    }

    //Получение всех пользователей
    public List<User> allUsers(){
        return repository.findAll();
    }

    //Удаление пользователя
    public boolean deleteUser(Long userId) {
        if (repository.findById(userId).isPresent()) {
            repository.deleteById(userId);
            return true;
        }
        return false;
    }
}
