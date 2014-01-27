package com.overinfo.processors

import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import scala.io.Source

/**
 * Created: Miguel A. Iglesias
 * Date: 1/14/14
 */
class TwitterSamplerTest extends FunSuite with ShouldMatchers{


  test("should find all the tags and words") {
    val tweets = Source.fromURL(getClass.getResource("/samples.txt")).getLines().toSet

  }
}
