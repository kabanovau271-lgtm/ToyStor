package com.example.ts.controller;

import com.example.ts.dto.BrandDto;
import com.example.ts.service.BrandService;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/brands")
public class BrandController {

  private final BrandService service;

  public BrandController(BrandService service) {
    this.service = service;
  }

  @GetMapping
  public List<BrandDto> getAllBrands() {
    return service.getAllBrands();
  }

  @GetMapping("/{id}")
  public BrandDto getBrandById(@PathVariable Long id) {
    return service.getBrandById(id);
  }

  @PostMapping
  public BrandDto createBrand(@RequestBody BrandDto dto) {
    return service.createBrand(dto);
  }

  @PutMapping("/{id}")
  public BrandDto updateBrand(@PathVariable Long id, @RequestBody BrandDto dto) {
    return service.updateBrand(id, dto);
  }

  @DeleteMapping("/{id}")
  public void deleteBrand(@PathVariable Long id) {
    service.deleteBrand(id);
  }
}