package com.phuonghn.pkm.repository;

import com.phuonghn.pkm.entity.Move;
import com.phuonghn.pkm.service.dto.MoveDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MoveRepo extends JpaRepository<Move, Long> {

    @Query("SELECT new com.phuonghn.pkm.service.dto.MoveDTO(" +
            " m.id, m.name, m.accuracy, m.pp, m.power," +
            " m.priority, m.type, t.name, m.generation, gpg.value," +
            " m.shortDescription, m.damageClass, gpdc.value) " +
            " FROM Move m join Type t on t.code = m.type" +
            " left join GlobalParam gpg on gpg.code = m.generation" +
            " left join GlobalParam gpdc on gpdc.code = m.damageClass" +
            " WHERE (:name IS NULL OR m.name LIKE %:name%) " +
            " AND (:type IS NULL OR m.type = :type) " +
            " AND (:damageClass IS NULL OR m.damageClass = :damageClass) " +
            " AND (:generation IS NULL OR m.generation = :generation)" +
            " order by m.generation, m.name"
    )
    Page<MoveDTO> search(String name, String type, String damageClass, String generation, Pageable pageable);
}
