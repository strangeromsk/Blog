package main.DTO;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class CalendarDto {
    List<String> years;
    Map<String, Integer> posts;
}
