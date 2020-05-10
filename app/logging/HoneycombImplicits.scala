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

import com.tersesystems.blindsight.{Logger, LoggerFactory, LoggerResolver}
import com.tersesystems.blindsight.api.{Arguments, Markers, ToArguments, ToMarkers}
import com.tersesystems.blindsight.flow.FlowBehavior
import com.tersesystems.logback.tracing.SpanInfo
import play.api.mvc.{RequestHeader, Result}

trait HoneycombImplicits extends com.tersesystems.blindsight.logstash.Implicits {

  def getLogger(request: RequestHeader): Logger = {
    implicit val loggerResolver: LoggerResolver[RequestHeader] = LoggerResolver { request =>
      org.slf4j.LoggerFactory.getLogger("request." + request.id)
    }
    LoggerFactory.getLogger(request).withMarker(request)
  }

  implicit def flowBehavior[B: ToArguments](implicit spanInfo: SpanInfo): FlowBehavior[B] = {
    new HoneycombFlowBehavior[B]()
  }

  implicit val resultToArguments: ToArguments[Result] = ToArguments { result =>
    Arguments("status" -> result.header.status)
  }

  implicit val requestToMarker: ToMarkers[RequestHeader] = ToMarkers { request =>
    Markers(Map("request.path" -> request.path, "request.host" -> request.host))
  }

  implicit def requestToSpanInfo(implicit request: RequestHeader): SpanInfo = {
    request.attrs(Attrs.spanInfo)
  }
}

object HoneycombImplicits extends HoneycombImplicits
