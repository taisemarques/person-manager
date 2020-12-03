package taise.service;

import taise.entity.Person;
import taise.repository.PersonRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Log4j2
public class PersonService {

    public static final int NOT_FOUND = 404;
    public static final int EMPTY_REQUIRED_FIELD = 450;
    public static final int INVALID_CPF = 451;
    public static final int SUCCESS = 200;
    public static final int UPDATE_SUCCESS = 1;
    public static final int CONFLICTED_CPF = 409;

    @Autowired
    private PersonRepository personRepository;

    public ResponseEntity<Person> create(Person p) {
        int result = validatePersonData(p);
        if (result != SUCCESS) {
            return new ResponseEntity<Person>(HttpStatus.valueOf(result));
        }
        p.setId(UUID.randomUUID());
        result = personRepository.save(p);
        return result == UPDATE_SUCCESS ? ResponseEntity.ok(p) : new ResponseEntity<Person>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private int validatePersonData(Person p) {
        if (p.getName().trim().isEmpty() || p.getCpf().trim().isEmpty()
                || p.getBirthDate().toString().isEmpty() || p.getBirthCountry().trim().isEmpty()
                || p.getBirthState().trim().isEmpty() || p.getBirthCity().trim().isEmpty()
                || p.getEmail().trim().isEmpty())
            return EMPTY_REQUIRED_FIELD;
        if (!Utils.isValidCPF(p.getCpf())){
            return INVALID_CPF;
        }
        List<Person> persons = personRepository.findWithParams(null, p.getCpf(),null,null,null,null);
        if(!persons.isEmpty()){
            return CONFLICTED_CPF;
        }
        return SUCCESS;
    }

    public ResponseEntity<Person> findById(String id){
        Optional<Person> person =  personRepository.findById(id);
        return person.isPresent() ?
                ResponseEntity.ok(person.get()) :
                new ResponseEntity<Person>(HttpStatus.valueOf(NOT_FOUND));
    }

    public ResponseEntity<List<Person>> findWithParams(String name, String cpf, Date birthDate, String birthCountry, String birthState, String birthCity) {
        List<Person> persons = personRepository.findWithParams(name, cpf, birthDate, birthCountry, birthState, birthCity);
        return persons.isEmpty() ?
                new ResponseEntity<List<Person>>(HttpStatus.valueOf(NOT_FOUND)) :
                ResponseEntity.ok(persons);
    }

    public ResponseEntity<Person> update(Person person) {
        List<Person> persons = personRepository.findWithParams(null, person.getCpf(),null,null,null,null);
        if(!persons.isEmpty()){
            return new ResponseEntity<Person>(HttpStatus.valueOf(CONFLICTED_CPF));
        }
        int result = personRepository.update(person);
        return result == 0?
                new ResponseEntity<Person>(HttpStatus.valueOf(NOT_FOUND)) :
                ResponseEntity.ok(person);
    }

    public ResponseEntity<Person> deleteById(String id) {
        Optional<Person> person = personRepository.deleteById(id);
        return person.isPresent() ?
                ResponseEntity.ok(person.get()) :
                new ResponseEntity<Person>(HttpStatus.valueOf(NOT_FOUND));
    }
}
