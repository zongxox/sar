package com.example.demo.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseQuery0202Req {
    private String title;

    private List<String> level;

    private Integer maxStudents;

    private String isPublished;

}
