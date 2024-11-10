package co.edu.javeriana.as.personapp.mongo.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.mongodb.MongoWriteException;

import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.mongo.document.EstudiosDocument;
import co.edu.javeriana.as.personapp.mongo.mapper.EstudiosMapperMongo;
import co.edu.javeriana.as.personapp.mongo.repository.EstudioRepositoryMongo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter("studyOutputAdapterMongo")
public class StudyOutputAdapterMongo implements StudyOutputPort {

    @Autowired
    private EstudioRepositoryMongo studyRepositoryMongo;

    @Autowired
    private EstudiosMapperMongo studyMapperMongo;
    
    @Override
    public Study save(Study study) {
        log.debug("Into save on Adapter MongoDB");
        try{
            EstudiosDocument persistedStudy = studyRepositoryMongo.save(studyMapperMongo.fromDomainToAdapter(study));
            return studyMapperMongo.fromAdapterToDomain(persistedStudy);
        } catch (MongoWriteException e) {
            log.warn(e.getMessage());
            return study;
        }
    }

    @Override
    public Boolean delete(Integer professionID, Integer personID) {
        log.debug("Into delete on Adapter MongoDB");
        EstudiosDocument estudio = studyRepositoryMongo.findByPrimaryProfesionAndPrimaryPersona(professionID, personID);

        // If the study does not exist
        if (estudio == null) {
            return false;
        }

        // If the study exists
        studyRepositoryMongo.delete(estudio);
        return true;
    }

    @Override
    public List<Study> find() {
        log.debug("Into find on Adapter MongoDB");
        return studyRepositoryMongo.findAll().stream().map(studyMapperMongo::fromAdapterToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Study findById(Integer professionID, Integer personID) {
        log.debug("Into findById on Adapter MongoDB");
        EstudiosDocument estudio = studyRepositoryMongo.findByPrimaryProfesionAndPrimaryPersona(professionID, personID);

        // If the study does not exist
        if (estudio == null) {
            return null;
        }
        
        // If the study exists
        return studyMapperMongo.fromAdapterToDomain(estudio);
    }
    
}