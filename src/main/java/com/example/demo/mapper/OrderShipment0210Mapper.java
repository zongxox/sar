package com.example.demo.mapper;

import com.example.demo.dao.OrderShipmentDel0210DAO;
import com.example.demo.dao.OrderShipmentInit0210DAO;
import com.example.demo.dao.OrderShipmentQuery0210DAO;
import com.example.demo.entity.OrderShipment;
import com.example.demo.req.OrderShipmentQuery0210Req;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderShipment0210Mapper {
    List<OrderShipmentInit0210DAO> init();

    List<OrderShipmentQuery0210DAO> query(OrderShipmentQuery0210Req req);

    int deleteById (OrderShipmentDel0210DAO dao);

    int insert(OrderShipment orderShipment);

    int update(OrderShipment orderShipment);
}
