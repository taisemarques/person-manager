package taise.repository;

import taise.entity.Person;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class PersonRowMapper implements RowMapper<Person> {
    @Override
    public Person mapRow(ResultSet rs, int rowNum) throws SQLException {

        Person p = new Person();

        p.setId(UUID.fromString(rs.getString("id")));
        p.setName(rs.getString("name"));
        p.setCpf(rs.getString("cpf"));
        p.setBirthDate(rs.getDate("birthDate"));
        p.setBirthCountry(rs.getString("birthCountry"));
        p.setBirthState(rs.getString("birthState"));
        p.setBirthCity(rs.getString("birthCity"));
        p.setFatherName(rs.getString("fatherName"));
        p.setMotherName(rs.getString("motherName"));
        p.setEmail(rs.getString("email"));

        return p;
    }
}
