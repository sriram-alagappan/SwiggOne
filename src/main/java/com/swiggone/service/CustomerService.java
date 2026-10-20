package com.swiggone.service;

import com.swiggone.exception.ResourceNotFoundException;
import com.swiggone.model.Customer;
import com.swiggone.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer getCustomerById(Integer id) {
        return customerRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer " + id + " was not found"));
    }

    public Customer addCustomer(Customer customer) {
        customer.setId(null);
        return customerRepository.save(customer);
    }

    public Customer updateCustomer(Integer id, Customer updatedCustomer) {
        Customer existingCustomer = getCustomerById(id);

        existingCustomer.setName(updatedCustomer.getName());
        existingCustomer.setEmail(updatedCustomer.getEmail());
        existingCustomer.setPhone(updatedCustomer.getPhone());

        return customerRepository.save(existingCustomer);
    }

    public void deleteCustomer(Integer id) {
        Customer customer = getCustomerById(id);
        customerRepository.delete(customer);
    }
}
