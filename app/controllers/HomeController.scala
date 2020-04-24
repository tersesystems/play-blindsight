package controllers

import com.tersesystems.blindsight.Logger
import javax.inject._
import logging._
import play.api.mvc._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(val controllerComponents: ControllerComponents)
  extends BaseController with Implicits {

  def index(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    implicit val logger: Logger = getLogger(request)
    logger.flow.info {
      val result = new IndexOperation().calculate()
      Ok(views.html.index(result))
    }
  }

  // Provide a scope for all operations with a request and logger in context...
  private class IndexOperation(implicit request: RequestHeader, logger: Logger) {
    def calculate(): Long = logger.flow.info {
      val result = System.currentTimeMillis() + scala.util.Random.nextInt()
      if (result % 10 == 0) {
        throw new IllegalStateException(s"Result $result is modulo 10!")
      }
      result
    }
  }

}
