package com.example.demo.service;

import com.example.demo.dao.*;
import com.example.demo.entity.PaymentRecord;
import com.example.demo.mapper.PaymentRecordMapper;
import com.example.demo.req.PrDel0212Req;
import com.example.demo.req.PrIns0212Req;
import com.example.demo.req.PrQuery0212Req;
import com.example.demo.req.PrUpd0212Req;
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
            } else if ("CANCELLED".equals(prInit0212Res.getStatus())) {
                prInit0212Res.setStatusName("取消");
            }
            res.add(prInit0212Res);
        }
        return res;
    }

    //查詢按鈕
    public List<PrQuery0212Res> query(PrQuery0212Req req) {
        PrQuery0212DAO param = new PrQuery0212DAO();
        BeanUtils.copyProperties(req, param);
        List<PaymentRecord> dao = paymentRecordMapper.query(param);

        List<PrQuery0212Res> res = new ArrayList<>();
        for (PaymentRecord s : dao) {
            PrQuery0212Res prQuery0212Res = new PrQuery0212Res();
            BeanUtils.copyProperties(s, prQuery0212Res);
            if ("PAID".equals(prQuery0212Res.getStatus())) {
                prQuery0212Res.setStatusName("已付款");
            } else if ("UNPAID".equals(prQuery0212Res.getStatus())) {
                prQuery0212Res.setStatusName("未付款");
            } else if ("CANCELLED".equals(prQuery0212Res.getStatus())) {
                prQuery0212Res.setStatusName("取消");
            }
            res.add(prQuery0212Res);
        }
        return res;
    }

    //刪除
    public int del(PrDel0212Req req) {
        PrDel0212DAO dao = PrDel0212DAO.builder()
                .id(Long.valueOf(req.getId()))
                .build();
        int rows = paymentRecordMapper.delete(dao);
        return rows;
    }

    //新增
    public int insert(PrIns0212Req req) {
        PrIns0212DAO dao = new PrIns0212DAO();
        BeanUtils.copyProperties(req, dao);
        //dao.setPayMethod(String.join(",", req.getPayMethod()));
        //dao.setQuantity(req.getQuantity().get(0));
        PaymentRecord paymentRecord = PaymentRecord.builder()
                .receiptNo(dao.getReceiptNo())
                .payerName(dao.getPayerName())
                .payeeName(dao.getPayeeName())
                .amount(Integer.valueOf(dao.getAmount()))
                .status(dao.getStatus())
                .payMethod(String.join(",",dao.getPayMethod()))
                .source(dao.getSource())
                .createTime(LocalDateTime.parse(dao.getCreateTime()))
                .updateTime(LocalDateTime.parse(dao.getUpdateTime()))
                .build();
        int rows = paymentRecordMapper.insert(paymentRecord);
        return rows;
    }


    //修改
    public int update(PrUpd0212Req req) {
        PrUpd0212DAO dao = new PrUpd0212DAO();
        BeanUtils.copyProperties(req, dao);
        PaymentRecord paymentRecord = PaymentRecord.builder()
                .id(Long.valueOf(dao.getId()))
                .receiptNo(dao.getReceiptNo())
                .payerName(dao.getPayerName())
                .payeeName(dao.getPayeeName())
                .amount(Integer.valueOf(dao.getAmount()))
                .status(dao.getStatus())
                .payMethod(String.join(",",dao.getPayMethod()))
                .source(dao.getSource())
                .createTime(LocalDateTime.parse(dao.getCreateTime()))
                .updateTime(LocalDateTime.parse(dao.getUpdateTime()))
                .build();
        int rows = paymentRecordMapper.update(paymentRecord);
        return rows;
    }




    //下載
    public List<PrDon0212Res> downloadList(PrQuery0212Req req) {
        PrQuery0212DAO param = new PrQuery0212DAO();
        BeanUtils.copyProperties(req, param);
        //2.呼叫 MyBatis / Mapper，依照 param 條件查詢資料庫
        List<PaymentRecord> list = paymentRecordMapper.query(param);

        //3.將查詢結果 DTO 轉成回傳用的 Response 物件 AdRes
        List<PrDon0212Res> rows = new ArrayList<>();

        //逐筆處理查詢出來的資料
        for (PaymentRecord s : list) {

            //建立一筆回傳用的 AdRes 物件
            PrDon0212Res r = new PrDon0212Res();

            //將 DTO 中同名欄位的資料複製到 AdRes
            BeanUtils.copyProperties(s, r);

//            if ("PAID".equals(r.getStatus())) {
//                r.setStatusName("已付款");
//            } else if ("UNPAID".equals(r.getStatus())) {
//                r.setStatusName("未付款");
//            } else if ("CANCELLED".equals(r.getStatus())) {
//                r.setStatusName("取消");
//            }

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

                    String receiptNo = getCell(row, 0, fmt);
                    String payerName = getCell(row, 1, fmt);
                    String payeeName = getCell(row, 2, fmt);
                    String amountStr = getCell(row, 3, fmt);
                    Integer  amount = amountStr == null ? null : Integer.valueOf(amountStr);

                    String status = getCell(row, 4, fmt);
                    String payMethod = getCell(row, 5, fmt);
                    String source = getCell(row, 6, fmt);

                    LocalDateTime createTime = getExcelDateTime(row, 7);
                    LocalDateTime updateTime = getExcelDateTime(row, 8);


                    PaymentRecord paymentRecord = new PaymentRecord();
                    paymentRecord.setReceiptNo(receiptNo);
                    paymentRecord.setPayerName(payerName);
                    paymentRecord.setPayeeName(payeeName);
                    paymentRecord.setAmount(amount);
                    paymentRecord.setStatus(status);
                    paymentRecord.setPayMethod(payMethod);
                    paymentRecord.setSource(source);
                    paymentRecord.setCreateTime(createTime);
                    paymentRecord.setUpdateTime(updateTime);
                    paymentRecordMapper.insert(paymentRecord);
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


    //pdf 依照欄位分組 顯示一頁
    public byte[] generatePdf(PrQuery0212Req req) throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("HH:mm:ss");

        PrQuery0212DAO param = new PrQuery0212DAO();
        BeanUtils.copyProperties(req, param);

        // 1) 查 DB：全部
        List<PaymentRecord> dao = paymentRecordMapper.query(param);

        // 2) DAO -> Res
        List<PrPdf0212Res> res = new ArrayList<>();
        for (PaymentRecord s : dao) {
            PrPdf0212Res prPdf0212Res = new PrPdf0212Res();
            BeanUtils.copyProperties(s, prPdf0212Res);
            prPdf0212Res.setCreateTimeStr(s.getCreateTime().format(formatter));
            prPdf0212Res.setUpdateTimeStr(s.getUpdateTime().format(formatter1));

            if ("PAID".equals(prPdf0212Res.getStatus())) {
                prPdf0212Res.setStatusName("已付款");
            } else if ("UNPAID".equals(prPdf0212Res.getStatus())) {
                prPdf0212Res.setStatusName("未付款");
            } else if ("CANCELLED".equals(prPdf0212Res.getStatus())) {
                prPdf0212Res.setStatusName("取消");
            }

            res.add(prPdf0212Res);
        }

        // 3) 依 location 排序（null last）
        Collections.sort(res, new Comparator<PrPdf0212Res>() {
            @Override
            public int compare(PrPdf0212Res a, PrPdf0212Res b) {

                String la = a.getPayMethod();
                String lb = b.getPayMethod();

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

        for (PrPdf0212Res r : res) {
            String loc = r.getPayMethod();

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
        ClassPathResource jrxmlResource = new ClassPathResource("reports/PaymentRecord0212.jrxml");
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
