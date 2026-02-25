package com.example.ts.service;

import com.example.ts.domain.Toy;
import com.example.ts.dto.ToyRequestDto;
import com.example.ts.dto.ToyResponseDto;
import com.example.ts.mapper.ToyMapper;
import com.example.ts.repository.ToyRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ToyServiceImpl implements ToyService {

  private final ToyRepository repository;
  private final ToyMapper mapper;

  public ToyServiceImpl(ToyRepository repository, ToyMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Override
  public List<ToyResponseDto> getAllToys() {
    return repository.findAll()
        .stream()
        .map(mapper::toDto)
        .toList();
  }

  @Override
  public ToyResponseDto getToyById(Long id) {
    Toy toy = repository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Toy not found"));
    return mapper.toDto(toy);
  }

  @Override
  public List<ToyResponseDto> getToysByName(String name) {
    return repository.findByName(name)
        .stream()
        .map(mapper::toDto)
        .toList();
  }


  @Override
  public ToyResponseDto createToy(ToyRequestDto dto) {
    Toy toy = mapper.toEntity(dto);
    return mapper.toDto(repository.save(toy));
  }

  @Override
  public ToyResponseDto updateToy(Long id, ToyRequestDto dto) {
    Toy toy = repository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Toy not found"));

    toy.setName(dto.getName());
    toy.setBrand(dto.getBrand());
    toy.setPrice(dto.getPrice());
    toy.setQuantity(dto.getQuantity());

    return mapper.toDto(repository.save(toy));
  }

  @Override
  public void deleteToy(Long id) {
    repository.deleteById(id);
  }
}