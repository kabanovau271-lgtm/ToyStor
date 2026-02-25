package com.example.ts.controller;

import com.example.ts.dto.ToyRequestDto;
import com.example.ts.dto.ToyResponseDto;
import com.example.ts.service.ToyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ToyController.class)
class ToyControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private ToyService toyService;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void shouldReturnAllToys() throws Exception {
    ToyResponseDto toy = new ToyResponseDto();
    toy.setId(1L);
    toy.setName("Car");
    toy.setBrand("Brand");
    toy.setPrice(100.0);

    Mockito.when(toyService.getAllToys())
        .thenReturn(List.of(toy));

    mockMvc.perform(get("/api/toys"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].name").value("Car"));
  }

  @Test
  void shouldReturnToyById() throws Exception {
    ToyResponseDto toy = new ToyResponseDto();
    toy.setId(1L);
    toy.setName("Car");
    toy.setBrand("Brand");
    toy.setPrice(100.0);

    Mockito.when(toyService.getToyById(1L))
        .thenReturn(toy);

    mockMvc.perform(get("/api/toys/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Car"));
  }

  @Test
  void shouldCreateToy() throws Exception {
    ToyRequestDto request = new ToyRequestDto();
    request.setName("Car");
    request.setBrand("Brand");
    request.setPrice(100.0);

    ToyResponseDto response = new ToyResponseDto();
    response.setId(1L);
    response.setName("Car");
    response.setBrand("Brand");
    response.setPrice(100.0);

    Mockito.when(toyService.createToy(Mockito.any()))
        .thenReturn(response);

    mockMvc.perform(post("/api/toys")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1));
  }

  @Test
  void shouldDeleteToy() throws Exception {
    mockMvc.perform(delete("/api/toys/1"))
        .andExpect(status().isOk());

    Mockito.verify(toyService).deleteToy(1L);
  }
}