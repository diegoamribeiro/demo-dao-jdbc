package application;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.dao.impl.DepartmentDaoJDBC;
import model.dao.impl.SellerDaoJDBC;
import model.entities.Department;

public class Program2 {
    public static void main(String[] args) {

        DepartmentDao departmentDao = DaoFactory.createDepartmentDao();

        System.out.println("|=== TEST 1 - Department departmentInsert ===|\n");
        Department department = new Department(null, "Guns");
        departmentDao.insert(department);
        System.out.println("Created department - " + department.getName() + " - id - " + department.getId());

    }
}
