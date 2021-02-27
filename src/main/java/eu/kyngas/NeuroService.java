package eu.kyngas;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@ApplicationScoped
public class NeuroService {
  @Inject
  @RestClient
  NeuroClient neuroClient;

  public String convertSrt(String file, String lang) throws IOException {
    List<String> lines = Arrays.asList(file.split("\n"));
    log.info("Lines: {}", lines);
    List<NeuroLine> items = parseInput(lines);
    log.info("Neuro: {}", items);

    for (NeuroLine neuro : items) {
      String input = String.join(" ", neuro.getLines());
      try {
        String result = translate(input, lang);
        neuro.setTranslated(result);
        log.info("Index {}, in: '{}', out: '{}'", neuro.getIndex(), input, result);
        Thread.sleep(1000L);
      } catch (Exception e) {
        log.error("Error ", e);
        neuro.setTranslated(input);
      }
    }

    List<String> result = new ArrayList<>();
    items.forEach(neuro -> neuro.write(result));
    log.info("Result: {}", result);
    return String.join("\n", result);
  }

  private List<NeuroLine> parseInput(List<String> lines) {
    List<NeuroLine> list = new ArrayList<>();

    List<String> state = new ArrayList<>();
    for (String line : lines) {
      if (line.isBlank()) {
        list.add(NeuroLine.parse(state));
        state = new ArrayList<>();
        continue;
      }
      state.add(line);
    }
    if (!state.isEmpty()) {
      list.add(NeuroLine.parse(state));
    }
    return list;
  }

  public String translate(String input, String lang) {
    return neuroClient.translate(input, "public", lang + ",inf").getTgt();
  }
}
