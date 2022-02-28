package io.ahad.Controller;

import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.ahad.Entity.Employee;
import io.ahad.Repository.Emp_Direct_Repo;
import io.ahad.Service.AppConstants;
import io.ahad.Service.Emp_Service;
import io.ahad.Service.EmployeeDTO;
import io.ahad.Service.NotFoundException;


@RestController
@RequestMapping("/emp")
public class Emp_Controller {

	
	    @Autowired
	    private Emp_Service service;
	    
	    @Autowired
	    private Emp_Direct_Repo repository;

	    
//	    @GetMapping
//	    public List<Employee> getAll(){
//	        return this.service.getAll();
//	    }
	        
//	    @GetMapping("/name/{name}")
//	      public ResponseEntity<List<Employee>> getEmployeeByName(@PathVariable("name") String name)throws NotFoundException{
//	      List<Employee> response=service.findAllByName(name);
//          return ResponseEntity.ok(response);
//	    }
//	    
//	    @GetMapping("/id/{id}")
//	      public ResponseEntity<List<Employee>> getEmployeeById(@PathVariable("id") long id)throws NotFoundException{
//	 List<Employee> response=repository.findAllById(id);
//	    return ResponseEntity.ok(response);
//	    }

	    
	    @GetMapping
	    //("/pagination/{offset}/{pageSize}")
	    private List<Employee> getEmployeesWithPagination(
	    		@RequestParam(value = "offset", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int offset,
	            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int pageSize) {
	        Page<Employee> employeesWithPagination = service.findEmployeeWithPagination(offset, pageSize);
	        List<Employee> emp=employeesWithPagination.getContent();
	        return  emp;
	    }
	    @GetMapping("/name/{name}")
	    //("/pagination/{offset}/{pageSize}")
	    private ResponseEntity<List<Employee>> getNewEmployeesWithPagination(@PathVariable("name") String name,
	    		@RequestParam(value = "offset", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int offset,
	            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int pageSize)throws NotFoundException
	    {
	        Page<Employee> employeesWithPagination = service.findNewEmployeeWithPagination(name,offset, pageSize);
	        List<Employee> emp=employeesWithPagination.getContent();
	        if(CollectionUtils.isEmpty(emp)){
	            throw new NotFoundException("Name", "name", name);
	 			}
	        return ResponseEntity.ok(emp); 
	    }
	    @GetMapping("/id/{id}")
	    //("/pagination/{offset}/{pageSize}")
	    private ResponseEntity<List<Employee>> getIdEmployeesWithPagination(@PathVariable("id") int id,
	    		@RequestParam(value = "offset", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int offset,
	            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int pageSize)throws NotFoundException
	    {
	        Page<Employee> employeesWithPagination = service.findIdEmployeeWithPagination(id,offset, pageSize);
	        List<Employee> emp=employeesWithPagination.getContent();
	        String idString=Integer.toString(id);
	        if(CollectionUtils.isEmpty(emp)){
	            throw new NotFoundException("Id", "emp_id",idString);
	 			}
	        return ResponseEntity.ok(emp);
	    }
}
