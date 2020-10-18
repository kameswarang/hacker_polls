package kganesh1795_hackerpolls.web.client;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import kganesh1795_hackerpolls.model.Candidate;
import kganesh1795_hackerpolls.service.CandidateService;

@Controller
@RequestMapping("/")
public class IndexController {
	@Autowired
	CandidateService candidateService;
	
	private List<Candidate> candidates;
	
	@GetMapping
	public String getPoll(@CookieValue(value="voted", defaultValue="not set") String voted, Model model, HttpServletResponse response) {
		if(voted.matches("not set")) {
			voted = "false";
			response.addCookie(new Cookie("voted", voted));
		}
		
		if(voted.matches("false")) {
			this.candidates = candidateService.getAllPollingCandidates();
			model.addAttribute("candidates", this.candidates);
		}
		model.addAttribute("voted", voted);
		
		return "poll";
	}
	
	@PostMapping
	public String postPoll(String votedFor, @CookieValue(value="voted")String voted, HttpServletResponse response) {
		if(voted.matches("true")) {
			return "redirect:/";
		}
		
		try {
			Candidate c = (Candidate) this.candidateService.loadUserByUsername(votedFor);
			if(c.getPollStatus() == true) {
				c.addOneVote();
				candidateService.updateCandidate(c);
				response.addCookie(new Cookie("voted", "true"));
			}
		}

		catch(UsernameNotFoundException e)  {
			return "redirect:/";			
		}
		
		return "redirect:/";
	}
}
