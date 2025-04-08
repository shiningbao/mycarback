package com.packt.cardatabase.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // 하이버네이트가 생성한 필드를 무시하도록
public class Owner {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long ownerId;
	private String firstname, lastname;
	
	public Owner() {}
	
	public Owner(String firstname, String lastname) {
		super();
		this.firstname = firstname;
		this.lastname = lastname;
	}
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="owner") // cascade를 ALL로 설정하면 모든 작업이 연속 적용됨, mappedBy="owner"는 Car 클래스(연관관계의 주인)에 있는 owner 필드가 이 관계의 기본키임을 지정함
	@JsonIgnore // 양방향 관계일 때 자동차가 직렬화되면 연결된 소유자가 직렬화되고 이어서 그가 소유한 자동차가 다시 직렬화되기 때문에 한 쪽의 직렬화를 막기 위해 추가
	private List<Car> cars; // 소유자는 자동차를 여러 대 가질 수 있으므로 필드 형식은 List<Car>

	public List<Car> getCars() {
		return cars;
	}

	public void setCars(List<Car> cars) {
		this.cars = cars;
	}

	public long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(long ownerId) {
		this.ownerId = ownerId;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	
}
