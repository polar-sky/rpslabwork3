package ru.vlsu.vetclinic.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.vlsu.vetclinic.service.UserRepr;
import ru.vlsu.vetclinic.service.UserService;

import javax.validation.Valid;

@Controller
public class AuthorizationController {

    private final UserService userService;

    //аннотация @autowired - при создании данного класса спринг будет искать есть ли класс, реализующий интерфейс petrepository и передаст этот класс в метод
    @Autowired
    public AuthorizationController(UserService userService) {
        this.userService = userService;
    }

    //метод для вывода страницы входа
    @GetMapping("/login")
    public String login(){
        return "login";
    }

    //метод для вывода страницы регистрации
    @GetMapping("/register")
    public String register(Model model){
        model.addAttribute("user", new UserRepr());
        return "register";
    }

    //регистрируем пользователя
    @PostMapping("/register")
    public String registerNewUser(@Valid UserRepr userRepr, BindingResult bindingResult){
       //возвращаем пользователя на страницу регистрации если есть ошибки
        if (bindingResult.hasErrors()){
            return "register";
        }
        if (!userRepr.getPassword().equals(userRepr.getRepeatPassword())){
            bindingResult.rejectValue("password", "", "Введенные пароли не идентичны");
            return "register";
        }

        //создаем пользователя и возвращаем его на страницу логин
        userService.create(userRepr);
        return "redirect:/login";
    }
}
