package co.edu.javeriana.as.personapp.terminal.mapper;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.terminal.model.ProfessionModelCli;

@Mapper
public class ProfesionMapperCli {

    public ProfessionModelCli fromDomainToAdapterCli(Profession profession){
        ProfessionModelCli profesionModelCli = new ProfessionModelCli();
        profesionModelCli.setId(profession.getIdentification());
        profesionModelCli.setName(profession.getName());
        profesionModelCli.setDescription(profession.getDescription());

        return profesionModelCli;
    }

    public Profession fromAdapterCliToDomain(ProfessionModelCli profesionModelCli){
        Profession profession = new Profession();
        profession.setIdentification(profesionModelCli.getId());
        profession.setName(profesionModelCli.getName());
        profession.setDescription(profesionModelCli.getDescription());

        return profession;
    }
}