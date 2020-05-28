/*
 *     SPDX-License-Identifier: CC0-1.0
 *
 *     Copyright 2020 Will Sargent.
 *
 *     Licensed under the CC0 Public Domain Dedication;
 *     You may obtain a copy of the License at
 *
 *         http://creativecommons.org/publicdomain/zero/1.0/
 *
 */

package logging

import com.tersesystems.blindsight._
import com.tersesystems.blindsight.DSL._
import com.tersesystems.blindsight.flow.FlowBehavior
import com.tersesystems.logback.tracing.{EventInfo, SpanInfo}
import net.logstash.logback.marker.LogstashMarker
import play.api.mvc.{RequestHeader, Result}

trait HoneycombImplicits {

  def getLogger(request: RequestHeader): Logger = {
    implicit val loggerResolver: LoggerResolver[RequestHeader] = LoggerResolver { request =>
      org.slf4j.LoggerFactory.getLogger("request." + request.id)
    }
    LoggerFactory.getLogger(request).withMarker(request)
  }

  // https://docs.honeycomb.io/working-with-your-data/tracing/send-trace-data/#span-events
  def honeycombEventMarker(implicit spanInfo: SpanInfo): Markers = {
    val eventInfo = EventInfo
      .builder()
      .setName(spanInfo.duration().toString)
      .setParentId(spanInfo.spanId())
      .setTraceId(spanInfo.traceId())
      .build()
    HoneycombFlowBehavior.eventMarkerFactory(eventInfo)
  }

  implicit def flowBehavior[B: ToArgument](implicit spanInfo: SpanInfo): FlowBehavior[B] = {
    new HoneycombFlowBehavior[B]()
  }

  // cheat so we don't have to use Markers(foo) in the SLF4J API (compiler gets confused if
  // absolutely everything uses context bounds so we have to sneak it in)
  implicit def toMarkersImplicitConversion[M: ToMarkers](m: M): Markers = implicitly[ToMarkers[M]].toMarkers(m)

  implicit val logstashMarkerToMarkers: ToMarkers[LogstashMarker] = ToMarkers(Markers(_))

  implicit val resultToArguments: ToArgument[Result] = ToArgument { result =>
    Argument(bobj("status" -> result.header.status))
  }

  implicit val requestToMarker: ToMarkers[RequestHeader] = ToMarkers { request =>
    Markers(bobj("request.path" -> request.path, "request.host" -> request.host))
  }

  implicit def requestToSpanInfo(implicit request: RequestHeader): SpanInfo = {
    request.attrs(Attrs.spanInfo)
  }
}

object HoneycombImplicits extends HoneycombImplicits
