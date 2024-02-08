package study.datajpa.dto;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item implements Persistable<String>{
	//genereatedValue를 사용하지 않는 경우에는 persistable을 상속받아서 
	//해당 객체를 저장할 때 신규인지 아닌지를 isNew함수를 이용해서 판단한다.
	@Id
	private String id;
	
	@CreatedDate
	private LocalDateTime createdDate;
	
	public Item(String id) {
		this.id = id;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public boolean isNew() {
		return createdDate == null; 
	}
	
	
}
