package com.example.demo.mapper;

import com.example.demo.dao.PrInit0212DAO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PaymentRecordMapper {
List<PrInit0212DAO> init();
}
