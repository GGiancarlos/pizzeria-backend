package com.ggutierrez.pizzeria.service;

import com.ggutierrez.pizzeria.persistence.entity.CustomerEntity;
import com.ggutierrez.pizzeria.persistence.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public CustomerEntity getByPhone(String phone) {
        return this.customerRepository.findAllByPhone(phone);
    }
}
