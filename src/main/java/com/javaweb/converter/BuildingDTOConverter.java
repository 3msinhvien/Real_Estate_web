package com.javaweb.converter;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.javaweb.model.BuildingDTO;
import com.javaweb.repository.entity.BuildingEntity;
import com.javaweb.repository.entity.RentAreaEntity;

// Nhận dữ liệu và chuyển nó thành dạng để trả ra
// Tách thành class riêng, không để chung với service cho clean
@Component
public class BuildingDTOConverter {
    @Autowired
    private ModelMapper modelMapper; // Dùng modelmapper để đỡ phải set từng thuộc tính bằng tay

    public BuildingDTO toBuildingDTO(BuildingEntity item) {

        BuildingDTO building = modelMapper.map(item, BuildingDTO.class);
        building.setAddress(item.getStreet() + ", " + item.getWard() + ", " + item.getDistrict().getName());
        List<RentAreaEntity> rentAreas = item.getItems();
        String areaResult = rentAreas.stream().map(it -> it.getValue().toString()).collect(Collectors.joining(","));
        building.setRentArea(areaResult);
        return building;
    }

}
