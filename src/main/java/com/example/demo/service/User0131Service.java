package com.example.demo.service;

import com.example.demo.entity.UserModel;
import com.example.demo.mapper.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service // 宣告這個類別是 Spring 管理的 Service Bean
@RequiredArgsConstructor
public class User0131Service {

    // 操作使用者資料表的 Repository（查詢 / 儲存使用者）
    private final UserRepository userRepository;

    // 密碼加密器（來自 WebSecurityConfig 裡面定義的 PasswordEncoder Bean）
    private final PasswordEncoder passwordEncoder;


    // 註冊使用者
    public void register(String username, String rawPassword) {

        // 先檢查帳號是否已存在
        if (userRepository.findByUsername(username).isPresent()) {

            // 如果已存在就直接丟例外（讓 Controller 回錯誤給前端）
            throw new RuntimeException("username 已存在");
        }

        // 建立一個新的使用者 Entity
        UserModel user = new UserModel();

        // 設定帳號
        user.setUsername(username);

        // 將「明碼密碼」用 BCrypt 加密後再存入資料庫
        // 這裡用的 passwordEncoder 就是上面建構子注入進來的那顆
        user.setPassword(passwordEncoder.encode(rawPassword)); // BCrypt

        // 儲存使用者到資料庫
        userRepository.save(user);
    }
}
