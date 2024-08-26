import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public enum Departments {
    IT,
    EXECUTIVE,
    MARKETING,
    FINANCE,
    SALES,
    HR;
    private int totalEmployees;
    private BigDecimal totalSalary= BigDecimal.ZERO;

    public void addSalary(BigDecimal salary){
        totalSalary = totalSalary.add(salary);
    }

    public void addEmployee(){
        totalEmployees++;
    }
    public String getSummary(){
        return ("Department: "+name()+"\nTotal employees: "+totalEmployees+"\nTotal salary: "+totalSalary);
    }
    public String getSummaryWithAverage(){
        BigDecimal averageSalary=BigDecimal.ZERO;
        if(totalEmployees!=0){
            averageSalary = totalSalary.divide(BigDecimal.valueOf(totalEmployees), 2, RoundingMode.HALF_UP);
        }
        return ("Department: "+name()+
                "\nTotal employees: "+totalEmployees+
                "\nTotal salary: "+totalSalary+
                "\nAverage salary: "+averageSalary);
    }
}
