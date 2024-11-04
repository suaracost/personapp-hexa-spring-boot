package co.edu.javeriana.as.personapp.application.port.in;

import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.application.port.out.PhoneOutputPort;
import co.edu.javeriana.as.personapp.domain.Person;
import java.util.List;

public interface PhoneInputPort {

    public void setPersintence(PhoneOutputPort phonePersistence);

    public Phone create(Phone phone);
    public Phone edit(String number, Phone phone) throws Exception;
    public Boolean drop(String number) throws Exception;
    public List<Phone> findAll();
    public Phone findOne(String number) throws Exception;
    public Integer count();
    public Person getPerson(String number) throws Exception;
}
