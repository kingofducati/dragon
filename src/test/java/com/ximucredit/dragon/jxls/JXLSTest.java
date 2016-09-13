package com.ximucredit.dragon.jxls;

import static org.junit.Assert.*;

import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.junit.Test;

public class JXLSTest {

	@Test
	public void testSample() throws Exception {
		List<Employee> employees = generateSampleEmployeeData();
        try(InputStream is = new FileInputStream("src/test/resources/object_collection_template.xls")) {
            try (OutputStream os = new FileOutputStream("target/object_collection_output.xls")) {
                Context context = new Context();
                context.putVar("employees", employees);
                JxlsHelper.getInstance().processTemplate(is, os, context);
            }
        }
	}
	
	@Test
	public void testGrid() throws Exception{
		List<Employee> employees = generateSampleEmployeeData();
        try(InputStream is = new FileInputStream("src/test/resources/grid_template.xls")) {
            try (OutputStream os = new FileOutputStream("target/grid_output.xls")) {
                Context context = new Context();
                context.putVar("headers", Arrays.asList("项目名称","项目类型","项目编号"));
                context.putVar("data", createGridData(employees));
                JxlsHelper.getInstance().processTemplate(is, os, context);
            }
        }
	}
	
	private List<List<Object>> createGridData(List<Employee> employees) {
        List<List<Object>> data = new ArrayList<>();
        for(Employee employee : employees){
            data.add( convertEmployeeToList(employee));
        }
        return data;
    }
	
	private List<Object> convertEmployeeToList(Employee employee){
        List<Object> list = new ArrayList<>();
        list.add(employee.getName());
        list.add(employee.getBirthDate());
        list.add(employee.getPayment());
        return list;
    }

	private List<Employee> generateSampleEmployeeData() throws ParseException {
        List<Employee> employees = new ArrayList<Employee>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MMM-dd", Locale.US);
        employees.add( new Employee("Elsa", dateFormat.parse("1970-Jul-10"), 1500, 0.15) );
        employees.add( new Employee("Oleg", dateFormat.parse("1973-Apr-30"), 2300, 0.25) );
        employees.add( new Employee("Neil", dateFormat.parse("1975-Oct-05"), 2500, 0.00) );
        employees.add( new Employee("Maria", dateFormat.parse("1978-Jan-07"), 1700, 0.15) );
        employees.add( new Employee("John", dateFormat.parse("1969-May-30"), 2800, 0.20) );
        return employees;
    }
}
