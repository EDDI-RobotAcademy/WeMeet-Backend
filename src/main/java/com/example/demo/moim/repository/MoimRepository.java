package com.example.demo.moim.repository;

import com.example.demo.moim.entity.Moim;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MoimRepository extends JpaRepository<Moim, Long>, JpaSpecificationExecutor<Moim> {
    @Query("select m from Moim m " +
            "join fetch m.moimPaymentInfo " +
            "join fetch m.participantsInfo " +
            "join fetch m.contents " +
            "join fetch m.destination " +
            "join fetch m.state " +
            "where m.id=:id")
    Optional<Moim> findById(Long id);

    @Query(value = "select m from Moim m", countQuery = "select count(m) from Moim m")
    List<Moim> findRecentPageableMoim(Pageable pageable);

    @Query("select m from Moim m join fetch m.moimPaymentInfo where m.id=:moimId")
    Optional<Moim> findByIdJoinFetchPaymentInfo(Long moimId);

}

