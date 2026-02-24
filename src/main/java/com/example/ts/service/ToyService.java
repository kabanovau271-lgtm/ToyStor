package com.example.ts.service;

import com.example.ts.dto.ToyRequestDto;
import com.example.ts.dto.ToyResponseDto;
import java.util.List;

public interface ToyService {

  List<ToyResponseDto> getAllToys();

  ToyResponseDto getToyById(Long id);

  List<ToyResponseDto> getToysByName(String name);

  ToyResponseDto createToy(ToyRequestDto dto);

  ToyResponseDto updateToy(Long id, ToyRequestDto dto);

  void deleteToy(Long id);
}