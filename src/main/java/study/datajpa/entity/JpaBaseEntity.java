package study.datajpa.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

@MappedSuperclass 
public class JpaBaseEntity {
	@Column(updatable = false)
	private LocalDateTime createdDate;
	private LocalDateTime updatedDate;
	
	@PrePersist
	public void prePersist() {
		LocalDateTime now = LocalDateTime.now();
		createdDate = now;
		updatedDate = now;
	}
	
	@PreUpdate
	public void preUpdate() {
		updatedDate = LocalDateTime.now();
	}
}
