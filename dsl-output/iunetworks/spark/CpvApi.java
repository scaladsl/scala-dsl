package am.iunetworks.ppcm.api.spark;

import java.util.UUID;
import javax.persistence.EntityNotFoundException;
import static spark.Spark.*;
import com.google.gson.Gson;
import am.iunetworks.ppcm.api.service.*;
import am.iunetworks.ppcm.api.model.*;
import org.apache.log4j.Logger;

public final class CpvApi extends BasicApi{
  public static void register(){
    final Logger logger = Logger.getLogger(CpvApi.class);

    get("/api/cpvs/:id", (req, res) -> {
    logger.info("get: /api/cpvs"+"/" + req.params(":id"));
    try{
      return toJson(new CpvServiceImpl().retrieveById(UUID.fromString(req.params(":id"))));
    }
    catch (EntityNotFoundException e){
      halt(404, "{\"message\" : \"not found\"}");
    }
    catch (Throwable e){
      halt(500, "{\"message\" : \"internal server error\"}");
    }
    return null;
    });

    get("/api/cpvs", (req, res) -> {
    logger.info("get: /api/cpvs");
    try{
      PageInfo paging = pageInfo(req);
      OrderInfo ordering = orderInfo(req);
      return toJson(new CpvServiceImpl().retrieveAll(paging, ordering));
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
