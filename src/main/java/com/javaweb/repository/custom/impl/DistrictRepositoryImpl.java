package com.javaweb.repository.custom.impl;

import org.springframework.stereotype.Repository;

import com.javaweb.repository.DistrictRepository;
import com.javaweb.repository.entity.DistrictEntity;

@Repository
public class DistrictRepositoryImpl implements DistrictRepository {

    @Override
    public DistrictEntity findNameByID(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findNameByID'");
    }

}

// public List<BuildingEntity> findAll(BuildingSearchBuilder buildingSearchBuilder) {

//     StringBuilder sql = new StringBuilder("SELECT b.* FROM building b");
//     joinTable(buildingSearchBuilder, sql);
//     StringBuilder where = new StringBuilder("\nWHERE 1 = 1");
//     queryNormal(buildingSearchBuilder, where);
//     querySpecial(buildingSearchBuilder, where);
//     System.out.println(where);
//     sql.append(where);
//     System.out.println(sql);
//     //System.out.println(sql);
//     Query query = entityManager.createNativeQuery(sql.toString(), BuildingEntity.class);
//     return query.getResultList();
// }