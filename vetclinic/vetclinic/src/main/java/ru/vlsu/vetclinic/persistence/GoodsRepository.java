package ru.vlsu.vetclinic.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GoodsRepository extends JpaRepository<Goods, Integer> {

    @Query("select g from Goods g where g.weight>=(select p.weight from Pet p where p.weight=:weight)")
    List<Goods> getGoodsSuitedForPets(@Param ("weight") double weight);
}
