package com.cgy.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /*
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                .withUser("user")
                .password("password")
                .roles("USER");
    }
    */

    public static class MyPasswordEncoder implements PasswordEncoder
    {
        @Override
        public String encode(CharSequence charSequence)
        {
            return charSequence.toString();
        }

        @Override
        public boolean matches(CharSequence charSequence, String s)
        {
            return s.equals(charSequence.toString());
        }
    }



    @Bean
    public static MyPasswordEncoder passwordEncoder() {
        return new MyPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        //super.configure(http);
        http
                .authorizeRequests()
                .antMatchers("/test/**").permitAll()
                .antMatchers("/login/**").permitAll()
                .antMatchers("/room/**").permitAll()
                .antMatchers("/expressage/**").permitAll()
                .antMatchers("/admin/**").permitAll()
                .antMatchers("/gameuser/**").permitAll()
                .antMatchers("/product/**").permitAll()

                .antMatchers("/static/**").permitAll()
                .antMatchers("/vendors/**").permitAll()
                .antMatchers("/build/**").permitAll()
                .antMatchers("/custom/**").permitAll()
                .antMatchers("/**").permitAll()
                .anyRequest().authenticated()
                .and().csrf().disable();
        http.formLogin().loginPage("/login").defaultSuccessUrl("/index")
                .permitAll();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception
    {
        auth
                .inMemoryAuthentication()
                    .withUser("root")
                    .password("root")
                    .roles("ADMIN")
                .and()
                    .withUser("user")
                    .password("user")
                    .roles("USER")
                .and();

    }

}
