package com.dosol.jpademo.repository;


import com.dosol.jpademo.domain.Board;
import com.dosol.jpademo.domain.Item;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Log4j2
public class ItemRepositoryTest {
    @Autowired
    private ItemRepository itemRepository;

    @Test
    public void testInsert() {
        Item item = Item.builder()
                .itemNn("iphone")
                .price(100)
                .stockNumber(1234)
                .itemDetail("Good")
                .build();
        itemRepository.save(item);

        Item item1 = new Item();
        item1.setItemNn("Gallexy");
        item1.setPrice(80);
        item1.setStockNumber(5678);
        item1.setItemDetail("Nice");
        //itemRepository.save(item1);
    }

    @Test
    public void testUpdate() {
        Item item = itemRepository.findById(1L).get();
        item.setPrice(2000);
        item.setItemNn("Gallexy");
        itemRepository.save(item);
    }

    @Test
    public void testDelete() {
//        Item item = itemRepository.findById(2L).get();
//        itemRepository.delete(item);
       itemRepository.deleteById(1L);
    }

    @Test
    public void testFindAll() {
        List<Item> items = itemRepository.findAll();
        for (Item item : items) {
            log.info(item.toString());
        }
    }

    @Test
    public void testFindById() {
        Item item = itemRepository.findById(2L).get();
        log.info(item);
    }
}
