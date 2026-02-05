package com.example.demo.controller;

import com.example.demo.req.TicketDel0206Req;
import com.example.demo.req.TicketQuery0206Req;
import com.example.demo.res.TicketInit0206Res;
import com.example.demo.res.TicketQuery0206Res;
import com.example.demo.service.Ticket0206Service;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/Ticket/0206")
@RequiredArgsConstructor
public class Ticket0206Controller {

    private final Ticket0206Service ticket0206Service;

    //頁面初始化
    @GetMapping("/init")
    public List<TicketInit0206Res> init(){
        return ticket0206Service.init();
    }

    //查詢案紐
    @PostMapping("/query")
    public List<TicketQuery0206Res> query(@RequestBody TicketQuery0206Req req){
        return ticket0206Service.query(req);
    }


    //刪除
    @GetMapping("/delete/{id}")
    public int del(@PathVariable String id){
        TicketDel0206Req req = new TicketDel0206Req();
        req.setId(id);
        int rows = ticket0206Service.del(req);
        return rows;
    }

//    //新增
//    @PostMapping("/insert")
//    public int insert(@RequestBody OrderShipmentIns0205Req req){
//        int rows = orderShipment0205Service.insert(req);
//        return rows;
//    }
//
//
//    //修改
//    @PostMapping("/update")
//    public int update(@RequestBody OrderShipmentUpd0205Req req){
//        return orderShipment0205Service.update(req);
//    }
//
//
//    //下載
//    @PostMapping("/downloadExcel")
//    public void downloadExcel(
//            @RequestBody OrderShipmentQuery0205Req req,
//            @RequestParam(defaultValue = "xlsx") String excelType,
//            HttpServletResponse response
//    ) throws Exception {
//
//        //拿到結果
//        List<OrderShipmentDon0205Res> rows = orderShipment0205Service.downloadList(req);
//
//        //判斷使用者要下載的是不是xls
//        boolean isXls = "xls".equalsIgnoreCase(excelType);
//
//        //把「資料（rows）」轉成「Excel 檔案內容」，並用 byte[] 的形式回傳
//        byte[] bytes = ExcelExportUtil0205.buildApplyDetailExcel(rows, isXls);
//
//        //起一個隨機檔名
//        String fileName = UUID.randomUUID().toString();
//
//
//        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");//告訴瀏覽器這是xlsx
//        response.setHeader("Content-Disposition",
//                    "attachment; filename=\"" + fileName + ".xlsx\"");
//
//
//        response.setContentLength(bytes.length);
//
//        try (ServletOutputStream out = response.getOutputStream()) {
//            out.write(bytes);
//            out.flush();
//        }
//    }
//
//
//
//
//    //上傳檔案
//    @PostMapping("/importExcel")
//    public String importExcel(@RequestParam("file") MultipartFile file) throws Exception {
//        orderShipment0205Service.importExcel(file);
//        return "匯入成功";
//    }
//
//
//    //pdf
//    @PostMapping("/pdf")
//    public ResponseEntity<byte[]> exportPdf(@RequestBody OrderShipmentQuery0205Req req) throws Exception {
//
//        byte[] pdfBytes = orderShipment0205Service.generatePdf(req);
//
//        return ResponseEntity.ok()
//                //告訴瀏覽器,這是一個 PDF，要怎麼顯示／下載
//                .header(HttpHeaders.CONTENT_DISPOSITION,
//                        //inline 直接在瀏覽器開啟顯示
//                        //如果使用者另存檔，預設檔名叫 taskSchedule0204.pdf
//                        //.header(HttpHeaders.CONTENT_DISPOSITION, 直接跳下載視窗
//                        //"attachment; filename=taskSchedule0204.pdf") 不在瀏覽器內顯示
//                        "inline; filename=OrderShipment0205.pdf")
//                .contentType(MediaType.APPLICATION_PDF)//告訴瀏覽器,這份回傳的內容是 PDF 檔案
//                .body(pdfBytes);//真正送出去的資料內容
//    }







}

