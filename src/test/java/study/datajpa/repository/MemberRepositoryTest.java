package study.datajpa.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import study.datajpa.dto.MemberDto;
import study.datajpa.dto.UserNameOnlyDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

@SpringBootTest
@Transactional
public class MemberRepositoryTest {
	@Autowired MemberRepository memberRepository;
	@Autowired TeamRepository teamRepository;
	@PersistenceContext EntityManager em;;
	
	@Test
	public void testMember() {
		Member member = new Member("MemberA");
		
		Member savedMember = memberRepository.save(member);
		
		Optional<Member> byId = memberRepository.findById(savedMember.getId());
		Member findMember = byId.orElse(member);
		
		assertThat(findMember.getId()).isEqualTo(member.getId());
		assertThat(findMember.getUserName()).isEqualTo(member.getUserName());
		assertThat(findMember).isEqualTo(member);
	}
	
	@Test
	public void findByUsernameAndAgeGreaterThen() {
		Member m1 = new Member("AAA", 10);
		Member m2 = new Member("AAA", 20);
		memberRepository.save(m1);
		memberRepository.save(m2);
		
		List<Member> result = memberRepository.findByUserNameAndAgeGreaterThan("AAA", 15);
		
		assertThat(result.get(0).getUserName()).isEqualTo("AAA");
		assertThat(result.get(0).getAge()).isEqualTo(20);
		assertThat(result.size()).isEqualTo(1);
	}
	
	@Test
	public void findByUserNameAndAge() {
		Member m1 = new Member("AAA", 10);
		Member m2 = new Member("AAA", 20);
		memberRepository.save(m1);
		memberRepository.save(m2);
		
		List<Member> result = memberRepository.findByUserNameAndAge("AAA", 20);
		
		assertThat(result.get(0).getUserName()).isEqualTo("AAA");
		assertThat(result.get(0).getAge()).isEqualTo(20);
		assertThat(result.size()).isEqualTo(1);
	}
	
	@Test
	public void findUserNameList() {
		Member m1 = new Member("AAA", 10);
		Member m2 = new Member("AAA", 20);
		memberRepository.save(m1);
		memberRepository.save(m2);
		
		List<String> userNameList = memberRepository.findUsernameList();
		
		for (String userName : userNameList) {
			assertThat(userName).isEqualTo("AAA");
		}
	}
	
	@Test
	public void findMemberDto() {
		Team team = new Team("teamA");
		teamRepository.save(team);
		
		Member m1 = new Member("AAA", 10, team);
		memberRepository.save(m1);
		
		List<MemberDto> memberDto =  memberRepository.findMemberDto();
		for (MemberDto dto : memberDto) {
			System.out.println("dto :: " + dto.toString());
		}
	}
	
	@Test
	public void findBynames() {
		Member m1 = new Member("AAA", 10);
		Member m2 = new Member("BBB", 20);
		memberRepository.save(m1);
		memberRepository.save(m2);
		
		List<Member> userList = memberRepository.findBynames(Arrays.asList("AAA", "BBB"));
		
		for (Member member : userList) {
//			System.out.println("member :: " + member);
		}
	}
	
	@Test
	public void paging() {
		//given
		Member m1 = new Member("member5", 20);
		Member m2 = new Member("member4", 20);
		Member m3 = new Member("member3", 20);
		Member m4 = new Member("member2", 20);
		Member m5 = new Member("member1", 20);
		memberRepository.save(m1);
		memberRepository.save(m2);
		memberRepository.save(m3);
		memberRepository.save(m4);
		memberRepository.save(m5);
		
		int age = 20;
		int offset = 0;
		int limit = 3;
		
		PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "userName"));
		
		//when
		Page<Member> page = memberRepository.findByAge(age, pageRequest);
		
		//dto로 변환
		Page<MemberDto> toMap = page.map(member -> new MemberDto(member.getId(), member.getUserName(), null)); 
		
		//then
		List<Member> content = page.getContent();
		long totalElements = page.getTotalElements();
		
		assertThat(content.size()).isEqualTo(3);
		assertThat(totalElements).isEqualTo(5);
		assertThat(page.getNumber()).isEqualTo(0);
		assertThat(page.getTotalPages()).isEqualTo(2);
		assertThat(page.isFirst()).isTrue();
		assertThat(page.hasNext()).isTrue();
	}
	
	@Test
	public void bulkUpdate() {
		//given
		Member m1 = new Member("member5", 20);
		Member m2 = new Member("member4", 20);
		Member m3 = new Member("member3", 20);
		Member m4 = new Member("member2", 20);
		Member m5 = new Member("member1", 20);
		memberRepository.save(m1);
		memberRepository.save(m2);
		memberRepository.save(m3);
		memberRepository.save(m4);
		memberRepository.save(m5);
		
		int resultCount = memberRepository.bulkAgePlus(20); //bulk update연산은 DB의 데이터를 업데이트시킴.
		/*
		em.flush(); //영속성 컨텍스트에서 DB로 데이터 저장
		em.clear(); //영속석 컨텍스트의 데이터 초기화
		*/
		
		Optional<Member> Optionalresult = memberRepository.findById(m1.getId());
		Member result = Optionalresult.orElseGet(null);
	}
	
	@Test
	public void findMemberLazy() {
		//given
		Team teamA = new Team("teamA");
		Team teamB = new Team("teamB");
		teamRepository.save(teamA);
		teamRepository.save(teamB);
		Member member1 = new Member("member1", 10 ,teamA);
		Member member2 = new Member("member2", 10 ,teamB);
		memberRepository.save(member1);
		memberRepository.save(member2);		
		
		em.flush();
		em.clear();
		
		//when
		List<Member> members = memberRepository.findAll();
//		List<Member> members = memberRepository.findMemberFetchJoin();
		
		for (Member member : members) {
			System.out.println("member.userName :: " + member.getUserName());
		}
	}
	
	@Test
	public void queryHint() {
		Member member1 = new Member("member1", 10);
		memberRepository.save(member1);
		
		em.flush();
		em.clear();
		
		Member findMember = memberRepository.findReadOnlyByUserName(member1.getUserName());
		findMember.setUserName("member2");
	}
	
	@Test
	public void callCustomFunc() {
		Member member1 = new Member("member1", 10);
		memberRepository.save(member1);
		
		List<Member> memberList = memberRepository.findMemberCustom();
	}
	
	@Test
	public void auditingTest() {
		Member member1 = new Member("member1", 10);
		memberRepository.save(member1);
		
		List<Member> memberList = memberRepository.findAll();
		
		for (Member member : memberList) {
			System.out.println("member auditing :: " + member);
		}	
	}
	
	@Test
	public void protections() {
		Team teamA = new Team("teamA");
		teamRepository.save(teamA);
		Member member1 = new Member("member1", 10 ,teamA);
		Member member2 = new Member("member2", 10 ,teamA );
		memberRepository.save(member1);
		memberRepository.save(member2);
		
//		List<UserNameOnlyDto> m11 = memberRepository.findProjectionBy();
//		
//		for (UserNameOnlyDto userNameOnly : m11) {
//			System.out.println(" " + userNameOnly);
//		}
		
	}
}



















