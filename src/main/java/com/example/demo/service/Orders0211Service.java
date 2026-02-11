package com.example.demo.service;

import com.example.demo.dao.*;
import com.example.demo.entity.Orders;
import com.example.demo.mapper.OrdersMapper;
import com.example.demo.req.OrdersDel0211Req;
import com.example.demo.req.OrdersIns0211Req;
import com.example.demo.req.OrdersQuery0211Req;
import com.example.demo.req.OrdersUpd0211Req;
import com.example.demo.res.OrdersDon0211Res;
import com.example.demo.res.OrdersInit0211Res;
import com.example.demo.res.OrdersPdf0211Res;
import com.example.demo.res.OrdersQuery0211Res;
import lombok.AllArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.BeanUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;


@Service
@AllArgsConstructor
public class Orders0211Service {

    private final OrdersMapper ordersMapper;

    //初始化查詢
    public List<OrdersInit0211Res> init() {
        List<OrdersInit0211DAO> init = ordersMapper.init();
        List<OrdersInit0211Res> res = new ArrayList<>();
        for (OrdersInit0211DAO s : init) {
            OrdersInit0211Res ordersInit0211Res = new OrdersInit0211Res();
            BeanUtils.copyProperties(s, ordersInit0211Res);
            if ("paid".equals(ordersInit0211Res.getStatus())) {
                ordersInit0211Res.setStatusName("已付款");
            } else if ("pending".equals(ordersInit0211Res.getStatus())) {
                ordersInit0211Res.setStatusName("待付款");
            }else if ("cancel".equals(ordersInit0211Res.getStatus())) {
                ordersInit0211Res.setStatusName("已取消");
            }else if ("refund".equals(ordersInit0211Res.getStatus())) {
                ordersInit0211Res.setStatusName("退款中");
            }
            res.add(ordersInit0211Res);
        }
        return res;
    }

    //查詢按鈕
    public List<OrdersQuery0211Res> query(OrdersQuery0211Req req) {
        OrdersQuery0211DAO param = new OrdersQuery0211DAO();
        BeanUtils.copyProperties(req, param);
        List<Orders> dao = ordersMapper.query(param);
        List<OrdersQuery0211Res> res = new ArrayList<>();
        for (Orders s : dao) {
            OrdersQuery0211Res ordersQuery0211Res = new OrdersQuery0211Res();
            BeanUtils.copyProperties(s, ordersQuery0211Res);
            if ("paid".equals(ordersQuery0211Res.getStatus())) {
                ordersQuery0211Res.setStatusName("已付款");
            } else if ("pending".equals(ordersQuery0211Res.getStatus())) {
                ordersQuery0211Res.setStatusName("待付款");
            }else if ("cancel".equals(ordersQuery0211Res.getStatus())) {
                ordersQuery0211Res.setStatusName("已取消");
            }else if ("refund".equals(ordersQuery0211Res.getStatus())) {
                ordersQuery0211Res.setStatusName("退款中");
            }

            res.add(ordersQuery0211Res);
        }
        return res;
    }

    //刪除
    public int del(OrdersDel0211Req req) {
        OrdersDel0211DAO dao = OrdersDel0211DAO.builder()
                .id(Long.valueOf(req.getId()))
                .build();
        int rows = ordersMapper.delete(dao);
        return rows;
    }

    //新增
    public int insert(OrdersIns0211Req req) {
        OrdersIns0211DAO dao =  OrdersIns0211DAO.builder()
                .orderNo(req.getOrderNo())
                .amount(Integer.valueOf(req.getAmount()))
                .status(req.getStatus())
                .remark(req.getRemark())
                .orderType(String.join(",",req.getOrderType()))
                .source(req.getSource())
                .customer(req.getCustomer())
                .createdAt(
                Date.from(LocalDateTime.parse(req.getCreatedAt())
                                .atZone(ZoneId.systemDefault())
                                .toInstant()))
                .updatedAt(Date.from(LocalDateTime.parse(req.getUpdatedAt())
                                .atZone(ZoneId.systemDefault())
                                .toInstant()))
                .build();
        Orders orders = new Orders();
        BeanUtils.copyProperties(dao, orders);
        int rows = ordersMapper.insert1(orders);
        return rows;
    }


    //修改
    public int update(OrdersUpd0211Req req) {
        //dao.setQuantity(req.getQuantity().get(0));
        OrdersUpd0211DAO dao =  OrdersUpd0211DAO.builder()
                .id(Long.valueOf(req.getId()))
                .orderNo(req.getOrderNo())
                .amount(Integer.valueOf(req.getAmount()))
                .status(req.getStatus())
                .remark(req.getRemark())
                .orderType(String.join(",",req.getOrderType()))
                .source(req.getSource())
                .customer(req.getCustomer())
                .createdAt(
                        Date.from(LocalDateTime.parse(req.getCreatedAt())
                                .atZone(ZoneId.systemDefault())
                                .toInstant()))
                .updatedAt(Date.from(LocalDateTime.parse(req.getUpdatedAt())
                        .atZone(ZoneId.systemDefault())
                        .toInstant()))
                .build();
        Orders orders = new Orders();
        BeanUtils.copyProperties(dao, orders);
        int rows = ordersMapper.update(orders);
        return rows;
    }




    //下載
    public List<OrdersDon0211Res> downloadList(OrdersQuery0211Req req) {
        OrdersQuery0211DAO param = new OrdersQuery0211DAO();
        BeanUtils.copyProperties(req, param);
        //2.呼叫 MyBatis / Mapper，依照 param 條件查詢資料庫
        List<Orders> list = ordersMapper.query(param);

        //3.將查詢結果 DTO 轉成回傳用的 Response 物件 AdRes
        List<OrdersDon0211Res> rows = new ArrayList<>();

        //逐筆處理查詢出來的資料
        for (Orders s : list) {

            //建立一筆回傳用的 AdRes 物件
            OrdersDon0211Res r = new OrdersDon0211Res();

            //將 DTO 中同名欄位的資料複製到 AdRes
            BeanUtils.copyProperties(s, r);

            //將轉換完成的物件加入回傳清單
            rows.add(r);
        }

        //4.回傳整理完成的資料清單（供 Excel 匯出使用）
        return rows;
    }



    //上傳
    @Transactional(rollbackFor = Exception.class)
    public void importExcel(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("請選擇檔案");
        }

        String fileName = file.getOriginalFilename();
        if (fileName == null || (!fileName.endsWith(".xlsx") && !fileName.endsWith(".xls"))) {
            throw new RuntimeException("只支援 .xlsx 或 .xls");
        }

            try (InputStream is = file.getInputStream();
                 Workbook workbook = WorkbookFactory.create(is)) {

                Sheet sheet = workbook.getSheetAt(0);
                DataFormatter fmt = new DataFormatter();

                // 如果你 Excel 第0列是表頭，就從第1列開始讀
                int startRow = 1;


                for (int r = startRow; r <= sheet.getLastRowNum(); r++) {
                    Row row = sheet.getRow(r);
                    if (row == null) continue;
                    if (isRowEmpty(row, fmt)) continue;

                    String orderNo = getCell(row, 0, fmt);
                    String amountStr = getCell(row, 1, fmt);
                    Integer  amount = amountStr == null ? null : Integer.valueOf(amountStr);
                    String status = getCell(row, 2, fmt);
                    String remark = getCell(row, 3, fmt);
                    String orderType = getCell(row, 4, fmt);
                    String source = getCell(row, 5, fmt);
                    String customer = getCell(row, 6, fmt);
                    Date createdAt = getExcelDateTime(row, 7);
                    Date updatedAt = getExcelDateTime(row, 8);


                    Orders orders = new Orders();
                    orders.setOrderNo(orderNo);
                    orders.setAmount(amount);
                    orders.setStatus(status);
                    orders.setRemark(remark);
                    orders.setOrderType(orderType);
                    orders.setSource(source);
                    orders.setCustomer(customer);
                    orders.setCreatedAt(createdAt);
                    orders.setUpdatedAt(updatedAt);
                    ordersMapper.insert1(orders);
                }

                //存檔路徑
                Path uploadDir = Paths.get("C:/Users/aresc/Desktop/excel");
                //存檔
                    //資料夾不再就創建資料夾,相當於mkdirs
                    if (!Files.exists(uploadDir)) {
                        Files.createDirectories(uploadDir);
                    }
                    //取得原本檔案的副檔名
                    String ext = fileName.substring(fileName.lastIndexOf('.'));
                    //用 現在時間+副檔案名 當檔名
                    String newName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"))+ ext;
                    //組出完整存檔路徑
                    Path savedFile = uploadDir.resolve(newName);

                    Files.copy(file.getInputStream(), savedFile, StandardCopyOption.REPLACE_EXISTING);

        } catch (Exception e) {
            throw new RuntimeException("匯入失敗：" + e.getMessage(), e);
        }
    }

    private Date getExcelDateTime(Row row, int colIndex) {

        Cell cell = row.getCell(colIndex);
        if (cell == null) return null;

        // 1. Excel 原生日期
        if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
            return cell.getDateCellValue();   // 直接回 Date
        }

        // 2. 公式結果是日期
        if (cell.getCellType() == CellType.FORMULA
                && cell.getCachedFormulaResultType() == CellType.NUMERIC
                && DateUtil.isCellDateFormatted(cell)) {
            return cell.getDateCellValue();
        }

        // 3. 當字串處理
        DataFormatter fmt = new DataFormatter();
        String text = fmt.formatCellValue(cell);
        if (text == null || text.trim().isEmpty()) return null;

        LocalDateTime ldt = parseExcelDateTime(text);

        if (ldt == null) return null;

        // 明確指定台北時區轉回 Date
        return Date.from(ldt.atZone(ZoneId.of("Asia/Taipei")).toInstant());
    }


    private LocalDateTime parseExcelDateTime(String s) {
        if (s == null) return null;

        //  1) 清理字串
        s = s.trim().replaceAll("\\s+", " ");
        if (s.isEmpty()) return null;

        //  2) 移除 AM/PM（避免 14:49:00 AM）
        s = s.replace(" AM", "").replace(" PM", "");

        List<DateTimeFormatter> formats = Arrays.asList(
                // 先支援你目前錯的格式
                DateTimeFormatter.ofPattern("yyyy/M/d H:mm:ss"),
                DateTimeFormatter.ofPattern("yyyy/M/d H:mm"),

                // 你原本的格式（保留）
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"),
                DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"),
                DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"),
                DateTimeFormatter.ofPattern("M/d/yy H:mm"),
                DateTimeFormatter.ofPattern("M/d/yy HH:mm"),
                DateTimeFormatter.ofPattern("M/d/yyyy H:mm"),
                DateTimeFormatter.ofPattern("M/d/yyyy HH:mm")
        );

        for (DateTimeFormatter f : formats) {
            try {
                return LocalDateTime.parse(s, f);
            } catch (DateTimeParseException ignore) {
            }
        }

        throw new RuntimeException("日期格式錯誤：" + s);
    }


    private String getCell(Row row, int colIndex, DataFormatter fmt) {
        Cell cell = row.getCell(colIndex);
        if (cell == null) return null;

        String val = fmt.formatCellValue(cell).trim();
        return val.isEmpty() ? null : val;
    }

    private boolean isRowEmpty(Row row, DataFormatter fmt) {
        for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
            Cell cell = row.getCell(c);
            if (cell != null && !fmt.formatCellValue(cell).trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }


    //pdf 依照欄位分組 顯示一頁
    public byte[] generatePdf(OrdersQuery0211Req req) throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("HH:mm:ss");

        OrdersQuery0211DAO param = new OrdersQuery0211DAO();
        BeanUtils.copyProperties(req, param);

        // 1) 查 DB：全部
        List<Orders> dao = ordersMapper.query(param);

        // 2) DAO -> Res
        List<OrdersPdf0211Res> res = new ArrayList<>();
        for (Orders s : dao) {
            OrdersPdf0211Res ordersPdf0211Res = new OrdersPdf0211Res();
            BeanUtils.copyProperties(s, ordersPdf0211Res);
            Date createdAt = ordersPdf0211Res.getCreatedAt();
            ordersPdf0211Res.setCreatedAtStr(
                    createdAt == null ? "" :
                            createdAt.toInstant()
                                    .atZone(ZoneId.of("Asia/Taipei"))
                                    .toLocalDateTime()
                                    .format(formatter)
            );
            Date updatedAt = ordersPdf0211Res.getCreatedAt();
            ordersPdf0211Res.setUpdatedAtStr(
                    createdAt == null ? "" :
                            createdAt.toInstant()
                                    .atZone(ZoneId.of("Asia/Taipei"))
                                    .toLocalDateTime()
                                    .format(formatter1)
            );

            if ("paid".equals(ordersPdf0211Res.getStatus())) {
                ordersPdf0211Res.setStatusName("已付款");
            } else if ("pending".equals(ordersPdf0211Res.getStatus())) {
                ordersPdf0211Res.setStatusName("待付款");
            }else if ("cancel".equals(ordersPdf0211Res.getStatus())) {
                ordersPdf0211Res.setStatusName("已取消");
            }else if ("refund".equals(ordersPdf0211Res.getStatus())) {
                ordersPdf0211Res.setStatusName("退款中");
            }

            res.add(ordersPdf0211Res);
        }

        // 3) 依 location 排序（null last）
        Collections.sort(res, new Comparator<OrdersPdf0211Res>() {
            @Override
            public int compare(OrdersPdf0211Res a, OrdersPdf0211Res b) {

                String la = a.getRemark();
                String lb = b.getRemark();

                if (la == null && lb == null) {
                    return 0;
                }

                // null 排後面
                if (la == null) {
                    return 1;
                }

                if (lb == null) {
                    return -1;
                }

                return la.compareTo(lb);
            }
        });

        // 4) 同 location 同一個 pageGroup（location 變更 -> 換頁）
        int pageIndex = 0;
        String prevLoc = null;
        boolean first = true;

        for (OrdersPdf0211Res r : res) {
            String loc = r.getRemark();

            if (first) {
                first = false;
                prevLoc = loc;
                r.setPageGroup(pageIndex);
                continue;
            }

            boolean changed;
            if (prevLoc == null && loc == null) {
                changed = false;
            } else if (prevLoc == null || loc == null) {
                changed = true;
            } else {
                changed = !prevLoc.equals(loc);
            }

            if (changed) {
                pageIndex++;
                prevLoc = loc;
            }

            r.setPageGroup(pageIndex);
        }

        // 5) Jasper
        ClassPathResource jrxmlResource = new ClassPathResource("reports/Orders0211.jrxml");
        JasperReport jasperReport;
        try (InputStream jrxmlStream = jrxmlResource.getInputStream()) {
            jasperReport = JasperCompileManager.compileReport(jrxmlStream);
        }

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(res);
        Map<String, Object> params = new HashMap<>();

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSource);
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }


}
