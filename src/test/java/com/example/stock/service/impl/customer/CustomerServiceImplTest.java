package com.example.stock.service.impl.customer;


import com.example.stock.data.CustomerRepository;
import com.example.stock.data.model.customer.CustomerEntity;
import com.example.stock.service.model.customer.CustomerDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class CustomerServiceImplTest {

    @MockBean
    private CustomerRepository customerRepository;

    private CustomerServiceImpl customerServiceImpl;

    @BeforeEach
    void setUp() {
        customerServiceImpl = new CustomerServiceImpl(customerRepository);
    }

    @Test
    void testCreateCustomer_Success() {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setName("Test");

        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setName("Test");

        when(customerRepository.save(any(CustomerEntity.class))).thenReturn(customerEntity);

        CustomerDTO result = customerServiceImpl.createCustomer(customerDTO);

        assertNotNull(result);
        assertEquals("Test", result.getName());
        verify(customerRepository, times(1)).save(any(CustomerEntity.class));
    }

    @Test
    void testCreateCustomer_Exception() {
        when(customerRepository.save(any(CustomerEntity.class))).thenThrow(new RuntimeException("Error saving customer"));

        CustomerDTO result = customerServiceImpl.createCustomer(new CustomerDTO());

        assertNull(result);
        verify(customerRepository, times(1)).save(any(CustomerEntity.class));
    }

    @Test
    void testFindCustomerById_Success() {
        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setId(1L);
        customerEntity.setName("Test");

        doReturn(Optional.of(customerEntity)).when(customerRepository).findById(anyLong());

        CustomerDTO result = customerServiceImpl.findCustomerById(1L);

        assertEquals(1L, result.getId());
        assertEquals("Test", result.getName());

        verify(customerRepository, times(1)).findById(anyLong());
    }

    @Test
    void testFindCustomerById_Success2() {
        // Arrange: CustomerEntity mock'u oluşturuluyor
        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setId(1L);
        customerEntity.setName("Test");

        // CustomerRepository.findById() metodunun davranışı tanımlanıyor
        doReturn(Optional.of(customerEntity)).when(customerRepository).findById(anyLong());

        // Act: findCustomerById() metodunu çağırıyoruz
        CustomerDTO result = customerServiceImpl.findCustomerById(1L);

        // Hata Ayıklama: Eğer result null ise, hatanın nereden kaynaklandığını bulmak için bu kontrolü yapıyoruz
        assertNotNull(result, "Result should not be null. Check if the CustomerServiceMapper or repository is returning null.");

        // Assert: Sonucun null olmadığını ve doğru değerler içerdiğini doğruluyoruz
        assertEquals(1L, result.getId());
        assertEquals("Test", result.getName());

        // CustomerRepository.findById() metodunun sadece bir kere çağrıldığını doğruluyoruz
        verify(customerRepository, times(1)).findById(anyLong());
    }


    @Test
    void testFindCustomerById_NotFound() {
        when(customerRepository.findById(anyLong())).thenReturn(Optional.empty());

        CustomerDTO result = customerServiceImpl.findCustomerById(1L);

        assertNull(result);
        verify(customerRepository, times(1)).findById(anyLong());
    }

    @Test
    void testUpdateCustomer_Success() {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(1L);
        customerDTO.setName("Test Customer");

        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setId(1L);
        customerEntity.setName("Test Customer");

        when(customerRepository.save(any(CustomerEntity.class))).thenReturn(customerEntity);

        CustomerDTO result = customerServiceImpl.updateCustomer(customerDTO);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Customer", result.getName());
        verify(customerRepository, times(1)).save(any(CustomerEntity.class));
    }

    @Test
    void testUpdateCustomer_Exception() {
        when(customerRepository.save(any(CustomerEntity.class))).thenThrow(new RuntimeException("Error updating customer"));

        Exception e = assertThrows(RuntimeException.class, () -> {
            customerServiceImpl.updateCustomer(new CustomerDTO());
        });
        assertEquals("Error updating customer", e.getMessage());
        verify(customerRepository, times(1)).save(any(CustomerEntity.class));
    }

    @Test
    void testDeleteCustomer_Success() {
        doNothing().when(customerRepository).deleteById(anyLong());

        Boolean result = customerServiceImpl.deleteCustomer(1L);

        assertTrue(result);
    }
}