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
class HomeController @Inject()(val controllerComponents: ControllerComponents,
                               loggerFactory: RequestLoggerFactory)
  extends BaseController with Implicits {

  def index(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    logger.flow.info {
      val result = doStuff
      Ok(views.html.index(result))
    }
  }

  private def doStuff(implicit request: RequestHeader): Int = logger.flow.info {
    1 + 1
  }

  private def logger(implicit request: RequestHeader): Logger = loggerFactory.getLogger(request)
}
