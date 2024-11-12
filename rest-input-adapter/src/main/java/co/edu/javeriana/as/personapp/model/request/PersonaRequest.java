package co.edu.javeriana.as.personapp.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonaRequest {
	private int dni;
	private String firstName;
	private String lastName;
	private String age;
	private String gender;
	private String database;
}