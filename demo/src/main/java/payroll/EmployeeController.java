package payroll;

import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.hateoas.CollectionModel;

@RestController
public class EmployeeController {

	private final EmployeeRepository repository;

	private final EmployeeModelAssembler assembler;

	public EmployeeController(EmployeeRepository repository, EmployeeModelAssembler assembler) {
		this.repository = repository;
		this.assembler = assembler;
	}

//	@GetMapping("/employees")
//	List<Employee> all() {
//		return repository.findAll();
//	}

	@GetMapping("/employees")
	CollectionModel<EntityModel<Employee>> all() {
//		List<EntityModel<Employee>> employees = repository.findAll().stream()
//				.map(employee -> new EntityModel<>(employee,
//						linkTo(methodOn(EmployeeController.class).one(employee.getId())).withSelfRel(),
//						linkTo(methodOn(EmployeeController.class).all()).withRel("employees")))
//				.collect(Collectors.toList());

		List<EntityModel<Employee>> employees = repository.findAll().stream().map(assembler::toModel)
				.collect(Collectors.toList());
		return new CollectionModel<>(employees, linkTo(methodOn(EmployeeController.class).all()).withSelfRel());
	}

//	@PostMapping("/employees")
//	Employee newEmployee(@RequestBody Employee newEmployee) {
//		return repository.save(newEmployee);
//	}

	@PostMapping("/employees")
	ResponseEntity<?> newEmployee(@RequestBody Employee newEmployee) throws URISyntaxException {
		EntityModel<Employee> entityModel = assembler.toModel(repository.save(newEmployee));
		return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
	}

//	@GetMapping("/employees/{id}")
//	Employee one(@PathVariable Long id) {
//		return repository.findById(id).orElseThrow(()-> new EmployeeNotFoundException(id));
//	}

	@GetMapping("/employees/{id}")
	EntityModel<Employee> one(@PathVariable Long id) {
		Employee employee = repository.findById(id).orElseThrow(() -> new EmployeeNotFoundException(id));

//		return new EntityModel<>(employee, linkTo(methodOn(EmployeeController.class).one(id)).withSelfRel(),
//				linkTo(methodOn(EmployeeController.class).all()).withRel("employees"));

		return assembler.toModel(employee);
	}

//	@PutMapping("/employees/{id}")
//	Employee replaceEmployee(@RequestBody Employee newEmployee, @PathVariable Long id) {
//		return repository.findById(id).map(Employee -> {
//			Employee.setName(newEmployee.getName());
//			Employee.setRole(newEmployee.getRole());
//			return repository.save(Employee);
//		}).orElseGet(() -> {
//			newEmployee.setId(id);
//			return repository.save(newEmployee);
//		});
//	}

	@PutMapping("/employees/{id}")
	ResponseEntity<?> replaceEmployee(@RequestBody Employee newEmployee, @PathVariable Long id)
			throws URISyntaxException {
		Employee updatedEmployee = repository.findById(id).map(employee -> {
			employee.setName(newEmployee.getName());
			employee.setRole(newEmployee.getRole());
			return repository.save(employee);
		}).orElseGet(() -> {
			newEmployee.setId(id);
			return repository.save(newEmployee);
		});

		EntityModel<Employee> entityModel = assembler.toModel(updatedEmployee);

		return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
		// GetRequiredLink () yöntemini kullanarak, SELF ile EmployeeModelAssembler
		// tarafından oluşturulan bağlantıyı alabiliyoruz.
	}

//	@DeleteMapping("/employees/{id}")
//	void deleteEmployee(@PathVariable Long id) {
//		repository.deleteById(id);
//	}

	@DeleteMapping("/employees/{id}")
	ResponseEntity<?> deleteEmployee(@PathVariable Long id) {
		repository.deleteById(id);
		return ResponseEntity.noContent().build();
	}

}
