package exam.service.impl;

import com.google.gson.Gson;
import exam.model.dto.CustomerSeedRootDtoJSON;
import exam.model.entity.Customer;
import exam.model.entity.Town;
import exam.repository.CustomerRepository;
import exam.repository.TownRepository;
import exam.service.CustomerService;
import exam.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Service
public class CustomerServiceImpl implements CustomerService {
    private static final String CUSTOMER_PATH_FILE = "src/main/resources/files/json/customers.json";

    private CustomerRepository customerRepository;
    private TownRepository townRepository;
    private ValidationUtil validationUtil;
    private ModelMapper modelMapper;
    private Gson gson;

    public CustomerServiceImpl(CustomerRepository customerRepository, TownRepository townRepository, ValidationUtil validationUtil, ModelMapper modelMapper, Gson gson) {
        this.customerRepository = customerRepository;
        this.townRepository = townRepository;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.gson = gson;
    }

    @Override
    public boolean areImported() {
        return customerRepository.count() > 0;
    }

    @Override
    public String readCustomersFileContent() throws IOException {
        return Files.readString(Path.of(CUSTOMER_PATH_FILE));
    }

    @Override
    public String importCustomers() throws IOException {
        StringBuilder builder = new StringBuilder();
        CustomerSeedRootDtoJSON[] customerSeedRootDtoJSONS = gson.fromJson(readCustomersFileContent(), CustomerSeedRootDtoJSON[].class);
        Arrays.stream(customerSeedRootDtoJSONS)
                .filter(customerSeedRootDtoJSON -> {
                    boolean isValid = validationUtil.isValid(customerSeedRootDtoJSON)
                            && !ifCustomerEmailExists(customerSeedRootDtoJSON.getEmail());
                    if (isValid) {
                        builder
                                .append(String.format("Successfully imported Customer %s %s - %s%n",
                                        customerSeedRootDtoJSON.getFirstName(),
                                        customerSeedRootDtoJSON.getLastName(),
                                        customerSeedRootDtoJSON.getEmail()));
                    } else {
                        builder
                                .append("Invalid Customer").append(System.lineSeparator());
                    }
                    return isValid;
                })
                .map(customerSeedRootDtoJSON -> {
                    Customer customer = modelMapper.map(customerSeedRootDtoJSON, Customer.class);
                    customer.setTown(findTownByName(customerSeedRootDtoJSON.getTown().getName()));
                    return customer;
                })
                .forEach(customerRepository::save);
        return builder.toString();
    }

    private Town findTownByName(String test) {
        return townRepository.findTownByName(test);
    }

    private boolean ifCustomerEmailExists(String email) {
        return customerRepository.existsByEmail(email);
    }
}