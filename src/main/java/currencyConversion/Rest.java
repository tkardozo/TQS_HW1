package currencyConversion;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import org.json.JSONObject;

/**
 *
 * @author tkardozo
 */
@Path("/")
public class Rest{
    private static Converter cc = new Converter();
       
    @GET
    @Produces("application/json")
    public Response coins() {
        JSONObject result = new JSONObject();
        result.put("coins", cc.getAvailableCoins());
        return Response.status(200).entity(result.toString()).build();
    }
    
    @Path("{from}")
    @GET
    @Produces("application/json")
    public Response rates(@PathParam("from") String from) {
        try{
            JSONObject result = new JSONObject();
            result.put("from", from);
            result.put("ratesTo", cc.getRates(from));
            return Response.status(200).entity(result.toString()).build();
        }catch(Exception e){
            JSONObject error = new JSONObject();
            error.put("error", e.getMessage());
            return Response.status(400).entity(error.toString()).build();
        }
    }
    
    @Path("{from}/{to}")
    @GET
    @Produces("application/json")
    public Response rateTo(@PathParam("from") String from, @PathParam("to") String to) {
        try{
            JSONObject result = new JSONObject();
            result.put("from", from);
            result.put("to", to);
            result.put("rate", cc.getRate(from, to));
            return Response.status(200).entity(result.toString()).build();
        }catch(Exception e){
            JSONObject error = new JSONObject();
            error.put("error", e.getMessage());
            return Response.status(400).entity(error.toString()).build();
        }
    }
    
    @Path("{from}/{to}/{value}")
    @GET
    @Produces("application/json")
    public Response convertTo(@PathParam("from") String from, @PathParam("to") String to, @PathParam("value") float value) {
        try{
            JSONObject result = new JSONObject();
            result.put("from", from);
            result.put("to", to);
            result.put("value", cc.convert(from, to, value) );
            return Response.status(200).entity(result.toString()).build();
        }catch(Exception e){
            JSONObject error = new JSONObject();
            error.put("error", e.getMessage());
            return Response.status(400).entity(error.toString()).build();
        }
    }
}
