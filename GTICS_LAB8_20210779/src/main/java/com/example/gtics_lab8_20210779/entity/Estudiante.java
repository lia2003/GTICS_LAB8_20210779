package com.example.gtics_lab8_20210779.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Estudiante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private Double gpa;
    private String facultad;
    private Integer creditosCompletados;

    private Integer posicionClasificacion;

}


