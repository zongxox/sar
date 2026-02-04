package com.example.demo.service;

import com.example.demo.dao.*;
import com.example.demo.entity.TaskSchedule;
import com.example.demo.mapper.TaskSchedule0204Repository;
import com.example.demo.req.TaskScheduleDel0204Req;
import com.example.demo.req.TaskScheduleIns0204Req;
import com.example.demo.req.TaskScheduleQuery0204Req;
import com.example.demo.req.TaskScheduleUpd0204Req;
import com.example.demo.res.TaskSchedule0204Res;
import com.example.demo.res.TaskScheduleDon0204Res;
import com.example.demo.res.TaskScheduleQuery0204Res;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.BeanUtils;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


@Service
@AllArgsConstructor
public class TaskSchedule0204Service {

    private final TaskSchedule0204Repository taskSchedule0204Repository;

    //初始化查詢
    public List<TaskSchedule0204Res> init() {
        List<TaskSchedule0204DAO> init = taskSchedule0204Repository.init();
        List<TaskSchedule0204Res> res = new ArrayList<>();
        for (TaskSchedule0204DAO s : init) {
            TaskSchedule0204Res taskSchedule0204Res = new TaskSchedule0204Res();
            BeanUtils.copyProperties(s, taskSchedule0204Res);
            if ("doing".equals(taskSchedule0204Res.getStatus())) {
                taskSchedule0204Res.setStatusName("進行中");
            } else if ("pending".equals(taskSchedule0204Res.getStatus())) {
                taskSchedule0204Res.setStatusName("待處理");
            }
            res.add(taskSchedule0204Res);
        }
        return res;
    }

    //查詢按鈕
    public List<TaskScheduleQuery0204Res> query(TaskScheduleQuery0204Req req) {
        List<TaskScheduleQuery0204DAO> dao = taskSchedule0204Repository.query(req);
        List<TaskScheduleQuery0204Res> res = new ArrayList<>();
        for (TaskScheduleQuery0204DAO s : dao) {
            TaskScheduleQuery0204Res taskScheduleQueryRes0204 = new TaskScheduleQuery0204Res();
            BeanUtils.copyProperties(s, taskScheduleQueryRes0204);
            if ("doing".equals(taskScheduleQueryRes0204.getStatus())) {
                taskScheduleQueryRes0204.setStatusName("進行中");
            } else if ("pending".equals(taskScheduleQueryRes0204.getStatus())) {
                taskScheduleQueryRes0204.setStatusName("待處理");
            }
            res.add(taskScheduleQueryRes0204);
        }
        return res;
    }

    //刪除
    public int del(TaskScheduleDel0204Req req) {
        TaskScheduleDel0204DAO dao = TaskScheduleDel0204DAO.builder()
                .id(Long.valueOf(req.getId()))
                .build();
        int rows = taskSchedule0204Repository.deleteById(dao);
        return rows;
    }

    //    //新增
    public int insert(TaskScheduleIns0204Req req) {
        TaskScheduleIns0204DAO dao = new TaskScheduleIns0204DAO();
        BeanUtils.copyProperties(req, dao);
        TaskSchedule taskSchedule = TaskSchedule.builder()
                .title(dao.getTitle())
                .status(dao.getStatus())
                .type(dao.getType())
                .amount(Integer.valueOf(dao.getAmount()))
                .priority(Integer.valueOf(dao.getPriority()))
                .remark(dao.getRemark())
                .location(String.join(",",dao.getLocation()))
                .startTime(LocalDateTime.parse(dao.getStartTime()))
                .endTime(LocalDateTime.parse(dao.getEndTime()))
                .build();
        int rows = taskSchedule0204Repository.insert(taskSchedule);
        return rows;
    }


    //修改
    public int update(TaskScheduleUpd0204Req req) {
        TaskScheduleUpd0204DAO dao = TaskScheduleUpd0204DAO.builder()
                .id(Long.valueOf(req.getId()))
                .title(req.getTitle())
                .status(req.getStatus())
                .type(req.getType())
                .amount(Integer.valueOf(req.getAmount()))
                .priority(Integer.valueOf(req.getPriority()))
                .remark(req.getRemark())
                .location(String.join(",",req.getLocation()))
                .startTime(LocalDateTime.parse(req.getStartTime()))
                .endTime(LocalDateTime.parse(req.getEndTime()))
                .build();
        int rows = taskSchedule0204Repository.update(dao);
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

                    String title = getCell(row, 0, fmt);
                    String status = getCell(row, 1, fmt);
                    String type = getCell(row, 2, fmt);
                    String amounteStr = getCell(row, 3, fmt);
                    Integer amount = amounteStr == null ? null : Integer.valueOf(amounteStr);
                    String priorityStr = getCell(row, 4, fmt);
                    Integer priority = priorityStr == null ? null : Integer.valueOf(priorityStr);
                    String remark = getCell(row, 5, fmt);
                    String location = getCell(row, 6, fmt);
                    LocalDateTime startTime = getExcelDateTime(row, 7);
                    LocalDateTime endTime = getExcelDateTime(row, 8);


                    TaskSchedule dao = new TaskSchedule();
                    dao.setTitle(title);
                    dao.setStatus(status);
                    dao.setType(type);
                    dao.setAmount(amount);
                    dao.setPriority(priority);
                    dao.setRemark(remark);
                    dao.setLocation(location);
                    dao.setStartTime(startTime);
                    dao.setEndTime(endTime);
                    taskSchedule0204Repository.insert(dao);
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



    //下載
    public List<TaskScheduleDon0204Res> downloadList(TaskScheduleQuery0204Req req) {

        //2.呼叫 MyBatis / Mapper，依照 param 條件查詢資料庫
        List<TaskScheduleQuery0204DAO> list = taskSchedule0204Repository.query(req);

        //3.將查詢結果 DTO 轉成回傳用的 Response 物件 AdRes
        List<TaskScheduleDon0204Res> rows = new ArrayList<>();

        //逐筆處理查詢出來的資料
        for (TaskScheduleQuery0204DAO s : list) {

            //建立一筆回傳用的 AdRes 物件
            TaskScheduleDon0204Res r = new TaskScheduleDon0204Res();

            //將 DTO 中同名欄位的資料複製到 AdRes
            BeanUtils.copyProperties(s, r);

            //將轉換完成的物件加入回傳清單
            rows.add(r);
        }

        //4.回傳整理完成的資料清單（供 Excel 匯出使用）
        return rows;
    }

//
//    //讀jrxml檔案,編譯,產生報表物件
//// 讀jrxml檔案,編譯,產生報表物件（帶查詢結果）
//    public JasperPrint testLoadReport(ProducQuery0203Req req) throws Exception {
//
//        // 1) 查詢資料（你已經有 query(req)）
//        List<ProductQuery0203Res> rows = query(req);
//
//        System.out.println("rows size = " + rows.size());
//        if (!rows.isEmpty()) {
//            System.out.println("first row id=" + rows.get(0).getId() + ", name=" + rows.get(0).getName());
//        }
//
//
//        // 2) 讀 jrxml
//        InputStream is = this.getClass().getResourceAsStream("/reports/product0203.jrxml");
//        if (is == null) {
//            throw new RuntimeException("找不到 product0203.jrxml");
//        }
//
//        // 3) 編譯
//        JasperReport report = JasperCompileManager.compileReport(is);
//
//        // 4) 把查詢結果丟進 DataSource
//        JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(rows);
//
//        // 5) 產生報表
//        Map<String, Object> params = new HashMap<>();
//        JasperPrint print = JasperFillManager.fillReport(report, params, ds);
//
//        return print;
//    }





}
