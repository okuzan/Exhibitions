package com.example.exhibitions.service;

import com.example.exhibitions.data.ExhibitionDTO;
import com.example.exhibitions.entity.Exhibition;
import com.example.exhibitions.entity.Hall;
import com.example.exhibitions.repository.ExhibitionRepository;
import com.example.exhibitions.repository.HallRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service("exhibitionService")
public class ExhibitionServiceImpl{

    @Autowired
    private ExhibitionRepository exhibitionRepo;

    @Autowired
    private HallRepository  hallRepo;

    private Exhibition mapDataToExhibition(final ExhibitionDTO data) {
        Exhibition exhibition = new Exhibition();
        exhibition.setName(data.getTitle());
        exhibition.setStartDate(data.parseDateTime(data.getStartDateTime()));
        exhibition.setEndDate(data.parseDateTime(data.getEndDateTime()));
        exhibition.setPrice(data.getPrice());
        List<Hall> hallsData = parseHalls(data.getHalls());
        List<Hall> halls = new ArrayList<>();
        for (Hall hall: hallsData){
            halls.add(hallRepo.getByNumber(hall.getNumber()));
        }
        exhibition.setHalls(halls);
        return exhibition;
    }
    public List<Hall> parseHalls(List<String> halls) {
        List<Hall> hallList = new ArrayList<>();
        for (String str : halls) {
            System.out.println(str);
            System.out.println(Arrays.toString(hallRepo.findAll().toArray()));
            hallList.add(hallRepo.getByNumber(Integer.valueOf(str)));
        }
        return hallList;
    }

    public Exhibition save(ExhibitionDTO dto) {
        Exhibition customerModel = mapDataToExhibition(dto);
        return exhibitionRepo.save(customerModel);
    }

}
