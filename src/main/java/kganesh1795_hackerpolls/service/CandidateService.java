package kganesh1795_hackerpolls.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import kganesh1795_hackerpolls.model.Candidate;
import kganesh1795_hackerpolls.repository.CandidateRepository;
import kganesh1795_hackerpolls.service.interfaces.CustomCandidateService;


@Service
public class CandidateService implements UserDetailsService, CustomCandidateService {
	private CandidateRepository candidateRepo;
	private PasswordEncoder passEnc;
	private Logger logger;
	
	@PostConstruct
	public void initLogger() {
		this.logger = LoggerFactory.getLogger(CandidateService.class);
	}
	
	@Autowired
	public CandidateService(CandidateRepository ur, PasswordEncoder pe) {
		this.candidateRepo = ur;
		this.passEnc = pe;
	}

	@Override
	public UserDetails loadUserByUsername(String u) throws UsernameNotFoundException {
		Candidate candidate = candidateRepo.findByUsername(u);
		if (candidate != null) {
			this.logger.debug(candidate.toString());
			return candidate;
		}
		else {
			throw new UsernameNotFoundException("User '" + u + "' not found");
		}
	}

	public Candidate saveCandidate(Candidate u) {
		u.setPassword(passEnc.encode(u.getPassword()));
		return candidateRepo.insert(u);
	}
	
	public Candidate updateCandidate(Candidate c) {
		return candidateRepo.save(c);
	}
	
	public void deleteCandidate(Candidate c) {
		candidateRepo.delete(c);
	}
	
	public List<Candidate> getAllCandidates() {
		List<Candidate> candidates = candidateRepo.findAll();
		
		return candidates;
	}
	
	public List<Candidate> getAllPollingCandidates() {
		List<Candidate> candidates = this.getAllCandidates();
		List<Candidate> pollingCandidates = candidates.stream().filter(c -> c.getPollStatus()==true).collect(Collectors.toList());
		return pollingCandidates;
	}
}