package com.ibeus.Comanda.Digital.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "addrress")
@Data
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cep;
    private String logadouro;
    private String bairro;
    private String localidade;
    private String uf;

    @OneToOne(mappedBy = "address")
    private Client client;
}
