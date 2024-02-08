 package study.datajpa.repository;

import java.util.Collection; 
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.QueryHint;
import study.datajpa.dto.MemberDto;
import study.datajpa.dto.UserNameOnlyDto;
import study.datajpa.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom{
	List<Member> findByUserNameAndAgeGreaterThan(String userName, int age);
	
	@Query("select m from Member m where m.userName = :userName and m.age = :age")
	List<Member> findByUserNameAndAge(@Param("userName") String userName, @Param("age") int age);
	
	@Query("select m.userName from Member m")
	List<String> findUsernameList();
	
	//return 값이 Dto인 경우
	@Query("select new study.datajpa.dto.MemberDto(m.id, m.userName, t.name) from Member m join m.team t")
	List<MemberDto> findMemberDto();
	
	@Query("select m from Member m where m.userName in :userNames")
	List<Member> findBynames(@Param("userNames") Collection<String> userNames);
	
//	@Query(value = "select m, t from Member m left join m.team t", 
//			countQuery = "select count(m.userName) from Member m")
	Page<Member> findByAge(int age, Pageable pageable);
	
	//bulk update
	@Modifying(clearAutomatically = true) //쿼리실행시 자동으로 flush, clear하는 옵션
	@Query("update Member m set m.age = m.age + 1 where m.age >= :age")
	int bulkAgePlus(@Param("age") int age);
	
	@Query("select m from Member m left join fetch m.team")
	List<Member> findMemberFetchJoin();
	
	@Override
	@EntityGraph(attributePaths = {"team"})
	List<Member> findAll();
	
	@EntityGraph(attributePaths = {"team"})
	@Query("select m from Member m")
	List<Member> findMemberEntityGraph();
	
	@EntityGraph(attributePaths = {"team"})
	List<Member> findEntityGraphByUserName(@Param("userName") String userName);
	
	@QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value="true") )
	Member findReadOnlyByUserName(String userName);
	
//	List<UserNameOnlyDto> findProjectionBy();
}
