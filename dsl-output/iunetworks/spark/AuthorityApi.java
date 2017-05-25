package am.iunetworks.ppcm.api.spark;

import java.util.UUID;
import javax.persistence.EntityNotFoundException;
import static spark.Spark.*;
import com.google.gson.Gson;
import am.iunetworks.ppcm.api.service.*;
import am.iunetworks.ppcm.api.model.*;
import org.apache.log4j.Logger;

public final class AuthorityApi extends BasicApi{
  public static void register(){
    final Logger logger = Logger.getLogger(AuthorityApi.class);

    get("/api/authorities/:id", (req, res) -> {
    logger.info("get: /api/authorities"+"/" + req.params(":id"));
    try{
      return toJson(new AuthorityServiceImpl().retrieveById(UUID.fromString(req.params(":id"))));
    }
    catch (PageIndexNotSpecifiedException e){
      halt(400, e.jsonMessage());
    }
    catch (EntityNotFoundException e){
      halt(404, "{\"message\" : \"not found\"}");
    }
    catch (Throwable e){
      halt(500, "{\"message\" : \"internal server error\"}");
    }
    return null;
    });

    get("/api/authorities", (req, res) -> {
    logger.info("get: /api/authorities");
    try{
      PageInfo paging = pageInfo(req);
      OrderInfo ordering = orderInfo(req);
      return toJson(new AuthorityServiceImpl().retrieveAll(paging, ordering));
    }
    catch (PageIndexNotSpecifiedException e){
      halt(400, e.jsonMessage());
    }
    catch (EntityNotFoundException e){
      halt(404, "{\"message\" : \"not found\"}");
    }
    catch (Throwable e){
      halt(500, "{\"message\" : \"internal server error\"}");
    }
    return null;
    });

  }
}
