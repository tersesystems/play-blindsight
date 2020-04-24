package logging

import javax.inject.Inject
import logging.HoneycombFlowBehavior._
import play.api.mvc.{EssentialAction, EssentialFilter}

import scala.concurrent.ExecutionContext

class LoggingFilter @Inject()(loggerFactory: RequestLoggerFactory)(implicit ec: ExecutionContext) extends EssentialFilter {

  override def apply(next: EssentialAction): EssentialAction = EssentialAction { request =>
    import com.tersesystems.blindsight.logstash.Implicits._

    next(request).map { result =>
      // The root span has to be logged _last_, after the child spans.
      val rootSpan = request.attrs(Attrs.spanInfo)
      val logger = loggerFactory.getLogger(request)
      logger.info(markerFactory(rootSpan),
        s"${rootSpan.name()} exit, duration ${rootSpan.duration()}")
      result
    }
  }
}
