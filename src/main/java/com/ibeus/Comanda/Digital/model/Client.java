package com.ibeus.Comanda.Digital.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "client")
@Data
public class Client {

    @Id
    private Long cpf;

    private String name;

    private String midName;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

    private int addressNumber;
}
