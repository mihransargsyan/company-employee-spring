package com.example.companyemployeespring.service;

import com.example.companyemployeespring.dto.CreateEmployeeRequest;
import com.example.companyemployeespring.entity.Company;
import com.example.companyemployeespring.entity.Employee;
import com.example.companyemployeespring.entity.EmployeeImage;
import com.example.companyemployeespring.repository.CompanyRepository;
import com.example.companyemployeespring.repository.EmployeeImageRepository;
import com.example.companyemployeespring.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final CompanyRepository companyRepository;
    private final EmployeeImageRepository employeeImageRepository;

    private String imagePath = "C:\\Users\\NZP\\IdeaProjects\\company-employee-spring\\src\\images\\";

    public void deleteById(int id) {
        employeeRepository.deleteById(id);
    }

    public Employee save(Employee employee) {
        return employeeRepository.save(employee);
    }

    public Employee findById(int id) {
        return employeeRepository.getById(id);
    }

    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    public Employee addEmployeeFromRequest(CreateEmployeeRequest createEmployeeRequest,
                                           MultipartFile[] uploadedFiles) throws IOException {
        Employee employee = getEmployeeFromRequest(createEmployeeRequest);
        employeeRepository.save(employee);
        saveEmployeeImages(uploadedFiles, employee);
        return employee;
    }

    private void saveEmployeeImages(MultipartFile[] uploadedFiles, Employee employee) throws IOException {
        if (uploadedFiles.length != 0) {
            for (MultipartFile uploadedFile : uploadedFiles) {
                String fileName = System.currentTimeMillis() + "_" + uploadedFile.getOriginalFilename();
                File newFile = new File(imagePath + fileName);
                uploadedFile.transferTo(newFile);
                EmployeeImage employeeImage = EmployeeImage.builder()
                        .name(fileName)
                        .employee(employee)
                        .build();
                employeeImageRepository.save(employeeImage);
            }
        }
    }

    private Employee getEmployeeFromRequest(CreateEmployeeRequest createEmployeeRequest) {
        Optional<Company> companyById = companyRepository.findById(createEmployeeRequest.getCompanyId());
        Company company = companyById.get();
        return Employee.builder()
                .id(createEmployeeRequest.getId())
                .name(createEmployeeRequest.getName())
                .surname(createEmployeeRequest.getSurname())
                .email(createEmployeeRequest.getEmail())
                .phoneNumber(createEmployeeRequest.getPhoneNumber())
                .salary(createEmployeeRequest.getSalary())
                .position(createEmployeeRequest.getPosition())
                .company(company)
                .build();
    }

}
