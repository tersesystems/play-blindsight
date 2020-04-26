package logging

import javax.inject.{Inject, Provider}
import play.api.http.DefaultHttpErrorHandler
import play.api.mvc.RequestHeader
import play.api.routing.Router
import play.api.{Configuration, Environment, OptionalSourceMapper, UsefulException}

class LoggingHttpErrorHandler @Inject()(environment: Environment,
                                        configuration: Configuration,
                                        sourceMapper: OptionalSourceMapper,
                                        router: Provider[Router])
  extends DefaultHttpErrorHandler(environment, configuration, sourceMapper, router) {

  override protected def logServerError(request: RequestHeader, usefulException: UsefulException): Unit = {
    import com.tersesystems.logback.tracing.SpanInfo
    import HoneycombFlowBehavior.spanMarkerFactory
    import HoneycombImplicits._

    val logger = getLogger(request)
    implicit val rootSpan: SpanInfo = request.attrs(Attrs.spanInfo)
    logger.error(spanMarkerFactory(rootSpan),
      s"${rootSpan.name()} exception, duration ${rootSpan.duration()}: {}", usefulException.cause)
  }
}
