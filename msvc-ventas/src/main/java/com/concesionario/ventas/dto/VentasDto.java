package com.concesionario.ventas.dto;

import com.cocecionario.coche.entities.Coche;
import com.cocecionario.coche.enums.EstadoVehiculo;
import com.concesionario.cliente.entities.Customer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VentasDto {

    private Long id;
    private Coche coche;
    private Customer customer;
    private LocalDateTime fechaVenta;
    private Double precioVentaTotal;
    private Long cocheId;
    private Long customerId;
    private EstadoVehiculo estadoVehiculo;
}
