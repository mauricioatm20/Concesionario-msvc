package com.concesionario.ventas.entities;

import com.cocecionario.coche.entities.Coche;

import com.concesionario.cliente.entities.Customer;
import com.concesionario.ventas.dto.VentasDto;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity(name = "ventas")
public class Ventas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL) //Cada coche solo puede estar asociado a una venta. Esto refleja que un coche vendido no puede ser parte de otra venta.
    @JoinColumn(name = "coche_id", nullable = false)
    private Coche coche;

    @ManyToOne (cascade = CascadeType.ALL)//Un cliente puede realizar múltiples compras, pero cada venta individual pertenece a un cliente específico.
    @JoinColumn(name = "customer_id" , nullable = false)
    private Customer customer;

    private LocalDateTime fechaVenta;
    private Double precioVentaTotal;


    public VentasDto getDto() {
        VentasDto ventasDto = new VentasDto();

        ventasDto.setId(id);
        ventasDto.setCoche(coche);
        ventasDto.setCustomer(customer);
        ventasDto.setFechaVenta(fechaVenta);
        ventasDto.setPrecioVentaTotal(precioVentaTotal);
        return ventasDto;
    }
}
