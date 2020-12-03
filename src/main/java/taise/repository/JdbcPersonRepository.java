package taise.repository;

import taise.entity.Person;

import java.util.List;
import java.util.Optional;

public interface JdbcPersonRepository {

    int count();

    int save(Person person);

    int update(Person person);

    Optional<Person> deleteById(String id);

    List<Person> findAll();

    Optional<Person> findById(String id);

}
