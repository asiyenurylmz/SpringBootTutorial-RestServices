package payroll;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class Employee {
	@Id
	@GeneratedValue 
	private Long id;
	private String name;
	private String role;
	
	public Employee() {}
	
	Employee(String name, String role){
		this.name=name;
		this.role=role;
	}
}
