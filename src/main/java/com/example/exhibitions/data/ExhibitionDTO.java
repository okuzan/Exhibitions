package com.example.exhibitions.data;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class ExhibitionDTO {

    @NotBlank(message = "{show.title.nonempty}")
    private String title;
    @NotNull(message = "{show.price.nonempty}")
    private Double price;
    @NotBlank(message = "{show.start.nonempty}")
    private String startDateTime;
    @NotBlank(message = "{show.end.nonempty}")
    private String endDateTime;
    @NotEmpty(message = "{show.halls.nonempty}")
    private List<String> halls;
    private final static String FORMAT = "EEE MMM d yyyy HH:mm:ss z";


    public LocalDateTime parseDateTime(String input) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMAT);
        input = input.substring(0, input.length() - 2) + ":" + input.substring(input.length() - 2);
        return LocalDateTime.parse(input, formatter);
    }

}



