package taise.controller;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import taise.entity.Person;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PersonControllerTest {

    @Autowired
    PersonController personController;

    Person personA = new Person(null, "Sara",
            "80714384054", new GregorianCalendar(1990, Calendar.APRIL, 11).getTime(),
            "Canada", "Quebec", "Drumonville", "John",
            "Margaret", "sara@email.com");
    Person personB = new Person(UUID.fromString("f12aff6f-81f2-4674-b674-d1b862807350"), "Patrick",
            "13211605002", new GregorianCalendar(1985, Calendar.DECEMBER, 5).getTime(),
            "Reino Unido", "Inglaterra", "Londres", "John",
            "Margaret", "patrick@email.com");
    Person personC = new Person(UUID.fromString("eeefaa64-3e46-45f2-87c7-6b6c65952cd4"), "Taise B F Marques",
            "61642512303", new GregorianCalendar(1983, Calendar.NOVEMBER, 20).getTime(),
            "Brasil", "Santa Catarina", "Criciuma", "Mercuti O. Fernandes",
            "Edilene B F Fernandes", "taise@email.com");
    Person personD = new Person(UUID.fromString("1567befa-7794-4c49-8a2f-4371effb42cf"), "Emily Marques",
            "61642512303", new GregorianCalendar(2014, Calendar.FEBRUARY, 03).getTime(),
            "Brasil", "Santa Catarina", "Florianopolis", "Julio",
            "Taise", "emily@email.com");

    String idA = "eeefaa64-3e46-45f2-87c7-6b6c65952cd4";
    String idB = "1567befa-7794-4c49-8a2f-4371effb42cf";
    String idC = "3b29d65d-4203-4538-bf5c-b3078129cde8";
    String idNotSaved = "5d475e40-599d-408a-89ce-beeb740ede59";

    public boolean equalExceptId(Person personA, Person personB){
        if(personA.getName().equals(personB.getName()) &&
                personA.getCpf().equals(personB.getCpf()) &&
                personA.getBirthDate().toString().equals(personB.getBirthDate().toString()) &&
                personA.getBirthCountry().equals(personB.getBirthCountry()) &&
                personA.getBirthState().equals(personB.getBirthState()) &&
                personA.getBirthCity().equals(personB.getBirthCity()) &&
                personA.getFatherName().equals(personB.getFatherName()) &&
                personA.getMotherName().equals(personB.getMotherName()) &&
                personA.getEmail().equals(personB.getEmail())){
            return true;
        }
        return false;
    }

    public boolean equal(Person personA, Person personB){
        if(equalExceptId(personA, personB) && personA.getId().equals(personB.getId())){
            return true;
        }
        return false;
    }

    @Test
    @Order(1)
    public void getAllPeople(){
        ResponseEntity<List<Person>> response = personController.readAll(null, null,null, null, null, null);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(3).isEqualTo(response.getBody().size());
        assertThat(idA).isEqualTo(response.getBody().get(0).getId().toString());
        assertThat(idB).isEqualTo(response.getBody().get(1).getId().toString());
        assertThat(idC).isEqualTo(response.getBody().get(2).getId().toString());
    }

    @Test
    @Order(2)
    public void getPersonById(){
        ResponseEntity<Person> response = personController.read(idA);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(idA).isEqualTo(response.getBody().getId().toString());
    }

    @Test
    @Order(3)
    public void getPersonByParam(){
        ResponseEntity<List<Person>> response = personController.readAll(null, null,new GregorianCalendar(2014, Calendar.FEBRUARY, 03).getTime(), null, null, null);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(1).isEqualTo(response.getBody().size());

        response = personController.readAll(null, null, null, "Brasil", null, null);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(3).isEqualTo(response.getBody().size());

        response = personController.readAll(null, null, null, "Brasil", "Santa Catarina", null);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(2).isEqualTo(response.getBody().size());

        response = personController.readAll("Julio Marques", "14508976069",null, null, null, "Campinas");
        assertThat(response.getStatusCode()).isEqualTo( HttpStatus.OK);
        assertThat(1).isEqualTo(response.getBody().size());
    }

    @Test
    @Order(4)
    public void createPerson(){
        ResponseEntity<Person> response = personController.create(personA);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(personA.getId()).isEqualTo(response.getBody().getId());
        assertThat(true).isEqualTo(equalExceptId(personA, response.getBody()));
    }

    @Test
    @Order(5)
    public void createPersonWithSameCPFOfAnotherPerson(){
        ResponseEntity<Person> response = personController.create(personC);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    @Order(6)
    public void updatePerson(){
        ResponseEntity<Person> response = personController.update(personC);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(true).isEqualTo(equal(personC, response.getBody()));
    }

    @Test
    @Order(7)
    public void updatePersonWithSameCPFOfAnotherPerson(){
        ResponseEntity<Person> response = personController.update(personD);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    @Order(8)
    public void deletePersonById(){
        ResponseEntity<Person> response = personController.delete(idA);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(idA).isEqualTo(response.getBody().getId().toString());
    }

    @Test
    @Order(9)
    public void deletePersonByIdNotFound(){
        ResponseEntity<Person> response = personController.delete(idNotSaved);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

}
