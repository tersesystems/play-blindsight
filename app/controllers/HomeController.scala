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

package controllers

import com.tersesystems.blindsight._
import com.tersesystems.blindsight.DSL._
import javax.inject._
import logging._
import play.api.mvc._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject() (val controllerComponents: ControllerComponents)
    extends BaseController
    with HoneycombImplicits {

  private val tracerMarker = org.slf4j.MarkerFactory.getMarker("TRACER")
  private def traceCondition(implicit request: Request[AnyContent]): Condition = request.queryString.contains("trace")

  def index(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    implicit val logger: Logger = getLogger(request)

    val traceLogger = logger.withCondition(traceCondition).withMarker(tracerMarker)
    traceLogger.trace { log =>
      log(st"The query string contains ${bobj("queryString" -> request.queryString)}")
    }

    logger.flow.info {
      val result = new IndexOperation().calculate()
      Ok(views.html.index(result))
    }
  }

  // Provide a scope for all operations with a request and logger in context...
  private class IndexOperation(implicit request: RequestHeader, logger: Logger) {
    def calculate(): Long = logger.flow.info {
      val result = System.currentTimeMillis() + scala.util.Random.nextInt()

      val eventLogger = logger.withMarker(honeycombEventMarker)
      eventLogger.info("This is a span event")
      eventLogger.info("This is also a span event")

      if (result % 10 == 0) {
        throw new IllegalStateException(s"Result $result is modulo 10!")
      }
      result
    }
  }

}
