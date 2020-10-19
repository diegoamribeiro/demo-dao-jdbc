package application;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.dao.impl.DepartmentDaoJDBC;
import model.dao.impl.SellerDaoJDBC;
import model.entities.Department;

public class Program2 {
    public static void main(String[] args) {

        DepartmentDao departmentDao = DaoFactory.createDepartmentDao();

//        System.out.println("|=== TEST 1 - Department departmentInsert ===|\n");
          Department department = new Department(null, "Guns");
//        departmentDao.insert(department);
//        System.out.println("Created department - " + department.getName() + " - id - " + department.getId());
//
//        System.out.println("|=== TEST 2 - Department departmentUpdate ===|\n");
//        department.setId(3);
//        department.setName("Home");
//        departmentDao.update(department);
//        System.out.println("Updated department - " + department.getName() + " - id - " + department.getId());

        System.out.println("|=== TEST 3 - Department departmentDelete ===|\n");
        departmentDao.deleteById(6);
        System.out.println("Deleted department - " + department.getName() + " - id - " + department.getId());

    }
}
