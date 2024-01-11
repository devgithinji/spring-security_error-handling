package org.densoft.authdemo.config;

import org.densoft.authdemo.exception.CustomAccessDeniedHandler;
import org.densoft.authdemo.exception.CustomAuthEntryPoint;
import org.densoft.authdemo.filter.JwtFilter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Configuration
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final CustomAuthProvider customAuthProvider;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomAuthEntryPoint customAuthEntryPoint;

    public SecurityConfig(JwtFilter jwtFilter,
                          CustomAuthProvider customAuthProvider,
                          CustomAccessDeniedHandler customAccessDeniedHandler, CustomAuthEntryPoint customAuthEntryPoint) {
        this.jwtFilter = jwtFilter;
        this.customAuthProvider = customAuthProvider;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
        this.customAuthEntryPoint = customAuthEntryPoint;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(customAuthProvider);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        httpSecurity.authorizeHttpRequests(registry -> registry
                .requestMatchers("/login", "/register").permitAll()
                .requestMatchers("/products/**").authenticated());


// shorter way but requires HandlerExceptionResolver autowiring
//        httpSecurity.exceptionHandling(configurer -> configurer
//                .accessDeniedHandler((request, response, accessDeniedException) -> handlerExceptionResolver.resolveException(request, response, null, accessDeniedException))
//                .authenticationEntryPoint((request, response, authException) -> handlerExceptionResolver.resolveException(request, response, null, authException))
//        );

        httpSecurity.exceptionHandling(configurer -> configurer
                .accessDeniedHandler(customAccessDeniedHandler)
                .authenticationEntryPoint(customAuthEntryPoint)
        );

        httpSecurity.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }


}
