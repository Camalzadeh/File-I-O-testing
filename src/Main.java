import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    static String data="src/employees.txt";
    static ArrayList<Employee> employees = new ArrayList<>();
    public static void readData(){
        try(
                FileReader fileReader=new FileReader(data);
                BufferedReader bufferedReader=new BufferedReader(fileReader))
        {
            String line;
            while((line=bufferedReader.readLine())!=null){
                String[] data=line.split(",");
                int id=Integer.parseInt(data[0]);
                String name=data[1];
                String surname=data[2];
                int age=Integer.parseInt(data[3]);
                BigDecimal salary=BigDecimal.valueOf(Double.parseDouble(data[4]));
                Departments department=Departments.valueOf(data[5]);
                boolean isPermanent=Boolean.parseBoolean(data[6]);
                LocalDate startDate=LocalDate.parse(data[7]);
                String email=data[8];
                String phone=data[9];
                Positions position=Positions.valueOf(data[10]);
                String address=data[11];
                employees.add(new Employee(id,name,surname,age,salary,department,isPermanent,startDate,email,phone,position,address));
            }
        }catch(IOException e){
            e.printStackTrace();
        }catch(IllegalArgumentException e){
            e.printStackTrace();
        }
        System.out.println("Data read successfully");
    }
    public static void writeData(Path path, Collection<String> employees){
        if(path!=null && !employees.isEmpty() && Files.exists(path)){
            try{
                Files.write(path,employees);
            }catch(IOException e){
                e.printStackTrace();
            }
        }
        System.out.println("Data written successfully");
    }
    public static void main(String[] args) {
        int counter =1;
        Path path = Paths.get("src/tests/test"+counter);
        while(Files.exists(path)){
            counter++;
            path = Paths.get("src/tests/test"+counter);
        }
        try{
            Files.createDirectories(path);
        }catch(IOException e){
            e.printStackTrace();
        }
        readData();

        Path path1=null, path2=null,path3=null,path4=null,
                path5=null,path6=null,path7=null,path8=null;

        try{
            path1 = path.resolve("processed_employees.txt");
            Files.createFile(path1);
            path2 = path.resolve("department_summary.txt");
            Files.createFile(path2);
            path3 = path.resolve("new_employees.txt");
            Files.createFile(path3);
            path4 = path.resolve("unique_employees.txt");
            Files.createFile(path4);
            path5 = path.resolve("average_salary_by_department.txt");
            Files.createFile(path5);
            path6 = path.resolve("longest_serving_employee.txt");
            Files.createFile(path6);
            path7 = path.resolve("employees_by_position.txt");
            Files.createFile(path7);
            path8 = path.resolve("employees_by_department_and_position.txt");
            Files.createFile(path8);
        }catch (InvalidPathException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        System.out.println("Files created successfully");

        employees = employees
                .stream()
                .filter(employee -> employee.isPermanent)
                .sorted(Comparator.comparing(o -> o.startDate))
                .map((employee)->{
                    employee.raiseSalary();
                    return employee;
                }).sorted(Comparator.comparing(Employee::getId))
                .collect(Collectors.toCollection(ArrayList::new));


        List<String> processed_employees = employees
                .stream()
                .map(Employee::toString)
                .toList();

        writeData(path1,processed_employees);

        employees.forEach(e->{
            e.department.addEmployee();
            e.department.addSalary(e.salary);
        });

        List<String> department_summary = Stream.of(Departments
                .values())
                .map(Departments::getSummary)
                .toList();

        writeData(path2,department_summary);

        List<String> new_employees = employees
                .stream()
                .filter(e->LocalDate.now().isBefore(e.startDate.plusYears(2)))
                .map(Employee::toString)
                .toList();

        writeData(path3,new_employees);

        List<String> unique_employees = new HashSet<>(employees)
                .stream()
                .sorted(Comparator.comparing(Employee::getId))
                .map(Employee::toString)
                .toList();

        writeData(path4,unique_employees);

        List<String> average_salary_by_department = Stream.of(Departments.values())
                .map(Departments::getSummaryWithAverage)
                .toList();

        writeData(path5,average_salary_by_department);

        List<String> longest_serving_employee = employees
                .stream()
                .max(Comparator.comparing(Employee::getStartDate))
                .stream()
                .map(Employee::toString)
                .toList();

        writeData(path6,longest_serving_employee);

        List<String> employees_by_position = employees
                .stream()
                .collect(Collectors.groupingBy(Employee::getPosition))
                .entrySet()
                .stream()
                .map(e -> e.getKey().toString()+ " : "+e.getValue().size()+"\n")
                .toList();


        writeData(path7,employees_by_position);

        List<String> employees_by_department_and_position = new ArrayList<>();
        employees_by_department_and_position.add("\n------------Grouping by department------------\n");


        List<String> forDepartment = employees.stream()
                .collect(Collectors.groupingBy(Employee::getDepartment))
                .entrySet()
                .stream()
                .map(e -> {
                    String departmentName = e.getKey().toString();
                    String employeeDetails = e.getValue().stream()
                            .map(emp -> emp.toString())
                            .collect(Collectors.joining("\n   "));
                    return departmentName + " :\n   " + employeeDetails + "\n";
                })
                .flatMap(e -> Arrays.stream(e.split("\n")))
                .toList();

        employees_by_department_and_position.addAll(forDepartment);
        employees_by_department_and_position.add("\n------------Grouping by position------------\n");

        List<String> forPosition = employees
                .stream()
                .collect(Collectors.groupingBy(Employee::getPosition))
                .entrySet()
                .stream()
                .map(e -> {
                    String positionName = e.getKey().toString();
                    String employeeDetails = e.getValue().stream()
                            .map(emp -> emp.toString())
                            .collect(Collectors.joining("\n   "));
                    return positionName + " :\n   " + employeeDetails + "\n";
                })
                .toList();

        employees_by_department_and_position.addAll(forPosition);

        writeData(path8,employees_by_department_and_position);
    }
}