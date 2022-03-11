package com.example.companyemployeespring.controller;

import com.example.companyemployeespring.dto.CreateEmployeeRequest;
import com.example.companyemployeespring.service.CompanyService;
import com.example.companyemployeespring.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class EmployeeController {

    private  final EmployeeService employeeService;
    private  final CompanyService companyService;

    private String imagePath = "C:\\Users\\NZP\\IdeaProjects\\company-employee-spring\\src\\images\\";

    @GetMapping("/employees")
    public String main(ModelMap map) {
        map.addAttribute("employees", employeeService.findAll());
        return "employees";
    }

    @GetMapping("/deleteEmployee/{id}")
    public String deleteEmployee(@PathVariable("id") int id) {
        employeeService.deleteById(id);
        return "redirect:/employees";
    }

    @GetMapping("/addEmployee")
    public String addEmployeePage(ModelMap map) {
        map.addAttribute("companies", companyService.findAll() );
        return "addEmployee";
    }

    @PostMapping("/addEmployee")
    public String addEmployee(@ModelAttribute CreateEmployeeRequest createEmployeeRequest,
                              @RequestParam("pictures") MultipartFile[] uploadedFiles) throws IOException {
        employeeService.addEmployeeFromRequest(createEmployeeRequest,uploadedFiles);
        return "redirect:/employees";
    }
}
