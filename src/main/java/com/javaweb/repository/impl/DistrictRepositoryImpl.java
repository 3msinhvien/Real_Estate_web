package com.javaweb.repository.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.stereotype.Repository;

import com.javaweb.repository.DistrictRepository;
import com.javaweb.repository.entity.BuildingEntity;
import com.javaweb.repository.entity.DistrictEntity;

@Repository
public class DistrictRepositoryImpl implements DistrictRepository{
    static final String DB_URL = "jdbc:mysql://localhost:3306/estatebasic";
	static final String USER = "root";
	static final String PASS = "tung.2802";

    @Override
    public DistrictEntity findNameByID(Long id) {
        // TODO Auto-generated method stub 
        String sql = "SELECT d.name FROM district d WHERE d.id = " + id + ";" ;
        DistrictEntity districtEntity = new DistrictEntity();
            try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                districtEntity.setName(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.print("Connect Failed ");
        }
        return districtEntity;
    }
    
}
