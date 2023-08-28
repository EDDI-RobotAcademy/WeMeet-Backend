package com.example.demo.travel.repository;

import com.example.demo.travel.entity.Travel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TravelRepository extends JpaRepository<Travel, Long> {
    @Query("select distinct t.country from Travel t")
    List<String> findCountries();
}
