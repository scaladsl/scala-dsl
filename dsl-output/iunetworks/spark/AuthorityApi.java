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

    get("/api/authority/:id", (req, res) -> {
    logger.info("get: /api/authority"+"/" + req.params(":id"));
    try{
      return toJson(new AuthorityServiceImpl().retrieveById(UUID.fromString(req.params(":id"))));
    }
    catch (EntityNotFoundException e){
      halt(404, "{\"message\" : \"not found\"}");
    }
    catch (Throwable e){
      halt(500, "{\"message\" : \"internal server error\"}");
    }
    return null;
    });

    get("/api/authority", (req, res) -> {
    logger.info("get: /api/authority");
    try{
      PageInfo paging = pageInfo(req);
      OrderInfo ordering = orderInfo(req);
      return toJson(new AuthorityServiceImpl().retriveAll(paging, ordering));
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
