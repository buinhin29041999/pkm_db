package com.phuonghn.pkm.service.impl;

import com.phuonghn.pkm.repository.GlobalParamRepo;
import com.phuonghn.pkm.service.GlobalParamService;
import com.phuonghn.pkm.service.dto.GlobalParamDTO;
import com.phuonghn.pkm.service.mapper.GlobalParamMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GlobalParamServiceImpl implements GlobalParamService {

    @Autowired
    private GlobalParamMapper globalParamMapper;

    @Autowired
    private GlobalParamRepo globalParamRepo;

    @Override
    public List<GlobalParamDTO> search(GlobalParamDTO dto) {
        return globalParamRepo.search(dto.getType());
    }
}
