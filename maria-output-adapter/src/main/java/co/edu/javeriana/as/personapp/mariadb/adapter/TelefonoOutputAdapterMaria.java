package co.edu.javeriana.as.personapp.mariadb.adapter;

import co.edu.javeriana.as.personapp.application.port.out.PhoneOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.mariadb.entity.TelefonoEntity;
import co.edu.javeriana.as.personapp.mariadb.mapper.TelefonoMapperMaria;
import co.edu.javeriana.as.personapp.mariadb.repository.TelefonoRepositoryMaria;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Adapter("phoneOutputAdapterMaria")
@Transactional
public class TelefonoOutputAdapterMaria implements PhoneOutputPort {

    @Autowired
    private TelefonoRepositoryMaria telefonoRepositoryMaria;

    @Autowired
    private TelefonoMapperMaria telefonoMapperMaria;

    @Override
    public Phone save(Phone phone) {
        log.debug("Into save on Adapter MariaDB");
        TelefonoEntity persistedPhone = telefonoRepositoryMaria.save(telefonoMapperMaria.fromDomainToAdapter(phone));
        return telefonoMapperMaria.fromAdapterToDomain(persistedPhone);
    }

    @Override
    public Boolean delete(String number) {
        log.debug("Into delete on Adapter MariaDB");
        telefonoRepositoryMaria.deleteById(number);
        return telefonoRepositoryMaria.findById(number).isEmpty();
    }

    @Override
    public List<Phone> find() {
        log.debug("Into find on Adapter MariaDB");
        return telefonoRepositoryMaria.findAll().stream().map(telefonoMapperMaria::fromAdapterToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Phone findById(String number) {
        log.debug("Into findById on Adapter MariaDB");
        if (telefonoRepositoryMaria.findById(number).isEmpty()) {
            return null;
        } else {
            return telefonoMapperMaria.fromAdapterToDomain(telefonoRepositoryMaria.findById(number).get());
        }
    }
}