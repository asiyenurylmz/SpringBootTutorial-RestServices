package payroll;

public class EmployeeNotFoundException extends RuntimeException{
	public EmployeeNotFoundException(Long id) {
		super("Could not found exception"+ id);
	}
}
