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
public class Penalty {
  private Long id;
  private Long userId;
  private Long loanId;
  private double amount;
  private boolean paid;
  private LocalDateTime createdAt;
  private LocalDateTime paidAt;
}


