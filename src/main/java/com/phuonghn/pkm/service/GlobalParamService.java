package com.phuonghn.pkm.service;

import com.phuonghn.pkm.service.dto.GlobalParamDTO;

import java.util.List;

public interface GlobalParamService {

    List<GlobalParamDTO> search(GlobalParamDTO dto);
}
