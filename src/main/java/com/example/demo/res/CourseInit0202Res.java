package com.example.demo.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseInit0202Res {

    private String level;

    private Integer maxStudents;

    private String isPublished;

    private String isPublishedName;

}
