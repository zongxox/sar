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
public class TaskScheduleDon0204Res {

    private Long id;            // 主鍵ID

    private String title;       // 任務或活動標題

    private String status;      // 狀態（例如：待處理、進行中、已完成）

    private String type;        // 任務或活動類型

    private Integer amount;  // 金額或數值

    private Integer priority;   // 優先順序

    private String remark;      // 備註說明

    private String location;    // 地點

    private LocalDateTime startTime;   // 開始時間

    private LocalDateTime endTime;     // 結束時間
}
