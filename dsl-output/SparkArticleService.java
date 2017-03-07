import static spark.Spark.*;
import spark.template.velocity.VelocityTemplateEngine;
import spark.ModelAndView;
import java.lang.Throwable;
import java.util.*;
import javax.persistence.EntityNotFoundException;
import com.google.gson.Gson;
import model.*;

public class SparkArticleService{
  public void includeArticleService(){
    get("/api/articles/:id", (req, res) -> {
    String jsonInString = "";
    try{
      jsonInString = new Gson().toJson(new ArticleServiceImpl().retrieveById(UUID.fromString(req.params(":id")));
    }
    catch (EntityNotFoundException e){
      halt(404, "Not Found");
    }
    catch (Throwable e){
      halt(500, "Internal Server Error");
    }
    return jsonInString;
    });

    get("/api/articles", (req, res) -> {
    String jsonInString = "";
    try{
      java.util.List<model.Article> articleServiceList = new ArticleServiceImpl().retrieveAll();
      jsonInString = new Gson().toJson(articleServiceList);
    }
    catch (EntityNotFoundException e){
      halt(404, "Not Found");
    }
    catch (Throwable e){
      halt(500, "Internal Server Error");
    }
    return jsonInString;
    });

    post("/api/articles", (req, res) -> {
    try{
      new ArticleServiceImpl().add(ArticleFromJson.getArticle(req.body()));
    }
    catch (EntityNotFoundException e){
      halt(404, "Not Found");
    }
    catch (Throwable e){
      halt(500, "Internal Server Error");
    }
    return "";
    });

    put("/api/articles/:id", (req, res) -> {
    try{
      new ArticleServiceImpl().update(UUID.fromString(req.params(":id")), ArticleFromJson.getArticle(req.body()));
    }
    catch (EntityNotFoundException e){
      halt(404, "Not Found");
    }
    catch (Throwable e){
      halt(500, "Internal Server Error");
    }
    return "";
    });

    delete("/api/articles/:id", (req, res) -> {
    try{
      new ArticleServiceImpl().remove(UUID.fromString(req.params(":id")));
    }
    catch (EntityNotFoundException e){
      halt(404, "Not Found");
    }
    catch (Throwable e){
      halt(500, "Internal Server Error");
    }
    return "";
    });

  }
}
