package com.example.exhibitions.service;

import com.example.exhibitions.data.ExhibitionDTO;
import com.example.exhibitions.entity.Exhibition;
import com.example.exhibitions.entity.Hall;
import com.example.exhibitions.paging.Paged;
import com.example.exhibitions.paging.Paging;
import com.example.exhibitions.repository.ExhibitionRepository;
import com.example.exhibitions.repository.HallRepository;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service("exhibitionService")
public class ExhibitionServiceImpl {

    @Autowired
    private ExhibitionRepository exhibitionRepo;

    @Autowired
    private HallRepository hallRepo;


    public Paged<Exhibition> getPage(int pageNumber, int size) {
        PageRequest request = PageRequest.of(pageNumber - 1, size, Sort.by(Sort.Direction.ASC, "id"));
        Page<Exhibition> postPage = exhibitionRepo.findAll(request);
        return new Paged<>(postPage, Paging.of(postPage.getTotalPages(), pageNumber, size));
    }

    public Paged<Exhibition> getPage(int pageNumber, int size, Specification spec) {
        PageRequest request = PageRequest.of(pageNumber - 1, size, Sort.by(Sort.Direction.ASC, "id"));
        Page<Exhibition> postPage = exhibitionRepo.findAll(spec, request);
        return new Paged<>(postPage, Paging.of(postPage.getTotalPages(), pageNumber, size));
    }

    /**
     * Retrieves list of shows filtered by time frame and further paginated based on page number and page size
     * @param pageNumber - ordinal page number
     * @param size - how many elements fit in one page
     * @param startStr - base64 encoded string of lower bound filter time
     * @param endStr- base64 encoded string of upper bound filter time
     * @return paginated portion of exhibitions from the repository
     */
    public Paged<Exhibition> getPageFiltered(int pageNumber, int size, String startStr, String endStr) {
        LocalDateTime start = ExhibitionDTO.parseDateTime(new String(Base64.decodeBase64(startStr.getBytes(StandardCharsets.UTF_8))));
        LocalDateTime end = ExhibitionDTO.parseDateTime(new String(Base64.decodeBase64(endStr.getBytes(StandardCharsets.UTF_8))));

        PageRequest request = PageRequest.of(pageNumber - 1, size, Sort.by(Sort.Direction.ASC, "id"));
        Page<Exhibition> postPage = exhibitionRepo.getAllByStartDateAfterAndEndDateBefore(start, end, request);
        return new Paged<>(postPage, Paging.of(postPage.getTotalPages(), pageNumber, size));
    }


    public Exhibition mapDataToExhibition(final ExhibitionDTO data) {
        Exhibition exhibition = new Exhibition();
        exhibition.setName(data.getName());
        if (data.getId() != null) exhibition.setId(data.getId());
        exhibition.setStartDate(data.parseDateTime(data.getStartDateTime()));
        exhibition.setEndDate(data.parseDateTime(data.getEndDateTime()));
        exhibition.setPrice(data.getPrice());
        List<Hall> hallsData = parseHalls(data.getHalls());
        List<Hall> halls = new ArrayList<>();
        for (Hall hall : hallsData) {
            halls.add(hallRepo.getByNumber(hall.getNumber()));
        }
        exhibition.setHalls(halls);
        return exhibition;
    }


    public List<Hall> parseHalls(List<String> halls) {
        List<Hall> hallList = new ArrayList<>();
        for (String str : halls) hallList.add(hallRepo.getByNumber(Integer.valueOf(str)));
        return hallList;
    }

    public void save(ExhibitionDTO dto) {
        Exhibition customerModel = mapDataToExhibition(dto);
        exhibitionRepo.save(customerModel);
    }

}