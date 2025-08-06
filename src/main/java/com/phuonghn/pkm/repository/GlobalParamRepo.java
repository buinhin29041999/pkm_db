package com.phuonghn.pkm.repository;

import com.phuonghn.pkm.entity.GlobalParam;
import com.phuonghn.pkm.service.dto.GlobalParamDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GlobalParamRepo extends JpaRepository<GlobalParam, Long> {

    @Query("SELECT new com.phuonghn.pkm.service.dto.GlobalParamDTO(g.id, g.code, g.value, g.type, g.description) FROM GlobalParam g WHERE g.type = ?1 order by g.value")
    List<GlobalParamDTO> search(String type);
}
