package com.pavelhabzansky.data.core

// https://console.firebase.google.com/u/0/project/citizen-app-be/database/citizen-app-be/data
const val FIREBASE_ROOT = "https://citizen-app-be.firebaseio.com/"

const val CONTENT_TYPE_JPG = "image/jpg"

const val IMG_MAX_SIZE = 1024*1024L

const val CITY_CHILD_NAME = "name"
const val CITY_CHILD_WIKI = "wiki"
const val CITY_CHILD_ID = "id"
const val CITY_CHILD_WWW = "www"
const val CITY_CHILD_RSS_FEED = "rssFeed"
const val CITY_CHILD_RSS_URL = "rssUrl"

const val WIKI_CHILD_CITIZENS = "citizens"
const val WIKI_CHILD_HEADLINE = "headline"
const val WIKI_CHILD_LOGO = "logo"
const val WIKI_CHILD_GPS = "gps"

const val GPS_CHILD_LAT = "lat"
const val GPS_CHILD_LNG = "lng"

const val NEWS_LOAD_SIZE = 50