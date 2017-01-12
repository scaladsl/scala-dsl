package am.iunetworks.ppcm.components.administration;
@RequestMapping("/private/administration/dictionaries/cpv")
@javax.annotation.Generated("Generated according to PPCM DSL")
public class AbstractCpvController extends AbstractController {

  @org.springframework.beans.factory.annotation.Autowired

  private CpvService service;

  protected CpvService getService() {
    return service;
  }

  protected String doIndex(org.springframework.ui.ModelMap modelMap) throws Throwable {
    return "/private/administration/dictionaries/cpv";
  }

  @RequestMapping(value = "", method = RequestMethod.GET)
  public final String index(org.springframework.ui.ModelMap modelMap) {
    log.debug("{}: AbstractCpvController.index(...)", getCaller());

    try {
      getSession().checkPermission("/private/administration/dictionaries/cpv");
      return doIndex(modelMap);
    }

    catch ( org.springframework.security.access.AccessDeniedException e ) {
      log.error("The AbstractCpvController.index(...) has been failed: " + e.getMessage(), e);
      return "redirect:public/access-denied";
    }

    catch ( Throwable e ) {
      log.error("The AbstractCpvController.index(...) has been failed: " + e.getMessage(), e);
      return "redirect:public/internal-error";
    }
  }

  public static class ListRequest {
    public java.util.UUID parent;
  }

  @RequestMapping(value = "/list", method = RequestMethod.POST)
  @ResponseBody
  public final AjaxResult list(@RequestBody ListRequest request) {
    log.debug("{}: AbstractCpvController.list(...)", getCaller());
  }

  try {
    getSession().checkPermission("/private/administration/dictionaries/cpv");
    java.collection.List<am.iunetworks.ppcm.components.administration.Cpv> result = service.list(request.parent);
    return AjaxResult.ok(result);
  }

  catch ( org.springframework.security.access.AccessDeniedException e ) {
    log.error("The AbstractCpvController.list(...) has been failed: " + e.getMessage(), e);
    return AjaxResult.error(AjaxResult.STATUS_ACCESS_DENIED);
  }

  catch ( Throwable e ) {
    log.error("The AbstractCpvController.list(...) has been failed: " + e.getMessage(), e);
    return AjaxResult.error(AjaxResult.STATUS_INTERNAL_ERROR);
  }
  public static class GetRequest {
    public java.util.UUID id;
  }

  @RequestMapping(value = "/get", method = RequestMethod.POST)
  @ResponseBody
  public final AjaxResult get(@RequestBody GetRequest request) {
    log.debug("{}: AbstractCpvController.get(...)", getCaller());
  }

  try {
    getSession().checkPermission("/private/administration/dictionaries/cpv");
    am.iunetworks.ppcm.components.administration.Cpv result = service.get(request.id);
    return AjaxResult.ok(result);
  }

  catch ( org.springframework.security.access.AccessDeniedException e ) {
    log.error("The AbstractCpvController.get(...) has been failed: " + e.getMessage(), e);
    return AjaxResult.error(AjaxResult.STATUS_ACCESS_DENIED);
  }

  catch ( Throwable e ) {
    log.error("The AbstractCpvController.get(...) has been failed: " + e.getMessage(), e);
    return AjaxResult.error(AjaxResult.STATUS_INTERNAL_ERROR);
  }
  public static class PahtRequest {
    public java.util.UUID id;
  }

  @RequestMapping(value = "/paht", method = RequestMethod.POST)
  @ResponseBody
  public final AjaxResult paht(@RequestBody PahtRequest request) {
    log.debug("{}: AbstractCpvController.paht(...)", getCaller());
  }

  try {
    getSession().checkPermission("/private/administration/dictionaries/cpv");
    java.collection.List<am.iunetworks.ppcm.components.administration.Cpv> result = service.paht(request.id);
    return AjaxResult.ok(result);
  }

  catch ( org.springframework.security.access.AccessDeniedException e ) {
    log.error("The AbstractCpvController.paht(...) has been failed: " + e.getMessage(), e);
    return AjaxResult.error(AjaxResult.STATUS_ACCESS_DENIED);
  }

  catch ( Throwable e ) {
    log.error("The AbstractCpvController.paht(...) has been failed: " + e.getMessage(), e);
    return AjaxResult.error(AjaxResult.STATUS_INTERNAL_ERROR);
  }
  public static class CountRequest {
  }

  @RequestMapping(value = "/count", method = RequestMethod.POST)
  @ResponseBody
  public final AjaxResult count(@RequestBody CountRequest request) {
    log.debug("{}: AbstractCpvController.count(...)", getCaller());
  }

  try {
    getSession().checkPermission("/private/administration/dictionaries/cpv");
    java.lang.Integer result = service.count(request.);
    return AjaxResult.ok(result);
  }

  catch ( org.springframework.security.access.AccessDeniedException e ) {
    log.error("The AbstractCpvController.count(...) has been failed: " + e.getMessage(), e);
    return AjaxResult.error(AjaxResult.STATUS_ACCESS_DENIED);
  }

  catch ( Throwable e ) {
    log.error("The AbstractCpvController.count(...) has been failed: " + e.getMessage(), e);
    return AjaxResult.error(AjaxResult.STATUS_INTERNAL_ERROR);
  }
  public static class AddRequest {
    public am.iunetworks.ppcm.components.administration.Cpv cpv;
  }

  @RequestMapping(value = "/add", method = RequestMethod.POST)
  @ResponseBody
  public final AjaxResult add(@RequestBody AddRequest request) {
    log.debug("{}: AbstractCpvController.add(...)", getCaller());
  }

  try {
    getSession().checkPermission("/private/administration/dictionaries/cpv");
    java.lang.Void result = service.add(request.cpv);
    return AjaxResult.ok(result);
  }

  catch ( org.springframework.security.access.AccessDeniedException e ) {
    log.error("The AbstractCpvController.add(...) has been failed: " + e.getMessage(), e);
    return AjaxResult.error(AjaxResult.STATUS_ACCESS_DENIED);
  }

  catch ( Throwable e ) {
    log.error("The AbstractCpvController.add(...) has been failed: " + e.getMessage(), e);
    return AjaxResult.error(AjaxResult.STATUS_INTERNAL_ERROR);
  }
  public static class SaveRequest {
    public am.iunetworks.ppcm.components.administration.Cpv cpvcode;
  }

  @RequestMapping(value = "/save", method = RequestMethod.POST)
  @ResponseBody
  public final AjaxResult save(@RequestBody SaveRequest request) {
    log.debug("{}: AbstractCpvController.save(...)", getCaller());
  }

  try {
    getSession().checkPermission("/private/administration/dictionaries/cpv");
    java.lang.Void result = service.save(request.cpvcode);
    return AjaxResult.ok(result);
  }

  catch ( org.springframework.security.access.AccessDeniedException e ) {
    log.error("The AbstractCpvController.save(...) has been failed: " + e.getMessage(), e);
    return AjaxResult.error(AjaxResult.STATUS_ACCESS_DENIED);
  }

  catch ( Throwable e ) {
    log.error("The AbstractCpvController.save(...) has been failed: " + e.getMessage(), e);
    return AjaxResult.error(AjaxResult.STATUS_INTERNAL_ERROR);
  }
}
