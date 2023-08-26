package com.example.demo.moim.repository;

import com.example.demo.moim.entity.Moim;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MoimRepository extends JpaRepository<Moim, Long> {
    @Query("select m from Moim m " +
            "left join fetch m.participants p " +
            "join fetch p.user " +
            "where m.id=:id")
    Optional<Moim> findById(Long id);

    @Query(value = "select m from Moim m", countQuery = "select count(m) from Moim m")
    List<Moim> findRecentPageableMoim(Pageable pageable);
}
