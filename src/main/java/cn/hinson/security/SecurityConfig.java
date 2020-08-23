package cn.hinson.security;

import cn.hinson.security.oauth2.ClientResources;
import cn.hinson.security.service.MyUserDetailsService;
import cn.hinson.security.service.MyUserInfoTokenServices;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.filter.CompositeFilter;

import javax.annotation.Resource;
import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.List;


@Configuration
@EnableOAuth2Client
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    OAuth2ClientContext oauth2ClientContext;

    @Resource
    MyLogoutSuccessHandler myLogoutSuccessHandler;

    @Bean
    UserDetailsService detailsService(){
        return new MyUserDetailsService();
    }

    @Autowired
    MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;

    @Autowired
    TokenAuthorizationFilter tokenAuthorizationFilter;

    /**\
     * 该方法用于设置权限，判定那些用户可以访问哪些资源
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        Log logger = LogFactory.getLog(SecurityConfig.class);
        logger.info("HttpSecurity http");
        http.rememberMe().and().antMatcher("/**").authorizeRequests()
                .antMatchers("/","/login/github", "/login", "/webjars/**","/test","/register.html").permitAll()
                .antMatchers("/userManager","/sysManager")
                .hasRole("ADMIN")
                .antMatchers("/userInfo")
                .hasAnyRole("ADMIN","USER")
                .anyRequest().authenticated().and().exceptionHandling()
                .and()
                    .logout().logoutSuccessHandler(myLogoutSuccessHandler)

                /*.logoutSuccessHandler(myLogoutSuccessHandler)*/
                .deleteCookies("JSESSIONID")
                /*.logoutSuccessHandler(myLogoutSuccessHandler)*/
                .and()
                .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).and()
                .addFilterBefore(ssoFilter(), BasicAuthenticationFilter.class)
                .formLogin().successHandler(myAuthenticationSuccessHandler).and();
        http.addFilterBefore(tokenAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);
        // @formatter:on
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//                .passwordEncoder(new BCryptPasswordEncoder())
//                .withUser("user1")
//                .password(new BCryptPasswordEncoder().encode("123456"))
//                .roles("USER");
        /*这个方法的主要作用是验证用户账户密码，
        * 需要传入new BCryptPassword()进行密码加密，
        * 用数据库信息来验证
        * detailsService()返回MyUserDetailsService
        * */
        auth.userDetailsService(detailsService()).passwordEncoder(new BCryptPasswordEncoder());
    }


    @Bean
    public FilterRegistrationBean<OAuth2ClientContextFilter> oauth2ClientFilterRegistration(OAuth2ClientContextFilter filter) {
        FilterRegistrationBean<OAuth2ClientContextFilter> registration = new FilterRegistrationBean<OAuth2ClientContextFilter>();
        registration.setFilter(filter);
        registration.setOrder(-100);
        return registration;
    }

    @Bean
    @ConfigurationProperties("github")
    public ClientResources github() {
        return new ClientResources("github");
    }

    @Bean
    @ConfigurationProperties("facebook")
    public ClientResources facebook() {
        return new ClientResources("facebook");
    }

    @Autowired
    GithubPrincipalExtractor githubPrincipalExtractor;

    @Autowired
    FacebookPrincipalExtractor facebookPrincipalExtractor;

    private Filter ssoFilter() {
        CompositeFilter filter = new CompositeFilter();
        List<Filter> filters = new ArrayList<>();
        filters.add(ssoFilter(facebook(), "/login/facebook", facebookPrincipalExtractor));
        filters.add(ssoFilter(github(), "/login/github", githubPrincipalExtractor));
        filter.setFilters(filters);
        return filter;
    }



    private Filter ssoFilter(ClientResources client, String path, AbstractPrincipalExtractor principalExtractor) {
        OAuth2ClientAuthenticationProcessingFilter filter = new OAuth2ClientAuthenticationProcessingFilter(
                path);
        filter.setAuthenticationSuccessHandler(myAuthenticationSuccessHandler);

        OAuth2RestTemplate template = new OAuth2RestTemplate(client.getClient(), oauth2ClientContext);
        filter.setRestTemplate(template);
        UserInfoTokenServices tokenServices = new MyUserInfoTokenServices(client.getResource().getUserInfoUri(), client.getClient().getClientId(), principalExtractor);
        tokenServices.setRestTemplate(template);
        filter.setTokenServices(tokenServices);
        return filter;
    }
}
