package ru.vlsu.vetclinic.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.vlsu.vetclinic.persistence.*;


import java.security.Principal;
import java.util.List;

@Controller
public class PetController {

    private PetRepository petRepo;
    private PetTypeRepository typeRepo;
    private UserRepository userRepo;
    private GoodsRepository goodsRepo;

    //autowired - при создании данного класса спринг будет искать есть ли класс, реализующий интерфейс petrepository и передаст этот класс в метод
    @Autowired
    public PetController(PetRepository petRepository, PetTypeRepository petTypeRepository, UserRepository userRepository, GoodsRepository goodsRepository){
        this.petRepo = petRepository;
        this.typeRepo= petTypeRepository;
        this.userRepo = userRepository;
        this.goodsRepo = goodsRepository;

    }

    //*ЖИВОТНЫЕ*
    //метод для возврата странички списка животных
    @GetMapping("/")
    public String petPage(Model model, Principal principal){

        List<Pet> pets;
        pets = petRepo.findByClientidUsername(principal.getName());
        model.addAttribute("pets", pets);
        return "pets";
    }

    //метод для возврата странички видов животных всех пользователей
    @GetMapping("/userspets")
    public String typePage(Model model){

        List<Pet> pets = petRepo.findAll();
        model.addAttribute("pets", pets);
        return "userspets";
    }

    //метод для вывода страницы подходящих переносок для животного
    @GetMapping("/thebestdeal/{id}")
    public String dealPage(Model model, @PathVariable ("id") Integer id){
        Pet pet = petRepo.getById(id);
        List<Goods> goods = goodsRepo.getGoodsSuitedForPets(pet.getWeight());
        model.addAttribute("pet", pet);
        model.addAttribute("goods", goods);
        return "bestdeal";
    }

    //метод для вывода странички создания животного
    @GetMapping("/newpet")
    @PreAuthorize("hasAuthority('USER')")
    public String createPet(Model model){
        List<PetType> types = typeRepo.findAll();
        Pet pet = new Pet();
        model.addAttribute("pet", pet);
        model.addAttribute("types", types);
        return "newpet";
    }
//метод для сохранения объекта модели Pet в бд
    @PostMapping("/save")
    public String savePet(Pet pet, Principal principal){
        User user = userRepo.findByUsername(principal.getName()).get();
        pet.setClientid(user);
        petRepo.save(pet);
        return "redirect:/";
    }

    //метод для вывода страницы редактирования
    @GetMapping("/pets/edit/{id}")
    public String showEditPet(@PathVariable("id") Integer id, Model model){
        Pet pet = petRepo.findById(id).get();
        List<PetType> types = typeRepo.findAll();
        model.addAttribute("pet", pet);
        model.addAttribute("types", types);
        return "newpet";
    }

    //удаление жиовтного по айди
    @GetMapping("/pets/delete/{id}")
    public String showDeletePet(@PathVariable("id") Integer id, Model model){
        petRepo.deleteById(id);
        return "redirect:/";
    }
}