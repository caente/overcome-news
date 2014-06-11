overcome-news
=============
This is a RESTFul API that allows to find the "intersection" between twitter accounts from different news media.

Each tweet is dissected in its words and it will keep track of how many times each word is repeated. But also,
it will be possible to find out what happens when several news media are combined together.

i.e.

```
 POST http://wheeledskylight.com:8081/words/intersect
```

Payload:

```javascript
{
"sources": [807095, 5988062],
"limit":5
}
```

Response:

```javascript
[{
  "word": "PUTIN",
  "count": 2
}, {
  "word": "SMALL",
  "count": 2
}, {
  "word": "YEARS",
  "count": 2
}, {
  "word": "NEW",
  "count": 2
}, {
  "word": "WORLD'S",
  "count": 2
}]
```

Where 807095 and 5988062 are:

```
GET http://wheeledskylight.com:8081/sources/6017542
```

```javascript
{
  "name": "Breaking News",
  "image_url": "http://pbs.twimg.com/profile_images/378800000700003994/53d967d27656bd5941e7e1fcddf47e0b_bigger.png",
  "screen_name": "BreakingNews",
  "twitter_id": 6017542
}
```


```
GET http://wheeledskylight.com:8081/sources/5988062
```

```javascript
{
  "name": "The Economist",
  "image_url": "http://pbs.twimg.com/profile_images/378800000609554821/03e5e25edd851b2d6d45075b78c341a2_bigger.png",
  "screen_name": "TheEconomist",
  "twitter_id": 5988062
}
```

The list of sources can found like this:

```
http://wheeledskylight.com:8081/sources
```

```javascript
[{
  "name": "CNN Breaking News",
  "image_url": "http://pbs.twimg.com/profile_images/1762504301/128x128_cnnbrk_avatar_bigger.gif",
  "screen_name": "cnnbrk",
  "twitter_id": 428333
}, {
  "name": "BBC News (World)",
  "image_url": "http://pbs.twimg.com/profile_images/2186836571/128x128_twitter_bbc_world_bigger.jpg",
  "screen_name": "BBCWorld",
  "twitter_id": 742143
}, {
  "name": "CNN",
  "image_url": "http://pbs.twimg.com/profile_images/378800000049679889/9097753c470683f49aa12a6c15eba5c7_bigger.jpeg",
  "screen_name": "CNN",
  "twitter_id": 759251
}, {
  "name": "The New York Times",
  "image_url": "http://pbs.twimg.com/profile_images/2044921128/finals_bigger.png",
  "screen_name": "nytimes",
  "twitter_id": 807095
}, {
  "name": "Reuters Top News",
  "image_url": "http://pbs.twimg.com/profile_images/3379693153/1008914c0ae75c9efb5f9c0161fce9a2_bigger.png",
  "screen_name": "Reuters",
  "twitter_id": 1652541
}, {
  "name": "CNN International",
  "image_url": "http://pbs.twimg.com/profile_images/840325508/cnn.twitter.avatar_bigger.jpg",
  "screen_name": "cnni",
  "twitter_id": 2097571
}, {
  "name": "Washington Post",
  "image_url": "http://pbs.twimg.com/profile_images/378800000252568100/7a366ac8d0f934b5318e721ba049e9b1_bigger.png",
  "screen_name": "washingtonpost",
  "twitter_id": 2467791
}, {
  "name": "msnbc",
  "image_url": "http://pbs.twimg.com/profile_images/3590632889/b5dbe762edb52ced8189c1ec33f3662d_bigger.jpeg",
  "screen_name": "msnbc",
  "twitter_id": 2836421
}, {
  "name": "Wall Street Journal",
  "image_url": "http://pbs.twimg.com/profile_images/421411290729291776/j3cisjbo_bigger.jpeg",
  "screen_name": "WSJ",
  "twitter_id": 3108351
}, {
  "name": "NPR News",
  "image_url": "http://pbs.twimg.com/profile_images/1796148436/nprnews_icon_bigger.jpg",
  "screen_name": "nprnews",
  "twitter_id": 5392522
}, {
  "name": "BBC Breaking News",
  "image_url": "http://pbs.twimg.com/profile_images/2186829506/128x128_twitter_bbc_breaking_bigger.jpg",
  "screen_name": "BBCBreaking",
  "twitter_id": 5402612
}, {
  "name": "The Economist",
  "image_url": "http://pbs.twimg.com/profile_images/378800000609554821/03e5e25edd851b2d6d45075b78c341a2_bigger.png",
  "screen_name": "TheEconomist",
  "twitter_id": 5988062
}, {
  "name": "Breaking News",
  "image_url": "http://pbs.twimg.com/profile_images/378800000700003994/53d967d27656bd5941e7e1fcddf47e0b_bigger.png",
  "screen_name": "BreakingNews",
  "twitter_id": 6017542
}, {
  "name": "NBC Nightly News",
  "image_url": "http://pbs.twimg.com/profile_images/3292385713/a9006684df6f93c5b6a03c2d531af62d_bigger.jpeg",
  "screen_name": "nbcnightlynews",
  "twitter_id": 8839632
}, {
  "name": "NASA",
  "image_url": "http://pbs.twimg.com/profile_images/188302352/nasalogo_twitter_bigger.jpg",
  "screen_name": "NASA",
  "twitter_id": 11348282
}, {
  "name": "NBC News World News",
  "image_url": "http://pbs.twimg.com/profile_images/2326460397/wzv2tnpbrhygxwkf7i2g_bigger.jpeg",
  "screen_name": "NBCNewsWorld",
  "twitter_id": 11855772
}, {
  "name": "Meet The Press",
  "image_url": "http://pbs.twimg.com/profile_images/378800000543462249/d82bb1313fc960715c9486f2fe072e8f_bigger.jpeg",
  "screen_name": "meetthepress",
  "twitter_id": 11856892
}, {
  "name": "PBS",
  "image_url": "http://pbs.twimg.com/profile_images/414991461646479360/ukThm70F_bigger.png",
  "screen_name": "PBS",
  "twitter_id": 12133382
}, {
  "name": "NBC News",
  "image_url": "http://pbs.twimg.com/profile_images/378800000521861809/871433564230dfb44c737174d88eecfe_bigger.png",
  "screen_name": "NBCNews",
  "twitter_id": 14173315
}, {
  "name": "TIME.com",
  "image_url": "http://pbs.twimg.com/profile_images/1700796190/Picture_24_bigger.png",
  "screen_name": "TIME",
  "twitter_id": 14293310
}, {
  "name": "Huffington Post",
  "image_url": "http://pbs.twimg.com/profile_images/378800000097342833/829b10206ae1092858ce1ad85a061b9f_bigger.jpeg",
  "screen_name": "HuffingtonPost",
  "twitter_id": 14511951
}, {
  "name": "The New Yorker",
  "image_url": "http://pbs.twimg.com/profile_images/421413599441981441/GMZ5UIRl_bigger.jpeg",
  "screen_name": "NewYorker",
  "twitter_id": 14677919
}, {
  "name": "CBS News",
  "image_url": "http://pbs.twimg.com/profile_images/1213095644/CBS-eye-white-bg_bigger.jpg",
  "screen_name": "CBSNews",
  "twitter_id": 15012486
}, {
  "name": "NatGeo Latinoamérica",
  "image_url": "http://pbs.twimg.com/profile_images/545395875/logo-NG_bigger.jpg",
  "screen_name": "NatGeo_la",
  "twitter_id": 15248067
}, {
  "name": "National Geographic",
  "image_url": "http://pbs.twimg.com/profile_images/2598886371/pmxchlgw0yw7m1e02naz_bigger.jpeg",
  "screen_name": "NatGeo",
  "twitter_id": 17471979
}, {
  "name": "The White House",
  "image_url": "http://pbs.twimg.com/profile_images/378800000609515644/027d48a311c2e770e6774a2649c05b4f_bigger.jpeg",
  "screen_name": "WhiteHouse",
  "twitter_id": 30313925
}, {
  "name": "Bloomberg News",
  "image_url": "http://pbs.twimg.com/profile_images/1573194528/News_tw_bigger.jpg",
  "screen_name": "BloombergNews",
  "twitter_id": 34713362
}, {
  "name": "CBS Evening News",
  "image_url": "http://pbs.twimg.com/profile_images/1384309692/avatar_bigger.jpg",
  "screen_name": "CBSEveningNews",
  "twitter_id": 42958829
}, {
  "name": "The Associated Press",
  "image_url": "http://pbs.twimg.com/profile_images/1848193664/APLogo_bigger.jpg",
  "screen_name": "AP",
  "twitter_id": 51241574
}, {
  "name": "ABC World News",
  "image_url": "http://pbs.twimg.com/profile_images/3736833968/b4df3d40bbd92531b0f4272fa52c6427_bigger.png",
  "screen_name": "ABCWorldNews",
  "twitter_id": 86141342
}, {
  "name": "The Guardian",
  "image_url": "http://pbs.twimg.com/profile_images/2814613165/f3c9e3989acac29769ce01b920f526bb_bigger.png",
  "screen_name": "guardian",
  "twitter_id": 87818409
}]
```
