package taise.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Person {

    private UUID id;
    @NotNull
    private String name;
    @NotNull
    private String cpf;
    @NotNull
    private Date birthDate;
    @NotNull
    private String birthCountry;
    @NotNull
    private String birthState;
    @NotNull
    private String birthCity;
    private String fatherName;
    private String motherName;
    @NotNull
    private String email;
}
