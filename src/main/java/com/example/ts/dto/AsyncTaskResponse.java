package com.example.ts.dto;

import com.example.ts.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AsyncTaskResponse {
  private Long taskId;
  private TaskStatus status;
}