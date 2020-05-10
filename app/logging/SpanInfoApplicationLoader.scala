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

import com.tersesystems.logback.tracing.SpanInfo
import com.tersesystems.logback.uniqueid.RandomUUIDIdGenerator
import javax.inject.Inject
import play.api.ApplicationLoader
import play.api.inject.guice.{GuiceApplicationLoader, GuiceableModule}
import play.api.inject.{SimpleModule, bind}
import play.api.libs.typedmap.{TypedKey, TypedMap}
import play.api.mvc.request.{DefaultRequestFactory, RemoteConnection, RequestFactory, RequestTarget}
import play.api.mvc.{
  CookieHeaderEncoding,
  FlashCookieBaker,
  Headers,
  RequestHeader,
  SessionCookieBaker
}

class SpanInfoApplicationLoader extends GuiceApplicationLoader {
  override protected def overrides(context: ApplicationLoader.Context): Seq[GuiceableModule] = {
    super.overrides(context) :+ GuiceableModule.guiceable(new SpanInfoModule)
  }
}

class SpanInfoModule extends SimpleModule(bind[RequestFactory] to classOf[SpanInfoRequestFactory])

class SpanInfoRequestFactory @Inject() (
    cookieHeaderEncoding: CookieHeaderEncoding,
    sessionBaker: SessionCookieBaker,
    flashBaker: FlashCookieBaker
) extends DefaultRequestFactory(cookieHeaderEncoding, sessionBaker, flashBaker) {

  override def createRequestHeader(
      connection: RemoteConnection,
      method: String,
      target: RequestTarget,
      version: String,
      headers: Headers,
      attrs: TypedMap
  ): RequestHeader = {
    val request = super.createRequestHeader(connection, method, target, version, headers, attrs)
    request.addAttr(Attrs.spanInfo, SpanInfoRequestFactory.rootSpan(request))
  }
}

object SpanInfoRequestFactory {

  private val idgen = new RandomUUIDIdGenerator
  private val builder = {
    SpanInfo
      .builder()
      .setServiceName("play-blindsight")
      .setIdGenerator(() => idgen.generateId())
  }

  def rootSpan[T](request: RequestHeader): SpanInfo = {
    val spanId = idgen.generateId()
    builder
      .setTraceId(spanId)
      .setSpanId(spanId)
      .setName(s"${request.method} ${request.path}")
      .buildNow
  }
}
