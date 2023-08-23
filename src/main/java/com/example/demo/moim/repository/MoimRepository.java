package com.example.demo.moim.repository;

import com.example.demo.moim.entity.Moim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MoimRepository extends JpaRepository<Moim, Long> {
    @Query("select m from Moim m " +
            "left join fetch m.participants p " +
            "join fetch p.user " +
            "where m.id=:id")
    Optional<Moim> findById(Long id);
}
