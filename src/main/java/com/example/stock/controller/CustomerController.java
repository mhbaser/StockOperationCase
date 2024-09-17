package com.example.stock.controller;

import com.example.stock.controller.model.Customer;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer")
public interface CustomerController {


    @PostMapping("/create")
    Customer createCustomer(@RequestBody Customer customer);

    @GetMapping("/get")
    Customer getCustomerById(@RequestParam("id") Long id);

    @PatchMapping("/update")
    Customer updateCustomer(@RequestBody Customer customer);

    @DeleteMapping("/delete")
    Boolean deleteCustomer(@RequestParam("id") Long id);

}
