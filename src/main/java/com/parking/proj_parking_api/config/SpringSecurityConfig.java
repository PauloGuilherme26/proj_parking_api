package com.parking.proj_parking_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.parking.proj_parking_api.jwt.JwtAuthenticationEntryPoint;
import com.parking.proj_parking_api.jwt.JwtAuthorizationFilter;
import com.parking.proj_parking_api.jwt.JwtUserDetailsService;

@EnableMethodSecurity
@EnableWebMvc
@Configuration
public class SpringSecurityConfig {

    private JwtUserDetailsService detailsService;

private static final String[] DOCUMENTATION_OPENAPI = {     //Liberação de acesso da documentação (swagger-ui) para o Spring Security
        "/docs/index.html",
        "/docs-parking.html", "/docs-parking/**", 
        "/v3/api-docs/**",
        "/swagger-ui-custom.html", "/swagger-ui.html", "/swagger-ui/**",
        "/**.html", "/webjars/**", "/configuration/**", "/swagger-resources/**"
};

@Bean
public SecurityFilterChain filterChain (HttpSecurity http) throws Exception {
    return http
            .cors(Customizer.withDefaults())    // Informa que existe um arquivo de configuração a ser aceito.
            .csrf(csrf -> csrf.disable())
            .formLogin(form -> form.disable())
            .httpBasic(basic -> basic.disable())
            .authorizeHttpRequests(auth -> auth
                                            .requestMatchers(HttpMethod.POST, "api/v1/usuarios").permitAll()
                                            .requestMatchers(HttpMethod.POST, "api/v1/auth").permitAll()                                                                                         
                                            .requestMatchers(DOCUMENTATION_OPENAPI).permitAll()
                                            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                                            .anyRequest().authenticated() )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtAuthorizationFilter(this.detailsService), UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling(ex -> ex.authenticationEntryPoint( new JwtAuthenticationEntryPoint() ) )   
            .build();   
    }
    
@Bean
public JwtAuthorizationFilter jwtAuthorizationFilter(JwtUserDetailsService detailsService) {
    this.detailsService = detailsService;
    return new JwtAuthorizationFilter(detailsService);
} 

@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}

@Bean
public AuthenticationManager authenticationManager (AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
} 

}
