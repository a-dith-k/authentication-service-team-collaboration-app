package site.adithk.authenticationservice.configurations;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import site.adithk.authenticationservice.configurations.filters.JwtAuthenticationFilter;
import site.adithk.authenticationservice.configurations.jwt.JwtAuthenticationEntryPoint;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    AuthenticationEntryPoint authenticationEntryPoint(){
        return  new JwtAuthenticationEntryPoint();
    }

    @Bean
    JwtAuthenticationFilter jwtFilter(){
        return new JwtAuthenticationFilter();
    };



    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http    .cors(withDefaults())
                .csrf(CsrfConfigurer::disable)
                .authorizeHttpRequests(auth->
                        auth
                                .requestMatchers(HttpMethod.POST,"/auth/**").permitAll()
                                .anyRequest().authenticated()

                ).exceptionHandling(ex->ex.authenticationEntryPoint(authenticationEntryPoint())).
                sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);
//                .oauth2ResourceServer(c -> c.opaqueToken(Customizer.withDefaults()));
                    ;
        return http.build();
    }




}
