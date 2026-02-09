package com.example.demo.service;

import com.example.demo.dao.*;
import com.example.demo.entity.Ticket;
import com.example.demo.mapper.Ticket0206Repository;
import com.example.demo.req.TicketDel0206Req;
import com.example.demo.req.TicketIns0206Req;
import com.example.demo.req.TicketQuery0206Req;
import com.example.demo.req.TicketUpd0206Req;
import com.example.demo.res.TicketInit0206Res;
import com.example.demo.res.TicketQuery0206Res;
import com.example.demo.res.TicketUpd0206Res;
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
public class Ticket0206Service {

    private final Ticket0206Repository ticket0206Repository;

    //初始化查詢
    public List<TicketInit0206Res> init() {
        List<TicketInit0206DAO> init = ticket0206Repository.init();
        List<TicketInit0206Res> res = new ArrayList<>();
        for (TicketInit0206DAO s : init) {
            TicketInit0206Res ticketInit0206Res = new TicketInit0206Res();
            BeanUtils.copyProperties(s, ticketInit0206Res);
            if ("NEW".equals(ticketInit0206Res.getStatus())) {
                ticketInit0206Res.setStatusName("新建");
            } else if ("IN_PROGRESS".equals(ticketInit0206Res.getStatus())) {
                ticketInit0206Res.setStatusName("處理中");
            } else if ("DONE".equals(ticketInit0206Res.getStatus())) {
                ticketInit0206Res.setStatusName("已完成");
            }
            res.add(ticketInit0206Res);
        }
        return res;
    }

    //查詢按鈕
    public List<TicketQuery0206Res> query(TicketQuery0206Req req) {
        List<TicketQuery0206DAO> dao = ticket0206Repository.query(req);
        List<TicketQuery0206Res> res = new ArrayList<>();
        for (TicketQuery0206DAO s : dao) {
            TicketQuery0206Res ticketQuery0206Res = new TicketQuery0206Res();
            BeanUtils.copyProperties(s, ticketQuery0206Res);
            if ("NEW".equals(ticketQuery0206Res.getStatus())) {
                ticketQuery0206Res.setStatusName("新建");
            } else if ("IN_PROGRESS".equals(ticketQuery0206Res.getStatus())) {
                ticketQuery0206Res.setStatusName("處理中");
            } else if ("DONE".equals(ticketQuery0206Res.getStatus())) {
                ticketQuery0206Res.setStatusName("已完成");
            }
            res.add(ticketQuery0206Res);
        }
        return res;
    }

    //刪除
    public int del(TicketDel0206Req req) {
        TicketDel0206DAO dao = TicketDel0206DAO.builder()
                .id(Long.valueOf(req.getId()))
                .build();
        int rows = ticket0206Repository.deleteById(dao);
        return rows;
    }

    //新增
    public int insert(TicketIns0206Req req) {
        TicketIns0206DAO dao = new TicketIns0206DAO();
        BeanUtils.copyProperties(req, dao);
        Ticket ticket = Ticket.builder()
                .userName(dao.getUserName())
                .title(dao.getTitle())
                .content(dao.getContent())
                .category(String.join(",",dao.getCategory()))
                .priority(Integer.valueOf(dao.getPriority()))
                .status(dao.getStatus())
                .contact(dao.getContact())
                .createdAt(LocalDateTime.parse(dao.getCreatedAt()))
                .updatedAt(LocalDateTime.parse(dao.getUpdatedAt()))
                .build();
        int rows = ticket0206Repository.insert(ticket);
        return rows;
    }


    //修改
    public int update(TicketUpd0206Req req) {

        TicketUpd0206DAO dao = TicketUpd0206DAO.builder()
                .id(Long.valueOf(req.getId()))
                .userName(req.getUserName())
                .title(req.getTitle())
                .content(req.getContent())
                .category(String.join(",",req.getCategory()))
                .priority(Integer.valueOf(req.getPriority()))
                .status(req.getStatus())
                .contact(req.getContact())
                .createdAt(LocalDateTime.parse(req.getCreatedAt()))
                .updatedAt(LocalDateTime.parse(req.getUpdatedAt()))
                .build();
        int rows = ticket0206Repository.update(dao);
        return rows;
    }




    //下載
    public List<TicketUpd0206Res> downloadList(TicketQuery0206Req req) {

        //2.呼叫 MyBatis / Mapper，依照 param 條件查詢資料庫
        List<TicketQuery0206DAO> list = ticket0206Repository.query(req);

        //3.將查詢結果 DTO 轉成回傳用的 Response 物件 AdRes
        List<TicketUpd0206Res> rows = new ArrayList<>();

        //逐筆處理查詢出來的資料
        for (TicketQuery0206DAO s : list) {

            //建立一筆回傳用的 AdRes 物件
            TicketUpd0206Res r = new TicketUpd0206Res();

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

                    String userName = getCell(row, 0, fmt);
                    String title = getCell(row, 1, fmt);
                    String content = getCell(row, 2, fmt);
                    String category = getCell(row, 3, fmt);
                    String priorityStr = getCell(row, 4, fmt);
                    Integer  priority = priorityStr == null ? null : Integer.valueOf(priorityStr);

                    String status = getCell(row, 5, fmt);
                    String contact = getCell(row, 6, fmt);
                    LocalDateTime createdAt = getExcelDateTime(row, 7);
                    LocalDateTime updatedAt = getExcelDateTime(row, 8);


                    Ticket dao = new Ticket();
                    dao.setUserName(userName);
                    dao.setTitle(title);
                    dao.setContent(content);
                    dao.setCategory(category);
                    dao.setPriority(priority);
                    dao.setStatus(status);
                    dao.setContact(contact);
                    dao.setCreatedAt(createdAt);
                    dao.setUserName(userName);
                    ticket0206Repository.insert(dao);
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
    public byte[] generatePdf(TicketQuery0206Req req) throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("HH:mm:ss");

        // 1) 查 DB：全部
        List<TicketQuery0206DAO> dao = ticket0206Repository.query(req);

        // 2) DAO -> Res
        List<TicketQuery0206Res> res = new ArrayList<>();
        for (TicketQuery0206DAO s : dao) {
            TicketQuery0206Res r = new TicketQuery0206Res();
            BeanUtils.copyProperties(s, r);

            r.setCreatedAtStr(s.getCreatedAt().format(formatter));
            r.setUpdatedAtStr(s.getUpdatedAt().format(formatter1));

            if ("NEW".equals(r.getStatus())) {
                r.setStatusName("新建");
            } else if ("IN_PROGRESS".equals(r.getStatus())) {
                r.setStatusName("處理中");
            } else if ("DONE".equals(r.getStatus())) {
                r.setStatusName("已完成");
            }

            res.add(r);
        }

        // 3) 依 location 排序（null last）
        Collections.sort(res, new Comparator<TicketQuery0206Res>() {
            @Override
            public int compare(TicketQuery0206Res a, TicketQuery0206Res b) {

                String la = a.getCategory();
                String lb = b.getCategory();

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

        for (TicketQuery0206Res r : res) {
            String loc = r.getCategory();

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
        ClassPathResource jrxmlResource = new ClassPathResource("reports/Ticket0206.jrxml");
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
