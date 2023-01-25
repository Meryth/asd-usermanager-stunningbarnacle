package com.example.usermanager.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    //TODO Secure the pages
    @Autowired
    private UserDetailsService userDetailsService;

    public SecurityConfig(UserDetailsService userDetailsService){
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        //For h2-console
        DBConsole(httpSecurity);
        /*httpSecurity
                .authorizeRequests()
                .antMatchers("/h2-console/**").permitAll()
                .and()
                .headers().frameOptions().disable();*/

        //For the pages
        httpPage(httpSecurity);
        /*httpSecurity.csrf().disable()
                .authorizeRequests()
                .antMatchers("/register").permitAll()
                .antMatchers("/register/**").permitAll()
                .antMatchers("/index").permitAll()
                .antMatchers("/user").hasAnyRole("ADMIN","USER")
                .and().
                formLogin(
                        form -> form
                                .loginPage("/login")
                                .loginProcessingUrl("/login")
                                .defaultSuccessUrl("/user")
                                .permitAll()
                ).logout(
                        logout -> logout
                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                                .permitAll()
                );*/


        //Session Timeout
        sessionTimeout(httpSecurity);
        /*httpSecurity.sessionManagement()
                .maximumSessions(1)
                .and().invalidSessionUrl("/login?expired");*/

        return  httpSecurity.build();
    }
    public void DBConsole(HttpSecurity httpsecurity) throws Exception{
        httpsecurity
                .authorizeRequests()
                .antMatchers("/h2-console/**").permitAll()
                .and()
                .headers().frameOptions().disable();
    }

    public void httpPage(HttpSecurity httpsecurity) throws Exception{
        httpsecurity.csrf().disable()
                .authorizeRequests()
                .antMatchers("/register").permitAll()
                .antMatchers("/register/**").permitAll()
                .antMatchers("/index").permitAll()
                .antMatchers("/user").hasAnyRole("ADMIN","USER")
                .and().
                formLogin(
                        form -> form
                                .loginPage("/login")
                                .loginProcessingUrl("/login")
                                .defaultSuccessUrl("/user")
                                .permitAll()
                ).logout(
                        logout -> logout
                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                                .permitAll()
                );
    }

    public void sessionTimeout(HttpSecurity httpsecurity) throws Exception{
        httpsecurity.sessionManagement()
                .maximumSessions(1)
                .and().invalidSessionUrl("/login?expired");
    }

    @Bean
    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    //This function is never used
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

}
