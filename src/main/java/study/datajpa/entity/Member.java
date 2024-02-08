package study.datajpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "userName", "age"})
public class Member { 
	@Id @GeneratedValue
	@Column(name = "member_id")
	private Long id;
	private String userName;
	private int age;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="team_id")
	private Team team;
	
	public Member(String userName) {
		this.userName = userName;
	}
	
	public Member(String username, int age) {
		this.userName = username;
		this.age = age;
	}
	
	public Member(String username, int age, Team team) {
		this.userName = username;
		this.age = age;
		if(team != null) changeTeam(team);
	}
	
	public void changeUserName(String userName) {
		this.userName = userName;
	}
	
	public void changeTeam(Team team) {
		this.team = team;
		team.getMembers().add(this);
	}
}
