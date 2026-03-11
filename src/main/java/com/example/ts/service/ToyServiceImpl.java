package com.example.ts.service;

import com.example.ts.domain.Brand;
import com.example.ts.domain.Toy;
import com.example.ts.dto.ToyRequestDto;
import com.example.ts.dto.ToyResponseDto;
import com.example.ts.mapper.ToyMapper;
import com.example.ts.repository.BrandRepository;
import com.example.ts.repository.ToyRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ToyServiceImpl implements ToyService {

  private final ToyRepository repository;
  private final ToyMapper mapper;
  private final BrandRepository brandRepository;

  public ToyServiceImpl(ToyRepository repository,
                        ToyMapper mapper,
                        BrandRepository brandRepository) {
    this.repository = repository;
    this.mapper = mapper;
    this.brandRepository = brandRepository;
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
        .orElseThrow(this::toyNotFound);
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

    Brand brand = brandRepository.findById(dto.getBrandId())
        .orElseThrow(() -> new IllegalArgumentException("Brand not found"));

    Toy toy = new Toy();
    toy.setName(dto.getName());
    toy.setPrice(dto.getPrice());
    toy.setQuantity(dto.getQuantity());
    toy.setBrand(brand);

    return mapper.toDto(repository.save(toy));
  }

  @Override
  public ToyResponseDto updateToy(Long id, ToyRequestDto dto) {

    Toy toy = repository.findById(id)
        .orElseThrow(this::toyNotFound);

    Brand brand = brandRepository.findById(dto.getBrandId())
        .orElseThrow(() -> new IllegalArgumentException("Brand not found"));

    toy.setName(dto.getName());
    toy.setPrice(dto.getPrice());
    toy.setQuantity(dto.getQuantity());
    toy.setBrand(brand);

    return mapper.toDto(repository.save(toy));
  }

  @Override
  public void deleteToy(Long id) {
    repository.deleteById(id);
  }

  private IllegalArgumentException toyNotFound() {
    return new IllegalArgumentException("Toy not found");
  }
}