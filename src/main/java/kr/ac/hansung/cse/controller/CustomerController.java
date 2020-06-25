package kr.ac.hansung.cse.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.ac.hansung.cse.model.Customer;
import kr.ac.hansung.cse.repo.CustomerRepository;

//@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
public class CustomerController {

	@Autowired
	CustomerRepository repository;

	@GetMapping("/customers")
	public ResponseEntity<List<Customer>> getAllCustomers() {
		List<Customer> customers = new ArrayList<>();
		try {
			repository.findAll().forEach(customers::add);	//DB를 조회해서 모든 레코드를 조회한다.

			if (customers.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(customers, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/customers/{id}")
	public ResponseEntity<Customer> getCustomerById(@PathVariable("id") long id) {
		Optional<Customer> customerData = repository.findById(id);	//자료형이 Optional인 이유는 customer가  있을 수도 있고 없을 수도 있기 때문이다.

		if (customerData.isPresent()) {
			return new ResponseEntity<>(customerData.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping(value = "/customers")
	public ResponseEntity<Customer> postCustomer(@RequestBody Customer customer) {
		try {
			Customer _customer = repository.save(new Customer(customer.getName(), customer.getAge()));	//인자로 넘어온 customer를 저장해서 리턴
			return new ResponseEntity<>(_customer, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED);
		}
	}

	@DeleteMapping("/customers/{id}")	//customer 한 명 지우기
	public ResponseEntity<HttpStatus> deleteCustomer(@PathVariable("id") long id) {
		try {
			repository.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}
	}

	@DeleteMapping("/customers")		//customer 싹 지우기
	public ResponseEntity<HttpStatus> deleteAllCustomers() {
		try {
			repository.deleteAll();
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}

	}

	@GetMapping(value = "customers/age/{age}")	//age를 바탕으로 조회해서 리턴
	public ResponseEntity<List<Customer>> findByAge(@PathVariable int age) {
		try {
			List<Customer> customers = repository.findByAge(age);

			if (customers.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(customers, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}
	}

	@PutMapping("/customers/{id}")		//customer의 정보 수정
	public ResponseEntity<Customer> updateCustomer(@PathVariable("id") long id, @RequestBody Customer customer) {
		Optional<Customer> customerData = repository.findById(id);

		if (customerData.isPresent()) {
			Customer _customer = customerData.get();
			_customer.setName(customer.getName());
			_customer.setAge(customer.getAge());
			_customer.setActive(customer.isActive());
			return new ResponseEntity<>(repository.save(_customer), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}