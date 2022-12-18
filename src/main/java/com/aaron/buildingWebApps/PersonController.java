package com.aaron.buildingWebApps;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.OPTIONS;
import static org.springframework.web.bind.annotation.RequestMethod.PATCH;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.List;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(
        methods = {POST,GET,OPTIONS,PUT,DELETE,PATCH}, 
        maxAge = 3600, 
        allowedHeaders = {"x-requested-with","origin","content-type","accept"}, 
        origins = "*")
public class PersonController {
	
	private PersonRepository personRepo;
	
	public PersonController(PersonRepository pRepo) {
		this.personRepo = pRepo;
	}
	
	@GetMapping("/persons")
	public ResponseEntity<Stream<Person>> getPersons(HttpServletRequest req) {
		List<Person> pList = getPersons();
		
        if (pList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(pList.stream());
	}
	
	@PostMapping("/persons/create")
	public ResponseEntity<Person> createPerson(
            HttpServletRequest req, 
            @RequestBody Person personData) {
		
		savePerson(personData);
		
		return ResponseEntity.ok(personData);
	}
	
	public List<Person> getPersons() {
		return personRepo.findAll();
	}
	
	public void savePerson(Person person) {
		personRepo.save(person);
	}
}
