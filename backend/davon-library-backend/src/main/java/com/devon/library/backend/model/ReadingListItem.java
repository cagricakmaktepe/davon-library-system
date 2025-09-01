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
public class ReadingListItem {
  private Long id;
  private Long userId;
  private Long bookId;
  private Integer priority;
  private LocalDateTime addedAt;
}


