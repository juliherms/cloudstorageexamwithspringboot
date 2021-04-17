package com.github.juliherms.cloudstorage.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.github.juliherms.cloudstorage.service.AuthenticationService;

/**
 * This class responsible to enable and configure spring security
 * 
 * @author jlv
 *
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private AuthenticationService authenticationService;

	public SecurityConfig(AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) {
		auth.authenticationProvider(this.authenticationService);
	}

	/**
	 * This method responsible to configure access route
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.headers().frameOptions().disable().and().csrf().disable();
        http.authorizeRequests()
                .antMatchers("/signup", "/css/**", "/js/**", "/h2/**").permitAll()
                .anyRequest().authenticated();

        http.formLogin()
                .loginPage("/login")
                .permitAll().and().logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login");
        http.formLogin()
                .defaultSuccessUrl("/home", true);
    }

}