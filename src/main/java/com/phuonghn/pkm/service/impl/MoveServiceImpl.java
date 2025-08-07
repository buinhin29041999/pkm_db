package com.phuonghn.pkm.service.impl;

import com.phuonghn.pkm.entity.Move;
import com.phuonghn.pkm.repository.MoveRepo;
import com.phuonghn.pkm.service.MoveService;
import com.phuonghn.pkm.service.dto.MoveDTO;
import com.phuonghn.pkm.service.mapper.MoveMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MoveServiceImpl implements MoveService {

    @Autowired
    private MoveMapper moveMapper;

    @Autowired
    private MoveRepo moveRepo;

    @Override
    public List<MoveDTO> findAll() {
        return Collections.emptyList();
    }

    @Override
    public Page<MoveDTO> search(MoveDTO dto, Pageable pageable) {
        return moveRepo.search(dto.getName(), dto.getType(), dto.getDamageClass(), dto.getGeneration(), pageable);
    }

    @Override
    public MoveDTO detail(Long id) {
        Move move = moveRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Move not found with id: " + id));
        return moveMapper.toDto(move);
    }
}
