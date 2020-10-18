package kganesh1795_hackerpolls;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import kganesh1795_hackerpolls.model.Admin;
import kganesh1795_hackerpolls.model.Candidate;
import kganesh1795_hackerpolls.service.AdminService;
import kganesh1795_hackerpolls.service.CandidateService;


@Component
public class HackerPollsInitialiser {
	private Logger lgr = LoggerFactory.getLogger(HackerPollsInitialiser.class);

	@EventListener(ContextRefreshedEvent.class)
	public void databaseInitialisation(ContextRefreshedEvent e) {
		CandidateService cs = e.getApplicationContext().getBean(CandidateService.class);
		AdminService as = e.getApplicationContext().getBean(AdminService.class);
		
		try {
			cs.loadUserByUsername("testUser");
			this.lgr.info("Found testUser");
		}
		catch(UsernameNotFoundException ue) {
			this.lgr.debug(ue.getMessage());
			this.lgr.info("Couldn't find testUser. Creating new testUser");
			Candidate c = new Candidate("testUser", "password", "Test", "User");
			cs.saveCandidate(c);
		}
		
		try {
			as.loadUserByUsername("admin");
		}
		catch(UsernameNotFoundException ue) {
			String admin = System.getenv("ADMIN");
			Admin a;
			if(admin == null) {
				a = new Admin("admin", "superadmin");
			}
			else {
				a = new Admin("admin", admin);				
			}
			as.saveAdmin(a);
		}
	}
}
