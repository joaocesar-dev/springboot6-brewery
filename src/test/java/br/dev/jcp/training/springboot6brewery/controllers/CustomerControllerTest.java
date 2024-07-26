package br.dev.jcp.training.springboot6brewery.controllers;

import br.dev.jcp.training.springboot6brewery.models.CustomerDTO;
import br.dev.jcp.training.springboot6brewery.services.CustomerService;
import br.dev.jcp.training.springboot6brewery.services.impl.CustomerServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CustomerController.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CustomerService customerService;

    CustomerServiceImpl customerServiceImpl;

    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;
    @Captor
    ArgumentCaptor<CustomerDTO> customerArgumentCaptor;

    @BeforeEach
    void setUp() {
        customerServiceImpl = new CustomerServiceImpl();
    }

    @Test
    void shouldPatchCustomer() throws Exception {
        CustomerDTO customer = customerServiceImpl.getAllCustomers().get(0);
        Map<String, Object> customerMap = new HashMap<>();
        customerMap.put("name", "John Doe");
        mockMvc.perform(patch(CustomerController.CUSTOMER_PATH_ID, customer.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(customerMap)))
                .andExpect(status().isNoContent());
        verify(customerService).patchCustomer(uuidArgumentCaptor.capture(), customerArgumentCaptor.capture());
        assertThat(uuidArgumentCaptor.getValue()).isEqualTo(customer.getId());
        assertThat(customerArgumentCaptor.getValue().getName()).isEqualTo(customerMap.get("name"));
    }

    @Test
    void shouldDeleteCustomer() throws Exception {
        CustomerDTO customer = customerServiceImpl.getAllCustomers().get(0);
        mockMvc.perform(delete(CustomerController.CUSTOMER_PATH_ID, customer.getId())
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        verify(customerService).deleteCustomer(uuidArgumentCaptor.capture());
        assertThat(customer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
    }

    @Test
    void shouldUpdateCustomer() throws Exception {
        CustomerDTO customer = customerServiceImpl.getAllCustomers().get(0);

        mockMvc.perform(put(CustomerController.CUSTOMER_PATH_ID, customer.getId())
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isOk());
        verify(customerService).updateCustomer(any(UUID.class), any(CustomerDTO.class));
    }

    @Test
    void shouldReturnANewCustomer() throws Exception {
        CustomerDTO newCustomer = createInputCustomer();
        CustomerDTO persistedCustomer = createPersistedCustomer(newCustomer);

        given(customerService.saveCustomer(any(CustomerDTO.class))).willReturn(persistedCustomer);

        mockMvc.perform(post(CustomerController.CUSTOMER_PATH)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(newCustomer)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id", is(persistedCustomer.getId().toString())))
                .andExpect(jsonPath("$.name", is(newCustomer.getName())));

    }

    @Test
    void shouldReturnCustomerById() throws Exception {
        CustomerDTO customer = createPersistedCustomer();
        given(customerService.getCustomerById(any(UUID.class))).willReturn(Optional.of(customer));
        mockMvc.perform(get(CustomerController.CUSTOMER_PATH_ID, customer.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(customer.getId().toString())))
                .andExpect(jsonPath("$.name", is(customer.getName())));
    }

    @Test
    void shouldReturnNotFoundWhenCustomerNotFound() throws Exception {
        given(customerService.getCustomerById(any(UUID.class))).willReturn(Optional.empty());
        mockMvc.perform(get(CustomerController.CUSTOMER_PATH_ID, UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnAllCustomers() throws Exception {
        given(customerService.getAllCustomers()).willReturn(createPersistedCustomers());
        mockMvc.perform(get(CustomerController.CUSTOMER_PATH)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(3)));

    }

    private CustomerDTO createInputCustomer() {
        return CustomerDTO.builder()
                .name("New Customer")
                .build();
    }

    private CustomerDTO createPersistedCustomer(CustomerDTO customer) {
        return CustomerDTO.builder()
                .id(UUID.randomUUID())
                .name(customer.getName())
                .version(1)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();
    }

    private CustomerDTO createPersistedCustomer() {
        return CustomerDTO.builder()
                .id(UUID.randomUUID())
                .name("Customer Test 1")
                .version(1)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();
    }

    private List<CustomerDTO> createPersistedCustomers() {
        ArrayList<CustomerDTO> list = new ArrayList<>();
        list.add(createPersistedCustomer());
        list.add(CustomerDTO.builder()
                .id(UUID.randomUUID())
                .name("Customer Test 2")
                .version(1)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build());
        list.add(CustomerDTO.builder()
                .id(UUID.randomUUID())
                .name("Customer Test 3")
                .version(1)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build());
        return list;
    }

}