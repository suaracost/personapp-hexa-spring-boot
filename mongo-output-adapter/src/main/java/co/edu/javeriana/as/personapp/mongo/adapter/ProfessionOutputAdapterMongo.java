package co.edu.javeriana.as.personapp.mongo.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.mongodb.MongoWriteException;

import co.edu.javeriana.as.personapp.application.port.out.ProfessionOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.mongo.document.ProfesionDocument;
import co.edu.javeriana.as.personapp.mongo.mapper.ProfesionMapperMongo;
import co.edu.javeriana.as.personapp.mongo.repository.ProfesionRepositoryMongo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter("professionOutputAdapterMongo")
public class ProfessionOutputAdapterMongo implements ProfessionOutputPort {
	
	@Autowired
    private ProfesionRepositoryMongo professionRepositoryMongo;
	
	@Autowired
	private ProfesionMapperMongo profesionMapperMongo;
	
	@Override
	public Profession save(Profession profession) {
		log.debug("Into save on Adapter MongoDB");
		try {
			ProfesionDocument persistedProfession = professionRepositoryMongo.save(profesionMapperMongo.fromDomainToAdapter(profession));
			return profesionMapperMongo.fromAdapterToDomain(persistedProfession);
		} catch (MongoWriteException e) {
			log.warn(e.getMessage());
			return profession;
		}		
	}

	@Override
	public Boolean delete(Integer identification) {
		log.debug("Into delete on Adapter MongoDB");
		professionRepositoryMongo.deleteById(identification);
		return professionRepositoryMongo.findById(identification).isEmpty();
	}

	@Override
	public List<Profession> find() {
		log.debug("Into find on Adapter MongoDB");
		return professionRepositoryMongo.findAll().stream().map(profesionMapperMongo::fromAdapterToDomain)
				.collect(Collectors.toList());
	}

	@Override
	public Profession findById(Integer identification) {
		log.debug("Into findById on Adapter MongoDB");
		if (professionRepositoryMongo.findById(identification).isEmpty()) {
			return null;
		} else {
			return profesionMapperMongo.fromAdapterToDomain(professionRepositoryMongo.findById(identification).get());
		}
	}

}