package model;
import static spark.Spark.*;
public class CommentService{
  public static void main(String[] args){
    staticFiles.location("/");
    staticFiles.expireTime(0);
    port (8080);
    get("/api/articles/:article_id/comments", (req, res) -> {
    try{
    }
    catch (EntityNotFoundException e){
      halt(404, "Not Found");
    }
    catch (Throwable e){
      halt(500, "Internal Server Error");
    }
    return "";
    });
    post("/api/articles/:article_id/comments", (req, res) -> {
    try{
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
