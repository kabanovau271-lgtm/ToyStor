package com.example.ts.dto;

import com.example.ts.domain.Brand;

public class ToyDto {

  private Long id;
  private String name;
  private Double price;
  private Brand brand;

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }

  public String getName() { return name; }
  public void setName(String name) { this.name = name; }

  public Double getPrice() { return price; }
  public void setPrice(Double price) { this.price = price; }

  public Brand getBrand() { return brand; }
  public void setBrand(Brand brand) { this.brand = brand; }
}