package com.example.demo.controller;

import com.example.demo.excelExportUtil.ExcelExportUtil0204;
import com.example.demo.req.TaskScheduleDel0204Req;
import com.example.demo.req.TaskScheduleIns0204Req;
import com.example.demo.req.TaskScheduleQuery0204Req;
import com.example.demo.req.TaskScheduleUpd0204Req;
import com.example.demo.res.TaskSchedule0204Res;
import com.example.demo.res.TaskScheduleDon0204Res;
import com.example.demo.res.TaskScheduleQuery0204Res;
import com.example.demo.service.TaskSchedule0204Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/TaskSchedule/0204")
@RequiredArgsConstructor
public class TaskSchedule0204Controller {

    private final TaskSchedule0204Service taskSchedule0204Service;

    //頁面初始化
    @GetMapping("/init")
    public List<TaskSchedule0204Res> init(){
        return taskSchedule0204Service.init();
    }

    //查詢案衂
    @PostMapping("/query")
    public List<TaskScheduleQuery0204Res> query(@RequestBody TaskScheduleQuery0204Req req){
        return taskSchedule0204Service.query(req);
    }


    //刪除
    @GetMapping("/delete/{id}")
    public int del(@PathVariable String id){
        TaskScheduleDel0204Req req = new TaskScheduleDel0204Req();
        req.setId(id);
        int rows = taskSchedule0204Service.del(req);
        return rows;
    }

    //新增
    @PostMapping("/insert")
    public int insert(@RequestBody TaskScheduleIns0204Req req){
        int rows = taskSchedule0204Service.insert(req);
        return rows;
    }


    //修改
    @PostMapping("/update")
    public int update(@RequestBody TaskScheduleUpd0204Req req){
        return taskSchedule0204Service.update(req);
    }


    //上傳檔案
    @PostMapping("/importExcel")
    public String importExcel(@RequestParam("file") MultipartFile file) throws Exception {
        taskSchedule0204Service.importExcel(file);
        return "匯入成功";
    }


    //下載
    @PostMapping("/downloadExcel")
    public void downloadExcel(
            @RequestBody TaskScheduleQuery0204Req req,
            @RequestParam(defaultValue = "xlsx") String excelType,
            HttpServletResponse response
    ) throws Exception {

        //拿到結果
        List<TaskScheduleDon0204Res> rows = taskSchedule0204Service.downloadList(req);

        //判斷使用者要下載的是不是xls
        boolean isXls = "xls".equalsIgnoreCase(excelType);

        //把「資料（rows）」轉成「Excel 檔案內容」，並用 byte[] 的形式回傳
        byte[] bytes = ExcelExportUtil0204.buildApplyDetailExcel(rows, isXls);

        //起一個隨機檔名
        String fileName = UUID.randomUUID().toString();


        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");//告訴瀏覽器這是xlsx
        response.setHeader("Content-Disposition",
                    "attachment; filename=\"" + fileName + ".xlsx\"");


        response.setContentLength(bytes.length);

        try (ServletOutputStream out = response.getOutputStream()) {
            out.write(bytes);
            out.flush();
        }
    }


    @GetMapping("/pdf")
    public ResponseEntity<byte[]> exportPdf() throws Exception {

        byte[] pdfBytes = taskSchedule0204Service.generatePdf();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=taskSchedule0204.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }





}

