package kganesh1795_hackerpolls.model;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@Document(collection = "candidate")
public class Candidate implements UserDetails {
	private static final long serialVersionUID = 1495006788422613180L;

	@Data
	@NoArgsConstructor
	public static class Skill {		
		@Id
		@NotNull(message = "Provide a name for the skill")
		@NotBlank(message = "Provide a name for the skill")
		String name;
		
		@NotNull(message="Provide a expertise for the skill")
		@Min(value=1, message="Minimum value allowed is 1")
		@Max(value=5, message="Maximum value allowed is 5")
		Integer expertise;
	}
	
	@Id
	@NonNull
	@NotBlank(message = "Provide a username atleast 5 characters long")
	@Size(min = 5, message = "Must be atleast five characters long")
	private String username;

	@NonNull	
	@NotBlank(message = "Provide a password atleast 5 characters long")
	@Size(min = 5, message = "Must be atleast five characters long")
	@JsonIgnore
	private String password;

	@SuppressWarnings("unused")
	private Collection<GrantedAuthority> authorities;

	@NotBlank(message = "Provide a name atleast 2 characters long")
	@Size(min = 2, message = "Must be atleast two characters long")
	private String firstName;

	@NotBlank(message = "Provide a name atleast 2 characters long")
	@Size(min = 2, message = "Must be atleast two characters long")
	private String lastName;
	
	public String getName() {
		return this.firstName + " " + this.lastName;
	}
	
	@Min(value=1, message="Minimum value allowed is 1")
	private Integer challengesSolved;
	
	@Min(value=1, message="Minimum value allowed is 1") 
	@Max(value=5, message="Maximum value allowed is 5")
	private Integer expertiseLevel;
	
	@Min(value=0, message="Votes cannot be lesser than 0")	
	private Integer votes;
	public void addOneVote() {
		this.votes += 1;
	}
	
	private Boolean voted;
	
	private Boolean pollStatus;
	
	@Valid
	private List<Skill> skills;
	public void addSkill(Skill s) {
		skills.add(s);
	}
	public void addSkills(List<Skill> ss) {
		if(this.skills == null) {
			this.skills = ss;
		}
		else {
			this.skills.addAll(ss);			
		}
	}
	
	public Candidate() {
		this.votes = 0;
		this.voted = false;
		this.pollStatus = false;
	}
	
	public Candidate(String u, String p) {
		this.username = u;
		this.password = p;
		this.votes = 0;
		this.voted = false;
		this.pollStatus = false;
	}
	
	public Candidate(String u, String p, String f, String l) {
		this.username = u;
		this.password = p;
		this.firstName = f;
		this.lastName = l;
		this.votes = 0;
		this.voted = false;
		this.pollStatus = false;
	}
	
	@Override
	@JsonIgnore
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
	}

	@Override
	@JsonIgnore
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	@JsonIgnore
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	@JsonIgnore
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	@JsonIgnore
	public boolean isEnabled() {
		return true;
	}
}