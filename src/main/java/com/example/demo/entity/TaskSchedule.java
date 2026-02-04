package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "task_schedule")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;            // 主鍵ID

    @Column(nullable = false, length = 100)
    private String title;       // 任務或活動標題

    @Column(length = 20)
    private String status;      // 狀態（例如：待處理、進行中、已完成）

    @Column(length = 30)
    private String type;        // 任務或活動類型

    @Column(precision = 10, scale = 2)
    private Integer amount;  // 金額或數值

    private Integer priority;   // 優先順序

    @Column(length = 255)
    private String remark;      // 備註說明

    @Column(length = 100)
    private String location;    // 地點

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;   // 開始時間

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;     // 結束時間
}
