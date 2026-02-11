package com.example.demo.service;

import com.example.demo.dao.PrInit0212DAO;
import com.example.demo.dao.PrQuery0212DAO;
import com.example.demo.entity.PaymentRecord;
import com.example.demo.mapper.PaymentRecordMapper;
import com.example.demo.req.PrQuery0212Req;
import com.example.demo.res.PrInit0212Res;
import com.example.demo.res.PrQuery0212Res;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@AllArgsConstructor
public class PaymentRecord0212Service {

    private final PaymentRecordMapper paymentRecordMapper;

    //初始化查詢
    public List<PrInit0212Res> init() {
        List<PrInit0212DAO> init = paymentRecordMapper.init();
        List<PrInit0212Res> res = new ArrayList<>();
        for (PrInit0212DAO s : init) {
            PrInit0212Res prInit0212Res = new PrInit0212Res();
            BeanUtils.copyProperties(s, prInit0212Res);
            if ("PAID".equals(prInit0212Res.getStatus())) {
                prInit0212Res.setStatusName("已付款");
            } else if ("UNPAID".equals(prInit0212Res.getStatus())) {
                prInit0212Res.setStatusName("未付款");
            }else if ("CANCELLED".equals(prInit0212Res.getStatus())) {
                prInit0212Res.setStatusName("取消");
            }
            res.add(prInit0212Res);
        }
        return res;
    }

    //查詢按鈕
    public List<PrQuery0212Res> query(PrQuery0212Req req) {
        PrQuery0212DAO param = new PrQuery0212DAO();
        BeanUtils.copyProperties(req,param);
        List<PaymentRecord> dao = paymentRecordMapper.query(param);

        List<PrQuery0212Res> res = new ArrayList<>();
        for (PaymentRecord s : dao) {
            PrQuery0212Res prQuery0212Res = new PrQuery0212Res();
            BeanUtils.copyProperties(s, prQuery0212Res);
            if ("PAID".equals(prQuery0212Res.getStatus())) {
                prQuery0212Res.setStatusName("已付款");
            } else if ("UNPAID".equals(prQuery0212Res.getStatus())) {
                prQuery0212Res.setStatusName("未付款");
            }else if ("CANCELLED".equals(prQuery0212Res.getStatus())) {
                prQuery0212Res.setStatusName("取消");
            }
            res.add(prQuery0212Res);
        }
        return res;
    }

//    //刪除
//    public int del(OrderShipmentDel0210Req req) {
//        OrderShipmentDel0210DAO dao = OrderShipmentDel0210DAO.builder()
//                .id(Integer.valueOf(req.getId()))
//                .build();
//        int rows = orderShipment0210Mapper.deleteById(dao);
//        return rows;
//    }
//
//    //新增
//    public int insert(OrderShipmentIns0210Req req) {
//        OrderShipmentIns0210DAO dao = new OrderShipmentIns0210DAO();
//        BeanUtils.copyProperties(req, dao);
//        dao.setQuantity(String.join(",",req.getQuantity()));
//        //dao.setQuantity(req.getQuantity().get(0));
//        OrderShipment orderShipment = OrderShipment.builder()
//                .orderNo(dao.getOrderNo())
//                .customerName(dao.getCustomerName())
//                .productName(dao.getProductName())
//                .quantity(Integer.valueOf(dao.getQuantity()))
//                .totalPrice(new BigDecimal(dao.getTotalPrice()))
//                .shippingAddress(dao.getShippingAddress())
//                .status(dao.getStatus())
//                .shippedAt(LocalDateTime.parse(dao.getShippedAt()))
//                .createdAt(LocalDateTime.parse(dao.getCreatedAt()))
//                .build();
//        int rows = orderShipment0210Mapper.insert(orderShipment);
//        return rows;
//    }
//
//
//    //修改
//    public int update(OrderShipmentUpd0210Req req) {
//        OrderShipmentUpd0210DAO dao = new OrderShipmentUpd0210DAO();
//        //dao.setQuantity(req.getQuantity().get(0));
//        BeanUtils.copyProperties(req, dao);
//        dao.setQuantity(String.join(",",req.getQuantity()));
//        OrderShipment orderShipment = OrderShipment.builder()
//                .id(Integer.valueOf(dao.getId()))
//                .orderNo(dao.getOrderNo())
//                .customerName(dao.getCustomerName())
//                .productName(dao.getProductName())
//                .quantity(Integer.valueOf(dao.getQuantity()))
//                .totalPrice(new BigDecimal(dao.getTotalPrice()))
//                .shippingAddress(dao.getShippingAddress())
//                .status(dao.getStatus())
//                .shippedAt(LocalDateTime.parse(dao.getShippedAt()))
//                .createdAt(LocalDateTime.parse(dao.getCreatedAt()))
//                .build();
//        int rows = orderShipment0210Mapper.update(orderShipment);
//        return rows;
//    }
//
//
//
//
//    //下載
//    public List<OrderShipmentDon0210Res> downloadList(OrderShipmentQuery0210Req req) {
//
//        //2.呼叫 MyBatis / Mapper，依照 param 條件查詢資料庫
//        List<OrderShipmentQuery0210DAO> list = orderShipment0210Mapper.query(req);
//
//        //3.將查詢結果 DTO 轉成回傳用的 Response 物件 AdRes
//        List<OrderShipmentDon0210Res> rows = new ArrayList<>();
//
//        //逐筆處理查詢出來的資料
//        for (OrderShipmentQuery0210DAO s : list) {
//
//            //建立一筆回傳用的 AdRes 物件
//            OrderShipmentDon0210Res r = new OrderShipmentDon0210Res();
//
//            //將 DTO 中同名欄位的資料複製到 AdRes
//            BeanUtils.copyProperties(s, r);
//
//            //將轉換完成的物件加入回傳清單
//            rows.add(r);
//        }
//
//        //4.回傳整理完成的資料清單（供 Excel 匯出使用）
//        return rows;
//    }
//
//
//
//    //上傳
//    @Transactional(rollbackFor = Exception.class)
//    public void importExcel(MultipartFile file) {
//        if (file == null || file.isEmpty()) {
//            throw new RuntimeException("請選擇檔案");
//        }
//
//        String fileName = file.getOriginalFilename();
//        if (fileName == null || (!fileName.endsWith(".xlsx") && !fileName.endsWith(".xls"))) {
//            throw new RuntimeException("只支援 .xlsx 或 .xls");
//        }
//
//            try (InputStream is = file.getInputStream();
//                 Workbook workbook = WorkbookFactory.create(is)) {
//
//                Sheet sheet = workbook.getSheetAt(0);
//                DataFormatter fmt = new DataFormatter();
//
//                // 如果你 Excel 第0列是表頭，就從第1列開始讀
//                int startRow = 1;
//
//
//                for (int r = startRow; r <= sheet.getLastRowNum(); r++) {
//                    Row row = sheet.getRow(r);
//                    if (row == null) continue;
//                    if (isRowEmpty(row, fmt)) continue;
//
//                    String orderNo = getCell(row, 0, fmt);
//                    String customerName = getCell(row, 1, fmt);
//                    String productName = getCell(row, 2, fmt);
//                    String quantityStr = getCell(row, 3, fmt);
//                    Integer  quantity = quantityStr == null ? null : Integer.valueOf(quantityStr);
//                    String totalPriceStr = getCell(row, 4, fmt);
//                    BigDecimal totalPrice = (totalPriceStr == null || totalPriceStr.trim().isEmpty()) ? null : new BigDecimal(totalPriceStr);
//                    String shippingAddress = getCell(row, 5, fmt);
//                    String status = getCell(row, 6, fmt);
//                    LocalDateTime shippedAt = getExcelDateTime(row, 7);
//                    LocalDateTime createdAt = getExcelDateTime(row, 8);
//
//
//                    OrderShipment orderShipment = new OrderShipment();
//                    orderShipment.setOrderNo(orderNo);
//                    orderShipment.setCustomerName(customerName);
//                    orderShipment.setProductName(productName);
//                    orderShipment.setQuantity(quantity);
//                    orderShipment.setTotalPrice(totalPrice);
//                    orderShipment.setShippingAddress(shippingAddress);
//                    orderShipment.setStatus(status);
//                    orderShipment.setShippedAt(shippedAt);
//                    orderShipment.setCreatedAt(createdAt);
//                    orderShipment0210Mapper.insert(orderShipment);
//                }
//
//                //存檔路徑
//                Path uploadDir = Paths.get("C:/Users/aresc/Desktop/excel");
//                //存檔
//                    //資料夾不再就創建資料夾,相當於mkdirs
//                    if (!Files.exists(uploadDir)) {
//                        Files.createDirectories(uploadDir);
//                    }
//                    //取得原本檔案的副檔名
//                    String ext = fileName.substring(fileName.lastIndexOf('.'));
//                    //用 現在時間+副檔案名 當檔名
//                    String newName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"))+ ext;
//                    //組出完整存檔路徑
//                    Path savedFile = uploadDir.resolve(newName);
//
//                    Files.copy(file.getInputStream(), savedFile, StandardCopyOption.REPLACE_EXISTING);
//
//        } catch (Exception e) {
//            throw new RuntimeException("匯入失敗：" + e.getMessage(), e);
//        }
//    }
//
//    private LocalDateTime getExcelDateTime(Row row, int colIndex) {
//        Cell cell = row.getCell(colIndex);
//        if (cell == null) return null;
//
//        // 1. 真正 Excel 日期型
//        if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
//            Date d = cell.getDateCellValue();
//            return d.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
//        }
//
//        // 2. 公式結果是日期
//        if (cell.getCellType() == CellType.FORMULA
//                && cell.getCachedFormulaResultType() == CellType.NUMERIC
//                && DateUtil.isCellDateFormatted(cell)) {
//            Date d = cell.getDateCellValue();
//            return d.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
//        }
//
//        // 3. 其他狀況，一律當字串處理（最保險）
//        DataFormatter fmt = new DataFormatter();
//        String text = fmt.formatCellValue(cell);
//        if (text == null || text.trim().isEmpty()) return null;
//
//        return parseExcelDateTime(text);
//    }
//
//
//    private LocalDateTime parseExcelDateTime(String s) {
//        if (s == null) return null;
//
//        //  1) 清理字串
//        s = s.trim().replaceAll("\\s+", " ");
//        if (s.isEmpty()) return null;
//
//        //  2) 移除 AM/PM（避免 14:49:00 AM）
//        s = s.replace(" AM", "").replace(" PM", "");
//
//        List<DateTimeFormatter> formats = Arrays.asList(
//                // 先支援你目前錯的格式
//                DateTimeFormatter.ofPattern("yyyy/M/d H:mm:ss"),
//                DateTimeFormatter.ofPattern("yyyy/M/d H:mm"),
//
//                // 你原本的格式（保留）
//                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
//                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"),
//                DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"),
//                DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"),
//                DateTimeFormatter.ofPattern("M/d/yy H:mm"),
//                DateTimeFormatter.ofPattern("M/d/yy HH:mm"),
//                DateTimeFormatter.ofPattern("M/d/yyyy H:mm"),
//                DateTimeFormatter.ofPattern("M/d/yyyy HH:mm")
//        );
//
//        for (DateTimeFormatter f : formats) {
//            try {
//                return LocalDateTime.parse(s, f);
//            } catch (DateTimeParseException ignore) {
//            }
//        }
//
//        throw new RuntimeException("日期格式錯誤：" + s);
//    }
//
//
//    private String getCell(Row row, int colIndex, DataFormatter fmt) {
//        Cell cell = row.getCell(colIndex);
//        if (cell == null) return null;
//
//        String val = fmt.formatCellValue(cell).trim();
//        return val.isEmpty() ? null : val;
//    }
//
//    private boolean isRowEmpty(Row row, DataFormatter fmt) {
//        for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
//            Cell cell = row.getCell(c);
//            if (cell != null && !fmt.formatCellValue(cell).trim().isEmpty()) {
//                return false;
//            }
//        }
//        return true;
//    }
//
//
//    //pdf 依照欄位分組 顯示一頁
//    public byte[] generatePdf(OrderShipmentQuery0210Req req) throws Exception {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
//        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("HH:mm:ss");
//
//        // 1) 查 DB：全部
//        List<OrderShipmentQuery0210DAO> dao = orderShipment0210Mapper.query(req);
//
//        // 2) DAO -> Res
//        List<OrderShipmentPdf0210Res> res = new ArrayList<>();
//        for (OrderShipmentQuery0210DAO s : dao) {
//            OrderShipmentPdf0210Res orderShipmentPdf0210Res = new OrderShipmentPdf0210Res();
//            BeanUtils.copyProperties(s, orderShipmentPdf0210Res);
//
//            orderShipmentPdf0210Res.setCreatedAtStr(s.getShippedAt().format(formatter));
//            orderShipmentPdf0210Res.setShippedAtStr(s.getCreatedAt().format(formatter1));
//
//            if ("1".equals(orderShipmentPdf0210Res.getStatus())) {
//                orderShipmentPdf0210Res.setStatusName("上架");
//            } else if ("0".equals(orderShipmentPdf0210Res.getStatus())) {
//                orderShipmentPdf0210Res.setStatusName("下架");
//            }
//
//            res.add(orderShipmentPdf0210Res);
//        }
//
//        // 3) 依 location 排序（null last）
//        Collections.sort(res, new Comparator<OrderShipmentPdf0210Res>() {
//            @Override
//            public int compare(OrderShipmentPdf0210Res a, OrderShipmentPdf0210Res b) {
//
//                String la = a.getProductName();
//                String lb = b.getProductName();
//
//                if (la == null && lb == null) {
//                    return 0;
//                }
//
//                // null 排後面
//                if (la == null) {
//                    return 1;
//                }
//
//                if (lb == null) {
//                    return -1;
//                }
//
//                return la.compareTo(lb);
//            }
//        });
//
//        // 4) 同 location 同一個 pageGroup（location 變更 -> 換頁）
//        int pageIndex = 0;
//        String prevLoc = null;
//        boolean first = true;
//
//        for (OrderShipmentPdf0210Res r : res) {
//            String loc = r.getProductName();
//
//            if (first) {
//                first = false;
//                prevLoc = loc;
//                r.setPageGroup(pageIndex);
//                continue;
//            }
//
//            boolean changed;
//            if (prevLoc == null && loc == null) {
//                changed = false;
//            } else if (prevLoc == null || loc == null) {
//                changed = true;
//            } else {
//                changed = !prevLoc.equals(loc);
//            }
//
//            if (changed) {
//                pageIndex++;
//                prevLoc = loc;
//            }
//
//            r.setPageGroup(pageIndex);
//        }
//
//        // 5) Jasper
//        ClassPathResource jrxmlResource = new ClassPathResource("reports/OrderShipment0210.jrxml");
//        JasperReport jasperReport;
//        try (InputStream jrxmlStream = jrxmlResource.getInputStream()) {
//            jasperReport = JasperCompileManager.compileReport(jrxmlStream);
//        }
//
//        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(res);
//        Map<String, Object> params = new HashMap<>();
//
//        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSource);
//        return JasperExportManager.exportReportToPdf(jasperPrint);
//    }


}
