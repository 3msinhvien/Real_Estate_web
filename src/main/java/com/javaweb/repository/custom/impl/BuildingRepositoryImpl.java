package com.javaweb.repository.custom.impl;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import com.javaweb.builder.BuildingSearchBuilder;
import com.javaweb.repository.custom.BuildingRepositoryCustom;
import com.javaweb.repository.entity.BuildingEntity;

@Repository
@Primary
public class BuildingRepositoryImpl implements BuildingRepositoryCustom{
	@PersistenceContext
	private EntityManager entityManager;

	public static void joinTable(BuildingSearchBuilder buildingSearchBuilder, StringBuilder sql) {
		Long staffId = buildingSearchBuilder.getStaffId();
		if (staffId != null) {
			sql.append("\nINNER JOIN assigmentbuilding ON b.id = assignmentbuilding.buidingid");
		}
		List<String> typeCode = buildingSearchBuilder.getTypeCode();
		if (typeCode != null && typeCode.size() != 0) {
			sql.append("\nINNER JOIN buildingrenttype ON b.id = buildingrenttype.buildingid ");
			sql.append("\nINNER JOIN renttype ON renttype.id = buildingrenttype.renttypeid ");
		}
		Long rentAreaFrom = buildingSearchBuilder.getAreaFrom();
		Long rentAreaTo = buildingSearchBuilder.getAreaTo();
		if (rentAreaFrom != null || rentAreaTo != null) {
			sql.append("\nINNER JOIN rentarea ON rentarea.buildingid = b.id");
		}
	}

	public static void queryNormal(BuildingSearchBuilder buildingSearchBuilder, StringBuilder where) {
		try {
			Field[] fields = BuildingSearchBuilder.class.getDeclaredFields();
			for (Field item : fields) {
				item.setAccessible(true);
				String fieldName = item.getName();
				if (!fieldName.equals("staffId") && fieldName.equals("typeCode")
						&& fieldName.startsWith("area")
						&& fieldName.startsWith("rentPrice")) {
					Object value = item.get(buildingSearchBuilder);
					if (value != null) {
						if (item.getType().getName().equals("java.lang.Long")) {
							where.append("AND b." + fieldName + " = " + value);
						} else if (item.getType().getName().equals("java.lang.String")) {
							where.append(" AND b." + fieldName + " LIKE '%" + value + "%' ");
						}
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void querySpecial(BuildingSearchBuilder buildingSearchBuilder, StringBuilder where) {
		Long staffId = buildingSearchBuilder.getStaffId();
		if (staffId != null) {
			where.append(" AND assigmentbuilding = " + staffId);
		}
		Long rentAreaTo = buildingSearchBuilder.getAreaTo();
		Long rentAreaFrom = buildingSearchBuilder.getAreaFrom();

		if (rentAreaTo != null || rentAreaFrom != null) {
			if (rentAreaFrom != null) {
				where.append(" AND rentarea.value >= " + rentAreaFrom);
			}
			if (rentAreaTo != null) {
				where.append(" AND rentarea.value <= " + rentAreaTo);
			}
		}

		Long rentPriceTo = buildingSearchBuilder.getRentPriceTo();
		Long rentPriceFrom = buildingSearchBuilder.getRentPriceFrom();
		if (rentPriceFrom != null || rentPriceTo != null) {
			if (rentPriceFrom != null) {
				where.append(" AND b.rentprice >= " + rentPriceFrom);
			}
			if (rentPriceTo != null) {
				where.append(" AND b.rentprice <= " + rentPriceTo);
			}
		}

		// Java 8
		List<String> typeCode = buildingSearchBuilder.getTypeCode();
		if (typeCode != null && typeCode.size() != 0) {
			where.append(" AND ( ");
			String sql = typeCode.stream().map(it -> "renttype.code like " + "'%" + it + "%' ")
					.collect(Collectors.joining(" OR "));
			where.append(sql);
			where.append(" ) ");
		}

		where.append("\nGROUP BY b.id;");
	}

	@Override
	public List<BuildingEntity> findAll(BuildingSearchBuilder buildingSearchBuilder) {

		StringBuilder sql = new StringBuilder("SELECT b.* FROM building b");
		joinTable(buildingSearchBuilder, sql);
		StringBuilder where = new StringBuilder("\nWHERE 1 = 1");
		queryNormal(buildingSearchBuilder, where);
		querySpecial(buildingSearchBuilder, where);
		sql.append(where);
		//System.out.println(sql);
		Query query = entityManager.createNativeQuery(sql.toString(), BuildingEntity.class);
		return query.getResultList();
	}

}
