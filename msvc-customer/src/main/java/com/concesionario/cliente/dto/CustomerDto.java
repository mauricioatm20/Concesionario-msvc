package com.concesionario.cliente.dto;

import com.concesionario.cliente.entities.Customer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDto {

    private Long id;

    public Customer toEntity(){
        Customer customer = new Customer();
        customer.setId(id);
        return customer;
    }
}
