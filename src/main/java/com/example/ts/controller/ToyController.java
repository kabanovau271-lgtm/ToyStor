package com.example.ts.controller;

import com.example.ts.dto.ToyRequestDto;
import com.example.ts.dto.ToyResponseDto;
import com.example.ts.service.ToyService;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/toys")
public class ToyController {

  private final ToyService service;

  public ToyController(ToyService service) {
    this.service = service;
  }

  @GetMapping
  public List<ToyResponseDto> getAll() {
    return service.getAllToys();
  }

  @GetMapping("/{id}")
  public ToyResponseDto getById(@PathVariable Long id) {
    return service.getToyById(id);
  }

  @GetMapping("/search")
  public List<ToyResponseDto> getByName(@RequestParam String name) {
    return service.getToysByName(name);
  }

  @PostMapping
  public ToyResponseDto create(@RequestBody ToyRequestDto dto) {
    return service.createToy(dto);
  }

  @PutMapping("/{id}")
  public ToyResponseDto update(@PathVariable Long id, @RequestBody ToyRequestDto dto) {
    return service.updateToy(id, dto);
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable Long id) {
    service.deleteToy(id);
  }
}
// vdz