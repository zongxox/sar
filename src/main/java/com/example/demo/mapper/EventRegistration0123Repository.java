package com.example.demo.mapper;

import com.example.demo.dao.*;
import com.example.demo.req.EventRegistrationQuery0123Req;
import com.example.demo.req.ProductUpdQuery0122Req;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class EventRegistration0123Repository {

    @Autowired
    private DataSource dataSource;

    /*init查詢*/
    public List<EventRegistrationInit0123DAO> init() {
        String sql =
                "SELECT  e.CODE, e.CONTENT FROM event_registration p " +
                "LEFT JOIN member_detail e ON e.TYPE='event_name' AND e.CODE = p.event_name ";

        List<EventRegistrationInit0123DAO> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = dataSource.getConnection();//連線數據庫
            ps = conn.prepareStatement(sql.toString());//把sql準備好之後,讓她變成一個物件

            rs = ps.executeQuery();//查詢出的結果
            while (rs.next()) {//將每一筆結果放到 dao
                EventRegistrationInit0123DAO dao = new EventRegistrationInit0123DAO();
                dao.setEventNameCode(rs.getString(1));
                dao.setEventNameContent(rs.getString(2));
                list.add(dao);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("查詢失敗: " + e.getMessage(), e);
        } finally {
            try {
                if (rs != null) rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (ps != null) ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /*init查詢*/
    public List<EventRegistrationInit0123DAO> init1() {
        String sql =
                "SELECT  e.CODE, e.CONTENT FROM event_registration p " +
                        "LEFT JOIN member_detail e ON e.TYPE='status_code' AND e.CODE = p.status_code ";

        List<EventRegistrationInit0123DAO> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = dataSource.getConnection();//連線數據庫
            ps = conn.prepareStatement(sql.toString());//把sql準備好之後,讓她變成一個物件

            rs = ps.executeQuery();//查詢出的結果
            while (rs.next()) {//將每一筆結果放到 dao
                EventRegistrationInit0123DAO dao = new EventRegistrationInit0123DAO();
                dao.setStatusCode(rs.getString(1));
                dao.setStatusContent(rs.getString(2));
                list.add(dao);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("查詢失敗: " + e.getMessage(), e);
        } finally {
            try {
                if (rs != null) rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (ps != null) ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return list;
    }


    public List<EventRegistrationInit0123DAO> init2() {
        String sql =
                "SELECT  e.CODE, e.CONTENT FROM event_registration p " +
                        "LEFT JOIN member_detail e ON e.TYPE='option_codes' AND e.CODE = p.option_codes ";

        List<EventRegistrationInit0123DAO> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = dataSource.getConnection();//連線數據庫
            ps = conn.prepareStatement(sql.toString());//把sql準備好之後,讓她變成一個物件

            rs = ps.executeQuery();//查詢出的結果
            while (rs.next()) {//將每一筆結果放到 dao
                EventRegistrationInit0123DAO dao = new EventRegistrationInit0123DAO();
                dao.setOptionCodesCode((rs.getString(1)));
                dao.setOptionCodesContent((rs.getString(2)));
                list.add(dao);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("查詢失敗: " + e.getMessage(), e);
        } finally {
            try {
                if (rs != null) rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (ps != null) ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return list;
    }




    /*查詢按鈕*/
    public List<EventRegistrationQueryDAO> query(EventRegistrationQuery0123Req req) {
                StringBuilder sql = new StringBuilder(
                "SELECT e.member_id, e.event_code, " +
                        "DATE_FORMAT(e.register_time,'%Y-%m-%d %H:%i:%s'), " +
                        "DATE_FORMAT(e.cancel_time,'%Y/%m/%d '), " +
                        "DATE_FORMAT(e.update_time,'%H:%i:%s'), " +
                        "c.CONTENT , d.CONTENT , e.phone, e.email, e.note, f.CONTENT " +
                        "FROM event_registration e " +
                        "LEFT JOIN member_detail c ON e.event_name = c.CODE AND c.TYPE='event_name' " +
                        "LEFT JOIN member_detail d ON e.status_code = d.CODE AND d.TYPE='status_code' " +
                        "LEFT JOIN member_detail f ON e.option_codes = f.CODE AND f.TYPE='option_codes' " +
                        "WHERE 1=1 "
        );



        List<Object> params = new ArrayList<>();


        if (req.getRegisterTime() != null && !req.getRegisterTime().trim().isEmpty()
                && req.getCancelTime() != null && !req.getCancelTime().trim().isEmpty()) {
            sql.append(" AND DATE_FORMAT(e.register_time,'%Y%m%d') BETWEEN ? AND ? ");
            params.add(req.getRegisterTime().trim()); // 起日 20260101
            params.add(req.getCancelTime().trim());   // 迄日 20260105
        }




        if (req.getStatusCode() != null && !req.getStatusCode().trim().isEmpty()) {
            sql.append(" AND e.status_code = ? ");
            params.add(req.getStatusCode().trim());
        }

        // id 如果前端傳遞過來 不是null 就將條件加入到sql
        if (req.getEventName()!= null && !req.getEventName().trim().isEmpty()) {
            sql.append(" AND e.event_name = ? ");
            params.add(req.getEventName().trim());
        }

        if (req.getOptionCodes() != null && !req.getOptionCodes().isEmpty()) {
            sql.append(" AND e.option_codes IN (");
            for (int i = 0; i < req.getOptionCodes().size(); i++) {
                sql.append("?");
                if (i < req.getOptionCodes().size() - 1) {
                    sql.append(",");
                }
                params.add(req.getOptionCodes().get(i).trim());
            }
            sql.append(") ");
        }

        sql.append(" ORDER BY e.member_id");

        List<EventRegistrationQueryDAO> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;


        try {
            conn = dataSource.getConnection();//連線數據庫
            ps = conn.prepareStatement(sql.toString());//把sql準備好之後,讓她變成一個物件

            // ps 塞參數（重點在這）
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            rs = ps.executeQuery();//查詢出的結果
            while (rs.next()) {//將每一筆結果放到 dao
                EventRegistrationQueryDAO dao = new EventRegistrationQueryDAO();
                dao.setMemberId(rs.getLong(1));
                dao.setEventCode(rs.getString(2));
                dao.setRegisterTime(rs.getString(3));
                dao.setCancelTime(rs.getString(4));
                dao.setUpdateTime(rs.getString(5));
                dao.setEventName(rs.getString(6));     // c.CONTENT
                dao.setStatusCode(rs.getString(7));    // d.CONTENT
                dao.setPhone(rs.getString(8));
                dao.setEmail(rs.getString(9));
                dao.setNote(rs.getString(10));
                dao.setOptionCodes(rs.getString(11));  // f.CONTENT
                list.add(dao);//把每一筆的res 循環結果放到list
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("查詢失敗: " + e.getMessage(), e);
        } finally {
            try {
                if (rs != null) rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (ps != null) ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

//    //刪除
//    public int deleteById(ProductDel0122DAO dao) {
//        String sql = "DELETE FROM product WHERE ID = ?";
//
//
//        try (Connection conn = dataSource.getConnection();
//             PreparedStatement ps = conn.prepareStatement(sql)) {
//
//            ps.setLong(1,dao.getId());
//            return ps.executeUpdate();
//
//        } catch (SQLException e) {
//            throw new RuntimeException("刪除失敗: " + e.getMessage(), e);
//        }
//    }



    //主表新增
    public int insert(EventRegistrationIns0123DAO dao) {
        String sql = "INSERT INTO event_registration(member_id, event_code, register_time,event_name, status_code, phone, email, note, option_codes, cancel_time,update_time)" +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";


        Connection conn = null;
        PreparedStatement ps = null;
        int rows = 0;

        try{
            conn = dataSource.getConnection();//連線數據庫
            ps = conn.prepareStatement(sql);
            ps.setLong(1, dao.getMemberId());
            ps.setString(2, dao.getEventCode());
            ps.setTimestamp(3, dao.getRegisterTime());
            ps.setString(4, dao.getEventName());
            ps.setString(5, dao.getStatusCode());
            ps.setString(6, dao.getPhone());
            ps.setString(7, dao.getEmail());
            ps.setString(8, dao.getNote());
            ps.setString(9, String.join(",", dao.getOptionCodes()));
            ps.setTimestamp(10, dao.getCancelTime());
            ps.setTimestamp(11, dao.getUpdateTime());
            rows = ps.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("新增失敗: " + e.getMessage(), e);
        }finally {
            try {
                if (ps != null) ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return rows;
    }


    //副表新增
    public int insert1(MemberDetail0123DAO dao) {

        String sql = "INSERT INTO member_detail(member_id,name,gender,phone,id_number,birth_date,address,hobby ) " +
                "VALUES (?, ?, ?, ?,?,?,?,?);";

        Connection conn = null;
        PreparedStatement ps = null;
        int rows = 0;

        try{
            conn = dataSource.getConnection();//連線數據庫
            ps = conn.prepareStatement(sql);
            ps.setLong(1,dao.getMemberId());
            ps.setString(2,dao.getName());
            ps.setString(3,dao.getGender());
            ps.setString(4,dao.getPhone());
            ps.setString(5,dao.getIdNumber());
            if (dao.getBirthDate() != null) {
                ps.setDate(6, new java.sql.Date(dao.getBirthDate().getTime()));
            } else {
                ps.setDate(6, null);
            }

            ps.setString(7,dao.getAddress());
            ps.setString(8,dao.getHobby());
            rows = ps.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("新增失敗: " + e.getMessage(), e);
        }finally {
            try {
                if (ps != null) ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return rows;
    }

    //跳轉修改先查詢
    public ProductUpdQuery0122DAO updQuery(ProductUpdQuery0122Req req) {
        String sql =
                "SELECT p.NAME, p.DESCRIPTION, p.PRICE, p.STOCK, d.CONTENT,e.CONTENT,p.SKU, " +
                 " c.CONTENT,p.CREATED_TIME, p.UPDATED_TIME " +
                 "FROM product p " +
                 "LEFT JOIN product_code c ON p.STATUS = c.CODE " +
                 "LEFT JOIN product_code d ON p.CATEGORY = d.CODE " +
                 "LEFT JOIN product_code e ON p.BRAND = e.CODE " +
                 "WHERE p.ID = ? ";



        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ProductUpdQuery0122DAO dao = new ProductUpdQuery0122DAO();

        try {
            conn = dataSource.getConnection();//連線數據庫
            ps = conn.prepareStatement(sql);//把sql準備好之後,讓她變成一個物件
            ps.setLong(1,req.getId());

            rs = ps.executeQuery();//查詢出的結果


            if (rs.next()) {
                dao.setName(rs.getString(1));
                dao.setDescription(rs.getString(2));
                dao.setPrice(rs.getInt(3));
                dao.setStock(rs.getInt(4));
                dao.setCategory(rs.getString(5));
                dao.setBrand(rs.getString(6));
                dao.setSku(rs.getString(7));
                dao.setStatus(rs.getString(8));
                dao.setCreatedTime(rs.getTimestamp(9).toLocalDateTime());
                dao.setUpdatedTime(rs.getTimestamp(10).toLocalDateTime());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (ps != null) ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        return dao;
    }

    //修改
    public int update(ProductUpd0122DAO dao) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE product SET ");

        int idx = 1;
        boolean hasSet = false;

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            // 動態 SET
            if (dao.getName() != null) {
                sql.append("NAME = ?");
                hasSet = true;
            }
            if (dao.getDescription() != null) {
                if (hasSet) sql.append(", ");
                sql.append("DESCRIPTION = ?");
                hasSet = true;
            }


            if (dao.getPrice() != null) {
                if (hasSet) sql.append(", ");
                sql.append("PRICE = ?");
                hasSet = true;
            }


            if (dao.getStock() != null) {
                if (hasSet) sql.append(", ");
                sql.append("STOCK = ?");
                hasSet = true;
            }

            if (dao.getCategory() != null) {
                if (hasSet) sql.append(", ");
                sql.append("CATEGORY = ?");
                hasSet = true;
            }

            if (dao.getBrand() != null) {
                if (hasSet) sql.append(", ");
                sql.append("BRAND = ?");
                hasSet = true;
            }
            if (dao.getSku() != null) {
                if (hasSet) sql.append(", ");
                sql.append("SKU = ?");
                hasSet = true;
            }

            if (dao.getStatus() != null) {
                if (hasSet) sql.append(", ");
                sql.append("STATUS = ?");
                hasSet = true;
            }

            if (dao.getCreatedTime() != null) {
                if (hasSet) sql.append(", ");
                sql.append("CREATED_TIME = ?");
                hasSet = true;
            }

            if (dao.getUpdatedTime() != null) {
                if (hasSet) sql.append(", ");
                sql.append("UPDATED_TIME = ?");
                hasSet = true;
            }

            if (!hasSet) throw new RuntimeException("沒有任何可更新欄位");


            // WHERE
            sql.append(" WHERE ID = ?");

            conn = dataSource.getConnection();
            ps = conn.prepareStatement(sql.toString());

            // 依序塞值（跟 SET 的順序一致）
            if (dao.getName() != null) ps.setString(idx++, dao.getName());
            if (dao.getDescription() != null) ps.setString(idx++, dao.getDescription());
            if (dao.getPrice()!= null) ps.setInt(idx++, dao.getPrice());
            if (dao.getStock() != null) ps.setInt(idx++, dao.getStock());

            if (dao.getCategory() != null) {
                String categoryStr = dao.getCategory().stream()
                        .filter(x -> x != null && !x.trim().isEmpty())
                        .map(String::trim)
                        .collect(java.util.stream.Collectors.joining(","));
                // post.category NOT NULL：至少給空字串
                ps.setString(idx++, categoryStr);
            }
            if (dao.getBrand() != null) ps.setString(idx++, dao.getBrand());
            if (dao.getSku() != null) ps.setString(idx++, dao.getSku());
            if (dao.getStatus() != null) ps.setString(idx++, dao.getStatus());

            if (dao.getCreatedTime() != null)ps.setTimestamp(idx++, Timestamp.valueOf(dao.getCreatedTime()));

            if (dao.getUpdatedTime() != null)ps.setTimestamp(idx++, Timestamp.valueOf(dao.getUpdatedTime()));

            //WHERE ID 不可為 null
            if (dao.getId() == null) throw new RuntimeException("ID 不可為空");
            ps.setLong(idx, dao.getId());

            return ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("修改失敗: " + e.getMessage(), e);
        } finally {
            try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }



}




