import entities.Address;
import entities.Employee;
import entities.Project;
import entities.Town;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Stream;

public class Engine implements Runnable {
    private final EntityManager entityManager;
    private final BufferedReader bufferedReader;

    public Engine(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public void run() {
        System.out.println("Please select exercise number:");
        try {
            int exerciseNumber = Integer.parseInt(bufferedReader.readLine());
            switch (exerciseNumber) {
                case 2:
                    Exercise_2_ChangeCasing();
                    break;
                case 3:
                    Exercise_3_ContainsEmployee();
                    break;
                case 4:
                    Exercise_4_EmployeesWithSalaryOver50000();
                    break;
                case 5:
                    Exercise_5_EmployeesFromDepartment();
                    break;
                case 6:
                    Exercise_6_AddingANewAddressAndUpdatingEmployee();
                    break;
                case 7:
                    Exercise_7_AddressesWithEmployeeCount();
                    break;
                case 8:
                    Exercise_8_GetEmployeeWithProject();
                    break;
                case 9:
                    Exercise_9_FindLatest10Projects();
                    break;
                case 10:
                    Exercise_10_IncreaseSalaries();
                    break;
                case 11:
                    Exercise_11_FindEmployeesByFirstName();
                    break;
                case 12:
                    Exercise_12_EmployeesMaximumSalaries();
                    break;
                case 13:
                    Exercise_13_RemoveTowns();
                    break;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            entityManager.close();
        }
    }

    private void Exercise_2_ChangeCasing() {
        entityManager.getTransaction().begin();
        entityManager.createQuery
                ("UPDATE Town AS t SET t.name = UPPER(t.name) WHERE LENGTH(t.name) > 5").executeUpdate();
        entityManager.getTransaction().commit();
    }

    private void Exercise_3_ContainsEmployee() throws IOException {
        System.out.println("Please enter employee full name:");
        String[] employeeName = bufferedReader.readLine().split(" ");
        String firstName = employeeName[0];
        String lastName = employeeName[1];
        List<Employee> matchList = entityManager.createQuery
                        ("SELECT e FROM Employee AS e WHERE e.firstName =: first_name" +
                                " AND e.lastName =: last_name", Employee.class)
                .setParameter("first_name", firstName)
                .setParameter("last_name", lastName)
                .getResultList();
        if (matchList.isEmpty()) {
            System.out.println("No");
        } else {
            System.out.println("Yes");
        }
    }

    private void Exercise_4_EmployeesWithSalaryOver50000() {
        BigDecimal salary = BigDecimal.valueOf(50000);
        List<Employee> employeeList = entityManager.createQuery
                        ("SELECT e FROM Employee AS e WHERE e.salary >: employee_salary ", Employee.class)
                .setParameter("employee_salary", salary)
                .getResultList();
        employeeList.forEach(employee -> System.out.printf("%s%n", employee.getFirstName()));
    }

    private void Exercise_5_EmployeesFromDepartment() {
        String departmentName = "Research and Development";
        List<Employee> employeeList = entityManager.createQuery
                        ("SELECT e FROM Employee AS e WHERE e.department.name =: department_name ORDER BY e.salary, e.id", Employee.class)
                .setParameter("department_name", departmentName)
                .getResultList();
        employeeList.forEach(employee -> System.out.printf("%s %s from %s - $%.2f%n",
                employee.getFirstName(),
                employee.getLastName(),
                employee.getDepartment().getName(),
                employee.getSalary()));
    }

    private void Exercise_6_AddingANewAddressAndUpdatingEmployee() throws IOException {
        System.out.println("Please enter employee last name:");
        String lastName = bufferedReader.readLine();
        entityManager.getTransaction().begin();
        Address address = new Address();
        address.setText("Vitoshka 15");
        entityManager.persist(address);
        int checkIfWeHaveSuchNameInDB = entityManager.createQuery
                        ("UPDATE Employee AS e SET e.address =: employee_address WHERE e.lastName =: last_name")
                .setParameter("employee_address", address)
                .setParameter("last_name", lastName)
                .executeUpdate();
        if (checkIfWeHaveSuchNameInDB != 0) {
            entityManager.getTransaction().commit();
        } else {
            entityManager.getTransaction().rollback();
        }
    }

    private void Exercise_7_AddressesWithEmployeeCount() {
        TypedQuery<Address> addressTypedQuery = entityManager.createQuery
                ("SELECT a FROM Address AS a ORDER BY a.employees.size desc", Address.class).setMaxResults(10);
        List<Address> resultList = addressTypedQuery.getResultList();
        for (Address address : resultList) {
            System.out.printf("%s, %s - %d employees%n",
                    address.getText(),
                    (address.getTown() == null) ? "Unknown" : address.getTown().getName(),
                    address.getEmployees().size());
        }
    }

    private void Exercise_8_GetEmployeeWithProject() throws IOException {
        System.out.println("Please insert employee ID:");
        int ID = Integer.parseInt(bufferedReader.readLine());
        Employee employee = entityManager.createQuery
                        ("SELECT e FROM Employee AS e WHERE e.id =: employee_id ", Employee.class)
                .setParameter("employee_id", ID)
                .getSingleResult();
        System.out.printf("%s %s - %s%n", employee.getFirstName(), employee.getLastName(), employee.getJobTitle());
        Set<Project> projects = employee.getProjects();
        ArrayList<String> allProjectNames = new ArrayList<>();
        for (Project project : projects) {
            allProjectNames.add(project.getName());
        }
        Stream<String> sortedNamesByAscendingOrder = allProjectNames.stream().sorted();
        sortedNamesByAscendingOrder.forEach(project_name -> System.out.printf("      %s%n", project_name));
    }

    private void Exercise_9_FindLatest10Projects() {
        // TODO : ЗАДАЧКАТА РАБОТИ НО ВАДИ МНОГО СТРАНЕН ФОРМАТ НА ЧАСОВЕТЕ.
        TypedQuery<Project> projectTypedQuery = entityManager
                .createQuery("SELECT p FROM Project AS p ORDER BY p.startDate desc", Project.class)
                .setMaxResults(10);
        StringBuilder builder = new StringBuilder();
        List<Project> resultList = projectTypedQuery.getResultList();
        resultList.sort(Comparator.comparing(Project::getName));
        for (Project project : resultList) {
            builder.append(String.format("Project name: %s", project.getName())).append(System.lineSeparator());
            builder.append(String.format("        Project Description: %s", project.getDescription())).append(System.lineSeparator());
            builder.append(String.format("        Project Start Date:%s", project.getStartDate())).append(System.lineSeparator());
            builder.append(String.format("        Project End Date: %s%n", project.getEndDate())).append(System.lineSeparator());
        }
        System.out.println(builder);
    }

    private void Exercise_10_IncreaseSalaries() {
        entityManager.getTransaction().begin();
        entityManager.createQuery
                ("UPDATE Employee AS e SET e.salary = e.salary * 1.12" +
                        " WHERE e.department.id IN (1,2,4,11)").executeUpdate();
        entityManager.getTransaction().commit();
        List<Employee> employeeList = entityManager.createQuery
                ("SELECT e FROM Employee AS e" +
                        " WHERE e.department.id IN (1,2,4,11)", Employee.class).getResultList();
        for (Employee employee : employeeList) {
            System.out.printf("%s %s ($%.2f)%n", employee.getFirstName(), employee.getLastName(), employee.getSalary());
        }
    }

    private void Exercise_11_FindEmployeesByFirstName() throws IOException {
        System.out.println("Please enter pattern:");
        String pattern = bufferedReader.readLine();
        List<Employee> employeeList = entityManager.createQuery
                        ("SELECT e FROM Employee AS e WHERE e.firstName LIKE : test ", Employee.class)
                .setParameter("test", pattern + "%").getResultList();
        for (Employee employee : employeeList) {
            System.out.printf("%s %s - %s - ($%.2f)%n",
                    employee.getFirstName(),
                    employee.getLastName(),
                    employee.getJobTitle(),
                    employee.getSalary());
        }
    }

    @SuppressWarnings("unchecked")
    private void Exercise_12_EmployeesMaximumSalaries() {
        BigDecimal firstDecimal = BigDecimal.valueOf(30000);
        BigDecimal secondDecimal = BigDecimal.valueOf(70000);
        List<Object[]> objectList = entityManager.createQuery(
                        " SELECT d.name, MAX(e.salary) FROM Department AS d" +
                                " JOIN Employee AS e ON d.id = e.department.id" +
                                " GROUP BY d.name" +
                                " HAVING MAX(e.salary) <: first_decimal OR MAX(e.salary) >: second_decimal")
                .setParameter("first_decimal", firstDecimal)
                .setParameter("second_decimal", secondDecimal)
                .getResultList();
        for (Object[] object : objectList) {
            System.out.printf("%s  %s%n", object[0], object[1]);
        }
    }

    private void Exercise_13_RemoveTowns() throws IOException {
        System.out.println("Please enter town name:");
        String townName = bufferedReader.readLine();
        Town town = entityManager.createQuery("SELECT t FROM Town AS t WHERE t.name =: town_name", Town.class)
                .setParameter("town_name", townName)
                .getSingleResult();
        List<Address> addresses = entityManager.createQuery("SELECT a FROM Address AS a WHERE a.town.name =: town_name ", Address.class)
                .setParameter("town_name", townName)
                .getResultList();
        entityManager.getTransaction().begin();
        addresses.forEach(address -> {
            for (Employee employee : address.getEmployees()) {
                employee.setAddress(null);
            }
            address.setTown(null);
            entityManager.remove(address);
        });
        entityManager.remove(town);
        entityManager.getTransaction().commit();
        System.out.printf("%d address%s in %s is deleted",
                addresses.size(),
                addresses.size() != 1 ? "es" : "",
                town.getName());
        entityManager.close();
    }
}