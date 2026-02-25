package com.example.ts.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ToyTest {

  @Test
  void gettersAndSetters_shouldWork() {
    Toy toy = new Toy();

    toy.setId(1L);
    toy.setName("Car");
    toy.setBrand("HotWheels");
    toy.setPrice(100.0);
    toy.setQuantity(5);

    assertEquals(1L, toy.getId());
    assertEquals("Car", toy.getName());
    assertEquals("HotWheels", toy.getBrand());
    assertEquals(100.0, toy.getPrice());
    assertEquals(5, toy.getQuantity());
  }
}