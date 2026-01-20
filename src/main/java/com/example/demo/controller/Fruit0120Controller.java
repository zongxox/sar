package com.example.demo.controller;

import com.example.demo.req.*;
import com.example.demo.res.*;
import com.example.demo.servicec.Fruit0120Service;
import com.example.demo.servicec.User0119Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/fruit/0120")
public class Fruit0120Controller {
    @Autowired
    private Fruit0120Service fruit0120Service;

    //初始化查詢
    @GetMapping("/initSelect")
    public List<FruitInit0120Res> selectAllIdAndAddress(){
        return fruit0120Service.initSelect();
    }

    //查詢按鈕
    @PostMapping("/query")
    public List<FruitQuery0120Res> query(@RequestBody FruitQuery0120Req req){
        return fruit0120Service.query(req);
    }

    //刪除
    @DeleteMapping("/delete/{id}")
    public int delete(@PathVariable String id) {
        FruitDel0120Req req = new FruitDel0120Req();
        req.setId(Long.valueOf(id));
        return fruit0120Service.delete(req);
    }


    //註冊按鈕
    @PostMapping("/insert")
    public int insert(@RequestBody FruitIns0120Req req){
        int rows = fruit0120Service.insert(req);
        return  rows;
    }

    //跳轉修改先查詢
    @PostMapping("/updQuery")
    public FruitUpdQuery0120Res updQuery(@RequestBody FruitUpdQuery0120Req req){
        return fruit0120Service.updQuery(req);
    }

    @PostMapping("/update")
    public int update(@RequestBody FruitUpd0120Req req) {
        return fruit0120Service.update(req);
    }


}
