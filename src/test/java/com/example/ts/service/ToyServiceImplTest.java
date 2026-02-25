package com.example.ts.service;

import com.example.ts.domain.Toy;
import com.example.ts.dto.ToyRequestDto;
import com.example.ts.dto.ToyResponseDto;
import com.example.ts.mapper.ToyMapper;
import com.example.ts.repository.ToyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ToyServiceImplTest {

    private ToyRepository repository;
    private ToyMapper mapper;
    private ToyServiceImpl service;

    @BeforeEach
    void setUp() {
        repository = mock(ToyRepository.class);
        mapper = mock(ToyMapper.class);
        service = new ToyServiceImpl(repository, mapper);
    }

    @Test
    void getAllToys_shouldReturnMappedList() {
        Toy toy = new Toy();
        ToyResponseDto dto = new ToyResponseDto();

        when(repository.findAll()).thenReturn(List.of(toy));
        when(mapper.toDto(toy)).thenReturn(dto);

        List<ToyResponseDto> result = service.getAllToys();

        assertEquals(1, result.size());
        verify(repository).findAll();
        verify(mapper).toDto(toy);
    }

    @Test
    void getToyById_shouldReturnToy() {
        Toy toy = new Toy();
        ToyResponseDto dto = new ToyResponseDto();

        when(repository.findById(1L)).thenReturn(Optional.of(toy));
        when(mapper.toDto(toy)).thenReturn(dto);

        ToyResponseDto result = service.getToyById(1L);

        assertEquals(dto, result);
        verify(repository).findById(1L);
        verify(mapper).toDto(toy);
    }

    @Test
    void getToyById_shouldThrowException_whenNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
            () -> service.getToyById(1L));
    }

    @Test
    void getToysByName_shouldReturnMappedList() {
        Toy toy = new Toy();
        ToyResponseDto dto = new ToyResponseDto();

        when(repository.findByName("Car")).thenReturn(List.of(toy));
        when(mapper.toDto(toy)).thenReturn(dto);

        List<ToyResponseDto> result = service.getToysByName("Car");

        assertEquals(1, result.size());
        verify(repository).findByName("Car");
        verify(mapper).toDto(toy);
    }

    @Test
    void createToy_shouldMapAndSave() {
        ToyRequestDto request = new ToyRequestDto();
        Toy toy = new Toy();
        ToyResponseDto response = new ToyResponseDto();

        when(mapper.toEntity(request)).thenReturn(toy);
        when(repository.save(toy)).thenReturn(toy);
        when(mapper.toDto(toy)).thenReturn(response);

        ToyResponseDto result = service.createToy(request);

        assertEquals(response, result);
        verify(mapper).toEntity(request);
        verify(repository).save(toy);
        verify(mapper).toDto(toy);
    }

    @Test
    void updateToy_shouldUpdateFieldsAndSave() {
        Toy toy = new Toy();
        ToyRequestDto dto = new ToyRequestDto();
        dto.setName("New");
        dto.setBrand("Brand");
        dto.setPrice(100.0);
        dto.setQuantity(5);

        when(repository.findById(1L)).thenReturn(Optional.of(toy));
        when(repository.save(toy)).thenReturn(toy);
        when(mapper.toDto(toy)).thenReturn(new ToyResponseDto());

        service.updateToy(1L, dto);

        assertEquals("New", toy.getName());
        assertEquals("Brand", toy.getBrand());
        assertEquals(100.0, toy.getPrice());
        assertEquals(5, toy.getQuantity());

        verify(repository).save(toy);
    }

    @Test
    void updateToy_shouldThrowException_whenNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
            () -> service.updateToy(1L, new ToyRequestDto()));
    }

    @Test
    void deleteToy_shouldCallRepository() {
        service.deleteToy(1L);
        verify(repository).deleteById(1L);
    }
}