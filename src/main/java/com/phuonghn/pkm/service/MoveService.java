package com.phuonghn.pkm.service;

import com.phuonghn.pkm.service.dto.MoveDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MoveService {

    List<MoveDTO> findAll();

    Page<MoveDTO> search(MoveDTO dto, Pageable pageable);

    MoveDTO detail(Long id);
}
