package kganesh1795_hackerpolls.web.client;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import kganesh1795_hackerpolls.model.Candidate;
import kganesh1795_hackerpolls.service.CandidateService;


@Controller
@RequestMapping("/register")
public class RegistrationController {
	@Autowired
	private CandidateService candidateService;

	@GetMapping
	public String register(Model model) {
		model.addAttribute(new Candidate());
		return "register";
	}

	@PostMapping
	public String processRegister(@Valid Candidate newCandidate, Errors error) {
		if (error.hasErrors()) {
			return "register";
		}
		candidateService.saveCandidate(newCandidate);
		return "redirect:/login?registrationSuccess";
	}
}