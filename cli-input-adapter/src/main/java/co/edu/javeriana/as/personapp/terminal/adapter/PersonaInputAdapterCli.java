package co.edu.javeriana.as.personapp.terminal.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.PersonInputPort;
import co.edu.javeriana.as.personapp.application.port.out.PersonOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.PersonUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.terminal.mapper.PersonaMapperCli;
import co.edu.javeriana.as.personapp.terminal.model.PersonaModelCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class PersonaInputAdapterCli {

	// MariaDB
	@Autowired
	@Qualifier("personOutputAdapterMaria")
	private PersonOutputPort personOutputPortMaria;

	// MongoDB
	@Autowired
	@Qualifier("personOutputAdapterMongo")
	private PersonOutputPort personOutputPortMongo;

	// Mapper
	@Autowired
	private PersonaMapperCli personaMapperCli;

	// UseCase
	PersonInputPort personInputPort;

	public void setPersonOutputPortInjection(String dbOption) throws InvalidOptionException {
		if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {

			personInputPort = new PersonUseCase(personOutputPortMaria);

		} else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {

			personInputPort = new PersonUseCase(personOutputPortMongo);

		} else {
			throw new InvalidOptionException("Invalid database option: " + dbOption);
		}
	}

	public void historial1() {
		log.info("Into historial PersonaEntity in Input Adapter");
		List<PersonaModelCli> persona = personInputPort.findAll().stream().map(personaMapperCli::fromDomainToAdapterCli)
					.collect(Collectors.toList());
		persona.forEach(p -> System.out.println(p.toString()));
	}
	public void historial() {
	    log.info("Into historial PersonaEntity in Input Adapter");
	    personInputPort.findAll().stream()
	        .map(personaMapperCli::fromDomainToAdapterCli)
	        .forEach(System.out::println);
	}

	public void crearPersona(PersonaModelCli persona, String database) {
		log.info("Into crearPersona PersonaEntity in Input Adapter");
		try{
			setPersonOutputPortInjection(database);

			personInputPort.create(personaMapperCli.fromAdapterCliToDomain(persona));

			log.info("PersonaEntity creada exitosamente: " + persona.toString());


		} catch (Exception e) {
			log.error("Error en crearPersona PersonaEntity in Input Adapter: " + e.getMessage());
		}
	}

	public void editarPersona(PersonaModelCli persona, String database) {
		log.info("Into editarPersona PersonaEntity in Input Adapter");
		try{
			setPersonOutputPortInjection(database);

			personInputPort.edit(persona.getCc(), personaMapperCli.fromAdapterCliToDomain(persona));

			log.info("PersonaEntity actualizada exitosamente: " + persona.toString());

		} catch (Exception e) {
			log.error("Error en editarPersona PersonaEntity in Input Adapter: " + e.getMessage());
		}
	}

	public void eliminarPersona(int cc, String database) {
		log.info("Into eliminarPersona PersonaEntity in Input Adapter");
		try{
			setPersonOutputPortInjection(database);

			personInputPort.drop(cc);

			log.info("PersonaEntity eliminada exitosamente: " + cc);

		} catch (Exception e) {
			log.error("Error en eliminarPersona PersonaEntity in Input Adapter: " + e.getMessage());
		}
	}

	public void buscarPersona(int cc, String database) {
		log.info("Into buscarPersona PersonaEntity in Input Adapter");
		try{
			setPersonOutputPortInjection(database);

			Person persona = personInputPort.findOne(cc);
			PersonaModelCli personaModelCli = personaMapperCli.fromDomainToAdapterCli(persona);
			System.out.println("Persona encontrada: " + personaModelCli.toString());

		} catch (Exception e) {
			log.error("Error en buscarPersona PersonaEntity in Input Adapter: " + e.getMessage());
		}
	}

}