package kganesh1795_hackerpolls.config.security;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class RoleBasedAuthSuccessHandler implements AuthenticationSuccessHandler {
	private RedirectStrategy rdrStgy = new DefaultRedirectStrategy();
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		rdrStgy.sendRedirect(request, response, targetUrl(authentication));
	}

	private String targetUrl(final Authentication auth) {
		Map<String, String> roleTargetUrlMap = new HashMap<>();
		
		roleTargetUrlMap.put("ROLE_USER", "/user/profile");
		roleTargetUrlMap.put("ROLE_ADMIN", "/admin/profiles");
		
		final Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
		for(GrantedAuthority ga : authorities) {
			if(roleTargetUrlMap.containsKey(ga.getAuthority())) {
				return roleTargetUrlMap.get(ga.getAuthority());
			}
		}
		
		throw new IllegalStateException();
	}
}