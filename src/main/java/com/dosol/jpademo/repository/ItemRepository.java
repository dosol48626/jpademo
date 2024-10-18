package com.dosol.jpademo.repository;

import com.dosol.jpademo.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
