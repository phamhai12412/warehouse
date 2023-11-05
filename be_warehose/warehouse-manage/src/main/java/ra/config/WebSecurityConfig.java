package ra.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import ra.security.jwt.CustomAccessDeniedHandler;
import ra.security.jwt.JwtEntryPoint;
import ra.security.jwt.JwtTokenFilter;
import ra.security.user_principal.UserDetailService;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) // phân quyền trực tiếp trên controller
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private CustomAccessDeniedHandler customAccessDeniedHandler;
    @Autowired
    private UserDetailService userDetailService;
    @Autowired
    private JwtEntryPoint jwtEntryPoint;

    @Bean
    public JwtTokenFilter jwtTokenFilter() {
        return new JwtTokenFilter();
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    protected AuthenticationManager authenticationManager() {
        try {
            return super.authenticationManager();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        try {
            auth.userDetailsService(userDetailService).passwordEncoder(passwordEncoder());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void configure(HttpSecurity http) {
        // cáu hình phân qyền đường dẫn
        try {
            http.cors().and().csrf().disable() // tắt cáu hình csrf
                    .authorizeRequests()
                    .antMatchers("/api/v1/sign/**").permitAll()
                    .antMatchers("/api/v1/storage/**").permitAll()
                    .antMatchers("/api/v1/shipping-report/**").permitAll()
                    .antMatchers("/api/v1/zone/**").permitAll()
                    .antMatchers("/api/v1/sign/**").permitAll()
                    .antMatchers("/api/v1/inventory/**").permitAll()
                    .antMatchers("/api/v1/categories/**").permitAll()
                    .antMatchers("/api/v1/products/**").permitAll()
                    .antMatchers("/api/v1/shipment/**").permitAll()
                    .antMatchers("/api/v1/users/**").permitAll()
                    .antMatchers("/api/v1/dashboard/**").permitAll()
//                    .antMatchers("/api/admin/use/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                    .anyRequest().authenticated() // các đường dân khác phả được xác thực
                    .and()
                    .exceptionHandling().accessDeniedHandler(customAccessDeniedHandler).authenticationEntryPoint(jwtEntryPoint)
                    .and()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS); // yếu cầu người dùng luôn xác thức bằng token
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        http.addFilterBefore(jwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}