package com.phuonghn.pkm.repository;

import com.phuonghn.pkm.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepo extends JpaRepository<Item, Long> {
}
