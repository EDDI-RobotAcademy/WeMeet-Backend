package com.example.demo.moim.service;

import com.example.demo.moim.controller.form.MoimReqForm;
import com.example.demo.moim.controller.form.MoimInfoResForm;
import com.example.demo.moim.entity.Moim;
import com.example.demo.moim.entity.Participant;
import com.example.demo.moim.repository.MoimRepository;
import com.example.demo.moim.repository.ParticipantRepository;
import com.example.demo.security.costomUser.CustomUserDetails;
import com.example.demo.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MoimServiceImpl implements MoimService{
    final MoimRepository moimRepository;
    final ParticipantRepository participantRepository;
    @Override
    @Transactional
    public ResponseEntity<Map<String, Object>> createMoim(MoimReqForm reqForm) {
        log.info(String.valueOf(reqForm.getTitle()));
        User user = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        Moim moim = Moim.toMoim(reqForm);
        Participant participant = new Participant(user, moim);
        moim.getParticipants().add(participant);
        moimRepository.save(moim);

        return requestMoim(moim.getId());
    }

    private ResponseEntity<Map<String, Object>> requestMoim(Long id) {
        Optional<Moim> savedMoim = moimRepository.findById(id);
        if (savedMoim.isEmpty()) {
            return ResponseEntity.status(204)
                    .body(Map.of("msg", "no contents: Moim id: "+id));
        }
        else {
            Moim moim = savedMoim.get();
            MoimInfoResForm moimInfoResForm = moim.toInfoResForm();
            return ResponseEntity.ok()
                    .body(Map.of("Moim", moimInfoResForm));
        }
    }
}
