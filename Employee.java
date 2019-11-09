
import java.util.Objects;


public class Employee extends Person implements java.lang.Comparable<Employee>{
   
    
    private String employeeID;
    private Employee manager;
    private int nrOfFreeDays;
    private double budget;


    
    public Employee(String employeeID, Employee manager,String firstname, String lastname, String gender, String email) {
        super(firstname, lastname, gender, email);
        this.employeeID = employeeID;
        this.manager = manager;
        this.nrOfFreeDays = 100;
        this.budget = 1000;
    }
   
    
    
    public String getEmployeeID() {
        return employeeID;
    }

    public Employee getManager() {
        return manager;
    }

    public int getNrOfFreeDays() {
        return nrOfFreeDays;
    }

    public double getBudget() {
        return budget;
    }

    public void setEmployeeID(String employeeID) {
        this.employeeID = employeeID;
    }

    public void setManager(Employee manager) {
        this.manager = manager;
    }

    public void setNrOfFreeDays(int nrOfFreeDays) {
        this.nrOfFreeDays = nrOfFreeDays;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }
    
    @Override
   public boolean equals(Object object){
       boolean test = false;
       if(object != null && object instanceof Employee){
       Employee temp = Employee.class.cast(object);
       if(temp.getEmployeeID().equals(this.employeeID))
           test=true;
       }

       return test;
   } 

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.employeeID);
        return hash;
    }
   
   public int compareTo(Employee e){
       return this.employeeID.compareTo(e.employeeID);
       
   }
   
    @Override
    public String toString(){
        if(this.manager == null)
            return(this.employeeID + " " + this.getFirstname() + " " + this.getLastname());
        else
            return(this.employeeID + " " + this.getFirstname() + " " + this.getLastname() + " (Manager: " + this.manager.getEmployeeID() + ")");

    }
    
}
