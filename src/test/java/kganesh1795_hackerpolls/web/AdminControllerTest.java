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
import kganesh1795_hackerpolls.model.Admin;


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = SecurityTestConfig.class)
@AutoConfigureMockMvc
public class AdminControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@Test
	public void get_admin_without_login_should_redirect_to_login() throws Exception {
		mockMvc.perform(get("/admin")).andExpect(redirectedUrlPattern("**/login"));
	}

	@Test
	@WithUserDetails(userDetailsServiceBeanName = "testAdminService", value = "admin")
	public void get_admin_with_login_should_return_view_home_and_model_with_username() throws Exception {
		ModelAndView mv = mockMvc.perform(get("/admin")).andReturn().getModelAndView();

		assertEquals(mv.getViewName(), "admin");
		assertEquals(((Admin) mv.getModel().get("admin")).getUsername(), "admin");
	}
}