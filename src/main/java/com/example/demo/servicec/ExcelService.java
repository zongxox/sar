package com.example.demo.servicec;

import com.example.demo.mapper.UserRepository;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ExcelService {

    @Autowired
    private UserRepository userRepository;

    public void importExcel(MultipartFile file) throws Exception {

        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0); // 第一個工作表

        for (int i = 1; i <= sheet.getLastRowNum(); i++) { // 跳過表頭
            Row row = sheet.getRow(i);
            if (row == null) continue;

            String account = row.getCell(0).toString();
            String password = row.getCell(1).toString();
            String username = row.getCell(2).toString();
            String email = row.getCell(3).toString();

            User user = new User();
            user.setAccount(account);
            user.setPassword(password);
            user.setUsername(username);
            user.setEmail(email);

            userRepository.save(user);
        }

        workbook.close();
    }
}