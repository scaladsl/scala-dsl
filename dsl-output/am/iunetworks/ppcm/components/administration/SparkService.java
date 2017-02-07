package am.iunetworks.ppcm.components.administration;
import static spark.Spark.*;
public class Spark {
  public static void main(String[] args) {
    post("/articles", (req, res) -> { return "posted successfully!"; });
    get("/articles/:id", (req, res) -> { return req.params(":id") + " posted successfully!"; });
    post("/articles/:id", (req, res) -> { return req.params(":id") + " posted successfully!"; });
    get("/articles/:id", (req, res) -> { return req.params(":id") + " posted successfully!"; });
  }
}
