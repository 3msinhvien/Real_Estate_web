package com.javaweb.repository.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.javaweb.repository.BuildingRepository;
import com.javaweb.repository.entity.BuildingEntity;
import com.javaweb.utils.NumberUtil;
import com.javaweb.utils.StringUtil;

@Repository
public class BuildingRepositoryImpl implements BuildingRepository {
	static final String DB_URL = "jdbc:mysql://localhost:3306/estatebasic";
	static final String USER = "root";
	static final String PASS = "tung.2802";

	public static void joinTable(Map<String, Object> params, List<String> typeCode, StringBuilder sql) {
		String staffId = (String) params.get("staffId");
		if (StringUtil.checkString(staffId) == true) {
			sql.append("\nINNER JOIN assigmentbuilding ON b.id = assignmentbuilding.buidingid");
		}
		if (typeCode != null && typeCode.size() != 0) {
			sql.append("\nINNER JOIN buildingrenttype ON b.id = buildingrenttype.buildingid ");
			sql.append("\nINNER JOIN renttype ON renttype.id = buildingrenttype.renttypeid ");
		}
		String rentAreaTo = (String) params.get("areaTo");
		String rentAreaFrom = (String) params.get("areaFrom");
		if (StringUtil.checkString(rentAreaFrom) == true || StringUtil.checkString(rentAreaTo) == true) {
			sql.append("\nINNER JOIN rentarea ON rentarea.buildingid = b.id");
		}
	}

	public static void queryNormal(Map<String, Object> params, StringBuilder where) {
		for (Map.Entry<String, Object> item : params.entrySet()) {
			if (!item.getKey().equals("staffId") && !item.getKey().equals("typeCode")
					&& !item.getKey().startsWith("area")
					&& !item.getKey().startsWith("rentPrice")) {
				String value = item.getValue().toString();
				if (StringUtil.checkString(value) == true) {
					if (NumberUtil.isNumber(value)) {
						where.append("AND b." + item.getKey() + " = " + value);
					} else {
						where.append(" AND b." + item.getKey() + " LIKE '%" + value + "%' ");
					}
				}
			}
		}
	}

	public static void querySpecial(Map<String, Object> params, List<String> typeCode, StringBuilder where) {
		String staffId = (String) params.get("staffId");
		if (StringUtil.checkString(staffId) == true) {
			where.append(" AND assigmentbuilding = " + staffId);
		}
		String rentAreaTo = (String) params.get("areaTo");
		String rentAreaFrom = (String) params.get("areaFrom");

		if (StringUtil.checkString(rentAreaFrom) == true || StringUtil.checkString(rentAreaTo) == true) {
			if (StringUtil.checkString(rentAreaFrom) == true) {
				where.append(" AND rentarea.value >= " + rentAreaFrom);
			}
			if (StringUtil.checkString(rentAreaTo) == true) {
				where.append(" AND rentarea.value <= " + rentAreaTo);
			}
		}

		String rentPriceTo = (String) params.get("rentPriceTo");
		String rentPriceFrom = (String) params.get("rentPriceFrom");
		if (StringUtil.checkString(rentPriceFrom) == true || StringUtil.checkString(rentPriceTo) == true) {
			if (StringUtil.checkString(rentPriceFrom) == true) {
				where.append(" AND rentprice.value >= " + rentPriceFrom);
			}
			if (StringUtil.checkString(rentPriceTo)) {
				where.append(" AND rentprice.value <= " + rentPriceTo);
			}
		}

		if (typeCode != null && typeCode.size() != 0) {
			List<String> code = new ArrayList<>();
			for ( String item : typeCode ) {
				code.add( "'" + item + "'") ;
			}
			where.append(" AND renttype.code IN (" + String.join(",", code) + ") ") ;
		}
		where.append("\nGROUP BY b.id;");
	}

	@Override
	public List<BuildingEntity> findAll(Map<String, Object> params, List<String> typeCode) {

		StringBuilder sql = new StringBuilder(
				"SELECT b.id , b.name , b.districtid, b.street, b.ward, b.numberofbasement, b.floorarea, b.rentprice, "
						+
						"b.managername, b.managerphonenumber, b.servicefee, b.brokeragefee \nFROM building b ");
		joinTable(params, typeCode, sql);
		StringBuilder where = new StringBuilder("\nWHERE 1 = 1");
		queryNormal(params, where);
		querySpecial(params, typeCode, where);
		sql.append(where);
		System.out.println(sql);
		List<BuildingEntity> res = new ArrayList<>();
		try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql.toString());) {
			while (rs.next()) {
				BuildingEntity buildingEntity = new BuildingEntity();
				buildingEntity.setId(rs.getLong("b.id"));
				buildingEntity.setName(rs.getString("b.name"));
				buildingEntity.setWard(rs.getString("b.ward"));
				buildingEntity.setDistrictid(rs.getLong("b.districtid"));
				buildingEntity.setStreet(rs.getString("b.street"));
				buildingEntity.setFloorArea(rs.getLong("b.floorarea"));
				buildingEntity.setRentPrice(rs.getLong("b.rentprice"));
				buildingEntity.setServiceFee(rs.getString("b.servicefee"));
				buildingEntity.setBrokerAgeFee(rs.getLong("b.brokeragefee"));
				buildingEntity.setManagerName(rs.getString("b.managername"));
				buildingEntity.setManagerPhoneNumber(rs.getString("b.managerphonenumber"));
				res.add(buildingEntity);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.print("Connect Failed ");
		}
		// TODO Auto-generated method stub
		return res;
	}

}
