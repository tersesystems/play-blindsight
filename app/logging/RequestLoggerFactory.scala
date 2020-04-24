
package logging

import com.tersesystems.blindsight.{Logger, LoggerFactory}
import play.api.mvc.RequestHeader

class RequestLoggerFactory {
  import Implicits._

  def getLogger(request: RequestHeader): Logger = LoggerFactory.getLogger(request).withMarker(request)
}
