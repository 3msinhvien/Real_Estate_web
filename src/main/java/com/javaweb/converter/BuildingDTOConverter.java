package com.javaweb.converter;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.javaweb.model.BuildingDTO;
import com.javaweb.repository.DistrictRepository;
import com.javaweb.repository.RentAreaRepository;
import com.javaweb.repository.entity.BuildingEntity;
import com.javaweb.repository.entity.DistrictEntity;
import com.javaweb.repository.entity.RentAreaEntity;

// Nhận dữ liệu và chuyển nó thành dạng để trả ra
// Tách thành class riêng, không để chung với service cho clean
@Component
public class BuildingDTOConverter {
    @Autowired
    private DistrictRepository districtRepository;
    @Autowired
    private RentAreaRepository rentAreaRepository;
    @Autowired
    private ModelMapper modelMapper; // Dùng modelmapper để đỡ phải set từng thuộc tính bằng tay

    public BuildingDTO toBuildingDTO(BuildingEntity item) {

        BuildingDTO building = modelMapper.map(item, BuildingDTO.class);
        DistrictEntity districtEntity = districtRepository.findNameByID(item.getDistrictid());
        building.setAddress(item.getStreet() + ", " + item.getWard() + ", " + districtEntity.getName());
        List<RentAreaEntity> rentAreas = rentAreaRepository.getValueByBuildingId(item.getId());
        String areaResult = rentAreas.stream().map(it -> it.getValue().toString()).collect(Collectors.joining(","));
        return building;
    }

}
