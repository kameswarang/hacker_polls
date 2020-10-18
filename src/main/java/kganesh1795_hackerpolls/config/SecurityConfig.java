package kganesh1795_hackerpolls.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private UserDetailsService candidateService;

	@Autowired
	private UserDetailsService adminService;
	
	@Autowired
	private AuthenticationSuccessHandler roleBasedAuthSuccessHandler;
	
	@Bean
	public PasswordEncoder passEnc() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder authMB) throws Exception {
		authMB.userDetailsService(this.candidateService).passwordEncoder(passEnc());

		authMB.userDetailsService(adminService).passwordEncoder(passEnc());
	}

	@Override
	protected void configure(HttpSecurity httpSec) throws Exception {
		httpSec.authorizeRequests()
			.antMatchers("/").anonymous()
			.antMatchers("/admin").hasRole("ADMIN")
			.antMatchers("/user").hasRole("USER")
			.antMatchers("/login").anonymous()
			.antMatchers("/logout").authenticated()
			.and().formLogin()
				.loginPage("/login")
				.successHandler(this.roleBasedAuthSuccessHandler)
			.and().logout().logoutSuccessUrl("/");
	}
}