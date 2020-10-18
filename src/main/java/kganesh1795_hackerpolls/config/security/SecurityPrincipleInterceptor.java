package kganesh1795_hackerpolls.config.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.SmartView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import kganesh1795_hackerpolls.model.Admin;
import kganesh1795_hackerpolls.model.Candidate;

public class SecurityPrincipleInterceptor extends HandlerInterceptorAdapter {
	public static boolean isRedirectView(ModelAndView mv) {
	    String viewName = mv.getViewName();
	    if (viewName.startsWith("redirect:/")) {
	        return true;
	    }
	    View view = mv.getView();
	    return (view != null && view instanceof SmartView
	      && ((SmartView) view).isRedirectView());
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object o, ModelAndView mv) {
		if(mv != null && !isRedirectView(mv)) {
			UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if(user.getUsername().matches("admin")) {
				Admin currentUser = (Admin) user;				
				mv.addObject("currentUser", currentUser);
			}
			else {
				Candidate currentUser = (Candidate) user;
				mv.addObject("currentUser", currentUser);
			}
		}
	}
}
