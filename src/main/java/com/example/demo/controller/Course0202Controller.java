package com.example.demo.controller;

import com.example.demo.CorsConfig.ExcelExportUtil0202;
import com.example.demo.req.CourseDel0202Req;
import com.example.demo.req.CourseIns0202Req;
import com.example.demo.req.CourseQuery0202Req;
import com.example.demo.req.CourseUpd0202Req;
import com.example.demo.res.CourseDon0202Res;
import com.example.demo.res.CourseInit0202Res;
import com.example.demo.res.CourseQuery0202Res;
import com.example.demo.service.Course0202Service;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/Course/0202")
@RequiredArgsConstructor
public class Course0202Controller {

    private final Course0202Service course0202Service;

    //頁面初始化
    @GetMapping("/init")
    public List<CourseInit0202Res> init(){
        return course0202Service.init();
    }

    //查詢案衂
    @PostMapping("/query")
    public List<CourseQuery0202Res> query(@RequestBody CourseQuery0202Req req){
        return course0202Service.query(req);
    }


    //刪除
    @GetMapping("/delete/{id}")
    public int del(@PathVariable String id){
        CourseDel0202Req req = new CourseDel0202Req();
        req.setId(id);
        int rows = course0202Service.del(req);
        return rows;
    }

    //新增
    @PostMapping("/insert")
    public int insert(@RequestBody CourseIns0202Req req){
        int rows = course0202Service.insert(req);
        return rows;
    }


    //修改
    @PostMapping("/update")
    public int update(@RequestBody CourseUpd0202Req req){
        return course0202Service.update(req);
    }


    //上傳檔案
    @PostMapping("/importExcel")
    public String importExcel(@RequestParam("file") MultipartFile file) throws Exception {
        course0202Service.importExcel(file);
        return "匯入成功";
    }


    //下載
    @PostMapping("/downloadExcel")
    public void downloadExcel(
            @RequestBody CourseQuery0202Req req,
            @RequestParam(defaultValue = "xlsx") String excelType,
            HttpServletResponse response
    ) throws Exception {

        //拿到結果
        List<CourseDon0202Res> rows = course0202Service.downloadList(req);

        //判斷使用者要下載的是不是xls
        boolean isXls = "xls".equalsIgnoreCase(excelType);

        //把「資料（rows）」轉成「Excel 檔案內容」，並用 byte[] 的形式回傳
        byte[] bytes = ExcelExportUtil0202.buildApplyDetailExcel(rows, isXls);

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

}

