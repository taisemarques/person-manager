package taise.repository;

import taise.entity.Person;
import static taise.service.Utils.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@Log4j2
public class PersonRepository implements JdbcPersonRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private UUID id;

    @Override
    public int count() {
        return jdbcTemplate.queryForObject("select count(*) from person", Integer.class);
    }

    @Override
    public int save(Person person) {
        return jdbcTemplate.update(
        "insert into person (id, name, cpf, birthDate, birthCountry, birthState, " +
				"birthCity, fatherName, motherName, email) values(?,?,?,?,?,?,?,?,?,?)",
        person.getId().toString(), person.getName(), person.getCpf(), person.getBirthDate(),
        person.getBirthCountry(), person.getBirthState(), person.getBirthCity(), person.getFatherName(),
        person.getMotherName(), person.getEmail() );
    }

    @Override
    public int update(Person person) {
        return jdbcTemplate.update(
                "update person set name = ?, cpf = ?, birthDate = ?, birthCountry = ?, " +
                        "birthState = ?, birthCity = ?, fatherName = ?, motherName = ?, email = ? " +
                        "where id = ?",
                person.getName(), person.getCpf(), person.getBirthDate(), person.getBirthCountry(),
                person.getBirthState(), person.getBirthCity(), person.getFatherName(),
                person.getMotherName(), person.getEmail(), person.getId().toString());
    }

    @Override
    public Optional<Person> deleteById(String id) {
        Optional<Person> person = findById(id);
        if(person.isPresent()){
            jdbcTemplate.update("delete person where id = ?", id);
        }
        return person;
    }

    public List<Person> findWithParams(String name, String cpf, Date birthDate, String birthCountry, String birthState, String birthCity) {
        if(isEmpty(name) && isEmpty(cpf) && isEmpty(birthDate) && isEmpty(birthCountry) &&
                isEmpty(birthState) && isEmpty(birthCity) ) {
            return findAll();
        } else {
            return findByFilter(name, cpf, birthDate, birthCountry, birthState, birthCity);
        }
    }

    @Override
    public List<Person> findAll(){
        return jdbcTemplate.query(
                "select * from person",
                (rs, rowNum) ->
                        new Person(
                                UUID.fromString(rs.getString("id")),
                                rs.getString("name"),
                                rs.getString("cpf"),
                                rs.getDate("birthDate"),
                                rs.getString("birthCountry"),
                                rs.getString("birthState"),
                                rs.getString("birthCity"),
                                rs.getString("fatherName"),
                                rs.getString("motherName"),
                                rs.getString("email")
                        ));
    }

    private List<Person> findByFilter(String name, String cpf, Date birthDate, String birthCountry, String birthState, String birthCity) {

        List<String> filters = Arrays.asList("name", "cpf", "birthDate", "birthCountry", "birthState", "birthCity");
        List<String> values = Arrays.asList(name, cpf, getDateByMask(birthDate, DATE_FORMAT), birthCountry, birthState, birthCity);

        return jdbcTemplate.query(
                concatFiltersToQuery("select * from person", filters, values),
                (rs, rowNum) ->
                        new Person(
                                UUID.fromString(rs.getString("id")),
                                rs.getString("name"),
                                rs.getString("cpf"),
                                rs.getDate("birthDate"),
                                rs.getString("birthCountry"),
                                rs.getString("birthState"),
                                rs.getString("birthCity"),
                                rs.getString("fatherName"),
                                rs.getString("motherName"),
                                rs.getString("email")
                        ));
    }

    @Override
    public Optional<Person> findById(String id) {
        try {
            return Optional.of(
                    jdbcTemplate.queryForObject(
                            "select * from person where id = ?",
                            new Object[]{id},
                            new PersonRowMapper()
                    )
            );
        } catch(EmptyResultDataAccessException e){
            return Optional.empty();
        }

    }

}
