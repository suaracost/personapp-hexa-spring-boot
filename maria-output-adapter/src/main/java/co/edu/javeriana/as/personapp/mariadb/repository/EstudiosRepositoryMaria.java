package co.edu.javeriana.as.personapp.mariadb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import co.edu.javeriana.as.personapp.mariadb.entity.EstudiosEntity;

public interface EstudiosRepositoryMaria extends JpaRepository<EstudiosEntity, co.edu.javeriana.as.personapp.mariadb.entity.EstudiosEntityPK> {
    
    @Query("SELECT e FROM EstudiosEntity e WHERE e.profesion.id = :professionID AND e.persona.id = :personID")
    EstudiosEntity findByProfesionAndPersona(@Param("professionID") Integer professionID, @Param("personID") Integer personID);
}