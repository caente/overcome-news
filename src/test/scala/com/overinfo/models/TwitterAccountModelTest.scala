package com.overinfo.models

import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import com.overinfo.streams.TwitterStreamer.TwitterCredentials

/**
 * Created: Miguel A. Iglesias
 * Date: 1/23/14
 */
class TwitterAccountModelTest extends FunSuite with ShouldMatchers {

  test("add twitter account") {
    val credentials = TwitterCredentials(
      consumer_key = "ja0Itrsso211M0CARoyA",
      consumer_secret = "meVcWeEHSy3MDpfv0Fq1eLPlfnopAzx9WGBAfHvsjU",
      access_token = "1407921984-X0ndlUS1UEuLGObSccan14Reh1tUhskrcCJfVhW",
      token_secret = "dS2fYOoMHD3zCyfx7FcW4txqAmPEdtYkB7VyUZg8HI"
    )
    TwitterAccountModel.addTwitterAccount(
      credentials
    )
    Thread.sleep(5000)
  }

  test("get feed") {
    println(TwitterAccountModel.getTwitterAccount(1407921984))
  }



}
