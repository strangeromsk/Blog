package main.DTO;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class CalendarDto {
    List<Integer> years;
    Map<String, Integer> posts;
}
