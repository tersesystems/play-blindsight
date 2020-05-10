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

import com.tersesystems.blindsight.api.{Markers, Statement, ToArguments}
import com.tersesystems.blindsight.flow.FlowBehavior
import com.tersesystems.blindsight.flow.FlowBehavior.Source
import com.tersesystems.logback.tracing.{EventMarkerFactory, SpanInfo, SpanMarkerFactory}
import org.slf4j.event.Level

import scala.collection.mutable
import scala.compat.java8.FunctionConverters._

class HoneycombFlowBehavior[B: ToArguments](implicit spanInfo: SpanInfo) extends FlowBehavior[B] {
  import HoneycombFlowBehavior._

  // Create a thread local stack of span info.
  private val threadLocalStack: ThreadLocal[SpanStack] = {
    val local: ThreadLocal[SpanStack] = new ThreadLocal()
    local.set(mutable.Stack[SpanInfo]())
    local
  }

  override def entryStatement(source: Source): Option[Statement] = {
    // Start a child span, and push it onto the stack
    spanInfo.withChild(source.enclosing.value, asJavaFunction(pushCurrentSpan))
    None
  }

  override def throwingStatement(
      throwable: Throwable,
      source: Source
  ): Option[(Level, Statement)] = {
    val span = popCurrentSpan
    Some {
      (
        Level.ERROR,
        Statement()
          .withThrowable(throwable)
          .withMarkers(Markers(spanMarkerFactory(span)))
          .withMessage(s"${source.enclosing.value} exception, duration ${span.duration()}")
      )
    }
  }

  override def exitStatement(resultValue: B, source: Source): Option[Statement] = Some {
    val span = popCurrentSpan
    Statement()
      .withMarkers(Markers(spanMarkerFactory(span)))
      .withMessage(s"${source.enclosing.value} exit with result {}, start time = ${span
        .startTime()}, duration ${span.duration()}")
      .withArguments(resultValue)
  }

  private def pushCurrentSpan(spanInfo: SpanInfo): Unit = threadLocalStack.get.push(spanInfo)
  private def popCurrentSpan: SpanInfo                  = threadLocalStack.get().pop()
}

object HoneycombFlowBehavior {
  val spanMarkerFactory  = new SpanMarkerFactory
  val eventMarkerFactory = new EventMarkerFactory

  type SpanStack = mutable.Stack[SpanInfo]
}
