package com.example.demo.controller;

import com.example.demo.CorsConfig.ExcelExportUtil0130;
import com.example.demo.req.*;
import com.example.demo.res.ArticleDon0130Res;
import com.example.demo.res.ArticleInit0130Res;
import com.example.demo.res.ArticleQuery0130Res;
import com.example.demo.res.ArticleUpdQuery0130Res;
import com.example.demo.servicec.Articles0130Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/Articles/0130")
public class Articles0130Controller {
    @Autowired
    private Articles0130Service articles0130Service;

    //頁面初始化
    @GetMapping("init")
    public List<ArticleInit0130Res> init(){
        return articles0130Service.init();
    }

    //查詢案衂
    @PostMapping("/query")
    public List<ArticleQuery0130Res> query(@RequestBody ArticleQuery0130Req req){
        return articles0130Service.query(req);
    }


    //刪除
    @GetMapping("/delete/{id}")
    public int del(@PathVariable String id){
        ArticleDel0130Req req = new ArticleDel0130Req();
        req.setId(id);
        int rows = articles0130Service.del(req);
        return rows;
    }

    //新增
    @PostMapping("/insert")
    public int insert(@RequestBody ArticleIns0130Req req){
        int rows = articles0130Service.insert(req);
        return rows;
    }

    //跳轉修改畫面查詢
    @GetMapping("/updQuery/{id}")
    public ArticleUpdQuery0130Res updaQuery(@PathVariable String id){
        ArticleUpdQuery0130Req req = new ArticleUpdQuery0130Req();
        req.setId(id);
        return articles0130Service.updQuery(req);
    }

    //修改
    @PostMapping("/update")
    public int update(@RequestBody ArticleUpd0130Req req){
        return articles0130Service.update(req);
    }


    //上傳檔案
    @PostMapping("/importExcel")
    public String importExcel(@RequestParam("file") MultipartFile file) throws Exception {
        articles0130Service.importExcel(file);
        return "匯入成功";
    }


    //下載
    @PostMapping("/downloadExcel")
    public void downloadExcel(
            @RequestBody ArticleQuery0130Req req,
            @RequestParam(defaultValue = "xlsx") String excelType,
            HttpServletResponse response
    ) throws Exception {

        //拿到結果
        List<ArticleDon0130Res> rows = articles0130Service.downloadList(req);

        //判斷使用者要下載的是不是xls
        boolean isXls = "xls".equalsIgnoreCase(excelType);

        //把「資料（rows）」轉成「Excel 檔案內容」，並用 byte[] 的形式回傳
        byte[] bytes = ExcelExportUtil0130.buildApplyDetailExcel(rows, isXls);

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

