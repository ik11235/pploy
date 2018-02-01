package models

import play.api.Logger
import play.api.libs.iteratee.Enumerator

import scala.concurrent.ExecutionContext
import scala.sys.process._

object ProcessEnumerator {
  // wraps a ProcessBuilder with Play's Enumerator
  // and executes the process in Future
  // so that the process' output can be streamed
  def apply(process: ProcessBuilder)(
      implicit
      executionContext: ExecutionContext
  ): Enumerator[String] = {
    Logger.info(s"[Process]: $process , $executionContext")

    Enumerator.enumerate[String](
      process.lineStream_!(ProcessLogger(line => ())).map { line =>
        Logger.info(s"[line]: $line")

        line + "\n"
      }
    )(executionContext)
  }
}
