package com.example.ts.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ToyTest {

  @Test
  void shouldSetAndGetFields() {
    Toy toy = new Toy();

    toy.setId(1L);
    toy.setName("Car");
    toy.setBrand("Brand");
    toy.setPrice(100.0);
    toy.setQuantity(5);


    assertEquals(1L, toy.getId());
    assertEquals("Car", toy.getName());
    assertEquals("Brand", toy.getBrand());
    assertEquals(100.0, toy.getPrice());
    assertEquals(5, toy.getQuantity());
  }
}