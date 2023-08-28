package com.example.demo.travel.repository;

import com.example.demo.travel.entity.TravelOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TravelOptionRepository extends JpaRepository<TravelOption, Long> {
}
