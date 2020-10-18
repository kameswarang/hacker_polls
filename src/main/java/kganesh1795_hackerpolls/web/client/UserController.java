package kganesh1795_hackerpolls.web.client;

import java.util.List;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kganesh1795_hackerpolls.dto.SkillsCollectionDto;
import kganesh1795_hackerpolls.model.Candidate;
import kganesh1795_hackerpolls.model.Candidate.Skill;
import kganesh1795_hackerpolls.service.CandidateService;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private CandidateService candidateService;
	
	private List<Candidate> candidates;

	@GetMapping({"", "/profile"})
	public String getProfile(Model model, @AuthenticationPrincipal(errorOnInvalidType = true) UserDetails principle) {
		Candidate profile = (Candidate) candidateService.loadUserByUsername(principle.getUsername());
		model.addAttribute("profile", profile);
		
		return "profile";
	}
	
	@GetMapping("/profile/edit")
	public String editProfile(@RequestParam Integer skills, @AuthenticationPrincipal(errorOnInvalidType = true) UserDetails principle, Model model) {
		Candidate profile = (Candidate) candidateService.loadUserByUsername(principle.getUsername());
		model.addAttribute("profile", profile);
		
		SkillsCollectionDto skillsDto = new SkillsCollectionDto();
		for(int i = 0; i < skills; i++) {
			skillsDto.addSkill(new Skill());
		}
				
		model.addAttribute("skillsDto", skillsDto);
		
		return "editProfile";
	}
	
	@PostMapping("/profile/edit")
	public String processEdit(@ModelAttribute("profile") @Valid Candidate profile, Errors error, @AuthenticationPrincipal(errorOnInvalidType = true) UserDetails principle, Model model) {
		if (error.hasErrors()) {
			SkillsCollectionDto skillsDto = new SkillsCollectionDto();
			for(int i = 0; i < 1; i++) {
				skillsDto.addSkill(new Skill());
			}
			model.addAttribute("skillsDto", skillsDto);
			model.addAttribute("skills", 1);
			return "editProfile";
		}
		
		Candidate profileOG = (Candidate) candidateService.loadUserByUsername(principle.getUsername());

		// don't allow received form data to manipulate the poll
		profile.setVotes(profileOG.getVotes());
		profile.setVoted(profileOG.getVoted());
		profile.setPollStatus(profileOG.getPollStatus());
		
		candidateService.updateCandidate(profile);
		return "redirect:/user/profile";
	}
	
	@PostMapping("/profile/addSkill")
	public String addSkill(@ModelAttribute("skillsDto") @Valid SkillsCollectionDto skillsDto, Errors errors, @AuthenticationPrincipal(errorOnInvalidType = true) UserDetails principle, Model model) {
		Candidate profile = (Candidate) candidateService.loadUserByUsername(principle.getUsername());

		if(errors.hasErrors()) {
			model.addAttribute("profile", profile);
			model.addAttribute("skills", 0);
			
			return "editProfile";
		}
		
		profile.addSkills(skillsDto.getSkills());
		candidateService.updateCandidate(profile);
		
		return "redirect:/user/profile";
	}
	
	@GetMapping("/poll")
	public String getPoll(@AuthenticationPrincipal(errorOnInvalidType = true) UserDetails principle, Model model) {
		Candidate profile = (Candidate) candidateService.loadUserByUsername(principle.getUsername());

		String voted = profile.getVoted().toString();		
		if(voted.matches("false")) {
			this.candidates = candidateService.getAllPollingCandidates();
			model.addAttribute("candidates", this.candidates);
		}
		model.addAttribute("voted", voted);
		
		return "poll";
	}
	
	@PostMapping("/poll")
	public String postPoll(String votedFor, @AuthenticationPrincipal(errorOnInvalidType = true) UserDetails principle) {
		Candidate profile = (Candidate) candidateService.loadUserByUsername(principle.getUsername());

		try {
			Candidate c = (Candidate) this.candidateService.loadUserByUsername(votedFor);
			if(c.getPollStatus() == true) {
				c.addOneVote();
				candidateService.updateCandidate(c);
				
				if(!profile.getUsername().matches("testUser")) {
					profile.setVoted(true);
					candidateService.updateCandidate(profile);
				}
			}
		}

		catch(UsernameNotFoundException e)  {
			return "redirect:/user/poll";			
		}
		
		return "redirect:/user/poll";
	}
}
