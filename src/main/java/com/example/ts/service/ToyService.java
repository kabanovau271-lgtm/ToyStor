package com.example.ts.service;

import com.example.ts.dto.ToyRequestDto;
import com.example.ts.dto.ToyResponseDto;
import java.util.List;
import org.springframework.data.domain.Page;

public interface ToyService {

  List<ToyResponseDto> getAllToys();

  ToyResponseDto getToyById(Long id);

  List<ToyResponseDto> getToysByName(String name);

  ToyResponseDto createToy(ToyRequestDto dto);

  ToyResponseDto updateToy(Long id, ToyRequestDto dto);

  void deleteToy(Long id);

  public Page<ToyResponseDto> getByCategoryAndPrice(
      String category,
      Double minPrice,
      int page,
      int size
  );

  Page<ToyResponseDto> getByCategoryAndPriceNative(
      String category,
      Double minPrice,
      int page,
      int size
  );

  List<ToyResponseDto> createToysBulk(List<ToyRequestDto> dto);
  public List<ToyResponseDto> createToysBulkNoTx(List<ToyRequestDto> dto);
  public List<ToyResponseDto> createToysBulkTx(List<ToyRequestDto> dto);
}