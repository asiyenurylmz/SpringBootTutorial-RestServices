package payroll;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class EmployeeModelAssembler implements RepresentationModelAssembler<Employee, EntityModel<Employee>>{

	@Override
	public EntityModel<Employee> toModel(Employee employee){
		return new EntityModel<>(employee, linkTo(methodOn(EmployeeController.class).one(employee.getId())).withSelfRel(),
				linkTo(methodOn(EmployeeController.class).all()).withRel("employees"));
			//non-resource object olan Employee, resource-based bir object olan EntityModel<Employee> object'e dönüştürüldü.
	}
}
