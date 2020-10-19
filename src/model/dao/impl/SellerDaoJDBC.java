package model.dao.impl;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellerDaoJDBC implements SellerDao {

    private final Connection connection;

    public SellerDaoJDBC(Connection connection){
        this.connection = connection;
    }

    @Override
    public void insert(Seller seller) {
        PreparedStatement mPreparedStatement = null;

        try {
            mPreparedStatement = connection.prepareStatement("" +
                    "INSERT INTO seller "+
                    "(Name, Email, BirthDate, BaseSalary, DepartmentId) "+
                    "VALUES "+
                    "(?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

            mPreparedStatement.setString(1, seller.getName());
            mPreparedStatement.setString(2, seller.getEmail());
            mPreparedStatement.setDate(3, new Date(seller.getBirthDate().getTime()));
            mPreparedStatement.setDouble(4, seller.getBaseSalary());
            mPreparedStatement.setInt(5, seller.getDepartment().getId());

            int rowsAffected = mPreparedStatement.executeUpdate();

            if (rowsAffected > 0){
                ResultSet resultSet = mPreparedStatement.getGeneratedKeys();
                if (resultSet.next()){
                    int id = resultSet.getInt(1);
                    seller.setId(id);
                }
                DB.closeResultSet(resultSet);
            }else {
                throw  new DbException("Unexpected error: No rows affected");
            }
        }catch (SQLException sqlException){
            throw new DbException(sqlException.getMessage());
        }finally {
            DB.closeStatement(mPreparedStatement);
        }
    }

    @Override
    public void update(Seller seller) {
        PreparedStatement mPreparedStatement = null;

        try{
            mPreparedStatement = connection.prepareStatement("" +
                    "UPDATE seller "+
                    "SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? "+
                    "WHERE id = ?");

            mPreparedStatement.setString(1, seller.getName());
            mPreparedStatement.setString(2, seller.getEmail());
            mPreparedStatement.setDate(3, new Date(seller.getBirthDate().getTime()));
            mPreparedStatement.setDouble(4, seller.getBaseSalary());
            mPreparedStatement.setInt(5, seller.getDepartment().getId());
            mPreparedStatement.setInt(6, seller.getId());

            mPreparedStatement.executeUpdate();

        }catch (SQLException sqlException){
            throw new DbException(sqlException.getMessage());
        }finally {
            DB.closeStatement(mPreparedStatement);
        }
    }

    @Override
    public void deleteById(Integer id) {

        PreparedStatement mPreparedStatement = null;

        try{
            mPreparedStatement = connection.prepareStatement("" +
                    "DELETE FROM seller WHERE Id = ?");

            mPreparedStatement.setInt(1, id);

            mPreparedStatement.executeUpdate();

        }catch (SQLException sqlException){
            throw new DbException(sqlException.getMessage());
        }finally {
            DB.closeStatement(mPreparedStatement);
        }
    }

    @Override
    public Seller findById(Integer id) {
        PreparedStatement mPreparedStatement = null;
        ResultSet resultSet = null;
        try{
            mPreparedStatement = connection.prepareStatement(
                    "SELECT seller.*,department.Name as DepName " +
                    "FROM seller INNER JOIN department " +
                    "ON seller.DepartmentId = department.Id " +
                    "WHERE seller.Id = ?");

            mPreparedStatement.setInt(1, id);
            resultSet = mPreparedStatement.executeQuery();

            if (resultSet.next()){

                Department department = instantiateDepartment(resultSet);
                Seller seller = instantiateSeller(resultSet, department);

                return seller;
            }
            return null;

        }catch (SQLException sqlException){
            throw new DbException(sqlException.getMessage());
        }finally {
            DB.closeStatement(mPreparedStatement);
            DB.closeResultSet(resultSet);
        }
    }

    private Department instantiateDepartment(ResultSet resultSet) throws SQLException {
        Department department = new Department();
        department.setId(resultSet.getInt("DepartmentId"));
        department.setName(resultSet.getString("DepName"));
        return department;
    }

    private Seller instantiateSeller(ResultSet resultSet, Department department) throws SQLException {
        Seller seller = new Seller();
        seller.setId(resultSet.getInt("Id"));
        seller.setName(resultSet.getString("Name"));
        seller.setEmail(resultSet.getString("Email"));
        seller.setBirthDate(resultSet.getDate("BirthDate"));
        seller.setBaseSalary(resultSet.getDouble("BaseSalary"));
        seller.setDepartment(department);
        return seller;
    }

    @Override
    public List<Seller> findAll() {
        PreparedStatement mPreparedStatement = null;
        ResultSet resultSet = null;
        try {
            mPreparedStatement = connection.prepareStatement("" +
                    "SELECT seller.*, department.Name as DepName " +
                    "FROM seller INNER JOIN department " +
                    "ON seller.DepartmentId = department.Id " +
                    "ORDER BY Name");

            resultSet = mPreparedStatement.executeQuery();

            List<Seller> sellerList = new ArrayList<>();
            Map<Integer, Department> departmentMap = new HashMap<>();

            while (resultSet.next()){

                Department department = departmentMap.get(resultSet.getInt("DepartmentId"));

                if (department == null){
                    department = instantiateDepartment(resultSet);
                    departmentMap.put(resultSet.getInt(("DepartmentId")), department);
                }
                Seller seller = instantiateSeller(resultSet, department);
                sellerList.add(seller);
            }
            return sellerList;

        }catch (SQLException sqlException){
            throw new DbException(sqlException.getMessage());
        }finally {
            DB.closeStatement(mPreparedStatement);
            DB.closeResultSet(resultSet);
        }
    }

    @Override
    public List<Seller> findByDepartment(Department departmentId) {

        PreparedStatement mPreparedStatement = null;
        ResultSet resultSet = null;
        try {
            mPreparedStatement = connection.prepareStatement("" +
                    "SELECT seller.*, department.Name as DepName " +
                    "FROM seller INNER JOIN department " +
                    "ON seller.DepartmentId = department.Id " +
                    "WHERE DepartmentId = ? " +
                    "ORDER BY Name");

            mPreparedStatement.setInt(1, departmentId.getId());
            resultSet = mPreparedStatement.executeQuery();

            List<Seller> sellerList = new ArrayList<>();
            Map<Integer, Department> departmentMap = new HashMap<>();

            while (resultSet.next()){

                Department department = departmentMap.get(resultSet.getInt("DepartmentId"));

                if (department == null){
                    department = instantiateDepartment(resultSet);
                    departmentMap.put(resultSet.getInt(("DepartmentId")), department);
                }

                Seller seller = instantiateSeller(resultSet, department);
                sellerList.add(seller);
            }
            return sellerList;

        }catch (SQLException sqlException){
            throw new DbException(sqlException.getMessage());
        }finally {
            DB.closeStatement(mPreparedStatement);
            DB.closeResultSet(resultSet);
        }
    }
}