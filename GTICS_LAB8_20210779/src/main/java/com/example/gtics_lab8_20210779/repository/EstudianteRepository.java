package com.example.gtics_lab8_20210779.repository;
import com.example.gtics_lab8_20210779.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {
    List<Estudiante> findByFacultadOrderByGpaDesc(String facultad);
}

