package com.example.ts.controller;

import com.example.ts.async.AsyncToyService;
import com.example.ts.dto.AsyncTaskResponse;
import com.example.ts.dto.ToyRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Асинхронные операции с игрушками")
@RestController
@RequestMapping("/toys/async")
@RequiredArgsConstructor
public class AsyncToyController {

  private static final String EXPECTED = "Ожидали";
  private static final String ACTUAL = "Получили";

  private final AsyncToyService asyncService;

  @Operation(summary = "Создать задачу")
  @PostMapping
  public ResponseEntity<Map<String, Long>> createAsync(@RequestBody List<ToyRequestDto> dtos) {

    Long taskId = asyncService.createTask();
    asyncService.processAsync(taskId, dtos);

    return ResponseEntity.accepted().body(Map.of("taskId", taskId));
  }

  @Operation(summary = "Статус задачи")
  @GetMapping("/status/{id}")
  public AsyncTaskResponse getStatus(@PathVariable Long id) {
    return new AsyncTaskResponse(id, asyncService.getStatus(id));
  }

  @Operation(summary = "Тест race condition")
  @GetMapping("/race/test")
  public Map<String, Long> raceUnsafe() {
    long result = asyncService.demoRaceCondition();
    return Map.of(EXPECTED, 10000L, ACTUAL, result);
  }

  @Operation(summary = "Исправленный тест race condition")
  @GetMapping("/race/atomic")
  public Map<String, Long> raceSafe() {
    long result = asyncService.demoFixedRaceCondition();
    return Map.of(EXPECTED, 10000L, ACTUAL, result);
  }

  @Operation(summary = "Синхронизированный тест race condition")
  @GetMapping("/race/sync")
  public Map<String, Long> raceSync() {
    long result = asyncService.demoSyncRaceCondition();
    return Map.of(EXPECTED, 10000L, ACTUAL, result);
  }
}