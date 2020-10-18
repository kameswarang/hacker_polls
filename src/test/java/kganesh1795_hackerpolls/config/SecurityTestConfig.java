package kganesh1795_hackerpolls.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import kganesh1795_hackerpolls.model.Admin;
import kganesh1795_hackerpolls.model.Candidate;

@TestConfiguration
public class SecurityTestConfig {
	@Bean
	public UserDetailsService testCandidateService() {
		Candidate user = new Candidate("testUser", "password");
		return new InMemoryUserDetailsManager(user);
	}

	@Bean
	public UserDetailsService testAdminService() {
		Admin admin  = new Admin("admin", "superadmin");
		return new InMemoryUserDetailsManager(admin);
	}
}