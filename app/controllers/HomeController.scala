package controllers

import com.tersesystems.blindsight.Logger
import com.tersesystems.logback.tracing.{EventInfo, SpanInfo}
import javax.inject._
import logging._
import play.api.mvc._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(val controllerComponents: ControllerComponents)
  extends BaseController with HoneycombImplicits {

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

      // XXX doesn't quite work yet
      //eventLogger.info("This is a span event")

      if (result % 10 == 0) {
        throw new IllegalStateException(s"Result $result is modulo 10!")
      }
      result
    }

    // https://docs.honeycomb.io/working-with-your-data/tracing/send-trace-data/#span-events
    def eventLogger(implicit spanInfo: SpanInfo): Logger = {
      val eventInfo = EventInfo.builder().setName(spanInfo.name())
        .setParentId(spanInfo.parentId())
        .setTraceId(spanInfo.traceId())
        .build()
      logger.withMarker(HoneycombFlowBehavior.eventMarkerFactory(eventInfo))
    }
  }
}
