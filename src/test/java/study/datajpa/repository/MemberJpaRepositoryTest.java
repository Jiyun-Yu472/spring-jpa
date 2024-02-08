package study.datajpa.repository;



import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import study.datajpa.entity.Member;

@SpringBootTest
@Transactional
public class MemberJpaRepositoryTest {
	@Autowired MemberJpaRepository memberJpaRepository;
	
	@Test
	public void testsMember() {
		Member member = new Member("memberA");
		Member savedMember = memberJpaRepository.save(member);
		
		Member findMember = memberJpaRepository.find(savedMember.getId());
		
		assertThat(findMember.getId()).isEqualTo(member.getId());
		assertThat(findMember.getUserName()).isEqualTo(member.getUserName());
		assertThat(findMember).isEqualTo(member);
	}
	
	@Test
	public void basicCRUD() {
		Member member1 = new Member("Member1");
		Member member2 = new Member("Member2");
		memberJpaRepository.save(member1);
		memberJpaRepository.save(member2);
		
		//단건 조회 검증
		Optional<Member> byId1 = memberJpaRepository.findById(member1.getId()).ofNullable(member1);
		Member findMember1 = byId1.get();
		
		Optional<Member> byId2 = memberJpaRepository.findById(member2.getId()).ofNullable(member2);
		Member findMember2 = byId2.get();
		
		assertThat(findMember1).isEqualTo(member1);
		assertThat(findMember2).isEqualTo(member2);
		
		//리스트 조회 검증
		List<Member> all = memberJpaRepository.findAll();
		assertThat(all.size()).isEqualTo(2);
		
		//카운트 검증
		long count = memberJpaRepository.count();
		assertThat(count).isEqualTo(2);
		
		memberJpaRepository.delete(member1);
		memberJpaRepository.delete(member2);
		
		long deletedcount = memberJpaRepository.count();
		assertThat(deletedcount).isEqualTo(0);
	}
	
	@Test
	public void findByUsernameAndAgeGreaterThen() {
		Member m1 = new Member("AAA", 10);
		Member m2 = new Member("AAA", 20);
		memberJpaRepository.save(m1);
		memberJpaRepository.save(m2);
		
		List<Member> result = memberJpaRepository.findByUsernameAndAgeGreaterThan("AAA", 15);
		
		assertThat(result.get(0).getUserName()).isEqualTo("AAA");
		assertThat(result.get(0).getAge()).isEqualTo(20);
		assertThat(result.size()).isEqualTo(1);
	}
	
	@Test
	public void paging() {
		//given
		Member m1 = new Member("AAA", 20);
		Member m2 = new Member("AAA", 20);
		Member m3 = new Member("AAA", 20);
		Member m4 = new Member("AAA", 20);
		Member m5 = new Member("AAA", 20);
		memberJpaRepository.save(m1);
		memberJpaRepository.save(m2);
		memberJpaRepository.save(m3);
		memberJpaRepository.save(m4);
		memberJpaRepository.save(m5);
		
		int age = 20;
		int offset = 0;
		int limit = 3;
		
		//when
		List<Member> members = memberJpaRepository.findByPage(age, offset, limit);
		long totalCount = memberJpaRepository.totalCount(age);
		
		//then
		assertThat(members.size()).isEqualTo(3);
		assertThat(totalCount).isEqualTo(5);
	}
}