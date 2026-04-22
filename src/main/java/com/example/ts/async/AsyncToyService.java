package com.example.ts.async;

import com.example.ts.dto.ToyRequestDto;
import com.example.ts.enums.TaskStatus;
import com.example.ts.service.ToyService;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AsyncToyService {

  private final ToyService toyService;

  private final Map<Long, TaskStatus> taskStatusMap = new ConcurrentHashMap<>();

  private final AtomicLong idGenerator = new AtomicLong(1);
  private final AtomicLong safeCounter = new AtomicLong(0);

  private long unsafeCounter = 0;
  private long syncCounter = 0;

  public Long createTask() {
    long id = idGenerator.getAndIncrement();
    taskStatusMap.put(id, TaskStatus.ACCEPTED);
    return id;
  }

  @Async
  public void processAsync(Long taskId, List<ToyRequestDto> dtos) {
    try {
      taskStatusMap.put(taskId, TaskStatus.IN_PROGRESS);

      toyService.createToysBulk(dtos);

      Thread.sleep(3000);

      taskStatusMap.put(taskId, TaskStatus.COMPLETED);

    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      taskStatusMap.put(taskId, TaskStatus.FAILED);
    } catch (Exception e) {
      taskStatusMap.put(taskId, TaskStatus.FAILED);
    }
  }

  public TaskStatus getStatus(Long taskId) {
    return taskStatusMap.get(taskId);
  }


  public void incrementUnsafe() {
    unsafeCounter++;
  }

  public synchronized void incrementSync() {
    syncCounter++;
  }

  public void incrementSafe() {
    safeCounter.incrementAndGet();
  }

  public void resetCounters() {
    unsafeCounter = 0;
    syncCounter = 0;
    safeCounter.set(0);
  }

  public long demoRaceCondition() {
    resetCounters();

    ExecutorService executor = Executors.newFixedThreadPool(50);

    for (int i = 0; i < 10000; i++) {
      executor.execute(this::incrementUnsafe);
    }

    executor.shutdown();

    try {
      executor.awaitTermination(1, TimeUnit.MINUTES);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }

    return unsafeCounter;
  }


  public long demoSyncRaceCondition() {
    resetCounters();

    ExecutorService executor = Executors.newFixedThreadPool(50);

    for (int i = 0; i < 10000; i++) {
      executor.execute(this::incrementSync);
    }

    executor.shutdown();

    try {
      executor.awaitTermination(1, TimeUnit.MINUTES);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }

    return syncCounter;
  }


  public long demoFixedRaceCondition() {
    resetCounters();

    ExecutorService executor = Executors.newFixedThreadPool(50);

    for (int i = 0; i < 10000; i++) {
      executor.execute(this::incrementSafe);
    }

    executor.shutdown();

    try {
      executor.awaitTermination(1, TimeUnit.MINUTES);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }

    return safeCounter.get();
  }
}