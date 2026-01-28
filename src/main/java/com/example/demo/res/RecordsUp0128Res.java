package com.example.demo.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class RecordsUp0128Res {

    private Integer userId;

    private String title;

    private String description;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String status;

    private String location;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}

