package co.edu.javeriana.as.personapp.mapper;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Gender;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.model.request.PersonaRequest;
import co.edu.javeriana.as.personapp.model.response.PersonaResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mapper
public class PersonaMapperRest {
	private static final Logger log = LoggerFactory.getLogger(PersonaMapperRest.class);
	
	public PersonaResponse fromDomainToAdapterRestMaria(Person person) {
		return fromDomainToAdapterRest(person, "MariaDB");
	}
	public PersonaResponse fromDomainToAdapterRestMongo(Person person) {
		return fromDomainToAdapterRest(person, "MongoDB");
	}
	
	public PersonaResponse fromDomainToAdapterRest(Person person, String database) {
		return new PersonaResponse(
				person.getIdentification()+"", 
				person.getFirstName(), 
				person.getLastName(), 
				person.getAge()+"", 
				person.getGender().toString(), 
				database,
				"OK");
	}

	public Person fromAdapterToDomain(PersonaRequest request) {
		log.info("Into fromAdapterToDomain");
        Person person = new Person();
        person.setIdentification(Integer.parseInt(request.getDni()));
        person.setFirstName(request.getFirstName());
        person.setLastName(request.getLastName());
        person.setAge(Integer.parseInt(request.getAge()));
        person.setGender(parseGender(request.getGender()));
        return person;
    }

	private Gender parseGender(String gender) {
        if ("M".equalsIgnoreCase(gender)) {
            return Gender.MALE;
        } else if ("F".equalsIgnoreCase(gender)) {
            return Gender.FEMALE;
        } else {
            return Gender.OTHER;
        }
    }
		
}