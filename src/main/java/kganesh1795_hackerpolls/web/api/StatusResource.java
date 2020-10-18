package kganesh1795_hackerpolls.web.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kganesh1795_hackerpolls.model.Candidate;
import kganesh1795_hackerpolls.service.CandidateService;

@RestController
@RequestMapping("/status")
public class StatusResource {
	@Autowired
	CandidateService candidateService;
	
	@GetMapping("/get")
	public List<Candidate> getStatus() {
		return candidateService.getAllPollingCandidates();
	}
}
