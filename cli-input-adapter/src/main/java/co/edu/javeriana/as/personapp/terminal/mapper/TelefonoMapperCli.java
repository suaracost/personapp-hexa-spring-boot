package co.edu.javeriana.as.personapp.terminal.mapper;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.terminal.model.PhoneModelCli;

@Mapper
public class TelefonoMapperCli {

    public PhoneModelCli fromDomainToAdapterCli(Phone phone) {
        PhoneModelCli phoneModelCli = new PhoneModelCli();
        phoneModelCli.setNumber(phone.getNumber());
        phoneModelCli.setCompany(phone.getCompany());
        phoneModelCli.setIdPerson(phone.getOwner().getIdentification()+"");
        return phoneModelCli;
    }

    public Phone fromAdapterCliToDomain(PhoneModelCli PhoneModelCli, Person Owner) {
        Phone phone = new Phone();
        phone.setNumber(PhoneModelCli.getNumber());
        phone.setCompany(PhoneModelCli.getCompany());
        phone.setOwner(Owner);
        return phone;
    }
}