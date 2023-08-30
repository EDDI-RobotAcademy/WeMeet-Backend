package com.example.demo.moim.repository;

import com.example.demo.moim.controller.form.dto.ParticipantDto;
import com.example.demo.moim.entity.Moim;
import com.example.demo.moim.entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    @Query("select p from Participant p join fetch p.user where p.moim = :moim")
    List<Participant> findAllByMoim(Moim moim);
}
