package co.edu.javeriana.as.personapp.model.response;

import java.time.LocalDate;

import co.edu.javeriana.as.personapp.model.request.EstudioRequest;

public class EstudioResponse extends EstudioRequest{
    
    private String status;
    
    public EstudioResponse(int idProfession, int idCcPerson, LocalDate graduationDate, String university, String database, String status) {
        super(idProfession, idCcPerson, graduationDate, university, database);
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
}