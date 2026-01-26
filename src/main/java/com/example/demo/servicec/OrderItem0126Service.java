package com.example.demo.servicec;

import com.example.demo.dao.*;
import com.example.demo.mapper.OrderItem0126Repository;
import com.example.demo.req.OrderItem0126DelReq;
import com.example.demo.req.OrderItem0126InsReq;
import com.example.demo.req.OrderItem0126QueryReq;
import com.example.demo.req.OrderItemUpd0126Req;
import com.example.demo.res.OrderItem0126QueryRes;
import com.example.demo.res.OrderItemInit0126Res;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


@Service
public class OrderItem0126Service {
    @Autowired
    private OrderItem0126Repository orderItem0126Repository;

    public List<OrderItemInit0126Res> init(){
        List<OrderItemInit0126DAO> init = orderItem0126Repository.init();
        List<OrderItemInit0126Res> res = new ArrayList<>();
        for (OrderItemInit0126DAO i : init){
            OrderItemInit0126Res r = new OrderItemInit0126Res();
            BeanUtils.copyProperties(i,r);
            res.add(r);
        }
        return res;
    }

    public List<OrderItemInit0126Res> init1(){
        List<OrderItemInit0126DAO> init = orderItem0126Repository.init1();
        List<OrderItemInit0126Res> res = new ArrayList<>();
        for (OrderItemInit0126DAO i : init){
            OrderItemInit0126Res r = new OrderItemInit0126Res();
            BeanUtils.copyProperties(i,r);
            res.add(r);
        }
        return res;
    }

    public List<OrderItemInit0126Res> init2(){
        List<OrderItemInit0126DAO> init = orderItem0126Repository.init2();
        List<OrderItemInit0126Res> res = new ArrayList<>();
        for (OrderItemInit0126DAO i : init){
            OrderItemInit0126Res r = new OrderItemInit0126Res();
            BeanUtils.copyProperties(i,r);
            res.add(r);
        }
        return res;
    }

    //查詢按鈕
    public List<OrderItem0126QueryRes>query(OrderItem0126QueryReq req){
        List<OrderItem0126QueryDAO> query = orderItem0126Repository.query(req);
        List<OrderItem0126QueryRes> res = new ArrayList<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("HH:mm:ss");

        for(OrderItem0126QueryDAO s : query){
            OrderItem0126QueryRes orderItem0126QueryRes = new OrderItem0126QueryRes();
            BeanUtils.copyProperties(s,orderItem0126QueryRes);

            if(orderItem0126QueryRes.getCreatedAt() != null){
                orderItem0126QueryRes.setCreatedAtStr(orderItem0126QueryRes.getCreatedAt().format(formatter));
            }

            if(orderItem0126QueryRes.getUpdatedAt() != null){
                orderItem0126QueryRes.setUpdatedAtStr(orderItem0126QueryRes.getUpdatedAt().format(formatter2));
            }

            res.add(orderItem0126QueryRes);
        }

        return res;
    }

    //刪除
    public int del(OrderItem0126DelReq req){
        OrderItem0126DelDAO dao = new OrderItem0126DelDAO();
        BeanUtils.copyProperties(req,dao);
        int rows = orderItem0126Repository.del(dao);
        return rows;
    }


    //新增
    public int insert(OrderItem0126InsReq req){
        OrderItem0126InsDAO dao = new OrderItem0126InsDAO();
        BeanUtils.copyProperties(req,dao);
        int rows = orderItem0126Repository.insert(dao);
        return rows;
    }

    //修改
    public int update(OrderItemUpd0126Req req){
        OrderItemUpd0126DAO dao = new OrderItemUpd0126DAO();
        BeanUtils.copyProperties(req,dao);
        int rows = orderItem0126Repository.update(dao);
        return rows;
    }


    // poi上傳
    public void importOrderItemExcel(MultipartFile file) throws Exception {

        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);

        for (int i = 1; i <= sheet.getLastRowNum(); i++) { // 跳過表頭
            Row row = sheet.getRow(i);
            if (row == null) continue;

            String orderIdStr = getCell(row, 0);
            String itemIdStr = getCell(row, 1);
            String productName = getCell(row, 2);
            String quantity = getCell(row, 3);
            String unitPriceStr = getCell(row, 4);
            String discountStr = getCell(row, 5);
            String status = getCell(row, 6);

            // 必填欄位檢查（空字串就跳過）
            if (isBlank(orderIdStr)
                    || isBlank(itemIdStr)
                    || isBlank(productName)
                    || isBlank(quantity)
                    || isBlank(unitPriceStr)
                    || isBlank(discountStr)) {
                continue;
            }

            Long orderId = parseLong(orderIdStr);
            Long itemId = parseLong(itemIdStr);
            Integer unitPrice = parseInt(unitPriceStr);
            Integer discount = parseInt(discountStr);

            // status 空白就給 NEW
            if (isBlank(status)) status = "NEW";

            orderItem0126Repository.insert1(orderId, itemId, productName, quantity, unitPrice, discount, status);
        }

        workbook.close();
    }

    private String getCell(Row row, int index) {
        Cell cell = row.getCell(index);
        return cell == null ? "" : cell.toString().trim();
    }


    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private Long parseLong(String s) {
        s = s.trim();
        if (s.endsWith(".0")) s = s.substring(0, s.length() - 2);
        return Long.parseLong(s);
    }

    private Integer parseInt(String s) {
        s = s.trim();
        if (s.endsWith(".0")) s = s.substring(0, s.length() - 2);
        return Integer.parseInt(s);
    }


}
