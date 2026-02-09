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
public class TicketUpd0206Req {
    private String id;

    private String userName;

    private String title;

    private String content;

    private List<String> category;

    private String priority;

    private String status;

    private String contact;

    private String createdAt;

    private String updatedAt;
}
