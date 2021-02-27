package eu.kyngas;

import lombok.Builder;
import lombok.Data;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class NeuroLine {
  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss,SSS");

  private int index;
  private LocalTime startTime;
  private LocalTime endTime;
  private List<String> lines;
  private String translated;

  public static NeuroLine parse(List<String> lines) {
    lines = lines.stream()
      .map(s -> s.replaceAll("[^a-zA-Z0-9.?!\\-,:;()%'> ]", ""))
      .collect(Collectors.toList());
    String[] split = lines.get(1).split(" --> ");
    return NeuroLine.builder()
      .index(Integer.parseInt(lines.get(0)))
      .startTime(LocalTime.parse(split[0], FORMATTER))
      .endTime(LocalTime.parse(split[1], FORMATTER))
      .lines(lines.stream().skip(2).collect(Collectors.toList()))
      .build();
  }

  public void write(List<String> lines) {
    lines.add(String.valueOf(index));
    lines.add(startTime.format(FORMATTER) + " --> " + endTime.format(FORMATTER));
    lines.add(translated == null ? String.join(" ", lines) : translated);
    lines.add("");
  }
}
