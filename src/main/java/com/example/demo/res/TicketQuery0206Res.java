package com.example.demo.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TicketQuery0206Res {
    private Long id;

    private String userName;

    private String title;

    private String content;

    private String category;

    private Integer priority;

    private String status;

    private String statusName;

    private String contact;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
