package com.example.demo.mapper;

import com.example.demo.dao.PrDel0212DAO;
import com.example.demo.dao.PrInit0212DAO;
import com.example.demo.dao.PrQuery0212DAO;
import com.example.demo.entity.PaymentRecord;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PaymentRecordMapper {
    List<PrInit0212DAO> init();

    List<PaymentRecord> query(PrQuery0212DAO dao);

    int delete(PrDel0212DAO dao);

    int insert(PaymentRecord paymentRecord);

    int update(PaymentRecord paymentRecord);
}
