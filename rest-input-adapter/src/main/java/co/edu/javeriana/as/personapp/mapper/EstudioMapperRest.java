package co.edu.javeriana.as.personapp.mapper;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.model.request.EstudioRequest;
import co.edu.javeriana.as.personapp.model.response.EstudioResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mapper
public class EstudioMapperRest {
    private static final Logger log = LoggerFactory.getLogger(EstudioMapperRest.class);

    public EstudioResponse fromDomainToAdapterRestMaria(Study study) {
        return fromDomainToAdapterRest(study, "MariaDB");
    }

    public EstudioResponse fromDomainToAdapterRestMongo(Study study) {
        return fromDomainToAdapterRest(study, "MongoDB");
    }

    public EstudioResponse fromDomainToAdapterRest(Study study, String database) {
        log.info("Into fromDomainToAdapterRest with study: {}", study);
        if (study == null) {
            log.error("Study object is null.");
            return new EstudioResponse(null, null, null, null, database, "Error: Study is null");
        }
        if (study.getPerson() == null) {
            log.error("Person is null in Study object.");
            return new EstudioResponse(null, null, study.getGraduationDate(), study.getUniversityName(), database, "Error: Person is null in Study");
        }
        if (study.getProfession() == null) {
            log.error("Profession is null in Study object.");
            return new EstudioResponse(study.getPerson().getIdentification() + "", null, study.getGraduationDate(), study.getUniversityName(), database, "Error: Profession is null in Study");
        }
    
        return new EstudioResponse(
            study.getProfession().getIdentification() + "",
            study.getPerson().getIdentification() + "",
            study.getGraduationDate(),
            study.getUniversityName(),
            database,
            "OK"
        );
    }
    
    

    public Study fromAdapterToDomain(EstudioRequest request, Profession profession, Person person) {
        log.info("Into fromAdapterToDomain with request: {}, profession: {}, person: {}", request, profession, person);
    
        if (profession == null || person == null) {
            log.error("Either profession or person is null - cannot create Study.");
            throw new IllegalArgumentException("Both Profession and Person must be provided and non-null.");
        }
    
        Study study = new Study();
        study.setProfession(profession);
        study.setPerson(person);
        study.setGraduationDate(request.getGraduationDate());
        study.setUniversityName(request.getUniversity());
    
        return study;
    }
    
    
}