package com.example.exhibitions.data;

import com.example.exhibitions.entity.Hall;
import com.example.exhibitions.repository.HallRepository;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotBlank;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class ExhibitionDTO {
    @Autowired
    private HallRepository hallRepo;

    @NotBlank
    private String title;
    @NotBlank
    private Double price;
    @NotBlank
    private String startDateTime;
    @NotBlank
    private String endDateTime;
    @NotBlank
    private List<String> halls;
    private final static String FORMAT = "EEE MMM d yyyy HH:mm:ss z";



    public LocalDateTime parseDateTime(String input) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMAT);
        input = input.substring(0, input.length() - 2) + ":" + input.substring(input.length() - 2);
        return LocalDateTime.parse(input, formatter);
    }

}



