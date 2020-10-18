package kganesh1795_hackerpolls.repository;

import org.springframework.data.repository.CrudRepository;

import kganesh1795_hackerpolls.model.Admin;


public interface AdminRepository extends CrudRepository<Admin, String> {
	public Admin findByUsername(String username);
}