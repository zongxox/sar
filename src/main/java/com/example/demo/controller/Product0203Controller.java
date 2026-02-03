package com.example.demo.controller;

import com.example.demo.excelExportUtil.ExcelExportUtil0203;
import com.example.demo.req.ProducQuery0203Req;
import com.example.demo.req.ProductDel0203Req;
import com.example.demo.req.ProductIns0203Req;
import com.example.demo.req.ProductUpd0203Req;
import com.example.demo.res.ProductDon0203Res;
import com.example.demo.res.ProductInit0203Res;
import com.example.demo.res.ProductQuery0203Res;
import com.example.demo.service.Product0203Service;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/Product/0203")
@RequiredArgsConstructor
public class Product0203Controller {

    private final Product0203Service product0203Service;

    //頁面初始化
    @GetMapping("/init")
    public List<ProductInit0203Res> init(){
        return product0203Service.init();
    }

    //查詢案衂
    @PostMapping("/query")
    public List<ProductQuery0203Res> query(@RequestBody ProducQuery0203Req req){
        return product0203Service.query(req);
    }


    //刪除
    @GetMapping("/delete/{id}")
    public int del(@PathVariable String id){
        ProductDel0203Req req = new ProductDel0203Req();
        req.setId(id);
        int rows = product0203Service.del(req);
        return rows;
    }

    //新增
    @PostMapping("/insert")
    public int insert(@RequestBody ProductIns0203Req req){
        int rows = product0203Service.insert(req);
        return rows;
    }


    //修改
    @PostMapping("/update")
    public int update(@RequestBody ProductUpd0203Req req){
        return product0203Service.update(req);
    }


    //上傳檔案
    @PostMapping("/importExcel")
    public String importExcel(@RequestParam("file") MultipartFile file) throws Exception {
        product0203Service.importExcel(file);
        return "匯入成功";
    }


    //下載
    @PostMapping("/downloadExcel")
    public void downloadExcel(
            @RequestBody ProducQuery0203Req req,
            @RequestParam(defaultValue = "xlsx") String excelType,
            HttpServletResponse response
    ) throws Exception {

        //拿到結果
        List<ProductDon0203Res> rows = product0203Service.downloadList(req);

        //判斷使用者要下載的是不是xls
        boolean isXls = "xls".equalsIgnoreCase(excelType);

        //把「資料（rows）」轉成「Excel 檔案內容」，並用 byte[] 的形式回傳
        byte[] bytes = ExcelExportUtil0203.buildApplyDetailExcel(rows, isXls);

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

//跨域請求
// ========= 下面這一段是「轉呼叫另一台電腦 API」用的設定 =========

    // 你在 Postman 成功時拿到的 JSESSIONID（測試用，正式不可寫死）
    private static final String JSESSIONID = "C8EE3CD9F45FDBBA13442987FE6A066B";

    // 你在 Postman 成功時拿到的 CSRF Token（測試用，正式不可寫死）
    private static final String CSRF = "35275dd7-f977-4cdb-9172-e54bf430ac7c";

    // 建立一個 WebClient，用來從「你這台後端」去呼叫「192.168.10.118 那台後端」
// baseUrl 代表之後所有請求都會以這個網址當開頭
    private final WebClient webClient = WebClient.builder()
            .baseUrl("http://192.168.10.118:8081") // 另一台電腦的後端網址
            .build();

    // 對外提供一支 API
// 讓前端可以呼叫這支 API
// 真正會轉去呼叫 192.168.10.118 的，是下面 method 裡面的 WebClient
    @PostMapping("/smr")
    public Mono<ResponseEntity<String>> proxy(@RequestBody String body) {

        // 使用 WebClient 發送一個 POST 請求到另一台電腦
        return webClient.post()

                // 設定實際要呼叫的路徑
                // 會組成： http://192.168.10.118:8081/smr/a10/a16/query
                .uri("/smr/a10/a16/query")

                // 告訴對方：我送過去的 request body 是 JSON
                .contentType(MediaType.APPLICATION_JSON)

                // 加上 CSRF Token header（對方系統需要）
                .header("X-Csrf-Token", CSRF)

                // 加上 Cookie header，把 JSESSIONID 帶給對方
                // 這樣對方才會認為你是「已登入的 session」
                .header(HttpHeaders.COOKIE, "JSESSIONID=" + JSESSIONID)

                // 把前端送進來的 JSON body 原封不動轉給對方
                .bodyValue(body)

                // 送出 request，並且取得對方回來的 response
                // exchangeToMono 讓你可以拿到「完整的 response（含狀態碼、header、body）」
                .exchangeToMono(resp ->

                        // 把對方回傳的 body 轉成 String
                        resp.bodyToMono(String.class)

                                // 如果對方回傳沒有 body，避免回傳 null
                                .defaultIfEmpty("")

                                // 將對方的 response 組成你要回給前端的 ResponseEntity
                                .map(respBody -> {

                                    // 建立一個新的 response header 給前端
                                    HttpHeaders out = new HttpHeaders();

                                    // 取得對方回傳的 Content-Type
                                    // 如果對方沒有帶 Content-Type，就預設當成 JSON
                                    MediaType ct = resp.headers()
                                            .contentType()
                                            .orElse(MediaType.APPLICATION_JSON);

                                    // 把對方的 Content-Type 設定回給前端
                                    out.setContentType(ct);

                                    // 將：
                                    // 1. 對方的 body
                                    // 2. 對方的狀態碼
                                    // 3. 我們整理過的 header
                                    // 原封不動包成新的 ResponseEntity 回給前端
                                    return new ResponseEntity<>(
                                            respBody,            // response body
                                            out,                 // response headers
                                            resp.statusCode()    // HTTP 狀態碼
                                    );
                                })
                );
    }

@PostMapping(value = "/report/test", produces = MediaType.APPLICATION_PDF_VALUE)
public ResponseEntity<byte[]> testReport(@RequestBody ProducQuery0203Req req) throws Exception {

    JasperPrint print = product0203Service.testLoadReport(req);

    byte[] pdf = JasperExportManager.exportReportToPdf(print);

    return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=product0203.pdf")
            .contentType(MediaType.APPLICATION_PDF)
            .body(pdf);
}




}

