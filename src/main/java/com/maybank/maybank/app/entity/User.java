package com.maybank.maybank.app.entity;



import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class User{

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private int Id;
	
	@Column(name = "user_name")
	private String userName;
	
	@Column(name = "password")
	private String password;
			
	//@Column(name = "user_type")
//	private String userType;
	
	@Column(name="role")
	@Enumerated(EnumType.STRING)
	private Role role;
	

	

	

	
	
}
	
	
	

	