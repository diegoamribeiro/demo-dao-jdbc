package application;

import model.entities.Department;

import java.text.ParseException;
import java.util.Date;

public class Program {
    public static void main(String[] args) throws ParseException {

        Department department = new Department(1, "Books");

        Seller seller = new Seller(
                1,
                "Diego Ribeiro",
                "dmribeiro@gmail.com",
                new Date(),
                2100.00,
                department);

        System.out.println(department);
        System.out.println(seller);
    }
}
