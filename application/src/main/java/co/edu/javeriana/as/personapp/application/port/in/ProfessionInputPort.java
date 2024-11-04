package co.edu.javeriana.as.personapp.application.port.in;

import co.edu.javeriana.as.personapp.application.port.out.ProfessionOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.Port;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;

import java.util.List;

@Port
public interface ProfessionInputPort {
    public void setPersintence(ProfessionOutputPort professionPersistence);
    public Profession create(Profession profession);
    public Profession edit(Integer identification, Profession profession) throws NoExistException;
    public Boolean drop(Integer identification) throws NoExistException;
    public List<Profession> findAll();
    public Profession findOne(Integer identification) throws NoExistException;
    public Integer count();
    public List<Study> getStudies(Integer identification) throws NoExistException;
}
