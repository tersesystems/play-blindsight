package logging

import com.tersesystems.logback.tracing.SpanInfo
import play.api.libs.typedmap.TypedKey

object Attrs {
  val spanInfo: TypedKey[SpanInfo] = TypedKey("Span Info")
}
