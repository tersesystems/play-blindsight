class Module extends play.api.inject.SimpleModule(
  play.api.inject.bind[Startup].toSelf.eagerly
)

class Startup {
  private val logger = org.slf4j.LoggerFactory.getLogger(getClass)

  if (! sys.env.contains("HONEYCOMB_API_KEY")) {
    logger.error("No HONEYCOMB_API_KEY environment variable found!")
  }

  if(!sys.env.contains("HONEYCOMB_DATASET")) {
    logger.error("No HONEYCOMB_DATASET environment variable found!")
  }
}
