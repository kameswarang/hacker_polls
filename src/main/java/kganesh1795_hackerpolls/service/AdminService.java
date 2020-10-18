package kganesh1795_hackerpolls.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import kganesh1795_hackerpolls.model.Admin;
import kganesh1795_hackerpolls.repository.AdminRepository;
import kganesh1795_hackerpolls.service.interfaces.CustomAdminService;


@Service
public class AdminService implements UserDetailsService, CustomAdminService {
	private AdminRepository adminRepo;
	private PasswordEncoder passEnc;

	@Autowired
	public AdminService(AdminRepository ar, PasswordEncoder pe) {
		this.adminRepo = ar;
		this.passEnc = pe;
	}
	
	@Override
	public UserDetails loadUserByUsername(String u) throws UsernameNotFoundException {
		Admin admin = adminRepo.findByUsername(u);
		if (admin != null) {
			return admin;
		} else
			throw new UsernameNotFoundException("Admin User '" + u + "' not found");
	}
	
	@Override
	public void saveAdmin(Admin a) {
		a.setPassword(passEnc.encode(a.getPassword()));
		adminRepo.save(a);
	}

	@Override
	public void updateAdmin(Admin a) {
		adminRepo.save(a);
	}
}
