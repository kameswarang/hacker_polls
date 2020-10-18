package kganesh1795_hackerpolls.service.interfaces;

import kganesh1795_hackerpolls.model.Candidate;

public interface CustomCandidateService {
	public Candidate saveCandidate(Candidate newUser);
	public Candidate updateCandidate(Candidate c);
	public void deleteCandidate(Candidate c);
}
