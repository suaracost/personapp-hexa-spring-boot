package co.edu.javeriana.as.personapp.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.PersonInputPort;
import co.edu.javeriana.as.personapp.application.port.in.ProfessionInputPort;
import co.edu.javeriana.as.personapp.application.port.in.StudyInputPort;
import co.edu.javeriana.as.personapp.application.port.out.PersonOutputPort;
import co.edu.javeriana.as.personapp.application.port.out.ProfessionOutputPort;
import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.PersonUseCase;
import co.edu.javeriana.as.personapp.application.usecase.ProfessionUseCase;
import co.edu.javeriana.as.personapp.application.usecase.StudyUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.mapper.EstudioMapperRest;
import co.edu.javeriana.as.personapp.model.request.EstudioRequest;
import co.edu.javeriana.as.personapp.model.response.EstudioResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class EstudioInputAdapterRest {

    // MariaDB Adapters
    @Autowired
    @Qualifier("studyOutputAdapterMaria")
    private StudyOutputPort studyOutputPortMaria;

    @Autowired
    @Qualifier("personOutputAdapterMaria")
    private PersonOutputPort personOutputPortMaria;

    @Autowired
    @Qualifier("professionOutputAdapterMaria")
    private ProfessionOutputPort professionOutputPortMaria;

    // MongoDB Adapters
    @Autowired
    @Qualifier("studyOutputAdapterMongo")
    private StudyOutputPort studyOutputPortMongo;

    @Autowired
    @Qualifier("personOutputAdapterMongo")
    private PersonOutputPort personOutputPortMongo;

    @Autowired
    @Qualifier("professionOutputAdapterMongo")
    private ProfessionOutputPort professionOutputPortMongo;

    // Mapper
    @Autowired
    private EstudioMapperRest studyMapperRest;

    // UseCase
    private StudyInputPort studyInputPort;
    private PersonInputPort personInputPort;
    private ProfessionInputPort professionInputPort;

    private String setStudyOutputPortInjection(String dbOption) throws InvalidOptionException {
        if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {

            // Assign the correct output port to the input port
            studyInputPort = new StudyUseCase(studyOutputPortMaria);
            personInputPort = new PersonUseCase(personOutputPortMaria);
            professionInputPort = new ProfessionUseCase(professionOutputPortMaria);

            return DatabaseOption.MARIA.toString();

        } else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {

            // Assign the correct output port to the input port
            studyInputPort = new StudyUseCase(studyOutputPortMongo);
            personInputPort = new PersonUseCase(personOutputPortMongo);
            professionInputPort = new ProfessionUseCase(professionOutputPortMongo);

            return  DatabaseOption.MONGO.toString();
            
        } else {
            throw new InvalidOptionException("Invalid database option: " + dbOption);
        }
    }

    // View All Studies
    public List<EstudioResponse> historial(String database) {
        log.info("Into historial StudyEntity in Input Adapter");
        try {
            if(setStudyOutputPortInjection(database).equalsIgnoreCase(DatabaseOption.MARIA.toString())){
                return studyInputPort.findAll().stream().map(studyMapperRest::fromDomainToAdapterRestMaria)
                        .collect(Collectors.toList());
            }else {
                return studyInputPort.findAll().stream().map(studyMapperRest::fromDomainToAdapterRestMongo)
                        .collect(Collectors.toList());
            }
        } catch (InvalidOptionException e) {
            log.error(e.getMessage());
            return new ArrayList<EstudioResponse>();
        }
    }


    public EstudioResponse crearEstudio(EstudioRequest request) {
        try {
            log.info("Starting crearEstudio with request: {}", request);
            
            setStudyOutputPortInjection(request.getDatabase());
            log.info("Output port injection set for database: {}", request.getDatabase());
    
            log.info("Fetching person with ID: {}", request.getIdCcPerson());
            Person person = personInputPort.findOne(Integer.parseInt(request.getIdCcPerson()));
            if (person == null) {
                log.warn("Person with ID {} not found", request.getIdCcPerson());
                return new EstudioResponse(request.getIdProfession(), request.getIdCcPerson(), request.getGraduationDate(), 
                        request.getUniversity(), request.getDatabase(), "Error: Person not found");
            }
            log.info("Person retrieved successfully: {}", person);
    
            log.info("Fetching profession with ID: {}", request.getIdProfession());
            Profession profession = professionInputPort.findOne(Integer.parseInt(request.getIdProfession()));
            if (profession == null) {
                log.warn("Profession with ID {} not found", request.getIdProfession());
                return new EstudioResponse(request.getIdProfession(), request.getIdCcPerson(), request.getGraduationDate(), 
                        request.getUniversity(), request.getDatabase(), "Error: Profession not found");
            }
            log.info("Profession retrieved successfully: {}", profession);
    
            log.info("Mapping StudyRequest to Study domain object");
            Study study = studyMapperRest.fromAdapterToDomain(request, profession, person);
            log.info("Mapped Study object: {}", study);
    
            log.info("Attempting to create Study object");
            Study createdStudy = studyInputPort.create(study);
            log.info("Study created successfully: {}", createdStudy);
            if (createdStudy == null) {
                log.error("Failed to create Study object");
                return new EstudioResponse(request.getIdProfession(), request.getIdCcPerson(), request.getGraduationDate(), 
                        request.getUniversity(), request.getDatabase(), "Error: Study creation failed");
            }
            log.info("Study created successfully: {}", createdStudy);
    
            if (request.getDatabase().equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
                return studyMapperRest.fromDomainToAdapterRestMaria(createdStudy);
            } else {
                return studyMapperRest.fromDomainToAdapterRestMongo(createdStudy);
            }
    
        } catch (InvalidOptionException | NoExistException e) {
            log.warn(e.getMessage());
            return new EstudioResponse(request.getIdProfession(), request.getIdCcPerson(), request.getGraduationDate(), 
                    request.getUniversity(), request.getDatabase(), "Error: Unable to create study");
        }
    }
    

    public EstudioResponse buscarEstudio(String database, String idProfession, String idCcPerson) {
        try {
            setStudyOutputPortInjection(database);
            Study study = studyInputPort.findOne(Integer.parseInt(idProfession), Integer.parseInt(idCcPerson));
            if (database.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
                return studyMapperRest.fromDomainToAdapterRestMaria(study);
            } else {
                return studyMapperRest.fromDomainToAdapterRestMongo(study);
            }
        } catch (InvalidOptionException | NoExistException e) {
            log.warn(e.getMessage());
            return new EstudioResponse(idProfession, idCcPerson, null, "", database, "Error: Study not found");
        }
    }


    public EstudioResponse eliminarEstudio(String database, String idProfession, String idCcPerson) {
        try {
            setStudyOutputPortInjection(database);
            Boolean eliminado = studyInputPort.drop(Integer.parseInt(idProfession), Integer.parseInt(idCcPerson));
            return new EstudioResponse(idProfession, idCcPerson, null, "", database, eliminado ? "Deleted" : "Failed to Delete");
        } catch (InvalidOptionException | NoExistException e) {
            log.warn(e.getMessage());
            return new EstudioResponse(idProfession, idCcPerson, null, "", database, "Error: Study not found or invalid database");
        }
    }


    public EstudioResponse actualizarEstudio(EstudioRequest request) {
        try {
            setStudyOutputPortInjection(request.getDatabase());
            Person person = personInputPort.findOne(Integer.parseInt(request.getIdCcPerson()));
            Profession profession = professionInputPort.findOne(Integer.parseInt(request.getIdProfession()));
            
            // Update study with profession and person as inputs
            Study study = studyInputPort.edit(Integer.parseInt(request.getIdProfession()), Integer.parseInt(request.getIdCcPerson()),
                                              studyMapperRest.fromAdapterToDomain(request, profession, person));
            
            if (request.getDatabase().equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
                return studyMapperRest.fromDomainToAdapterRestMaria(study);
            } else {
                return studyMapperRest.fromDomainToAdapterRestMongo(study);
            }
        } catch (InvalidOptionException | NoExistException e) {
            log.warn(e.getMessage());
            return new EstudioResponse(request.getIdProfession(), request.getIdCcPerson(), request.getGraduationDate(), 
                                     request.getUniversity(), request.getDatabase(), "Error: Update Failed");
        }
    }
}