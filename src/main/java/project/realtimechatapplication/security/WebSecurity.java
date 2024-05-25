package project.realtimechatapplication.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configurable
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurity {

  private final JwtAuthenticationFilter jwtAuthenticationFilter;

  @Bean
  protected SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception {

    httpSecurity
        .cors(cors ->cors
            .configurationSource(corsConfigurationSource())
        )
        .csrf(CsrfConfigurer::disable)
        .httpBasic(HttpBasicConfigurer::disable)
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        )
        .authorizeHttpRequests(request -> request
            .requestMatchers("/", "/auth/**").permitAll()
            .requestMatchers("/user/**").hasRole("USER")
            .requestMatchers("/admin/**").hasRole("ADMIN")
            .anyRequest().authenticated())
        .exceptionHandling(exception -> exception
            .authenticationEntryPoint(new FailedAuthenticationEntryPoint()))
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    return httpSecurity.build();
  }

  @Bean
  protected CorsConfigurationSource corsConfigurationSource() {

    CorsConfiguration configuration = new CorsConfiguration();
    configuration.addAllowedOrigin("*");
    configuration.addAllowedMethod("*");
    configuration.addAllowedHeader("*");

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);

    return source;
  }
}

class FailedAuthenticationEntryPoint implements AuthenticationEntryPoint {

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException authException) throws IOException {
    response.setContentType("application/json;charset=UTF-8");
    response.setStatus(HttpServletResponse.SC_FORBIDDEN);

    response.getWriter().write("{\"code\": \"NP\", \"message\": \"No Permission.\"}");
  }
}
