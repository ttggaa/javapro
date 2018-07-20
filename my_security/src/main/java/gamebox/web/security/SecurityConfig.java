package gamebox.web.security;

import gamebox.web.persistence.service.WebAccountService;
import gamebox.web.security.AuthFailureHandler;
import gamebox.web.security.AuthSuccessHandler;
import gamebox.web.security.WebPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private WebAccountService webAccountService;

    @Autowired
    private AuthSuccessHandler authSuccessHandler;

    @Autowired
    private AuthFailureHandler authFailureHandler;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //super.configure(auth);
        auth.userDetailsService(webAccountService)
                .passwordEncoder(new WebPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //super.configure(http);
        http
                .authorizeRequests()
                .antMatchers("/vendors/**").permitAll()
                .antMatchers("/build/**").permitAll()
                .antMatchers("/custom/**").permitAll()
                    .anyRequest().authenticated()
                .and()
                .formLogin()
                    .loginPage("/login")
                    .loginProcessingUrl("/sign_in")
                    //.successForwardUrl("/login/sign_in")
                    .successHandler(authSuccessHandler)
                    //.failureForwardUrl("/sign_in")
                    .failureHandler(authFailureHandler)
                    //.defaultSuccessUrl("/index")
                    .permitAll()
                .and()
                .logout()
                    .logoutUrl("/logout")
                    //.logoutSuccessHandler()
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
                .and()
                .csrf().disable()
        ;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
    }

}
