package com.fptaptech.s4.security;
import com.fptaptech.s4.security.jwt.AuthTokenFilter;
import com.fptaptech.s4.security.jwt.JwtAuthEntryPoint;
import com.fptaptech.s4.security.user.HotelUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
public class WebSecurityConfig {
    private final HotelUserDetailsService userDetailsService;
    private final JwtAuthEntryPoint jwtAuthEntryPoint;

    @Bean
    public AuthTokenFilter authenticationTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        var authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exception -> exception.authenticationEntryPoint(jwtAuthEntryPoint))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
<<<<<<< HEAD
//                        .requestMatchers("/auth/register-user", "/auth/login").permitAll()  // Public API cho auth
//                        .requestMatchers("/api/user/update").authenticated() // Yêu cầu đăng nhập cho endpoint cập nhật
//                        .requestMatchers("/api/user/**").authenticated()
//                        .requestMatchers("/admin/branches/all", "/admin/branches/{id}", "/admin/branches/hotel/**").permitAll()  // Public cho Branch
//                        .requestMatchers("/shuttles/all","/shuttles/types","/shuttles/car-by-id/{carId}","/shuttles/all","/shuttles/all-available-cars","/shuttles/all","/shuttles/available-cars-by-date-and-type").permitAll()
//                        .requestMatchers("/spas/all","/spas/spa-by-id/{spaId}","/spas//spa-by-name","/spas/all").permitAll()
//                        .requestMatchers("/restaurants/all","/restaurants/restaurant-by-id/{restaurantId}","/restaurants/types").permitAll()
//                        .requestMatchers("/reviews/branch/{branchId}","reviews/room/{roomId}").permitAll()
//                        .requestMatchers("/submitOrder","/vnpay-payment").permitAll()
//                        .requestMatchers("/auth/**", "/rooms/**", "/booked/**", "/bookings/**","/shuttles/**","/spas/**","/restaurants/**").permitAll()
//                        .requestMatchers("/admin/hotels/all", "/admin/hotels/{id}").permitAll()  // Public cho Hotel
//                        .requestMatchers("/roles/**").hasRole("ADMIN")  // Chỉ ADMIN
                        .requestMatchers("/**").permitAll()
=======
                        .requestMatchers("/auth/register-user", "/auth/login").permitAll()  // Public API cho auth
                        .requestMatchers("/api/user/update").authenticated() // Yêu cầu đăng nhập cho endpoint cập nhật
                        .requestMatchers("/api/user/**").authenticated()
                        .requestMatchers("/admin/branches/all", "/admin/branches/{id}", "/admin/branches/hotel/**").permitAll()  // Public cho Branch
                        .requestMatchers("/shuttles/all","/shuttles/types","/shuttles/car-by-id/{carId}","/shuttles/all","/shuttles/all-available-cars","/shuttles/all","/shuttles/available-cars-by-date-and-type").permitAll()
                        .requestMatchers("/spas/all","/spas/spa-by-id/{spaId}","/spas//spa-by-name","/spas/all").permitAll()
                        .requestMatchers("/restaurants/all","/restaurants/restaurant-by-id/{restaurantId}","/restaurants/types").permitAll()
                        .requestMatchers("/reviews/branch/{branchId}","reviews/room/{roomId}").permitAll()
                        .requestMatchers("/auth/**", "/rooms/**", "/booked/**", "/bookings/**","/shuttles/**","/spas/**","/restaurants/**","/auth/reset-password").permitAll()
                        .requestMatchers("/admin/hotels/all", "/admin/hotels/{id}").permitAll()  // Public cho Hotel
                        .requestMatchers("/roles/**").hasRole("ADMIN")  // Chỉ ADMIN
>>>>>>> e8b5063760eacc32b9abc1bd030145adcafc8060
                        .anyRequest().authenticated());  // Các API còn lại yêu cầu xác thực
        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(authenticationTokenFilter(),
                UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}




/*@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
public class WebSecurityConfig {
    private final HotelUserDetailsService userDetailsService;
    private final JwtAuthEntryPoint jwtAuthEntryPoint;

    @Bean
    public AuthTokenFilter authenticationTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        var authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exception -> exception.authenticationEntryPoint(jwtAuthEntryPoint))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/register-user", "/auth/login").permitAll()  // Cho phép truy cập mà không cần xác thực
                        .requestMatchers("/auth/**", "/rooms/**", "/booked/**", "/bookings/**").permitAll()
                        .requestMatchers("/admin/hotels/all", "/admin/branches/hotel/**", "/admin/branches/{id}", "/admin/branches/all")
                        .permitAll()
                        .requestMatchers("/roles/**").hasRole("ADMIN")
                        .anyRequest().authenticated());
        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(authenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}*/



/*
@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
public class WebSecurityConfig {
    private final HotelUserDetailsService userDetailsService;
    private final JwtAuthEntryPoint jwtAuthEntryPoint;

    @Bean
    public AuthTokenFilter authenticationTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        var authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exception -> exception.authenticationEntryPoint(jwtAuthEntryPoint))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/register-user", "/auth/login").permitAll()  // Cho phép truy cập mà không cần xác thực
                        .requestMatchers("/auth/**", "/rooms/**", "/booked/**", "/bookings/**").permitAll()
                        .requestMatchers("/roles/**").hasRole("ADMIN")
                        .requestMatchers("/admin/hotels/all", "/admin/branches/hotel/**", "/admin/branches/{id}", "/admin/branches/all")
                        .hasAnyRole("ADMIN", "USER", "EMPLOYEE")
                        .anyRequest().authenticated());
        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(authenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
*/

