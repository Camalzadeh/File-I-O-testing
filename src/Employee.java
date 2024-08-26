import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class Employee {
    int id;
    String name;
    String surname;
    int age;
    BigDecimal salary;
    public Departments department;
    boolean isPermanent;
    LocalDate startDate;
    String email;
    String phone;
    Positions position;
    String address;

    public Employee(int id,String name, String surname, int age, BigDecimal salary, Departments department, boolean isPermanent, LocalDate startDate, String email, String phone, Positions position, String address) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.salary = salary;
        this.department = department;
        this.isPermanent = isPermanent;
        this.startDate = startDate;
        this.email = email;
        this.phone = phone;
        this.position = position;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public Positions getPosition() {
        return position;
    }

    public Departments getDepartment() {
        return department;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void raiseSalary() {
        salary =  salary.multiply(BigDecimal.valueOf(1.1));
    }

    @Override
    public String toString() {
        return "id : " + id +
                ", name : '" + name + '\'' +
                ", surname : '" + surname + '\'' +
                ", age : " + age +
                ", salary : " + salary +
                ", department : '" + department + '\'' +
                ", isPermanent : " + isPermanent +
                ", startDate : " + startDate +
                ", email : '" + email + '\'' +
                ", phone : '" + phone + '\'' +
                ", position : '" + position + '\'' +
                ", address : '" + address + '\''
                ;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Employee employee)) return false;
        return id == employee.id || Objects.equals(email, employee.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }
}
