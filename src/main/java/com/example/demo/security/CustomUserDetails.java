package com.example.demo.security;

import com.example.demo.entity.UserModel;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

//把你自己的 UserModel 轉成 Spring Security 要的 UserDetails
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    // 真正從資料庫查出來的使用者資料
    private final UserModel user;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 回傳此使用者擁有的「權限 / 角色」
        // 你現在還沒做角色系統，所以回傳空集合
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        // 回傳此使用者在資料庫中儲存的「加密後密碼」
        // Spring Security 會拿這個值去跟登入時輸入的密碼做比對
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        // 回傳此使用者的帳號
        // Spring Security 會用它當成 principal 的名稱
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        // 帳號是否「沒有過期」
        // true = 沒過期，可以登入
        // false = 已過期，不能登入
        return true;   // 你目前沒有設計過期機制，所以先全部允許
    }

    @Override
    public boolean isAccountNonLocked() {
        // 帳號是否「沒有被鎖定」
        // true = 沒鎖，可以登入
        // false = 已鎖，不能登入
        return true;   // 你目前沒有鎖帳機制，所以先全部允許
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // 密碼是否「沒有過期」
        // true = 密碼有效
        // false = 密碼過期（例如強制改密碼）
        return true;   // 你目前沒有密碼過期機制，所以先全部允許
    }

    @Override
    public boolean isEnabled() {
        // 此帳號是否啟用
        // true = 啟用中
        // false = 停用帳號
        return true;   // 你目前沒有啟用/停用欄位，所以先全部允許
    }
}
