import static spark.Spark.*;
import spark.template.velocity.VelocityTemplateEngine;
import spark.ModelAndView;
import java.lang.Throwable;
import java.util.*;
import javax.persistence.EntityNotFoundException;
import com.google.gson.Gson;
import model.*;

public class SparkCommentService{
  public void includeCommentService(){
    get("/api/articles/:article_id/comments", (req, res) -> {
    String jsonInString = "";
    try{
      java.util.List<model.Comment> commentServiceList = new CommentServiceImpl().retrieveByArticle(UUID.fromString(req.params(":article_id")));
      jsonInString = new Gson().toJson(commentServiceList);
    }
    catch (EntityNotFoundException e){
      halt(404, "Not Found");
    }
    catch (Throwable e){
      halt(500, "Internal Server Error");
    }
    return jsonInString;
    });
    post("/api/articles/:article_id/comments", (req, res) -> {
    try{
      new CommentServiceImpl().add(UUID.fromString(req.params(":article_id")), CommentFromJson.getComment(req.body()));
    }
    catch (EntityNotFoundException e){
      halt(404, "Not Found");
    }
    catch (Throwable e){
      halt(500, "Internal Server Error");
    }
    return "";
    });
    put("/api/articles/:article_id/comments/:id", (req, res) -> {
    try{
      new CommentServiceImpl().save(UUID.fromString(req.params(":article_id")), UUID.fromString(req.params(":id")), CommentFromJson.getComment(req.body()));
    }
    catch (EntityNotFoundException e){
      halt(404, "Not Found");
    }
    catch (Throwable e){
      halt(500, "Internal Server Error");
    }
    return "";
    });
    delete("/api/articles/:article_id/comments/:id", (req, res) -> {
    try{
      new CommentServiceImpl().remove(UUID.fromString(req.params(":article_id")), UUID.fromString(req.params(":id")));
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
