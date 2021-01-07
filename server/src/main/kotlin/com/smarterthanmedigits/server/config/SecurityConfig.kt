package com.smarterthanmedigits.server.config

import com.smarterthanmedigits.server.security.JwtConfigurer
import com.smarterthanmedigits.server.security.JwtTokenProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.BeanIds
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy


@Configuration
class SecurityConfig @Autowired constructor(val jwtTokenProvider: JwtTokenProvider) : WebSecurityConfigurerAdapter() {

    @Bean(name = [BeanIds.AUTHENTICATION_MANAGER])
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }

    override fun configure(http: HttpSecurity) {
        http
                .httpBasic()
                .disable()
                .csrf()
                .disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(LOGIN_ENDPOINT)
                .permitAll()
                .antMatchers(ADMIN_ENDPOINT)
                .hasRole("ADMIN")
                .anyRequest()
                .authenticated()
                .and()
                .apply(JwtConfigurer(jwtTokenProvider))
    }



    companion object {
        private const val ADMIN_ENDPOINT = "/api/v1/admin/**"
        private const val LOGIN_ENDPOINT = "/api/v1/auth/**"
    }
}