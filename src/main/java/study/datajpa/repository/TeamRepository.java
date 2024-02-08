package study.datajpa.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import study.datajpa.entity.Team;

@Repository
public class TeamRepository {
	@PersistenceContext private EntityManager em;
	
	public Team save(Team team) {
		em.persist(team);
		return team;
	}
	
	public void delete(Team team) {
		em.remove(team);
	}
	
	public List<Team> findAll() {
		return em.createQuery("select m from Team t)", Team.class)
				.getResultList();
	}
	
	public Optional<Team> findById(Long id) {
		Team team = em.find(Team.class, id);
		return Optional.ofNullable(team);
	}
	
	public long count() {
		return em.createQuery("select count(t) from Team m", Long.class)
				.getSingleResult();
	}
	
	public Team find(Long id) {
		return em.find(Team.class, id);
	}
}
