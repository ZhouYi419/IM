package com.zy.im.domain;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class BaseEntity {
    private LocalDateTime createdTime;

    private LocalDateTime updatedTime;
}
