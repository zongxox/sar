package com.example.demo.service;

import com.example.demo.dao.*;
import com.example.demo.entity.OrderShipment;
import com.example.demo.mapper.OrderShipment0205Repository;
import com.example.demo.req.OrderShipmentDel0205Req;
import com.example.demo.req.OrderShipmentIns0205Req;
import com.example.demo.req.OrderShipmentQuery0205Req;
import com.example.demo.req.OrderShipmentUpd0205Req;
import com.example.demo.res.*;
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
import java.math.BigDecimal;
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
public class OrderShipment0205Service {

    private final OrderShipment0205Repository orderShipment0205Repository;

    //初始化查詢
    public List<OrderShipmentInit0205Res> init() {
        List<OrderShipmentInit0205DAO> init = orderShipment0205Repository.init();
        List<OrderShipmentInit0205Res> res = new ArrayList<>();
        for (OrderShipmentInit0205DAO s : init) {
            OrderShipmentInit0205Res orderShipmentInit0205Res = new OrderShipmentInit0205Res();
            BeanUtils.copyProperties(s, orderShipmentInit0205Res);
            if ("1".equals(orderShipmentInit0205Res.getStatus())) {
                orderShipmentInit0205Res.setStatusName("上架");
            } else if ("0".equals(orderShipmentInit0205Res.getStatus())) {
                orderShipmentInit0205Res.setStatusName("下架");
            }
            res.add(orderShipmentInit0205Res);
        }
        return res;
    }

    //查詢按鈕
    public List<OrderShipmentQuery0205Res> query(OrderShipmentQuery0205Req req) {
        List<OrderShipmentQuery0205DAO> dao = orderShipment0205Repository.query(req);
        List<OrderShipmentQuery0205Res> res = new ArrayList<>();
        for (OrderShipmentQuery0205DAO s : dao) {
            OrderShipmentQuery0205Res orderShipmentQuery0205Res = new OrderShipmentQuery0205Res();
            BeanUtils.copyProperties(s, orderShipmentQuery0205Res);
            if ("1".equals(orderShipmentQuery0205Res.getStatus())) {
                orderShipmentQuery0205Res.setStatusName("上架");
            } else if ("0".equals(orderShipmentQuery0205Res.getStatus())) {
                orderShipmentQuery0205Res.setStatusName("下架");
            }
            res.add(orderShipmentQuery0205Res);
        }
        return res;
    }

    //刪除
    public int del(OrderShipmentDel0205Req req) {
        OrderShipmentDel0205DAO dao = OrderShipmentDel0205DAO.builder()
                .id(Integer.valueOf(req.getId()))
                .build();
        int rows = orderShipment0205Repository.deleteById(dao);
        return rows;
    }

    //新增
    public int insert(OrderShipmentIns0205Req req) {
        OrderShipmentIns0205DAO dao = new OrderShipmentIns0205DAO();
        BeanUtils.copyProperties(req, dao);
        OrderShipment orderShipment = OrderShipment.builder()
                .orderNo(dao.getOrderNo())
                .customerName(dao.getCustomerName())
                .productName(dao.getProductName())
                .quantity(Integer.valueOf(String.join(",", dao.getQuantity())))
                .totalPrice(new BigDecimal(dao.getTotalPrice()))
                .shippingAddress(dao.getShippingAddress())
                .status(dao.getStatus())
                .shippedAt(LocalDateTime.parse(dao.getShippedAt()))
                .createdAt(LocalDateTime.parse(dao.getCreatedAt()))
                .build();
        int rows = orderShipment0205Repository.insert(orderShipment);
        return rows;
    }


    //修改
    public int update(OrderShipmentUpd0205Req req) {
        Integer qty = Integer.valueOf(req.getQuantity().get(0));

        OrderShipmentUpd0205DAO dao = OrderShipmentUpd0205DAO.builder()
                .id(Integer.valueOf(req.getId()))
                .orderNo(req.getOrderNo())
                .customerName(req.getCustomerName())
                .productName(req.getProductName())
                .quantity(qty)
                .totalPrice(new BigDecimal(req.getTotalPrice()))
                .shippingAddress(req.getShippingAddress())
                .status(req.getStatus())
                .shippedAt(LocalDateTime.parse(req.getShippedAt()))
                .createdAt(LocalDateTime.parse(req.getCreatedAt()))
                .build();
        int rows = orderShipment0205Repository.update(dao);
        return rows;
    }




    //下載
    public List<OrderShipmentDon0205Res> downloadList(OrderShipmentQuery0205Req req) {

        //2.呼叫 MyBatis / Mapper，依照 param 條件查詢資料庫
        List<OrderShipmentQuery0205DAO> list = orderShipment0205Repository.query(req);

        //3.將查詢結果 DTO 轉成回傳用的 Response 物件 AdRes
        List<OrderShipmentDon0205Res> rows = new ArrayList<>();

        //逐筆處理查詢出來的資料
        for (OrderShipmentQuery0205DAO s : list) {

            //建立一筆回傳用的 AdRes 物件
            OrderShipmentDon0205Res r = new OrderShipmentDon0205Res();

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
                    String customerName = getCell(row, 1, fmt);
                    String productName = getCell(row, 2, fmt);
                    String quantityStr = getCell(row, 3, fmt);
                    Integer  quantity = quantityStr == null ? null : Integer.valueOf(quantityStr);
                    String totalPriceStr = getCell(row, 4, fmt);
                    BigDecimal totalPrice = (totalPriceStr == null || totalPriceStr.trim().isEmpty()) ? null : new BigDecimal(totalPriceStr);
                    String shippingAddress = getCell(row, 5, fmt);
                    String status = getCell(row, 6, fmt);
                    LocalDateTime shippedAt = getExcelDateTime(row, 7);
                    LocalDateTime createdAt = getExcelDateTime(row, 8);


                    OrderShipment dao = new OrderShipment();
                    dao.setOrderNo(orderNo);
                    dao.setCustomerName(customerName);
                    dao.setProductName(productName);
                    dao.setQuantity(quantity);
                    dao.setTotalPrice(totalPrice);
                    dao.setShippingAddress(shippingAddress);
                    dao.setStatus(status);
                    dao.setShippedAt(shippedAt);
                    dao.setCreatedAt(createdAt);
                    orderShipment0205Repository.insert(dao);
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

    private LocalDateTime getExcelDateTime(Row row, int colIndex) {
        Cell cell = row.getCell(colIndex);
        if (cell == null) return null;

        // 1. 真正 Excel 日期型
        if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
            Date d = cell.getDateCellValue();
            return d.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        }

        // 2. 公式結果是日期
        if (cell.getCellType() == CellType.FORMULA
                && cell.getCachedFormulaResultType() == CellType.NUMERIC
                && DateUtil.isCellDateFormatted(cell)) {
            Date d = cell.getDateCellValue();
            return d.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        }

        // 3. 其他狀況，一律當字串處理（最保險）
        DataFormatter fmt = new DataFormatter();
        String text = fmt.formatCellValue(cell);
        if (text == null || text.trim().isEmpty()) return null;

        return parseExcelDateTime(text);
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


    //pdf
    public byte[] generatePdf(OrderShipmentQuery0205Req req) throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern(" HH:mm:ss");
        // 1) 查 DB：全部
        List<OrderShipmentQuery0205DAO> dao = orderShipment0205Repository.query(req);

        //由小到大排序
        //拿a 跟 b 用字串比大小
        // 使用 Collections.sort 對 dao 這個 List 進行排序
        // 第二個參數是一個「比較規則」
        Collections.sort(dao, new Comparator<OrderShipmentQuery0205DAO>() {

            // 這個方法是排序時一定會被呼叫的比較方法
            // a、b 代表要拿來比較的兩筆資料
            @Override
            public int compare(OrderShipmentQuery0205DAO a, OrderShipmentQuery0205DAO b) {

                // 如果 a 的 location 跟 b 的 location 都是 null
                // 代表兩筆資料在排序上視為一樣
                if (a.getProductName() == null && b.getProductName() == null) {
                    return 0;
                }

                // 如果只有 a 的 location 是 null
                // 規則：null 排在後面
                if (a.getProductName() == null) {
                    return 1;   // 回傳正數，表示 a 排在 b 後面
                }

                // 如果只有 b 的 location 是 null
                // 規則：null 排在後面
                if (b.getProductName() == null) {
                    return -1;  // 回傳負數，表示 a 排在 b 前面
                }

                // 兩筆的 location 都不是 null
                // 直接用字串的 compareTo 來比較大小
                return a.getProductName().compareTo(b.getProductName());
            }
        });


        List<OrderShipmentPdf0205Res> res = new ArrayList<>();
        for(OrderShipmentQuery0205DAO s : dao){
            OrderShipmentPdf0205Res orderShipmentPdf0205Res = new OrderShipmentPdf0205Res();
            BeanUtils.copyProperties(s,orderShipmentPdf0205Res);
            orderShipmentPdf0205Res.setShippedAtStr(s.getShippedAt().format(formatter));
            orderShipmentPdf0205Res.setCreatedAtStr(s.getCreatedAt().format(formatter1));
            if ("1".equals(orderShipmentPdf0205Res.getStatus())) {
                orderShipmentPdf0205Res.setStatus("進行中");
            } else if ("0".equals(orderShipmentPdf0205Res.getStatus())) {
                orderShipmentPdf0205Res.setStatus("待處理");
            }

            res.add(orderShipmentPdf0205Res);
        }



        // 2) 讀 jrxml（路徑依你實際放的位置調整）
        // 例如你放在 src/main/resources/reports/taskSchedule0204.jrxml
        ClassPathResource jrxmlResource = new ClassPathResource("reports/OrderShipment0205.jrxml");
        // 若你是放 src/main/resources/taskSchedule0204.jrxml，改成：
        // ClassPathResource jrxmlResource = new ClassPathResource("taskSchedule0204.jrxml");

        JasperReport jasperReport;
        try (InputStream jrxmlStream = jrxmlResource.getInputStream()) {
            jasperReport = JasperCompileManager.compileReport(jrxmlStream);
        }

        // 3 將查詢到的數據放到dataSource裡面
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(res);

        Map<String, Object> params = new HashMap<>();
        // params.put("xxx", "yyy"); // 若你 jrxml 有 parameters 才需要放

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSource);

        // 4) 產生 PDF bytes
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

}
