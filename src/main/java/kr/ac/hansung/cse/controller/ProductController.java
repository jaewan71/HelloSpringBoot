package kr.ac.hansung.cse.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.ac.hansung.cse.model.Product;
import kr.ac.hansung.cse.repo.ProductRepository;

//@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1")
public class ProductController {

	@Autowired
	ProductRepository repository;

	@GetMapping("/products")
	public ResponseEntity<List<Product>> getAllproducts() {
		List<Product> products = new ArrayList<>();
		try {
			repository.findAll().forEach(products::add);	//DB를 조회해서 모든 레코드를 조회한다.

			if (products.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(products, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/products/{id}")
	public ResponseEntity<Product> getProductById(@PathVariable("id") int id) {
		Optional<Product> productData = repository.findById(id);	//자료형이 Optional인 이유는 customer가  있을 수도 있고 없을 수도 있기 때문이다.

		if (productData.isPresent()) {
			return new ResponseEntity<>(productData.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
				
	@PostMapping(value = "/products")
	public ResponseEntity<Product> postProduct(@RequestBody Product product) {
		try {
			Product _product = repository.save(new Product(product.getName(), product.getCategory(),product.getPrice(),product.getManufacturer(),product.getUnitInStock(),product.getDescription()));	//인자로 넘어온 customer를 저장해서 리턴
			return new ResponseEntity<>(_product, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED);
		}
	}

	@DeleteMapping("/products/{id}")	//customer 한 명 지우기
	public ResponseEntity<HttpStatus> deleteProduct(@PathVariable("id") int id) {
		try {
			repository.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}
	}

	@DeleteMapping("/products")		//customer 싹 지우기
	public ResponseEntity<HttpStatus> deleteAllProducts() {
		try {
			repository.deleteAll();
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}

	}

	@GetMapping(value = "products/category/{category}")	//category를 바탕으로 조회해서 리턴
	public ResponseEntity<List<Product>> findByCategory(@PathVariable String category) {		//("id") 지워도되나
		try {
			List<Product> products = repository.findByCategory(category);

			if (products.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(products, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}
	}

	@PutMapping("/products/{id}")		//customer의 정보 수정
	public ResponseEntity<Product> updateProduct(@PathVariable("id") int id, @RequestBody Product product) {
		Optional<Product> productData = repository.findById(id);
			//int id, String name, String category, int price, String manufacturer, int unitInStock, String description
		if (productData.isPresent()) {
			Product _product = productData.get();
			_product.setId(product.getId());
			_product.setName(product.getName());
			_product.setCategory(product.getCategory());
			_product.setPrice(product.getPrice());
			_product.setManufacturer(product.getManufacturer());
			_product.setUnitInStock(product.getUnitInStock());
			_product.setDescription(product.getDescription());
			return new ResponseEntity<>(repository.save(_product), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}