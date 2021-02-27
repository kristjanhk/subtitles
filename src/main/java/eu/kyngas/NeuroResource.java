package eu.kyngas;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

@Consumes(MediaType.TEXT_PLAIN)
@Produces(MediaType.TEXT_PLAIN)
@Path("neuro/translate")
public class NeuroResource {
  @Inject
  NeuroService neuroService;

  @POST
  @Path("srt")
  public String convertSrt(@QueryParam("lang") String lang, String body) throws IOException {
    return neuroService.convertSrt(body, lang);
  }

  @POST
  @Path("sentence")
  public String convertSentence(@QueryParam("lang") String lang, String input) {
    return neuroService.translate(input, lang);
  }
}
