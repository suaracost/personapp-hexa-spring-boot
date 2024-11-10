package co.edu.javeriana.as.personapp.mapper;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.mariadb.entity.PersonaEntity;
import co.edu.javeriana.as.personapp.model.request.TelefonoRequest;
import co.edu.javeriana.as.personapp.model.response.TelefonoResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.lang.NonNull;

@Mapper
public class TelefonoMapperRest {
	private static final Logger log = LoggerFactory.getLogger(PersonaMapperRest.class);
	
	public TelefonoResponse fromDomainToAdapterRestMaria(Phone phone) {
		return fromDomainToAdapterRest(phone, "MariaDB");
	}
	public TelefonoResponse fromDomainToAdapterRestMongo(Phone phone) {
		return fromDomainToAdapterRest(phone, "MongoDB");
	}
	
	public TelefonoResponse fromDomainToAdapterRest(Phone phone, String database) {
		return new TelefonoResponse(
                phone.getNumber()+"", 
                phone.getCompany(), 
                phone.getOwner().getIdentification()+"",
				database,
				"OK");
	}

	public Phone fromAdapterToDomain(TelefonoRequest request,Person owner) {
		log.info("Into fromAdapterToDomain");
        Phone phone = new Phone();
        phone.setNumber(request.getNumber());
        phone.setCompany(request.getCompany());
        phone.setOwner(owner);
        return phone;
    
    }
		
    private @NonNull Person validateDuenio(@NonNull Person owner) {
        Person duenio = new Person();
        duenio.setIdentification(owner.getIdentification());
        return duenio;
    }
    private @NonNull Person validateOwner(PersonaEntity duenio) {
		Person owner = new Person();
		owner.setIdentification(duenio.getCc());
		return owner;
	}
}