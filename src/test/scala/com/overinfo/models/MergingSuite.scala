package com.overinfo.models

import org.scalatest.{ShouldMatchers, FunSuite}
import com.overinfo.models.SourcesModel.Source
import com.overinfo.models.WordsModel.{WordMerged, TweetWord}
import org.joda.time.DateTime


/**
 * Created: Miguel A. Iglesias
 * Date: 4/25/14
 */
class MergingSuite extends FunSuite with ShouldMatchers {


  test("find words") {
    val expected = List(
      WordMerged("NEEDS", 2),
      WordMerged("NARROWING", 2)
    )
    val merged = economist.map(w => WordMerged(w.word, w.count))
    WordsModel.mergeWords(merged, nytimes) should be === expected
  }

  val economist = List(
    TweetWord("5351def971c56bdb3dff4c6b", Source("The Economist", "http://pbs.twimg.com/profile_images/378800000609554821/03e5e25edd851b2d6d45075b78c341a2_bigger.png", "TheEconomist", 5988062), "NEEDS", 1, "For the world's sake, and its own, China needs to change the way it builds and runs its cities http://t.co/N6VREmZsax http://t.co/bVUBUTXBlA", DateTime.now),
    TweetWord("5351def971c56bdb3dff4c6c", Source("The Economist", "http://pbs.twimg.com/profile_images/378800000609554821/03e5e25edd851b2d6d45075b78c341a2_bigger.png", "TheEconomist", 5988062), "WORLD 'S", 1, "For the world's sake, and its own, China needs to change the way it builds and runs its cities http://t.co/N6VREmZsax http://t.co/bVUBUTXBlA", DateTime.now),
    TweetWord("5351def971c56bdb3dff4c6d", Source("The Economist", "http://pbs.twimg.com/profile_images/378800000609554821/03e5e25edd851b2d6d45075b78c341a2_bigger.png", "TheEconomist", 5988062), "CHANGE", 1, "For the world's sake, and its own, China needs to change the way it builds and runs its cities http://t.co/N6VREmZsax http://t.co/bVUBUTXBlA", DateTime.now),
    TweetWord("5351def971c56bdb3dff4c6e", Source("The Economist", "http://pbs.twimg.com/profile_images/378800000609554821/03e5e25edd851b2d6d45075b78c341a2_bigger.png", "TheEconomist", 5988062), "CHINA", 1, "For the world's sake, and its own, China needs to change the way it builds and runs its cities http://t.co/N6VREmZsax http://t.co/bVUBUTXBlA", DateTime.now),
    TweetWord("5351def971c56bdb3dff4c6f", Source("The Economist", "http://pbs.twimg.com/profile_images/378800000609554821/03e5e25edd851b2d6d45075b78c341a2_bigger.png", "TheEconomist", 5988062), "CITIES", 1, "For the world's sake, and its own, China needs to change the way it builds and runs its cities http://t.co/N6VREmZsax http://t.co/bVUBUTXBlA", DateTime.now),
    TweetWord("5351def971c56bdb3dff4c70", Source("The Economist", "http://pbs.twimg.com/profile_images/378800000609554821/03e5e25edd851b2d6d45075b78c341a2_bigger.png", "TheEconomist", 5988062), "http://t.co/bVUBUTXBlA", 1, "For the world's sake, and its own, China needs to change the way it builds and runs its cities http://t.co/N6VREmZsax http://t.co/bVUBUTXBlA", DateTime.now),
    TweetWord("5351def971c56bdb3dff4c71", Source("The Economist", "http://pbs.twimg.com/profile_images/378800000609554821/03e5e25edd851b2d6d45075b78c341a2_bigger.png", "TheEconomist", 5988062), "http://t.co/N6VREmZsax", 1, "For the world's sake, and its own, China needs to change the way it builds and runs its cities http://t.co/N6VREmZsax http://t.co/bVUBUTXBlA", DateTime.now),
    TweetWord("5351def971c56bdb3dff4c72", Source("The Economist", "http://pbs.twimg.com/profile_images/378800000609554821/03e5e25edd851b2d6d45075b78c341a2_bigger.png", "TheEconomist", 5988062), "BUILDS", 1, "For the world's sake, and its own, China needs to change the way it builds and runs its cities http://t.co/N6VREmZsax http://t.co/bVUBUTXBlA", DateTime.now),
    TweetWord("5351def971c56bdb3dff4c73", Source("The Economist", "http://pbs.twimg.com/profile_images/378800000609554821/03e5e25edd851b2d6d45075b78c341a2_bigger.png", "TheEconomist", 5988062), "RUNS", 1, "For the world's sake, and its own, China needs to change the way it builds and runs its cities http://t.co/N6VREmZsax http://t.co/bVUBUTXBlA", DateTime.now),
    TweetWord("5351def971c56bdb3dff4c74", Source("The Economist", "http://pbs.twimg.com/profile_images/378800000609554821/03e5e25edd851b2d6d45075b78c341a2_bigger.png", "TheEconomist", 5988062), "SAKE", 1, "For the world's sake, and its own, China needs to change the way it builds and runs its cities http://t.co/N6VREmZsax http://t.co/bVUBUTXBlA", DateTime.now),
    TweetWord("5351def971c56bdb3dff4c75", Source("The Economist", "http://pbs.twimg.com/profile_images/378800000609554821/03e5e25edd851b2d6d45075b78c341a2_bigger.png", "TheEconomist", 5988062), "OWN", 1, "For the world's sake, and its own, China needs to change the way it builds and runs its cities http://t.co/N6VREmZsax http://t.co/bVUBUTXBlA", DateTime.now),
    TweetWord("535223ff71c56bdb3dff5080", Source("The Economist", "http://pbs.twimg.com/profile_images/378800000609554821/03e5e25edd851b2d6d45075b78c341a2_bigger.png", "TheEconomist", 5988062), "WEEK", 1, "Another week, another IPCC report: options for limiting climate change are narrowing http://t.co/3HUiMPS4Y9", DateTime.now),
    TweetWord("535223ff71c56bdb3dff5081", Source("The Economist", "http://pbs.twimg.com/profile_images/378800000609554821/03e5e25edd851b2d6d45075b78c341a2_bigger.png", "TheEconomist", 5988062), "ANOTHER", 1, "Another week, another IPCC report: options for limiting climate change are narrowing http://t.co/3HUiMPS4Y9", DateTime.now),
    TweetWord("535223ff71c56bdb3dff5082", Source("The Economist", "http://pbs.twimg.com/profile_images/378800000609554821/03e5e25edd851b2d6d45075b78c341a2_bigger.png", "TheEconomist", 5988062), "OPTIONS", 1, "Another week, another IPCC report: options for limiting climate change are narrowing http://t.co/3HUiMPS4Y9", DateTime.now),
    TweetWord("535223ff71c56bdb3dff5083", Source("The Economist", "http://pbs.twimg.com/profile_images/378800000609554821/03e5e25edd851b2d6d45075b78c341a2_bigger.png", "TheEconomist", 5988062), "CHANGE", 1, "Another week, another IPCC report: options for limiting climate change are narrowing http://t.co/3HUiMPS4Y9", DateTime.now),
    TweetWord("535223ff71c56bdb3dff5084", Source("The Economist", "http://pbs.twimg.com/profile_images/378800000609554821/03e5e25edd851b2d6d45075b78c341a2_bigger.png", "TheEconomist", 5988062), "REPORT:", 1, "Another week, another IPCC report: options for limiting climate change are narrowing http://t.co/3HUiMPS4Y9", DateTime.now),
    TweetWord("535223ff71c56bdb3dff5085", Source("The Economist", "http://pbs.twimg.com/profile_images/378800000609554821/03e5e25edd851b2d6d45075b78c341a2_bigger.png", "TheEconomist", 5988062), "NARROWING", 1, "Another week, another IPCC report: options for limiting climate change are narrowing http://t.co/3HUiMPS4Y9", DateTime.now),
    TweetWord("535223ff71c56bdb3dff5086", Source("The Economist", "http://pbs.twimg.com/profile_images/378800000609554821/03e5e25edd851b2d6d45075b78c341a2_bigger.png", "TheEconomist", 5988062), "IPCC", 1, "Another week, another IPCC report: options for limiting climate change are narrowing http://t.co/3HUiMPS4Y9", DateTime.now),
    TweetWord("535223ff71c56bdb3dff5087", Source("The Economist", "http://pbs.twimg.com/profile_images/378800000609554821/03e5e25edd851b2d6d45075b78c341a2_bigger.png", "TheEconomist", 5988062), "CLIMATE", 1, "Another week, another IPCC report: options for limiting climate change are narrowing http://t.co/3HUiMPS4Y9", DateTime.now),
    TweetWord("535223ff71c56bdb3dff5088", Source("The Economist", "http://pbs.twimg.com/profile_images/378800000609554821/03e5e25edd851b2d6d45075b78c341a2_bigger.png", "TheEconomist", 5988062), "LIMITING", 1, "Another week, another IPCC report: options for limiting climate change are narrowing http://t.co/3HUiMPS4Y9", DateTime.now)
  )


  val nytimes = List(
    TweetWord("5351b61b71c56bdb3dff472f", Source("The New York Times", "http://pbs.twimg.com/profile_images/2044921128/finals_bigger.png", "nytimes", 807095), "FORUM", 1, "The Lede: Snowden Defends His Part in Putin Forum http://t.co/FbQA4gfSom", DateTime.now),
    TweetWord("5351b61b71c56bdb3dff4730", Source("The New York Times", "http://pbs.twimg.com/profile_images/2044921128/finals_bigger.png", "nytimes", 807095), "LEDE:", 1, "The Lede: Snowden Defends His Part in Putin Forum http://t.co/FbQA4gfSom", DateTime.now),
    TweetWord("5351b61b71c56bdb3dff4731", Source("The New York Times", "http://pbs.twimg.com/profile_images/2044921128/finals_bigger.png", "nytimes", 807095), "SNOWDEN", 1, "The Lede: Snowden Defends His Part in Putin Forum http://t.co/FbQA4gfSom", DateTime.now),
    TweetWord("5351b61b71c56bdb3dff4732", Source("The New York Times", "http://pbs.twimg.com/profile_images/2044921128/finals_bigger.png", "nytimes", 807095), "http://t.co/FbQA4gfSom", 1, "The Lede: Snowden Defends His Part in Putin Forum http://t.co/FbQA4gfSom", DateTime.now),
    TweetWord("5351b61b71c56bdb3dff4733", Source("The New York Times", "http://pbs.twimg.com/profile_images/2044921128/finals_bigger.png", "nytimes", 807095), "DEFENDS", 1, "The Lede: Snowden Defends His Part in Putin Forum http://t.co/FbQA4gfSom", DateTime.now),
    TweetWord("5351b61b71c56bdb3dff4734", Source("The New York Times", "http://pbs.twimg.com/profile_images/2044921128/finals_bigger.png", "nytimes", 807095), "PUTIN", 1, "The Lede: Snowden Defends His Part in Putin Forum http://t.co/FbQA4gfSom", DateTime.now),
    TweetWord("5351b75671c56bdb3dff4760", Source("The New York Times", "http://pbs.twimg.com/profile_images/2044921128/finals_bigger.png", "nytimes", 807095), "RECORD", 1, "Record Store Day Underscores a Small Renaissance http://t.co/sPBvOImQbG", DateTime.now),
    TweetWord("5351b75671c56bdb3dff4761", Source("The New York Times", "http://pbs.twimg.com/profile_images/2044921128/finals_bigger.png", "nytimes", 807095), "http://t.co/sPBvOImQbG", 1, "Record Store Day Underscores a Small Renaissance http://t.co/sPBvOImQbG", DateTime.now),
    TweetWord("5351b75671c56bdb3dff4762", Source("The New York Times", "http://pbs.twimg.com/profile_images/2044921128/finals_bigger.png", "nytimes", 807095), "STORE", 1, "Record Store Day Underscores a Small Renaissance http://t.co/sPBvOImQbG", DateTime.now),
    TweetWord("5351b75671c56bdb3dff4763", Source("The New York Times", "http://pbs.twimg.com/profile_images/2044921128/finals_bigger.png", "nytimes", 807095), "RENAISSANCE", 1, "Record Store Day Underscores a Small Renaissance http://t.co/sPBvOImQbG", DateTime.now),
    TweetWord("5351b75671c56bdb3dff4764", Source("The New York Times", "http://pbs.twimg.com/profile_images/2044921128/finals_bigger.png", "nytimes", 807095), "SMALL", 1, "Record Store Day Underscores a Small Renaissance http://t.co/sPBvOImQbG", DateTime.now),
    TweetWord("5351b75671c56bdb3dff4765", Source("The New York Times", "http://pbs.twimg.com/profile_images/2044921128/finals_bigger.png", "nytimes", 807095), "UNDERSCORES", 1, "Record Store Day Underscores a Small Renaissance http://t.co/sPBvOImQbG", DateTime.now),
    TweetWord("5351bc8971c56bdb3dff47f9", Source("The New York Times", "http://pbs.twimg.com/profile_images/2044921128/finals_bigger.png", "nytimes", 807095), "DAY'S", 1, "What you need to know about the day 's biggest stories: the Evening Briefing on@nytnow.http :// t.co / D2uxWtoOqD", DateTime.now),
    TweetWord("5351bc8971c56bdb3dff47fa", Source("The New York Times", "http://pbs.twimg.com/profile_images/2044921128/finals_bigger.png", "nytimes", 807095), "http://t.co/D2uxWtoOqD", 1, "What you need to know about the day 's biggest stories: the Evening Briefing on@nytnow.http :// t.co / D2uxWtoOqD", DateTime.now),
    TweetWord("5351bc8971c56bdb3dff47fb", Source("The New York Times", "http://pbs.twimg.com/profile_images/2044921128/finals_bigger.png", "nytimes", 807095), "STORIES:", 1, "What you need to know about the day 's biggest stories: the Evening Briefing on@nytnow.http :// t.co / D2uxWtoOqD", DateTime.now),
    TweetWord("5351bc8971c56bdb3dff47fc", Source("The New York Times", "http://pbs.twimg.com/profile_images/2044921128/finals_bigger.png", "nytimes", 807095), "BRIEFING", 1, "What you need to know about the day 's biggest stories: the Evening Briefing on@nytnow.http :// t.co / D2uxWtoOqD", DateTime.now),
    TweetWord("5351bc8971c56bdb3dff47fd", Source("The New York Times", "http://pbs.twimg.com/profile_images/2044921128/finals_bigger.png", "nytimes", 807095), "NEEDS", 1, "What you need to know about the day 's biggest stories: the Evening Briefing on@nytnow.http :// t.co / D2uxWtoOqD", DateTime.now),
    TweetWord("5351bc8971c56bdb3dff47fe", Source("The New York Times", "http://pbs.twimg.com/profile_images/2044921128/finals_bigger.png", "nytimes", 807095), "BIGGEST", 1, "What you need to know about the day 's biggest stories: the Evening Briefing on@nytnow.http :// t.co / D2uxWtoOqD", DateTime.now),
    TweetWord("5351bc8971c56bdb3dff47ff", Source("The New York Times", "http://pbs.twimg.com/profile_images/2044921128/finals_bigger.png", "nytimes", 807095), "@NYTNOW", 1, "What you need to know about the day 's biggest stories: the Evening Briefing on@nytnow.http :// t.co / D2uxWtoOqD", DateTime.now),
    TweetWord("5351bc8971c56bdb3dff4800", Source("The New York Times", "http://pbs.twimg.com/profile_images/2044921128/finals_bigger.png", "nytimes", 807095), "KNOW", 1, " What you need to know about the day 's biggest stories: the Evening Briefing on@nytnow.http :// t.co / D2uxWtoOqD", DateTime.now),
    TweetWord("535223ff71c56bdb3dff5085", Source("The New York Times", "http://pbs.twimg.com/profile_images/2044921128/finals_bigger.png", "nytimes", 807095), "NARROWING", 1, "Another week, another IPCC report: options for limiting climate change are narrowing http://t.co/3HUiMPS4Y9", DateTime.now)
  )
}
