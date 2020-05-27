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
import com.tersesystems.logback.tracing.SpanInfo
import net.logstash.logback.marker.LogstashMarker
import play.api.mvc.{RequestHeader, Result}

trait HoneycombImplicits {

  def getLogger(request: RequestHeader): Logger = {
    implicit val loggerResolver: LoggerResolver[RequestHeader] = LoggerResolver { request =>
      org.slf4j.LoggerFactory.getLogger("request." + request.id)
    }
    LoggerFactory.getLogger(request).withMarker(request)
  }

  implicit def flowBehavior[B: ToArgument](implicit spanInfo: SpanInfo): FlowBehavior[B] = {
    new HoneycombFlowBehavior[B]()
  }

  implicit val logstashMarkerToMarkers: ToMarkers[LogstashMarker] = ToMarkers(Markers(_))

  implicit val resultToArguments: ToArgument[Result] = ToArgument { result =>
    Argument(bodj("status" -> result.header.status))
  }

  implicit val requestToMarker: ToMarkers[RequestHeader] = ToMarkers { request =>
    Markers(bodj("request.path" -> request.path, "request.host" -> request.host))
  }

  implicit def requestToSpanInfo(implicit request: RequestHeader): SpanInfo = {
    request.attrs(Attrs.spanInfo)
  }
}

object HoneycombImplicits extends HoneycombImplicits
