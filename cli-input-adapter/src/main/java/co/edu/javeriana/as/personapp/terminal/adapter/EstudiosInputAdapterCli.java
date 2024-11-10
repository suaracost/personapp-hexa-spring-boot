package co.edu.javeriana.as.personapp.terminal.adapter;

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
import co.edu.javeriana.as.personapp.terminal.mapper.EstudiosMapperCli;
import co.edu.javeriana.as.personapp.terminal.model.StudyModelCli;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@Slf4j
@Adapter
public class EstudiosInputAdapterCli {

    //MariaDB
    @Autowired
    @Qualifier("studyOutputAdapterMaria")
    private StudyOutputPort studyOutputPortMaria;

    @Autowired
    @Qualifier("personOutputAdapterMaria")
    private PersonOutputPort personOutputPortMaria;

    @Autowired
    @Qualifier("professionOutputAdapterMaria")
    private ProfessionOutputPort professionOutputPortMaria;

    //MongoDB
    @Autowired
    @Qualifier("studyOutputAdapterMongo")
    private StudyOutputPort studyOutputPortMongo;

    @Autowired
    @Qualifier("personOutputAdapterMongo")
    private PersonOutputPort personOutputPortMongo;

    @Autowired
    @Qualifier("professionOutputAdapterMongo")
    private ProfessionOutputPort professionOutputPortMongo;

    @Autowired
    private EstudiosMapperCli EstudiosMapperCli;

    //Puertos de entrada a la aplicación
    StudyInputPort studyInputPort;
    ProfessionInputPort professionInputPort;
    PersonInputPort personInputPort;

    public void setStudyOutputPortInjection(String dbOption) throws InvalidOptionException {
        if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {

            studyInputPort = new StudyUseCase(studyOutputPortMaria);
            professionInputPort = new ProfessionUseCase(professionOutputPortMaria);
            personInputPort = new PersonUseCase(personOutputPortMaria);

        } else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {

            studyInputPort = new StudyUseCase(studyOutputPortMongo);
            professionInputPort = new ProfessionUseCase(professionOutputPortMongo);
            personInputPort = new PersonUseCase(personOutputPortMongo);

        } else {
            throw new InvalidOptionException("Invalid database option: " + dbOption);
        }
    }

    public void historial(){
        log.info("Into historial StudyEntity in Input Adapter");
        System.out.println("Estudios encontrados:");
        List<Study> estudios = studyInputPort.findAll();
        log.info("Número de estudios encontrados: {}", estudios.size()); // Verifica el tamaño
        estudios.stream()
            .map(EstudiosMapperCli::fromDomainToAdapterCli)
            .forEach(System.out::println);
    }
    

    public void crearEstudios(StudyModelCli Estudios, String dbOption){
        log.info("Into crearEstudios StudyEntity in Input Adapter");
        try{
            setStudyOutputPortInjection(dbOption);

            // Se obtienen los objetos de Person y Profession
            Person person = personInputPort.findOne(Integer.parseInt(Estudios.getIdPerson()));
            Profession profession = professionInputPort.findOne(Integer.parseInt(Estudios.getIdProfession()));

            // Se crea el estudio
            studyInputPort.create(EstudiosMapperCli.fromAdapterCliToDomain(Estudios, profession, person));

            // Se imprime el estudio creado
            System.out.println("Estudios creado correctamente "+ Estudios.toString());
        }catch (Exception e){
            log.warn(e.getMessage());
            System.out.println("Error al crear el Estudios");
        }
    }

    public void editarEstudio(StudyModelCli estudios, String dbOption) {
        log.info("Into editarEstudio StudyEntity in Input Adapter");
        try {
            setStudyOutputPortInjection(dbOption);

            // Se obtienen los objetos de Person y Profession
            Integer idPerson = Integer.parseInt(estudios.getIdPerson());
            Integer idProfession = Integer.parseInt(estudios.getIdProfession());
            Person person = personInputPort.findOne(idPerson);
            Profession profession = professionInputPort.findOne(idProfession);

            // Se edita el estudio
            studyInputPort.edit(idProfession, idPerson, EstudiosMapperCli.fromAdapterCliToDomain(estudios, profession, person));

            // Se imprime el estudio editado
            System.out.println("Estudio editado correctamente " + estudios.toString());
        } catch (Exception e) {
            log.warn(e.getMessage());
            System.out.println("Error al editar el estudio");
        }
    }

    public void eliminarEstudio(Integer idProfesion, Integer idPersona, String dbOption){
        log.info("Into buscarEstudioPorProfesionYPersona StudyEntity in Input Adapter");
        try {
            boolean resultado = studyInputPort.drop(idProfesion,idPersona);
			if (resultado){
				System.out.println("Estudio con id profesion " + idProfesion + "y id persona " + idPersona + " eliminados correctamente.");
            } else {
                System.out.println("No se encontró ningún estudio para la profesión con ID: " + idProfesion +
                        " y la persona con ID: " + idPersona);
            }
        } catch (NoExistException e) {
            log.warn(e.getMessage());
            System.out.println("Error al buscar estudio por profesión y persona");
        }
    }

    public void buscarEstudio(Integer idProfesion, Integer idPersona, String dbOption) {
        log.info("Into buscarEstudioPorProfesionYPersona StudyEntity in Input Adapter");
        try {
            Study estudio = studyInputPort.findOne(idProfesion, idPersona);
            if (estudio != null) {
                System.out.println("Estudio encontrado:");
                System.out.println(mapStudyToReadableString(estudio));
            } else {
                System.out.println("No se encontró ningún estudio para la profesión con ID: " + idProfesion +
                        " y la persona con ID: " + idPersona);
            }
        } catch (NoExistException e) {
            log.warn(e.getMessage());
            System.out.println("Error al buscar estudio por profesión y persona");
        }
    }

    private String mapStudyToReadableString(Study estudio) {
        return "ID: " + estudio.getPerson().getIdentification() +
                ", Profesion: " + estudio.getProfession().getIdentification() +
                ", Universidad: " + estudio.getUniversityName() +
                ", Fecha de graduación: " + estudio.getGraduationDate();
    }
    
}