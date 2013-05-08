$ ->



    class TweetsViewModel
         tweets: ko.observableArray()
    viewModel = new TweetsViewModel()
    ko.applyBindings viewModel
    handler = (msg) ->
        data = JSON.parse msg.data
        viewModel.tweets.unshift data.tweetList[0]
    feed = new EventSource '/tweetFeed'
    feed.addEventListener('message', handler, false)
    $('#webticker').webTicker()