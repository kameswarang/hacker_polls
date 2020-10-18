package kganesh1795_hackerpolls.dto;

import java.util.LinkedList;
import java.util.List;

import javax.validation.Valid;

import kganesh1795_hackerpolls.model.Candidate.Skill;
import lombok.Data;
import lombok.Setter;

@Data
public class SkillsCollectionDto {
	@Setter
	@Valid
	private List<Skill> skills;
	
	public SkillsCollectionDto() {
		this.skills = new LinkedList<>();
	}
	
	public void addSkill(Skill s) {
		this.skills.add(s);
	}
}
