package com.example.demo.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TicketIns0206DAO {
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
