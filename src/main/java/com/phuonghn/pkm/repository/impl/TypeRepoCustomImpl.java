package com.phuonghn.pkm.repository.impl;

import com.phuonghn.pkm.common.utils.DataUtils;
import com.phuonghn.pkm.repository.TypeRepoCustom;
import com.phuonghn.pkm.service.dto.TypeDTO;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class TypeRepoCustomImpl implements TypeRepoCustom {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<TypeDTO> getTypeAtk(Long typeId) {
        List<TypeDTO> rsDTOs = new ArrayList<>();
        StringBuilder query = new StringBuilder();
        HashMap<Object, Object> map = new HashMap<>();
        query.append("select t.*, td.effect from type t join type_detail td on t.id = td.def_type_id where td.atk_type_id = :typeId");
        map.put("typeId", typeId);
        Query queryExecuted = entityManager.createNativeQuery(query.toString());
        map.forEach((k, v) -> {
            queryExecuted.setParameter(k.toString(), v);
        });
        List<Object[]> objects = queryExecuted.getResultList();
        if (!objects.isEmpty()) {
            rsDTOs.addAll(DataUtils.convertListObjectsToClass(DataUtils.changeParamTypeSqlToJava("id," +
                    "name," +
                    "bg_hex_color," +
                    "text_hex_color," +
                    "effect"), objects, TypeDTO.class));
        }
        return rsDTOs;
    }

    @Override
    public List<TypeDTO> getTypeDef(List<Long> typeIds) {
        List<TypeDTO> rsDTOs = new ArrayList<>();
        StringBuilder query = new StringBuilder();
        HashMap<Object, Object> map = new HashMap<>();
        query.append("select t.*, td.effect from type t join type_detail td on t.id = td.atk_type_id where td.def_type_id in :typeId");
        map.put("typeId", typeIds);
        Query queryExecuted = entityManager.createNativeQuery(query.toString());
        map.forEach((k, v) -> {
            queryExecuted.setParameter(k.toString(), v);
        });
        List<Object[]> objects = queryExecuted.getResultList();
        if (!objects.isEmpty()) {
            rsDTOs.addAll(DataUtils.convertListObjectsToClass(DataUtils.changeParamTypeSqlToJava("id," +
                    "name," +
                    "bg_hex_color," +
                    "text_hex_color," +
                    "effect"), objects, TypeDTO.class));
        }
        return rsDTOs;
    }
}
