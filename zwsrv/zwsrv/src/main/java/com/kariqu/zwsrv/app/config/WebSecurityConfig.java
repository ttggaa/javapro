package com.kariqu.zwsrv.app.config;

import com.kariqu.zwsrv.app.Application;
import com.kariqu.zwsrv.thelib.security.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

/**
 * Created by simon on 10/03/17.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

        @Autowired
        CustomUserDetailsService userDetailsService;

        @Autowired
        AccountAuthenticationProvider authenticationProvider;

        @Autowired
        JwtAuthenticationTokenProvider authenticationTokenProvider;

        @Autowired
        PasswordEncoder passwordEncoderBean;

        @Bean
        public JwtAuthenticationTokenFilter authenticationTokenFilterBean() throws Exception {
                return new JwtAuthenticationTokenFilter(Application.ignoreFilterList, Application.isProdEnv);
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
                auth.authenticationProvider(authenticationProvider);
                auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoderBean);
        }

        @Override
        @Bean
        public AuthenticationManager authenticationManagerBean() throws Exception {
                return super.authenticationManagerBean();
        }

        @Override
        public void configure(WebSecurity web) throws Exception {

        }

        @Override
        public void configure(HttpSecurity http) throws Exception {

                http
                        .headers().frameOptions().disable()
                        .addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN));

//                http.exceptionHandling().authenticationEntryPoint(...)
                http.exceptionHandling()
                        .accessDeniedHandler(accessDeniedHandler()) // handle access denied in general (for example comming from @PreAuthorization
                        .authenticationEntryPoint(authenticationEntryPoint()) // handle authentication exceptions for unauthorized calls.
                        .and();

                http
                        .formLogin()
                        .loginPage("/home/login").usernameParameter("identifier").passwordParameter("credential").permitAll()
                        .defaultSuccessUrl("/home") //如果设置为STATELESS 则无效
                        .authenticationDetailsSource(authenticationDetailsSource())
                        .successHandler(authenticationSuccessHandler())
                        .permitAll()
                        .and()
                        .logout() ////如果设置为STATELESS 则无效
                        .logoutSuccessUrl("/home/login") //如果设置为STATELESS 则无效
                        .permitAll()
                        .and()
                        .logout()
                        .permitAll().and().csrf().disable();

                http.logout().deleteCookies("JSESSIONID");

                http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);



                http
                        .authorizeRequests()

                        .antMatchers("/account/**").permitAll()
                        .antMatchers("/address/**").permitAll()
                        .antMatchers("/auth/**").permitAll()
                        .antMatchers("/coins/**").permitAll()
                        .antMatchers("/config/**").permitAll()
                        .antMatchers("/delivery_order/**").permitAll()
                        .antMatchers("/file/**").permitAll()
                        .antMatchers("/game/**").permitAll()
                        .antMatchers("/notice/**").permitAll()
                        .antMatchers("/plat/**").permitAll()
                        .antMatchers("/rong/**").permitAll()
                        .antMatchers("/room/**").permitAll()
                        .antMatchers("/shipping/**").permitAll()
                        .antMatchers("/user/**").permitAll()
                        .antMatchers("/user_count/**").permitAll()
                        .antMatchers("/wxpay/**").permitAll()
                        .antMatchers("/signin/**").permitAll()
                        .antMatchers("/lobby/**").permitAll()

                        .antMatchers("/contrib/**", "/js/**", "/img/**", "/css/**").permitAll()
                        .anyRequest().authenticated()
                        .and()
                        .csrf().disable();

                http.addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
        }

        AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> authenticationDetailsSource() {
                return new AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails>() {
                        @Override
                        public WebAuthenticationDetails buildDetails(
                                HttpServletRequest request) {
                                return new CustomWebAuthenticationDetails(request);
                        }

                };
        }

        @Bean
        AuthenticationSuccessHandler authenticationSuccessHandler() {
                SavedRequestAwareAuthenticationSuccessHandler handler = new SavedRequestAwareAuthenticationSuccessHandler() {
                        @Override
                        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
                                CurrentUserDetails currentUser = SecurityUtil.currentUser();
                                if (currentUser!=null) {
                                        String token = authenticationTokenProvider.createToken(currentUser,true);
//                                        response.addHeader(SecurityConstants.AUTHORIZATION_HEADER, token);
                                        Cookie cookie = new Cookie(SecurityConstants.AUTHORIZATION_COOKIE_NAME, token);
                                        cookie.setPath("/");
                                        response.addCookie(cookie);
                                        super.onAuthenticationSuccess(request, response, authentication);
                                }
                        }
                };
                handler.setDefaultTargetUrl("/home");
                handler.setAlwaysUseDefaultTargetUrl(false);
                return handler;
        }

        @Bean
        @Autowired
        AccessDeniedHandler accessDeniedHandler() {
                return new AccessDeniedExceptionHandler();
        }


        @Bean
        @Autowired
        public Http401UnauthorizedEntryPoint authenticationEntryPoint() {
                return new Http401UnauthorizedEntryPoint();
        }

        @Autowired
        public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

                auth.authenticationProvider(new AuthenticationProvider() {
                        @Override
                        public boolean supports(Class<?> authentication) {
                                return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
                        }

                        @Override
                        public Authentication authenticate(Authentication authentication) throws AuthenticationException {
                                UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
                                Collection<GrantedAuthority> authorities = token.getAuthorities();
                                return new UsernamePasswordAuthenticationToken(token.getName(), token.getCredentials(), authorities);
                        }
                });
        }
}
