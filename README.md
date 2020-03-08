# Days

[![Build Status](https://travis-ci.org/clloret/days.svg?branch=master)](https://travis-ci.org/clloret/days) [![Codacy Badge](https://api.codacy.com/project/badge/Grade/0373cb4e24b84e77bbb7a62e94e0509e)](https://www.codacy.com/app/clloret/days?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=clloret/days&amp;utm_campaign=Badge_Grade) [![codecov](https://codecov.io/gh/clloret/days/branch/master/graph/badge.svg)](https://codecov.io/gh/clloret/days)

Days is a simple Android app that helps to control event dates.

This project also showcases Clean Architecture, the MVP pattern with Mosby, Repository pattern, RXJava 2, Dagger 2, Room and Material Design.

<p align="center">
<a href='https://play.google.com/store/apps/details?id=com.clloret.days&pcampaignid=pcampaignidMKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1'><img alt='Get it on Google Play' src='https://play.google.com/intl/es/badges/static/images/badges/en_badge_web_generic.png' height='90px'/></a>
</p>

## Features

### For the user
- Countdown and progressive: Keep track of how many days are left or how many days have passed since each event.
- Tags for organizing events: You can use multiple tags to organize your events.
- Favorite events: Assign favorite events to keep them close at hand.
- Event sorting: You can sort events in the most convenient way.
- Simple and easy to use Material Design: Days has a simple design, so that it is agile and comfortable to use. Also use and follow the Material Design style guide.
- Cloud storage - Integrates with the Airtable Service API to provide cloud storage ([Airtable configuration tutorial](https://github.com/clloret/days/wiki/Airtable-configuration-tutorial)).
- Multi-platform data access: Thanks to the storage in Airtable it is possible to consult and modify the data from any compatible device.
- Local storage - If cloud storage is not used, data is stored on the phone. If cloud storage is used, storage on the phone is used as a data cache.
- Free and open source: It is free and the source code of the application is available.

### For other developers:
- Clean Architecture: Try to follow the principles of Clean Architecture.
- MVP: Implementation of the MVP architecture pattern for the presentation layer.
- Repository pattern: Implementation of the Repository pattern for the persistence layer.
- Room: Use of the Room library for local data storage.
- RxJava 2: Use of the RxJava2 library for reactive programming.
- Material Design: Implementation of Google's Material Design style.
- Unit testing: Unit tests have been implemented to check the correct functioning of the application. Using the Robolectric, Mockito and Mockwebserver libraries to avoid dependencies.
- Dagger 2: Implementation of dependency injection using Dagger 2.
  
## Screenshots
[![Navigation Drawer][screen1th]][screen1]
[![Main screen][screen2th]][screen2]
[![Order][screen3th]][screen3]
[![View/Edit event][screen4th]][screen4]
[![New event][screen5th]][screen5]
[![New tag][screen6th]][screen6]
[![Settings][screen7th]][screen7]

## Libraries
The following libraries are used in the project:
- [Airtable Android](https://github.com/clloret/airtable.android)
- [Android Support Libraries](https://developer.android.com/topic/libraries/support-library)
- [ButterKnife](https://github.com/JakeWharton/butterknife)
- [Dagger](https://github.com/google/dagger)
- [EventBus](https://github.com/greenrobot/EventBus)
- [FABProgressCircle](https://github.com/JorgeCastilloPrz/FABProgressCircle)
- [Gson](https://github.com/google/gson)
- [Joda-Time](https://github.com/JodaOrg/joda-time)
- [Material-About-Library](https://github.com/daniel-stoneuk/material-about-library)
- [Mockito](https://github.com/mockito/mockito)
- [MockWebServer](https://github.com/square/okhttp/tree/master/mockwebserver)
- [Mosby](https://github.com/sockeqwe/mosby)
- [OkHttp](https://github.com/square/okhttp)
- [Robolectric](https://github.com/robolectric/robolectric)
- [Room](https://developer.android.com/topic/libraries/architecture/room)
- [RxAndroid](https://github.com/ReactiveX/RxAndroid)
- [Stetho](https://github.com/facebook/stetho)
- [Timber](https://github.com/JakeWharton/timber)

## Requirements

- JDK 1.8
- [Android SDK](http://developer.android.com/sdk/index.html).
- Android 10 [(API 29)](https://developer.android.com/studio/releases/platforms#10).
- Latest Android SDK Tools and build tools.

## To-dos

This project is still in progress. Here are the some features that I will finish in the future.

### For users:
- [x] Notifications
- [ ] Widgets
- [x] Tasker integration

### For developers:
- [x] Kotlin
- [ ] Architecture components

## License

This project is licensed under the GPLv3 License - see the [LICENSE](LICENSE) file for details

[screen1]: screenshots/original/screenshot1.png
[screen2]: screenshots/original/screenshot2.png
[screen3]: screenshots/original/screenshot3.png
[screen4]: screenshots/original/screenshot4.png
[screen5]: screenshots/original/screenshot5.png
[screen6]: screenshots/original/screenshot6.png
[screen7]: screenshots/original/screenshot7.png
[screen1th]: screenshots/thumb/screenshot1.png
[screen2th]: screenshots/thumb/screenshot2.png
[screen3th]: screenshots/thumb/screenshot3.png
[screen4th]: screenshots/thumb/screenshot4.png
[screen5th]: screenshots/thumb/screenshot5.png
[screen6th]: screenshots/thumb/screenshot6.png
[screen7th]: screenshots/thumb/screenshot7.png
