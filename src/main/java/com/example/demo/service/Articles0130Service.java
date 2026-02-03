package com.example.demo.service;

import com.example.demo.dao.*;
import com.example.demo.entity.Article;
import com.example.demo.mapper.Articles0130Repository;
import com.example.demo.req.*;
import com.example.demo.res.ArticleDon0130Res;
import com.example.demo.res.ArticleInit0130Res;
import com.example.demo.res.ArticleQuery0130Res;
import com.example.demo.res.ArticleUpdQuery0130Res;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


@Service
public class Articles0130Service {
    @Autowired
    private Articles0130Repository f;

    //初始化查詢
    public List<ArticleInit0130Res> init() {
        List<ArticleInit0130DAO> init = f.init();
        List<ArticleInit0130Res> res = new ArrayList<>();
        for (ArticleInit0130DAO s : init) {
            ArticleInit0130Res articleInit0130Res = new ArticleInit0130Res();
            BeanUtils.copyProperties(s, articleInit0130Res);
            if ("published".equals(articleInit0130Res.getStatus())) {
                articleInit0130Res.setStatusName("已發布");
            } else if ("draft".equals(articleInit0130Res.getStatus())) {
                articleInit0130Res.setStatusName("草稿");
            }
            res.add(articleInit0130Res);
        }
        return res;
    }

    //查詢按鈕
    public List<ArticleQuery0130Res> query(ArticleQuery0130Req req) {
        List<ArticleQuery0130DAO> dao = f.query(req);
        List<ArticleQuery0130Res> res = new ArrayList<>();
        for (ArticleQuery0130DAO s : dao) {
            ArticleQuery0130Res articleQuery0130Res = new ArticleQuery0130Res();
            BeanUtils.copyProperties(s, articleQuery0130Res);
            if ("published".equals(articleQuery0130Res.getStatus())) {
                articleQuery0130Res.setStatusName("已發布");
            } else if ("draft".equals(articleQuery0130Res.getStatus())) {
                articleQuery0130Res.setStatusName("草稿");
            }
            if(s.getViewsB() != null){
                articleQuery0130Res.setViewsA(s.getViewsB());
            }
            res.add(articleQuery0130Res);
        }
        return res;
    }

    //刪除
    public int del(ArticleDel0130Req req) {
        ArticleDel0130DAO dao = ArticleDel0130DAO.builder()
                .id(Integer.valueOf(req.getId()))
                .build();
        int rows = f.deleteById(dao);
        return rows;
    }

    //新增
    public int insert(ArticleIns0130Req req) {
        Article article = Article.builder()
                .title(req.getTitle())
                .content(req.getContent())
                .summary(req.getSummary())
                .author(req.getAuthor())
                .category(String.join(",",req.getCategory()))
                .status(req.getStatus())
                .views(Integer.valueOf(req.getViews()))
                .createdAt(LocalDateTime.parse(req.getCreatedAt()))
                .updatedAt(LocalDateTime.parse(req.getUpdatedAt()))
                .build();
        int rows = f.insert(article);
        return rows;
    }


    //跳轉修改畫面查詢
    public ArticleUpdQuery0130Res updQuery(ArticleUpdQuery0130Req req) {
        ArticleUpdQuery0130DAO dao = f.updQuery(req);
        ArticleUpdQuery0130Res res = new ArticleUpdQuery0130Res();
        BeanUtils.copyProperties(dao, res);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if(res.getCreatedAt() != null){
            res.setCreatedAtStr(res.getCreatedAt().format(formatter));
        }
        if(res.getUpdatedAt() != null){
            res.setUpdatedAtStr(res.getUpdatedAt().format(formatter2));
        }

        return res;
    }

    //修改
    public int update(ArticleUpd0130Req req) {
        LocalDate createdDate = LocalDate.parse(req.getCreatedAt());  // "2026-01-30"
        LocalTime updatedTime = LocalTime.parse(req.getUpdatedAt());  // "17:21"
        ArticleUpd0130DAO dao = ArticleUpd0130DAO.builder()
                .id(Integer.valueOf(req.getId()))
                .title(req.getTitle())
                .content(req.getContent())
                .summary(req.getSummary())
                .author(req.getAuthor())
                .category(req.getCategory())
                .status(req.getStatus())
                .views(Integer.valueOf(req.getViews()))
                .createdAt(createdDate.atStartOfDay())
                .updatedAt(createdDate.atTime(updatedTime))
                .build();
        int rows = f.update(dao);
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
                String content = getCell(row, 1, fmt);
                String summary  = getCell(row, 2, fmt);
                String author    = getCell(row, 3, fmt);
                String category    = getCell(row, 4, fmt);
                String status  = getCell(row, 5, fmt);
                String viewsStr = getCell(row, 6, fmt);
                Integer views = viewsStr == null ? null : Integer.valueOf(viewsStr);
                LocalDateTime createdAt  = getExcelDateTime(row, 7);
                LocalDateTime updatedAt = getExcelDateTime(row, 8);


                List<ArticleUpdQuery0130DAO> dao = new ArrayList<>();
                    Article dao1 = new Article();
                    dao1.setTitle(title);
                    dao1.setContent(content);
                    dao1.setSummary(summary);
                    dao1.setAuthor(author);
                    dao1.setCategory(category);
                    dao1.setStatus(status);
                    dao1.setViews(views);
                    dao1.setCreatedAt(createdAt);
                    dao1.setUpdatedAt(updatedAt);
                    Article article = new Article();
                    BeanUtils.copyProperties(dao1, article);
                    f.insert1(article);
            }
        } catch (Exception e) {
            throw new RuntimeException("匯入失敗：" + e.getMessage(), e);
        }
    }

    private LocalDateTime getExcelDateTime(Row row, int colIndex) {
        Cell cell = row.getCell(colIndex);
        if (cell == null) return null;

        // 1) Excel 日期型（最穩）
        if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
            Date d = cell.getDateCellValue();
            return d.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        }

        // 2) 字串型（走你原本 parseExcelDateTime）
        if (cell.getCellType() == CellType.STRING) {
            return parseExcelDateTime(cell.getStringCellValue());
        }

        // 3) 公式
        if (cell.getCellType() == CellType.FORMULA) {
            if (cell.getCachedFormulaResultType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
                Date d = cell.getDateCellValue();
                return d.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            }
            if (cell.getCachedFormulaResultType() == CellType.STRING) {
                return parseExcelDateTime(cell.getStringCellValue());
            }
        }

        // 其他型別就當空
        return null;
    }


    private LocalDateTime parseExcelDateTime(String s) {
        if (s == null) return null;
        s = s.trim();
        if (s.isEmpty()) return null;

        List<DateTimeFormatter> formats = Arrays.asList(
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
    public List<ArticleDon0130Res> downloadList(ArticleQuery0130Req req) {

        //2.呼叫 MyBatis / Mapper，依照 param 條件查詢資料庫
        List<ArticleQuery0130DAO> list = f.query(req);

        //3.將查詢結果 DTO 轉成回傳用的 Response 物件 AdRes
        List<ArticleDon0130Res> rows = new ArrayList<>();

        //逐筆處理查詢出來的資料
        for (ArticleQuery0130DAO s : list) {

            //建立一筆回傳用的 AdRes 物件
            ArticleDon0130Res r = new ArticleDon0130Res();

            //將 DTO 中同名欄位的資料複製到 AdRes
            BeanUtils.copyProperties(s, r);

            //將轉換完成的物件加入回傳清單
            rows.add(r);
        }

        //4.回傳整理完成的資料清單（供 Excel 匯出使用）
        return rows;
    }

    //查詢按鈕
    public List<ArticleQuery0130Res> query2(ArticleQuery0130Req req) {
        List<ArticleQuery0130DAO> dao = f.query2(req);
        List<ArticleQuery0130Res> res = new ArrayList<>();
        for (ArticleQuery0130DAO s : dao) {
            ArticleQuery0130Res articleQuery0130Res = new ArticleQuery0130Res();
            BeanUtils.copyProperties(s, articleQuery0130Res);
            if ("published".equals(articleQuery0130Res.getStatus())) {
                articleQuery0130Res.setStatusName("已發布");
            } else if ("draft".equals(articleQuery0130Res.getStatus())) {
                articleQuery0130Res.setStatusName("草稿");
            }
            if(s.getViewsB() != null){
                articleQuery0130Res.setViewsA(s.getViewsB());
            }
            res.add(articleQuery0130Res);
        }
        return res;
    }
}
