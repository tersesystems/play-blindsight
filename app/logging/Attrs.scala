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
import play.api.libs.typedmap.TypedKey

object Attrs {
  val spanInfo: TypedKey[SpanInfo] = TypedKey("Span Info")
}
