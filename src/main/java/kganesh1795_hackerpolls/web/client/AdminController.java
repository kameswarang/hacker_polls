package kganesh1795_hackerpolls.web.client;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import kganesh1795_hackerpolls.dto.SkillsCollectionDto;
import kganesh1795_hackerpolls.model.Admin;
import kganesh1795_hackerpolls.model.Candidate;
import kganesh1795_hackerpolls.model.Candidate.Skill;
import kganesh1795_hackerpolls.service.AdminService;
import kganesh1795_hackerpolls.service.CandidateService;


@Controller
@RequestMapping("/admin")
public class AdminController {
	@Autowired
	AdminService adminService;

	@Autowired
	CandidateService candidateService;
	
	private List<Candidate> candidates;
	
	@GetMapping({"", "/profiles"})
	public String getHome(Model model) {
		List<Candidate> candidates = candidateService.getAllCandidates();
		
		model.addAttribute("candidates", candidates);
		
		return "admin";
	}
	
	@GetMapping("/profile/add")
	public String addProfile(Model model) {
		model.addAttribute("profile", new Candidate());

		return "addProfile";
	}
	
	@PostMapping("profile/add")
	public String processAddProfile(@ModelAttribute("profile") @Valid Candidate newCandidate, Errors error) {
		if (error.hasErrors()) {
			return "addProfile";
		}
		candidateService.saveCandidate(newCandidate);
		return "redirect:/admin/profiles";
	}
	
	@GetMapping("/profile/view")
	public String viewProfile(@RequestParam String candidate, Model model) {
		Candidate currentProfile = (Candidate) candidateService.loadUserByUsername(candidate);
		
		model.addAttribute("profile", currentProfile);
		
		return "profile";
	}
	
	@GetMapping("/profile/edit")
	public String editProfile(@RequestParam String candidate, @RequestParam Integer skills, Model model) {
		Candidate currentProfile = (Candidate) candidateService.loadUserByUsername(candidate);

		model.addAttribute("profile", currentProfile);
		
		SkillsCollectionDto skillsDto = new SkillsCollectionDto();
		for(int i = 0; i < skills; i++) {
			skillsDto.addSkill(new Skill());
		}
				
		model.addAttribute("skillsDto", skillsDto);
		
		return "editProfile";
	}
	
	@PostMapping("/profile/edit")
	public String processEdit(@ModelAttribute("profile") @Valid Candidate profile, Errors error, Model model) {
		if (error.hasErrors()) {
			SkillsCollectionDto skillsDto = new SkillsCollectionDto();
			for(int i = 0; i < 1; i++) {
				skillsDto.addSkill(new Skill());
			}
			model.addAttribute("skillsDto", skillsDto);
			model.addAttribute("skills", 1);
			return "editProfile";
		}	
		
		// don't allow received form data to manipulate votes or voting 
		Candidate c = (Candidate) this.candidateService.loadUserByUsername(profile.getUsername());
		profile.setVotes(c.getVotes());
		profile.setVoted(c.getVoted());
		
		candidateService.updateCandidate(profile);
		return "redirect:/admin/profiles";
	}
	
	@PostMapping("/profile/addSkill")
	public String addSkill(@RequestParam String candidate, @ModelAttribute("skillsDto") @Valid SkillsCollectionDto skillsDto, Errors errors, Model model) {
		Candidate profile = (Candidate) this.candidateService.loadUserByUsername(candidate);
		
		if(errors.hasErrors()) {
			System.out.println(errors);	
			model.addAttribute("profile", profile);
			model.addAttribute("skills", 0);
			
			return "editProfile";
		}
		
		profile.addSkills(skillsDto.getSkills());
		candidateService.updateCandidate(profile);
		
		return "redirect:/admin/profiles";
	}
	
	@PostMapping("/profile/delete")
	@ResponseBody
	public String deleteProfile(@RequestBody Map<String, String> payload, HttpServletRequest request) {
		Candidate c = (Candidate) candidateService.loadUserByUsername(payload.get("candidate"));
		this.candidateService.deleteCandidate(c);
		
		return "Profile " + payload.get("candidate") + " deleted";
	}
	
	@GetMapping("/poll")
	public String getPoll(@AuthenticationPrincipal(errorOnInvalidType = true) UserDetails principle, Model model) {
		Admin currentUser = (Admin) this.adminService.loadUserByUsername(principle.getUsername());

		String voted = currentUser.getVoted().toString();
		if(voted.matches("false")) {
			this.candidates = candidateService.getAllPollingCandidates();
			model.addAttribute("candidates", this.candidates);
		}
		model.addAttribute("voted", voted);
		
		return "poll";
	}
	
	@PostMapping("/poll")	
	public String postPoll(String votedFor, @AuthenticationPrincipal(errorOnInvalidType = true) UserDetails principle) {
		Admin currentUser = (Admin) this.adminService.loadUserByUsername(principle.getUsername());
		try {
			Candidate c = (Candidate) this.candidateService.loadUserByUsername(votedFor);
			if(c.getPollStatus() == true) {
				c.addOneVote();
				candidateService.updateCandidate(c);
				
				currentUser.setVoted(true);
				this.adminService.updateAdmin(currentUser);
			}
		}

		catch(UsernameNotFoundException e)  {
			return "redirect:/admin/poll";			
		}
		
		return "redirect:/admin/poll";
	}
}