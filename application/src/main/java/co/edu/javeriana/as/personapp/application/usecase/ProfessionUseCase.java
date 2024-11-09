package co.edu.javeriana.as.personapp.application.usecase;

import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.ProfessionInputPort;
import co.edu.javeriana.as.personapp.application.port.out.ProfessionOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.UseCase;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import lombok.extern.slf4j.Slf4j;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.domain.Study;
import java.util.List;

@Slf4j
@UseCase
public class ProfessionUseCase implements ProfessionInputPort {
    
    private ProfessionOutputPort professionPersistence;

    public ProfessionUseCase(@Qualifier("professionOutputAdapterMaria") ProfessionOutputPort professionPersistence) {
        this.professionPersistence=professionPersistence;
    }

    @Override
    public void setPersintence(ProfessionOutputPort professionPersistence) {
        this.professionPersistence=professionPersistence;
    }

    @Override
    public Profession create(Profession profession) {
        log.debug("Into create on Application Domain");
        return professionPersistence.save(profession);
    }

    @Override
    public Profession edit(Integer identification, Profession profession) throws NoExistException {
        Profession oldProfession = professionPersistence.findById(identification);
        if (oldProfession != null)
            return professionPersistence.save(profession);
        throw new NoExistException(
                "The profession with id " + identification + " does not exist into db, cannot be edited");
        
    }

    @Override
    public Boolean drop(Integer identification) throws NoExistException {
        Profession oldProfession = professionPersistence.findById(identification);
        if(oldProfession!=null)
            return professionPersistence.delete(identification);

        throw new NoExistException("The profession with id " + identification + " does not exist into db, cannot be dropped");
    }

    @Override
    public List<Profession> findAll(){
        log.info("Output: " + professionPersistence.getClass());
        return professionPersistence.find();
    }

    @Override
    public Profession findOne(Integer identification) throws NoExistException {
        Profession oldProfession = professionPersistence.findById(identification);
        if(oldProfession!=null)
            return oldProfession;
        throw new NoExistException("The profession with id " + identification + " does not exist into db");
    }

    @Override
    public Integer count() {
        return findAll().size();
    }

    @Override
    public List<Study> getStudies(Integer identification) throws NoExistException {
        Profession oldProfession = professionPersistence.findById(identification);
        if(oldProfession!=null)
            return oldProfession.getStudies();
        throw new NoExistException("The profession with id " + identification + " does not exist into db");
        
    }


    
}
