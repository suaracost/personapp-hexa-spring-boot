package co.edu.javeriana.as.personapp.application.port.out;

import co.edu.javeriana.as.personapp.common.annotations.Port;
import java.util.List;
import co.edu.javeriana.as.personapp.domain.Study;

@Port
public interface StudyOutputPort {
	public Study save(Study study);
	public Boolean delete(Integer professionID, Integer personID);
	public List<Study> find();
	public Study findById(Integer professionID, Integer personID);
}
