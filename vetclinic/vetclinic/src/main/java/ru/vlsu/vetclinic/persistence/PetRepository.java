package ru.vlsu.vetclinic.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

//Репозиторий - класс, ответственный за сохранение сущностей в бд и "доставание" их оттудава
@Repository
public interface PetRepository extends JpaRepository<Pet, Integer> {
    List<Pet> findByClientidUsername(String username);
}
