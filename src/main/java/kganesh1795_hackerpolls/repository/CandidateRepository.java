package kganesh1795_hackerpolls.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import kganesh1795_hackerpolls.model.Candidate;


public interface CandidateRepository extends MongoRepository <Candidate, String> {
	public Candidate findByUsername(String username);
	public Candidate findByFirstName(String firstName);
	public List<Candidate> findAll();
}