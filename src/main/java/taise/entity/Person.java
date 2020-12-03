package taise.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

import static taise.service.Utils.*;

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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT, locale = "pt-BR", timezone = "Brazil/East")
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
