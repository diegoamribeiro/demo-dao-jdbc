package application;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;
import java.text.ParseException;
import java.util.Date;
import java.util.List;


public class Program {
    public static void main(String[] args) throws ParseException {

        SellerDao sellerDao = DaoFactory.createSellerDao();

        System.out.println("|=== TEST 1 - Seller findById ===|\n");
        Seller seller = sellerDao.findById(3);
        System.out.println(seller);

        System.out.println();

        System.out.println("|=== TEST 2 - Seller findByDepartment ===|\n");
        Department department = new Department(2, null);
        List<Seller> sellerList = sellerDao.findByDepartment(department);

        for (Seller list : sellerList ){
            System.out.println(list);
        }

        System.out.println();

        System.out.println("|=== TEST 3 - Seller findByAll ===|\n");
        List<Seller> sellerListAll = sellerDao.findAll();

        for (Seller list : sellerListAll ){
            System.out.println(list);
        }

        System.out.println();

        System.out.println("|=== TEST 4 - Seller sellerInsert ===|\n");
        Seller seller1 = new Seller(null, "Greg", "greg@gmail.com", new Date(), 4000.00, department);
        sellerDao.insert(seller1);
        System.out.println("Inserted new id = " + seller1.getId());
    }
}
