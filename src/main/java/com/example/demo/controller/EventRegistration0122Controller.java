package com.example.demo.controller;

import com.example.demo.req.EventRegistrationIns0123Req;
import com.example.demo.req.EventRegistrationQuery0123Req;
import com.example.demo.res.EventRegistrationInit0123Res;
import com.example.demo.res.EventRegistrationInit10123Res;
import com.example.demo.res.EventRegistrationInit20123Res;
import com.example.demo.res.EventRegistrationQueryRes;
import com.example.demo.servicec.EventRegistration0123Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/eventRegistration/0123")
public class EventRegistration0122Controller {
    @Autowired
    private EventRegistration0123Service eventRegistration0123Service;

    @GetMapping("/initSelect")
    public List<EventRegistrationInit0123Res> init(){
        return eventRegistration0123Service.init();
    }

    @GetMapping("/initSelect1")
    public List<EventRegistrationInit10123Res> init1(){
        return eventRegistration0123Service.init1();
    }

    @GetMapping("/initSelect2")
    public List<EventRegistrationInit20123Res> init2(){
        return eventRegistration0123Service.init2();
    }

    //查詢按鈕
    @PostMapping("/query")
    public List<EventRegistrationQueryRes> query(@RequestBody EventRegistrationQuery0123Req req){
        return eventRegistration0123Service.query(req);
    }

//    //刪除
//    @DeleteMapping("/delete/{id}")
//    public int delete(@PathVariable String id) {
//        ProductDel0122Req req = new ProductDel0122Req();
//        req.setId(Long.valueOf(id));
//        return product0122Service.delete(req);
//    }

    //註冊按鈕
    @PostMapping("/insert")
    public int insert(@RequestBody EventRegistrationIns0123Req req){
        int rows = eventRegistration0123Service.insert(req);
        return  rows;
    }


    //附表註冊按鈕
//    @PostMapping("/insert1")
//    public int insert1(@RequestBody MemberDetail0123Req req){
//        int rows = eventRegistration0123Service.insert1(req);
//        return  rows;
//    }

//
//    //跳轉修改先查詢
//    @PostMapping("/updQuery")
//    public ProductUpdQuery0122Res updQuery(@RequestBody ProductUpdQuery0122Req req){
//        return product0122Service.updQuery(req);
//    }
//
//    //修改按鈕
//    @PostMapping("/update")
//    public int update(@RequestBody ProductUpd0122Req req) {
//        return product0122Service.update(req);
//    }
}
