package com.example.ts.service;

import com.example.ts.domain.Toy;
import com.example.ts.dto.ToyRequestDto;
import com.example.ts.dto.ToyResponseDto;
import com.example.ts.mapper.ToyMapper;
import com.example.ts.repository.ToyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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
void updateToy_shouldThrowException_whenToyNotFound() {
    ToyRequestDto dto = new ToyRequestDto();

    when(repository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(RuntimeException.class,
            () -> service.updateToy(1L, dto));
}
    @Test
    void getAllToys_shouldReturnList() {
        Toy toy = new Toy();
        ToyResponseDto dto = new ToyResponseDto();

        when(repository.findAll()).thenReturn(List.of(toy));
        when(mapper.toDto(toy)).thenReturn(dto);

        List<ToyResponseDto> result = service.getAllToys();

        assertEquals(1, result.size());
        verify(repository).findAll();
    }
@Test
void getToysByName_shouldReturnList() {
    Toy toy = new Toy();
    ToyResponseDto dto = new ToyResponseDto();

    when(repository.findByName("Car")).thenReturn(List.of(toy));
    when(mapper.toDto(toy)).thenReturn(dto);

    List<ToyResponseDto> result = service.getToysByName("Car");

    assertEquals(1, result.size());
    verify(repository).findByName("Car");
}
    @Test
    void getToyById_shouldReturnToy() {
        Toy toy = new Toy();
        ToyResponseDto dto = new ToyResponseDto();

        when(repository.findById(1L)).thenReturn(Optional.of(toy));
        when(mapper.toDto(toy)).thenReturn(dto);

        ToyResponseDto result = service.getToyById(1L);

        assertNotNull(result);
        verify(repository).findById(1L);
    }

    @Test
    void getToyById_shouldThrowException() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> service.getToyById(1L));
    }



    @Test
    void createToy_shouldSaveToy() {
        ToyRequestDto request = new ToyRequestDto();
        Toy toy = new Toy();
        ToyResponseDto response = new ToyResponseDto();

        when(mapper.toEntity(request)).thenReturn(toy);
        when(repository.save(toy)).thenReturn(toy);
        when(mapper.toDto(toy)).thenReturn(response);

        ToyResponseDto result = service.createToy(request);

        assertNotNull(result);
        verify(repository).save(toy);
    }

    @Test
    void updateToy_shouldUpdateFields() {
        Toy toy = new Toy();
        ToyRequestDto dto = new ToyRequestDto();
        dto.setName("New");
        dto.setBrand("Brand");
        dto.setPrice(100.0);
        dto.setQuantity(5);

        when(repository.findById(1L)).thenReturn(Optional.of(toy));
        when(repository.save(toy)).thenReturn(toy);
        when(mapper.toDto(toy)).thenReturn(new ToyResponseDto());

        ToyResponseDto result = service.updateToy(1L, dto);

        assertEquals("New", toy.getName());
        verify(repository).save(toy);
    }

    @Test
    void deleteToy_shouldCallRepository() {
        service.deleteToy(1L);
        verify(repository).deleteById(1L);
    }
}