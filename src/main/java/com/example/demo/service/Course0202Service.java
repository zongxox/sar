package com.example.demo.service;

import com.example.demo.dao.*;
import com.example.demo.entity.Course;
import com.example.demo.mapper.Course0202Repository;
import com.example.demo.req.CourseDel0202Req;
import com.example.demo.req.CourseIns0202Req;
import com.example.demo.req.CourseQuery0202Req;
import com.example.demo.req.CourseUpd0202Req;
import com.example.demo.res.CourseDon0202Res;
import com.example.demo.res.CourseInit0202Res;
import com.example.demo.res.CourseQuery0202Res;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
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
public class Course0202Service {

    private final Course0202Repository course0202Repository;

    //初始化查詢
    public List<CourseInit0202Res> init() {
        List<CourseInit0202DAO> init = course0202Repository.init();
        List<CourseInit0202Res> res = new ArrayList<>();
        for (CourseInit0202DAO s : init) {
            CourseInit0202Res courseInit0202Res = new CourseInit0202Res();
            BeanUtils.copyProperties(s, courseInit0202Res);
            if ("1".equals(courseInit0202Res.getIsPublished())) {
                courseInit0202Res.setIsPublishedName("上架");
            } else if ("0".equals(courseInit0202Res.getIsPublished())) {
                courseInit0202Res.setIsPublishedName("下架");
            }
            res.add(courseInit0202Res);
        }
        return res;
    }

    //查詢按鈕
    public List<CourseQuery0202Res> query(CourseQuery0202Req req) {
        List<CourseQuery0202DAO> dao = course0202Repository.query(req);
        List<CourseQuery0202Res> res = new ArrayList<>();
        for (CourseQuery0202DAO s : dao) {
            CourseQuery0202Res courseQuery0202Res = new CourseQuery0202Res();
            BeanUtils.copyProperties(s, courseQuery0202Res);
            if ("1".equals(courseQuery0202Res.getIsPublished())) {
                courseQuery0202Res.setIsPublishedName("上架");
            } else if ("0".equals(courseQuery0202Res.getIsPublished())) {
                courseQuery0202Res.setIsPublishedName("下架");
            }
            res.add(courseQuery0202Res);
        }
        return res;
    }

    //刪除
    public int del(CourseDel0202Req req) {
        CourseDel0202DAO dao = CourseDel0202DAO.builder()
                .id(Long.valueOf(req.getId()))
                .build();
        int rows = course0202Repository.deleteById(dao);
        return rows;
    }

    //新增
    public int insert(CourseIns0202Req req) {
        CourseIns0202DAO dao = new CourseIns0202DAO();
        BeanUtils.copyProperties(req, dao);
        dao.setLevel(String.join(",",req.getLevel()));
        Course course =  Course.builder()
                .title(dao.getTitle())
                .teacherName(dao.getTeacherName())
                .description(dao.getDescription())
                .price(dao.getPrice())
                .level(dao.getLevel())
                .maxStudents(dao.getMaxStudents())
                .isPublished(dao.getIsPublished())
                .createdAt(dao.getCreatedAt())
                .updatedAt(dao.getUpdatedAt())
                .build();
        int rows = course0202Repository.insert(course);
        return rows;
    }



    //修改
    public int update(CourseUpd0202Req req) {
        CourseUpd0202DAO dao = CourseUpd0202DAO.builder()
                .id(Long.valueOf(req.getId()))
                .title(req.getTitle())
                .teacherName(req.getTeacherName())
                .description(req.getDescription())
                .price(Integer.valueOf(req.getPrice()))
                .level(String.join(",",req.getLevel()))
                .maxStudents(Integer.valueOf(req.getMaxStudents()))
                .isPublished(req.getIsPublished())
                .createdAt(LocalDateTime.parse(req.getCreatedAt()))
                .updatedAt(LocalDateTime.parse(req.getUpdatedAt()))
                .build();
        int rows = course0202Repository.update(dao);
        return rows;
    }

    //上傳
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

                String title     = getCell(row, 0, fmt);
                String teacherName = getCell(row, 1, fmt);
                String description  = getCell(row, 2, fmt);
                String priceStr = getCell(row, 3, fmt);
                Integer price = priceStr == null ? null : Integer.valueOf(priceStr);
                String level    = getCell(row, 4, fmt);
                String maxStudentsStr = getCell(row, 5, fmt);
                Integer maxStudents = maxStudentsStr == null ? null : Integer.valueOf(maxStudentsStr);
                String isPublished  = getCell(row, 6, fmt);
                LocalDateTime createdAt  = getExcelDateTime(row, 7);
                LocalDateTime updatedAt = getExcelDateTime(row, 8);


                Course dao1 = new Course();
                    dao1.setTitle(title);
                    dao1.setTeacherName(teacherName);
                    dao1.setDescription(description);
                    dao1.setPrice(price);
                    dao1.setLevel(level);
                    dao1.setMaxStudents(maxStudents);
                    dao1.setIsPublished(isPublished);
                    dao1.setCreatedAt(createdAt);
                    dao1.setUpdatedAt(updatedAt);
                course0202Repository.insert(dao1);
            }
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
            } catch (DateTimeParseException ignore) {}
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
    public List<CourseDon0202Res> downloadList(CourseQuery0202Req req) {

        //2.呼叫 MyBatis / Mapper，依照 param 條件查詢資料庫
        List<CourseQuery0202DAO> list = course0202Repository.query(req);

        //3.將查詢結果 DTO 轉成回傳用的 Response 物件 AdRes
        List<CourseDon0202Res> rows = new ArrayList<>();

        //逐筆處理查詢出來的資料
        for (CourseQuery0202DAO s : list) {

            //建立一筆回傳用的 AdRes 物件
            CourseDon0202Res r = new CourseDon0202Res();

            //將 DTO 中同名欄位的資料複製到 AdRes
            BeanUtils.copyProperties(s, r);

            //將轉換完成的物件加入回傳清單
            rows.add(r);
        }

        //4.回傳整理完成的資料清單（供 Excel 匯出使用）
        return rows;
    }
}
