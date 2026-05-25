package com.SoporteMicroServicio.SoporteM.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "personal_soporte")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class PersonalSoporte {

    @Id
    private Long rutPersonalS; 

    @Column(nullable = false, length = 100)
    private String nombre;
    
    @Column(nullable= false, length= 100)
    private String apellido;

    @Column(nullable= false, unique= true, length= 150)
    private String email;

    @Column(nullable= false, length = 50)
    private String rol;

    @Column(nullable= false, length= 20)
    private String estado;
    
}
