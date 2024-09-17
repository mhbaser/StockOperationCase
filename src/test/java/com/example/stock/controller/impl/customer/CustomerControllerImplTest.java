package com.example.stock.controller.impl.customer;

import com.example.stock.controller.model.Customer;
import com.example.stock.service.CustomerService;
import com.example.stock.service.model.customer.CustomerDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.EmptyResultDataAccessException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class CustomerControllerImplTest {

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerControllerImpl customerControllerImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateCustomer_Success() {
        Customer customer = new Customer();
        customer.setName("John Doe");

        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setName("John Doe");

        when(customerService.createCustomer(any(CustomerDTO.class))).thenReturn(customerDTO);

        Customer result = customerControllerImpl.createCustomer(customer);

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        verify(customerService, times(1)).createCustomer(any(CustomerDTO.class));
    }

    @Test
    void testCreateCustomer_Exception() {
        Customer customer = new Customer();
        when(customerService.createCustomer(any(CustomerDTO.class))).thenThrow(new RuntimeException("Error creating customer"));

        Customer result = customerControllerImpl.createCustomer(customer);

        assertNull(result);
        verify(customerService, times(1)).createCustomer(any(CustomerDTO.class));
    }

    @Test
    void testGetCustomerById_Success() {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(1L);
        customerDTO.setName("John Doe");

        when(customerService.findCustomerById(anyLong())).thenReturn(customerDTO);

        Customer result = customerControllerImpl.getCustomerById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John Doe", result.getName());
        verify(customerService, times(1)).findCustomerById(anyLong());
    }

    @Test
    void testGetCustomerById_EmptyResultDataAccessException() {
        when(customerService.findCustomerById(anyLong())).thenThrow(new EmptyResultDataAccessException("No customer found", 1));

        Customer result = customerControllerImpl.getCustomerById(1L);

        assertNotNull(result);
        assertEquals(new Customer(), result);
        verify(customerService, times(1)).findCustomerById(anyLong());
    }

    @Test
    void testUpdateCustomer_Success() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("John Doe");

        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(1L);
        customerDTO.setName("John Doe");

        when(customerService.updateCustomer(any(CustomerDTO.class))).thenReturn(customerDTO);

        Customer result = customerControllerImpl.updateCustomer(customer);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John Doe", result.getName());
        verify(customerService, times(1)).updateCustomer(any(CustomerDTO.class));
    }

    @Test
    void testUpdateCustomer_Exception() {
        Customer customer = new Customer();
        when(customerService.updateCustomer(any(CustomerDTO.class))).thenThrow(new RuntimeException("Error updating customer"));

        Customer result = customerControllerImpl.updateCustomer(customer);

        assertNull(result);
        verify(customerService, times(1)).updateCustomer(any(CustomerDTO.class));
    }

    @Test
    void testDeleteCustomer_Exception() {
        doThrow(new RuntimeException("Error deleting customer")).when(customerService).deleteCustomer(anyLong());

        Boolean result = customerControllerImpl.deleteCustomer(1L);

        assertFalse(result);
        verify(customerService, times(1)).deleteCustomer(anyLong());
    }
}
