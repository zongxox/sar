package com.example.demo.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskSchedule0204DAO {

    private String status;      // 狀態（例如：待處理、進行中、已完成）

    private String type;        // 任務或活動類型

    private String location;    // 地點

}
