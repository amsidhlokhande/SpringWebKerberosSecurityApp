package com.amsidh.mvc.security;

import com.amsidh.mvc.service.intf.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.kerberos.authentication.KerberosAuthenticationProvider;
import org.springframework.security.kerberos.authentication.KerberosServiceAuthenticationProvider;
import org.springframework.security.kerberos.authentication.sun.SunJaasKerberosClient;
import org.springframework.security.kerberos.authentication.sun.SunJaasKerberosTicketValidator;
import org.springframework.security.kerberos.web.authentication.SpnegoAuthenticationProcessingFilter;
import org.springframework.security.kerberos.web.authentication.SpnegoEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.nio.file.Paths;

@Configuration
@EnableWebSecurity
public class SpringWebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${app.service-principal}")
    private String servicePrincipal;

    @Value("${app.keytab-location}")
    private String keyTabLocation;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.exceptionHandling()
                .authenticationEntryPoint(spnegoEntryPoint())
                .and().authorizeRequests()
                .antMatchers("/nonsecured").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginPage("/login").permitAll()
                .and()
                .logout().permitAll()
                .and()
                .addFilterBefore(spnegoAuthenticationProcessingFilter( authenticationManagerBean()),
                        BasicAuthenticationFilter.class);

    }

    @Bean
    public AuthenticationManager anAuthenticationManager() throws Exception {
        return authenticationManager();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(kerberosAuthenticationProvider())
                .authenticationProvider(kerberosServiceAuthenticationProvider());
    }

    @Bean
    public KerberosAuthenticationProvider kerberosAuthenticationProvider() {
        KerberosAuthenticationProvider kerberosAuthenticationProvider = new KerberosAuthenticationProvider();
        kerberosAuthenticationProvider.setKerberosClient(getSunJaasKerberosClient());
        kerberosAuthenticationProvider.setUserDetailsService(myUserDetailsService());
        return kerberosAuthenticationProvider;
    }

    private SpnegoEntryPoint spnegoEntryPoint() {
        return new SpnegoEntryPoint("/login");
    }

    @Bean
    public SpnegoAuthenticationProcessingFilter spnegoAuthenticationProcessingFilter(AuthenticationManager authenticationManager) {
        SpnegoAuthenticationProcessingFilter spnegoAuthenticationProcessingFilter = new SpnegoAuthenticationProcessingFilter();
        spnegoAuthenticationProcessingFilter.setAuthenticationManager(authenticationManager);
        return spnegoAuthenticationProcessingFilter;
    }

    @Bean
    public KerberosServiceAuthenticationProvider kerberosServiceAuthenticationProvider() {
        KerberosServiceAuthenticationProvider kerberosServiceAuthenticationProvider = new KerberosServiceAuthenticationProvider();
        kerberosServiceAuthenticationProvider.setTicketValidator(sunJaasKerberosTicketValidator());
        kerberosServiceAuthenticationProvider.setUserDetailsService(myUserDetailsService());
        return kerberosServiceAuthenticationProvider;
    }

    @Bean
    public SunJaasKerberosTicketValidator sunJaasKerberosTicketValidator() {
        SunJaasKerberosTicketValidator KerberosTicketValidator = new SunJaasKerberosTicketValidator();
        KerberosTicketValidator.setServicePrincipal(servicePrincipal);
        KerberosTicketValidator.setKeyTabLocation(new FileSystemResource(Paths.get(keyTabLocation).normalize().toAbsolutePath().toString()));
        KerberosTicketValidator.setDebug(true);
        return KerberosTicketValidator;
    }
    @Bean
    public UserDetailsService myUserDetailsService() {
        return new MyUserDetailsService();
    }

    @Bean
    public SunJaasKerberosClient getSunJaasKerberosClient () {
        SunJaasKerberosClient kerberosClient = new SunJaasKerberosClient();
        kerberosClient.setDebug(true);
        return kerberosClient;
    }

}
