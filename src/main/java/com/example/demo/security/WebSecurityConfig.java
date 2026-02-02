package com.example.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
// Spring Security 的過濾器鏈
@Configuration   // 告訴 Spring：這是一個設定類
public class WebSecurityConfig {

    // 密碼加密器（註冊、登入比對都會用）
    @Bean
    public PasswordEncoder passwordEncoder() {
        // 使用 BCrypt 演算法加密密碼
        return new BCryptPasswordEncoder();
    }

    // 設定「使用資料庫帳密」的驗證提供者
    @Bean
    public DaoAuthenticationProvider authenticationProvider(
            UserDetailsService userDetailsService, //CustomUserDetailsService
            PasswordEncoder passwordEncoder        // 上面那顆 BCrypt
    ) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

        // 告訴 Spring Security：使用這個 UserDetailsService 來查使用者
        provider.setUserDetailsService(userDetailsService);

        // 告訴 Spring Security：用這個加密器來比對密碼
        provider.setPasswordEncoder(passwordEncoder);

        return provider;
    }

    // 主要的 Spring Security 設定入口
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           DaoAuthenticationProvider provider) throws Exception {

        // 指定登入時使用你上面設定的 authenticationProvider
        http.authenticationProvider(provider);

        // 關閉 CSRF（前後端分離、目前沒有處理 csrf token）
        http.csrf().disable();

        // 啟用 CORS（會去使用下面那顆 CorsConfigurationSource）
        http.cors(); // 讓 Angular(4200) 可以跨網域並帶 cookie

        http
                .authorizeRequests()

// 1) 先放行 OPTIONS（避免預檢被擋）
                .antMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll()

// 2) 放行你的 proxy API（建議放行這段就好）
                .antMatchers(org.springframework.http.HttpMethod.POST, "/Course/0202/smr/a10/a16/query").permitAll()
// 或者你想放大範圍：
// .antMatchers("/Course/0202/smr/**").permitAll()

// 原本放行的
                .antMatchers("/users/0130/register", "/login").permitAll()

// 其他照舊要登入
                .anyRequest().authenticated()

                .and()

                // 例外處理（未登入時怎麼回）
                .exceptionHandling()

                // 沒登入或 session 失效時，直接回 401（給前端判斷）
                .authenticationEntryPoint((req, res, e) -> res.sendError(401))
                .and()

                // 使用 Spring Security 內建的表單登入機制
                .formLogin()

                // 登入處理網址（不用自己寫 controller）
                .loginProcessingUrl("/login")

                // 登入成功只回 200（不做畫面 redirect）
                .successHandler((req, res, auth) -> res.setStatus(200))

                // 登入失敗回 401
                .failureHandler((req, res, ex) -> res.sendError(401))
                .permitAll()
                .and()

                // 登出設定
                .logout()

                // 登出網址
                .logoutUrl("/logout")

                // 登出成功只回 200
                .logoutSuccessHandler((req, res, auth) -> res.setStatus(200))
                .permitAll();

        // 建立並回傳整個 Security filter chain
        return http.build();
    }

    // CORS 設定（讓前端 http://localhost:4200 可以呼叫後端並帶 cookie）
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();

        // 允許帶 cookie（session）
        config.setAllowCredentials(true);

        // 只允許這個來源呼叫（Angular 開發環境）
        config.setAllowedOrigins(Arrays.asList("http://localhost:4200"));

        // 允許的 HTTP method
        config.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE","OPTIONS"));

        // 允許所有 header
        config.setAllowedHeaders(Arrays.asList("*"));

        // 將上面的 CORS 設定套用到所有路徑
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}
