package taise.controller;

import taise.entity.Person;
import taise.service.PersonService;
import static taise.service.Utils.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Log4j2
@RestController
@RequestMapping(path = "/persons", produces = MediaType.APPLICATION_JSON_VALUE)
public class PersonController {

    private PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Person> create(@RequestBody Person person) {
        return personService.create(person);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Person> read(@PathVariable("id") String id) {
        return personService.findById(id);
    }

    @GetMapping
    public ResponseEntity<List<Person>> readAll(@RequestParam(name="nome", required=false) String name,
                                                @RequestParam(name="cpf", required=false) String cpf,
                                                @RequestParam(name="data", required=false)
                                                    @DateTimeFormat(pattern = DATE_FORMAT) Date birthDate,
                                                @RequestParam(name="pais", required=false) String birthCountry,
                                                @RequestParam(name="estado", required=false) String birthState,
                                                @RequestParam(name="cidade", required=false) String birthCity) {
        return personService.findWithParams(name, cpf, birthDate, birthCountry, birthState, birthCity);
    }

    @PutMapping
    public ResponseEntity<Person> update(@RequestBody Person person) {
        return personService.update(person);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Person> delete(@PathVariable("id") String id) {
        return personService.deleteById(id);
    }
}
