package kr.ac.hansung.cse.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity	//Hibernate에 의해 테이블로 만들어짐
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Customer implements Serializable {	//Json포맷으로 Client로 데이터를 보내기 때문에 Serializable이 필요함

	/**
	 * 
	 */
	private static final long serialVersionUID = 4577239542252345341L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "customer_id")
	private Long id;

	@Column(name = "name")
	private String name;

	@Column(name = "age")
	private int age;

	@Column(name = "active")
	private boolean active;

	public Customer(String name, int age) {
		this.name = name;
		this.age = age;
		this.active = false;
	}

}