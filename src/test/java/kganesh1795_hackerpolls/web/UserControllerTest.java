package kganesh1795_hackerpolls.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.ModelAndView;

import kganesh1795_hackerpolls.config.SecurityTestConfig;
import kganesh1795_hackerpolls.model.Candidate;


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = SecurityTestConfig.class)
@AutoConfigureMockMvc
public class UserControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@Test
	public void get_home_without_user_should_redirect_to_login() throws Exception {
		mockMvc.perform(get("/")).andExpect(redirectedUrlPattern("**/login"));
	}

	@Test
	@WithUserDetails(userDetailsServiceBeanName = "testCandidateService", value = "testUser")
	public void get_home_with_user_should_return_view_home_and_model_with_username() throws Exception {
		ModelAndView mv = mockMvc.perform(get("/home")).andReturn().getModelAndView();

		assertEquals(mv.getViewName(), "home");
		assertEquals(((Candidate) mv.getModel().get("candidate")).getUsername(), "testUser");
	}
	
	/*@Test
	@WithUserDetails(userDetailsServiceBeanName = "testAdminService", value = "admin")
	public void get_home_with_admin_login_should_return_view_home_and_username_as_admin() throws Exception {
		ModelAndView mv = mockMvc.perform(get("/home")).andReturn().getModelAndView();

		assertEquals(mv.getViewName(), "home");
		assertEquals(((Admin) mv.getModel().get("user")).getUsername(), "admin");
	}*/

}