package com.example.demo.travel.repository;

import com.example.demo.travel.entity.TravelOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TravelOptionRepository extends JpaRepository<TravelOption, Long> {
    @Query("select to from TravelOption to where to.travel.country = :country and to.travel.city = :city")
    List<TravelOption> findOptionsByCountryAndCity(String country, String city);
}
