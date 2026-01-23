package com.example.demo.servicec;

import com.example.demo.dao.EventRegistrationInit0123DAO;
import com.example.demo.dao.EventRegistrationIns0123DAO;
import com.example.demo.dao.EventRegistrationQueryDAO;
import com.example.demo.dao.MemberDetail0123DAO;
import com.example.demo.mapper.EventRegistration0123Repository;
import com.example.demo.req.EventRegistrationIns0123Req;
import com.example.demo.req.EventRegistrationQuery0123Req;
import com.example.demo.res.EventRegistrationInit0123Res;
import com.example.demo.res.EventRegistrationInit10123Res;
import com.example.demo.res.EventRegistrationInit20123Res;
import com.example.demo.res.EventRegistrationQueryRes;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class EventRegistration0123Service {
    @Autowired
    private EventRegistration0123Repository eventRegistration0123Repository;

    public List<EventRegistrationInit0123Res> init(){
        List<EventRegistrationInit0123DAO> init = eventRegistration0123Repository.init();
        List<EventRegistrationInit0123Res> res = new ArrayList<>();
        for (EventRegistrationInit0123DAO s : init){
            EventRegistrationInit0123Res eventRegistrationInit0123Res = new EventRegistrationInit0123Res();
            BeanUtils.copyProperties(s,eventRegistrationInit0123Res);
            res.add(eventRegistrationInit0123Res);
        }
        return res;
    }

    public List<EventRegistrationInit10123Res> init1(){
        List<EventRegistrationInit0123DAO> init = eventRegistration0123Repository.init1();
        List<EventRegistrationInit10123Res> res = new ArrayList<>();
        for (EventRegistrationInit0123DAO s : init){
            EventRegistrationInit10123Res eventRegistrationInit10123Res = new EventRegistrationInit10123Res();
            BeanUtils.copyProperties(s,eventRegistrationInit10123Res);
            res.add(eventRegistrationInit10123Res);
        }
        return res;
    }


    public List<EventRegistrationInit20123Res> init2(){
        List<EventRegistrationInit0123DAO> init = eventRegistration0123Repository.init2();
        List<EventRegistrationInit20123Res> res = new ArrayList<>();
        for (EventRegistrationInit0123DAO s : init){
            EventRegistrationInit20123Res eventRegistrationInit20123Res = new EventRegistrationInit20123Res();
            BeanUtils.copyProperties(s,eventRegistrationInit20123Res);
            res.add(eventRegistrationInit20123Res);
        }
        return res;
    }


    //查詢按鈕
    public List<EventRegistrationQueryRes> query(EventRegistrationQuery0123Req req){
        List<EventRegistrationQueryDAO> dao = eventRegistration0123Repository.query(req);
        List<EventRegistrationQueryRes> eventRegistrationQueryRes = new ArrayList<>();

        for (EventRegistrationQueryDAO s : dao){
            EventRegistrationQueryRes res = new EventRegistrationQueryRes();
            BeanUtils.copyProperties(s, res);
            eventRegistrationQueryRes.add(res);
        }
        return eventRegistrationQueryRes;
    }


//    //刪除
//    public int delete(ProductDel0122Req req) {
//        ProductDel0122DAO dao = new ProductDel0122DAO();
//        BeanUtils.copyProperties(req, dao);
//        return productRepository.deleteById(dao);
//    }
//

    //註冊按鈕
    public int insert(EventRegistrationIns0123Req req){
        EventRegistrationIns0123DAO dao = new EventRegistrationIns0123DAO();
        BeanUtils.copyProperties(req, dao);
        int rows = eventRegistration0123Repository.insert(dao);
        if (req.getMemberDetail() != null) {
            MemberDetail0123DAO detailDao = new MemberDetail0123DAO();
            BeanUtils.copyProperties(req.getMemberDetail(), detailDao);

            eventRegistration0123Repository.insert1(detailDao);
        }
        return rows;
    }

    //副表註冊按鈕
//    public int insert1(MemberDetail0123Req req){
//        MemberDetail0123DAO dao = new MemberDetail0123DAO();
//        BeanUtils.copyProperties(req, dao);
//        int rows = eventRegistration0123Repository.insert1(dao);
//        return rows;
//    }

//    //跳轉修改先查詢
//    public ProductUpdQuery0122Res updQuery(ProductUpdQuery0122Req req){
//        ProductUpdQuery0122DAO dao = productRepository.updQuery(req);
//        ProductUpdQuery0122Res res = new ProductUpdQuery0122Res();
//        BeanUtils.copyProperties(dao, res);
//        return res;
//    }
//
//    //修改
//    public int update(ProductUpd0122Req req){
//        ProductUpd0122DAO dao = new ProductUpd0122DAO();
//        BeanUtils.copyProperties(req, dao);
//        int rows = productRepository.update(dao);
//        return rows;
//    }


}
