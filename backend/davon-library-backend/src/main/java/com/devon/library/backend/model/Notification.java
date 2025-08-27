package com.devon.library.backend.model;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
  private Long id;
  private Long userId;
  private String type;
  private String title;
  private String message;
  private LocalDateTime createdAt;
  private Boolean read;
}
