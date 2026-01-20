package com.example.demo.servicec;

import com.example.demo.dao.*;
import com.example.demo.mapper.FruitRepository;
import com.example.demo.mapper.UserRepository;
import com.example.demo.req.*;
import com.example.demo.res.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class Fruit0120Service {
    @Autowired
    private FruitRepository fruitRepository;



    //頁面初始化查詢
    public List<FruitInit0120Res> initSelect() {
        List<FruitInit0120DAO> fruitInit0120DAO = fruitRepository.initSelect();
        List<FruitInit0120Res> fruitInit0120Res = new ArrayList<>();
        for (FruitInit0120DAO dao : fruitInit0120DAO) {
            FruitInit0120Res res = new FruitInit0120Res();
            BeanUtils.copyProperties(dao, res);
            fruitInit0120Res.add(res);
        }

        //return fruitInit0120Res;
    }


    //查詢按鈕
    public List<FruitQuery0120Res> query(FruitQuery0120Req req) {
        List<FruitQuery0120DAO> dao = fruitRepository.query(req);
        List<FruitQuery0120Res> res = new ArrayList<>();
        for (FruitQuery0120DAO dao1 : dao) {
            FruitQuery0120Res fruitQuery0120Res = new FruitQuery0120Res();
            BeanUtils.copyProperties(dao1, fruitQuery0120Res);
            res.add(fruitQuery0120Res);
        }
        return res;
    }

    //刪除
    public int delete(FruitDel0120Req req) {
        return fruitRepository.deleteById(req);
    }

    //註冊按鈕
    public int insert(FruitIns0120Req req){
        int rows = fruitRepository.insert(req);
        return rows;
    }

    //跳轉修改先查詢
    public FruitUpdQuery0120Res updQuery(FruitUpdQuery0120Req req){
        FruitUpdQuery0120DAO dao = fruitRepository.updQuery(req);
        FruitUpdQuery0120Res res = new FruitUpdQuery0120Res();
        BeanUtils.copyProperties(dao, res);
        return res;
    }

    //修改
    public int update(FruitUpd0120Req req){
        int rows = fruitRepository.update(req);
        return rows;
    }


}
