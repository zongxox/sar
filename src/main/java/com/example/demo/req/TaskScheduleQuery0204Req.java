package com.example.demo.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskScheduleQuery0204Req {
    private String title;       // 任務或活動標題

    private String status;      // 狀態（例如：待處理、進行中、已完成）

    private String type;        // 任務或活動類型

    private List<String> location;    // 地點

}
