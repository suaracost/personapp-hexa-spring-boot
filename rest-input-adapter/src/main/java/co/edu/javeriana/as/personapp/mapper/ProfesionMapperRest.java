package co.edu.javeriana.as.personapp.mapper;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.model.request.ProfesionRequest;
import co.edu.javeriana.as.personapp.model.response.ProfesionResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mapper
public class ProfesionMapperRest {
	private static final Logger log = LoggerFactory.getLogger(PersonaMapperRest.class);
	
	public ProfesionResponse fromDomainToAdapterRestMaria(Profession profession) {
		return fromDomainToAdapterRest(profession, "MariaDB");
	}
	public ProfesionResponse fromDomainToAdapterRestMongo(Profession profession) {
		return fromDomainToAdapterRest(profession, "MongoDB");
	}
	
	public ProfesionResponse fromDomainToAdapterRest(Profession profession, String database) {
		return new ProfesionResponse(
				profession.getIdentification()+"",
				profession.getName(),
				profession.getDescription(),
				database,
				"OK");
	}

	public Profession fromAdapterToDomain(ProfesionRequest request) {
		log.info("Into fromAdapterToDomain");
        Profession profession  = new Profession();
		profession.setIdentification(Integer.parseInt(request.getIdentification()));
		profession.setName(request.getName());
		profession.setDescription(request.getDescription());
        
        return profession;
    }


		
}