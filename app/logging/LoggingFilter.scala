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

import com.tersesystems.blindsight.api.Markers
import javax.inject.Inject
import play.api.mvc.{EssentialAction, EssentialFilter}

import scala.concurrent.ExecutionContext

class LoggingFilter @Inject()(implicit ec: ExecutionContext) extends EssentialFilter {

  override def apply(next: EssentialAction): EssentialAction = EssentialAction { request =>
    import logging.HoneycombFlowBehavior._
    import HoneycombImplicits._

    if (request.path.startsWith("/assets")) {
      next(request)
    } else {
      next(request).map { result =>
        // The root span has to be logged _last_, after the child spans.
        val rootSpan = request.attrs(Attrs.spanInfo)
        val logger = getLogger(request)
        logger.info(spanMarkerFactory(rootSpan),
          s"${rootSpan.name()} exit, duration ${rootSpan.duration()}")
        result
      }
    }
  }
}
