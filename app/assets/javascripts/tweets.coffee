$ ->
  class TweetsViewModel
         tweets: ko.observableArray()
    viewModel = new TweetsViewModel()
    ko.applyBindings viewModel
    handler = (msg) ->
        checkLen($('#ticker li').size(),3, remove)
        data = JSON.parse msg.data
        viewModel.tweets.unshift data.tweetList[0]
    feed = new EventSource '/tweetFeed'
    feed.addEventListener('message', handler, false)
    tick = -> $('#ticker li:first').slideUp(-> $(this).appendTo($('#ticker')).slideDown())
    remove = ->
        console.log("Removing...")
        $('ul li:first').remove()
        console.log("Removed")
    checkLen = (size, max, callback) ->
        console.log("We have " + size + " tweets")
        if size > max
            callback()
    setInterval ( ->
      checkLen($('#ticker li').size(),3,tick)
    ), 3000