package co.edu.javeriana.as.personapp.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.PersonInputPort;
import co.edu.javeriana.as.personapp.application.port.out.PersonOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.PersonUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.mapper.PersonaMapperRest;
import co.edu.javeriana.as.personapp.model.request.PersonaRequest;
import co.edu.javeriana.as.personapp.model.response.PersonaResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class PersonaInputAdapterRest {

	@Autowired
	@Qualifier("personOutputAdapterMaria")
	private PersonOutputPort personOutputPortMaria;

	@Autowired
	@Qualifier("personOutputAdapterMongo")
	private PersonOutputPort personOutputPortMongo;

	@Autowired
	private PersonaMapperRest personaMapperRest;

	PersonInputPort personInputPort;

	private String setPersonOutputPortInjection(String dbOption) throws InvalidOptionException {
		if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
			personInputPort = new PersonUseCase(personOutputPortMaria);
			return DatabaseOption.MARIA.toString();
		} else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
			personInputPort = new PersonUseCase(personOutputPortMongo);
			return  DatabaseOption.MONGO.toString();
		} else {
			throw new InvalidOptionException("Invalid database option: " + dbOption);
		}
	}

	public List<PersonaResponse> historial(String database) {
		log.info("Into historial PersonaEntity in Input Adapter");
		try {
			if(setPersonOutputPortInjection(database).equalsIgnoreCase(DatabaseOption.MARIA.toString())){
				return personInputPort.findAll().stream().map(personaMapperRest::fromDomainToAdapterRestMaria)
						.collect(Collectors.toList());
			}else {
				return personInputPort.findAll().stream().map(personaMapperRest::fromDomainToAdapterRestMongo)
						.collect(Collectors.toList());
			}
			
		} catch (InvalidOptionException e) {
			log.warn(e.getMessage());
			return new ArrayList<PersonaResponse>();
		}
	}

	public PersonaResponse crearPersona(PersonaRequest request) {
        try {
            setPersonOutputPortInjection(request.getDatabase());
            Person person = personInputPort.create(personaMapperRest.fromAdapterToDomain(request));
            if (request.getDatabase().equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
                return personaMapperRest.fromDomainToAdapterRestMaria(person);
            } else {
                return personaMapperRest.fromDomainToAdapterRestMongo(person);
            }
        } catch (InvalidOptionException e) {
            log.warn(e.getMessage());
            return new PersonaResponse(request.getDni(), request.getFirstName(), request.getLastName(),
                    request.getAge(), request.getGender(), request.getDatabase(),
                    "Error: Invalid Database Option");
        }
    }
	

	public PersonaResponse buscarPersona(String database, Integer idInteger) {
        try {
            setPersonOutputPortInjection(database);
            Person person = personInputPort.findOne(idInteger);
            if (database.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
                return personaMapperRest.fromDomainToAdapterRestMaria(person);
            } else {
                return personaMapperRest.fromDomainToAdapterRestMongo(person);
            }
        } catch (InvalidOptionException | NoExistException e) {
            log.warn(e.getMessage());
            return new PersonaResponse(idInteger.toString(), "", "", "", "", database,
                    "Error: Person not found");
        }
    }
	

	public PersonaResponse eliminarPersona(String database, Integer idInteger) {
        try {
            setPersonOutputPortInjection(database);
            Boolean eliminado = personInputPort.drop(idInteger);
            return new PersonaResponse(idInteger.toString(), "", "", "", "", database,
                    eliminado ? "Deleted" : "Failed to Delete");
        } catch (InvalidOptionException | NoExistException e) {
            log.warn(e.getMessage());
            return new PersonaResponse(idInteger.toString(), "", "", "", "", database,
                    "Error: Person not found or invalid database");
        }
    }
	

	public PersonaResponse actualizarPersona(PersonaRequest request) {
        try {
            setPersonOutputPortInjection(request.getDatabase());
            Person person = personInputPort.edit(Integer.parseInt(request.getDni()),
                    personaMapperRest.fromAdapterToDomain(request));
            if (request.getDatabase().equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
                return personaMapperRest.fromDomainToAdapterRestMaria(person);
            } else {
                return personaMapperRest.fromDomainToAdapterRestMongo(person);
            }
        } catch (InvalidOptionException | NoExistException e) {
            log.warn(e.getMessage());
            return new PersonaResponse(request.getDni(), request.getFirstName(), request.getLastName(),
                    request.getAge(), request.getGender(), request.getDatabase(),
                    "Error: Update Failed");
        }
    }
	


}