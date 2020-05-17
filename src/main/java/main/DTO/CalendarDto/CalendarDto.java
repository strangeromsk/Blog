package main.DTO.CalendarDto;

import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class CalendarDto {
    List<Integer> years;
    Map<Date, Long> posts;
}
