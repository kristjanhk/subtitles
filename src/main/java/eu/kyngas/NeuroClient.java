package eu.kyngas;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

@RegisterRestClient(configKey = "neuro.url")
public interface NeuroClient {

  @GET
  @Path("translate")
  NeuroResult translate(@QueryParam("src") String src,
                        @QueryParam("auth") String auth,
                        @QueryParam("conf") String conf);
}
