package com.example.demo.service;

import com.example.demo.dao.*;
import com.example.demo.entity.Product;
import com.example.demo.mapper.Product0203Repository;
import com.example.demo.req.ProducQuery0203Req;
import com.example.demo.req.ProductDel0203Req;
import com.example.demo.req.ProductIns0203Req;
import com.example.demo.req.ProductUpd0203Req;
import com.example.demo.res.ProductDon0203Res;
import com.example.demo.res.ProductInit0203Res;
import com.example.demo.res.ProductQuery0203Res;
import lombok.AllArgsConstructor;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
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
import java.util.*;


@Service
@AllArgsConstructor
public class Product0203Service {

    private final Product0203Repository product0203Repository;

    //初始化查詢
    public List<ProductInit0203Res> init() {
        List<ProductInit0203DAO> init = product0203Repository.init();
        List<ProductInit0203Res> res = new ArrayList<>();
        for (ProductInit0203DAO s : init) {
            ProductInit0203Res productInit0203Res = new ProductInit0203Res();
            BeanUtils.copyProperties(s, productInit0203Res);
            if ("onsale".equals(productInit0203Res.getStatus())) {
                productInit0203Res.setStatusName("上架");
            } else if ("offsale".equals(productInit0203Res.getStatus())) {
                productInit0203Res.setStatusName("下架");
            }
            res.add(productInit0203Res);
        }
        return res;
    }

    //查詢按鈕
    public List<ProductQuery0203Res> query(ProducQuery0203Req req) {
        List<ProductQuery0203DAO> dao = product0203Repository.query(req);
        List<ProductQuery0203Res> res = new ArrayList<>();
        for (ProductQuery0203DAO s : dao) {
            ProductQuery0203Res productQuery0203Res = new ProductQuery0203Res();
            BeanUtils.copyProperties(s, productQuery0203Res);
            if ("onsale".equals(productQuery0203Res.getStatus())) {
                productQuery0203Res.setStatusName("上架");
            } else if ("offsale".equals(productQuery0203Res.getStatus())) {
                productQuery0203Res.setStatusName("下架");
            }
            res.add(productQuery0203Res);
        }
        return res;
    }

    //刪除
    public int del(ProductDel0203Req req) {
        ProductDel0203DAO dao = ProductDel0203DAO.builder()
                .id(Long.valueOf(req.getId()))
                .build();
        int rows = product0203Repository.deleteById(dao);
        return rows;
    }

    //    //新增
    public int insert(ProductIns0203Req req) {
        ProductIns0203DAO dao = new ProductIns0203DAO();
        BeanUtils.copyProperties(req, dao);
        Product course = Product.builder()
                .name(dao.getName())
                .description(dao.getDescription())
                .price(Integer.valueOf(dao.getPrice()))
                .stock(Integer.valueOf(dao.getStock()))
                .category(String.join(",", dao.getCategory()))
                .brand(dao.getBrand())
                .sku(dao.getSku())
                .status(dao.getStatus())
                .createdTime(LocalDateTime.parse(dao.getCreatedTime()))
                .updatedTime(LocalDateTime.parse(dao.getUpdatedTime()))
                .build();
        int rows = product0203Repository.insert(course);
        return rows;
    }


    //修改
    public int update(ProductUpd0203Req req) {
        ProductUpd0203DAO dao = ProductUpd0203DAO.builder()
                .id(Long.valueOf(req.getId()))
                .name(req.getName())
                .description(req.getDescription())
                .price(Integer.valueOf(req.getPrice()))
                .stock(Integer.valueOf(req.getStock()))
                .category(String.join(",", req.getCategory()))
                .brand(req.getBrand())
                .sku(req.getSku())
                .status(req.getStatus())
                .createdTime(LocalDateTime.parse(req.getCreatedTime()))
                .updatedTime(LocalDateTime.parse(req.getUpdatedTime()))
                .build();
        int rows = product0203Repository.update(dao);
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

                    String name = getCell(row, 0, fmt);
                    String description = getCell(row, 1, fmt);
                    String priceStr = getCell(row, 2, fmt);
                    Integer price = priceStr == null ? null : Integer.valueOf(priceStr);
                    String stockStr = getCell(row, 3, fmt);
                    Integer stock = stockStr == null ? null : Integer.valueOf(stockStr);
                    String category = getCell(row, 4, fmt);
                    String brand = getCell(row, 5, fmt);
                    String sku = getCell(row, 6, fmt);
                    String status = getCell(row, 7, fmt);
                    LocalDateTime createdTime = getExcelDateTime(row, 8);
                    LocalDateTime updatedTime = getExcelDateTime(row, 9);


                    Product dao = new Product();
                    dao.setName(name);
                    dao.setDescription(description);
                    dao.setPrice(price);
                    dao.setStock(stock);
                    dao.setCategory(category);
                    dao.setBrand(brand);
                    dao.setSku(sku);
                    dao.setStatus(status);
                    dao.setCreatedTime(createdTime);
                    dao.setUpdatedTime(updatedTime);
                    product0203Repository.insert(dao);
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
    public List<ProductDon0203Res> downloadList(ProducQuery0203Req req) {

        //2.呼叫 MyBatis / Mapper，依照 param 條件查詢資料庫
        List<ProductQuery0203DAO> list = product0203Repository.query(req);

        //3.將查詢結果 DTO 轉成回傳用的 Response 物件 AdRes
        List<ProductDon0203Res> rows = new ArrayList<>();

        //逐筆處理查詢出來的資料
        for (ProductQuery0203DAO s : list) {

            //建立一筆回傳用的 AdRes 物件
            ProductDon0203Res r = new ProductDon0203Res();

            //將 DTO 中同名欄位的資料複製到 AdRes
            BeanUtils.copyProperties(s, r);

            //將轉換完成的物件加入回傳清單
            rows.add(r);
        }

        //4.回傳整理完成的資料清單（供 Excel 匯出使用）
        return rows;
    }


    //讀jrxml檔案,編譯,產生報表物件
// 讀jrxml檔案,編譯,產生報表物件（帶查詢結果）
    public JasperPrint testLoadReport(ProducQuery0203Req req) throws Exception {

        // 1) 查詢資料（你已經有 query(req)）
        List<ProductQuery0203Res> rows = query(req);

        System.out.println("rows size = " + rows.size());
        if (!rows.isEmpty()) {
            System.out.println("first row id=" + rows.get(0).getId() + ", name=" + rows.get(0).getName());
        }


        // 2) 讀 jrxml
        InputStream is = this.getClass().getResourceAsStream("/reports/product0203.jrxml");
        if (is == null) {
            throw new RuntimeException("找不到 product0203.jrxml");
        }

        // 3) 編譯
        JasperReport report = JasperCompileManager.compileReport(is);

        // 4) 把查詢結果丟進 DataSource
        JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(rows);

        // 5) 產生報表
        Map<String, Object> params = new HashMap<>();
        JasperPrint print = JasperFillManager.fillReport(report, params, ds);

        return print;
    }





}
