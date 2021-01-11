## Android Integration


### Installation

To install library you need to add Tradedoubler Maven Repository to your project build.gradle file:


```
allprojects {
   repositories {
       maven { 
        url 'https://pkgs.dev.azure.com/tradedoubler-sdk/tradedoubler-android-sdk/_packaging/release-repo/maven/v1'
        name 'release-repo' 
    }
   }
}


```


The next step is adding the library as the application dependency:


```
dependencies {
   implementation "com.tradedoubler:android-sdk:1.0.0"
}
```



### Set Min SDK Version

The library requires the use of Android from version 5.0 or SDK from version 21:


```
android {
   defaultConfig {
       minSdkVersion 21
   }
}
```



### Additional Transient Dependencies


#### Play Services Ads Identifier

SDK uses Google Play services to have access to Android Advertising Id that is recommended unique identifier for advertising purposes. If for some reason (for example, if an app is distributed outside Google Play store or Google Play services are outdated on device), SDK will fallback to the user Android Id as an unique device identifier.

[https://developers.google.com/android/reference/com/google/android/gms/ads/identifier/AdvertisingIdClient](https://developers.google.com/android/reference/com/google/android/gms/ads/identifier/AdvertisingIdClient)


#### Play Install Referrer Library

SDK uses Install Referrer library services to securely retrieve referral content from Google Play.

It’s required to retrieve the TDUID from the application install URL.

[https://developer.android.com/google/play/installreferrer/library](https://developer.android.com/google/play/installreferrer/library)


### **Manifest Permissions**

To utilize tracking features your app needs to have access to the internet. To achieve that, you must add permissions to the Android Manifest file of your project.


```
<uses-permission android:name="android.permission.INTERNET"/> 
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
```



### Initialize SDK

Enable the Tradedoubler SDK in your [Application](https://developer.android.com/reference/android/app/Application.html)’s onCreate() method. In addition, you need to initialize the library with parameters organizationId and secretCode provided by Tradedoubler.


```kotlin
class App : Application() {

   override fun onCreate() {
       super.onCreate()

       TradeDoublerSdk.create(applicationContext)
       TradeDoublerSdk.getInstance().organizationId = "your_organization_id"
       TradeDoublerSdk.getInstance().secretCode = "you_secret_code"
   }
}
```



### Identify User

To perform tracking you may need to identify the user. By default, all identifiers are not used directly. SDK is working with SHA-256 digest of provided identifiers.


#### Android Advertising Id

The AAID is used as an advertising identifier by default. If for some reason the AAID is not available, SDK will fallback to use Android Id.


#### User Email

If a user is logged, you can set the user email as an additional identifier. This will help to properly perform cross device tracking. You can achieve this by using userEmail property.


```kotlin
TradeDoublerSdk.getInstance().userEmail = "email@test.com"
```


Please remember to clear this value after user logout.


```kotlin
TradeDoublerSdk.getInstance().userEmail = null
```


If both identifiers are present, most of the tracking events are sent for each identifier.


 


### Get TDUID

To provide proper sale and advertising tracking from affiliate links we need to provide TDUID retrieved from advertising URLs. There are two main types of advertising URLs that can be used.


#### Installation URL

SDK uses Install Referrer library services to securely retrieve referral content from Google Play.

It’s required to retrieve TDUID from the application install URL. 

Caution Referrer URL used to install app needs to have specific format:

[_https://play.google.com/store/apps/details?id=your.packagename<b>&referrer=tduid%3Dtduid_value</b>_](https://play.google.com/store/apps/details?id=your.packagename&referrer=tduid%3Dtduid_value)

Only **referrer** parameter parts from query params are available for the application. In most cases it contains encoded nested query string, so in given example SDK will have access to referral URL with value **tduid=tduid_value**

By default retrieving TDUID from the installation URL is enabled, you can disable it by using **useInstallReferrer** property. This can be helpful if your app install links are different than default ones or you need to provide different extraction of TDUID parameters.


```kotlin
class App : Application() {

   override fun onCreate() {
       super.onCreate()

       TradeDoublerSdk.create(applicationContext)
	//setup here
//...
       TradeDoublerSdk.getInstance().useInstallReferrer = false
   }
 }
```


In such a case you can extract and provide TDUID value directly to the SDK instance.


```kotlin
private fun setTduid(){
    TradeDoublerSdk.getInstance().tduid = "tduid_value"
}
```


To learn more about testing installation referrers, check the Testing and Troubleshooting section.


#### Open App URL or Intent

[https://developer.android.com/training/app-links](https://developer.android.com/training/app-links)

On Android we have few possibilities to open the app with additional parameters using URLs, custom schema or deeplinks. As flow for each option could be different, SDK contains a set of methods that can help with TDUID retrieval. Each method from this set will try to retrieve TDUID from a given parameter. If TDUID is found, it is saved inside SDK storage. In most cases methods should be used in your start Activity or in Activity that is set as a target component for opening app links. TDUID is expected to be one of the query parameters of your app open URL.


```kotlin
class MainViewActivity: AppCompatActivity() {

   override fun onCreate(savedInstanceState: Bundle?) {
       super.onCreate(savedInstanceState)
       TradeDoublerSdk.getInstance().retrieveAndSetTduidFromIntent(intent)
    //or
    TradeDoublerSdk.getInstance().retrieveAndSetTduidFromUri(intent.data)
   }
}
```


By default TDUID is extracted from query parameter of URL:


```
https://www.your.domain.com?tduid=3e28242cd1c67ca5d9b19d2395e52941
```


In case of non standardized URL, it is developer responsibility to extract TDUID and set it to SDK instance.

To learn more about app links, check the Testing and Troubleshooting section.


### Disable Tracking

In some cases, like testing the development version of an application, we may not want to track anything. In that case we may use property **isTrackingEnabled** to switch off and on tracking events in application:


```kotlin
class App : Application() {

   override fun onCreate() {
       super.onCreate()

       TradeDoublerSdk.create(applicationContext)
	//setup here
//...
       TradeDoublerSdk.getInstance().isTrackingEnabled = false
   }
}
```



### Enable Logs

By default, SDK prints only error messages like missing parameters or exceptions. In case of problems there is possibility to enable more detailed logs by changing value of isLoggingEnabled property:


```kotlin
class App : Application() {

   override fun onCreate() {
       super.onCreate()
       TradeDoublerSdk.create(applicationContext)
	//setup here
//...
       TradeDoublerSdk.getInstance().isLoggingEnabled = true
   }
}
```



## Usage


### Tracking App Installation


<table>
  <tr>
   <td>Name
   </td>
   <td>Info
   </td>
   <td>Is required
   </td>
  </tr>
  <tr>
   <td>appInstallEventId
   </td>
   <td>Id of installation event in Tradedoubler system
   </td>
   <td>true
   </td>
  </tr>
</table>


SDK provides a method to track installation. It should be invoked in the onCreate method of the Application class after configuring the SDK instance. No additional check is required as the framework is aware about connectivity and only one install event will be sent. To track installation you need to provide an identifier of the event used to track installation on Tradedoubler side.


```kotlin
class App : Application() {

   override fun onCreate() {
       super.onCreate()

       TradeDoublerSdk.create(applicationContext,initClient())
       //setup here
       //...

       TradeDoublerSdk.getInstance().trackInstall("install_app_event_id")
   }
}
```



### Tracking the Opening of the Application

SDK provides a method to track installation. Depending on your intentions it should be invoked in the onCreate method of the Application class after configuring the SDK instance or on the first creation of your entry activity. 

By default, closing all activities will not kill the application, it will live in the background, so in some cases tapping the launcher icon will not create a new application but will bring application from the background to the foreground and because of that tracking opening app on Application class level may not be sufficient for your needs.


```kotlin
class App : Application() {

   override fun onCreate() {
       super.onCreate()

       TradeDoublerSdk.create(applicationContext)
       //setup here
       //...
       TradeDoublerSdk.getInstance().trackOpenApp()
   }
}
```


Second possibility is to track the opening app on your app start activity level. It gives you more flexibility in terms of control when events should be tracked.


```kotlin
class MainViewActivity: AppCompatActivity() {

   override fun onCreate(savedInstanceState: Bundle?) {
       super.onCreate(savedInstanceState)


       if(savedInstanceState == null){
           TradeDoublerSdk.getInstance().trackOpenApp()
       }
   }
}
```



### Track Leads

SDK provides a method for tracking leads.:


```kotlin
TradeDoublerSdk.getInstance().trackLead("lead_event_id", "lead_id")
```


Parameters


<table>
  <tr>
   <td>Name
   </td>
   <td>Info
   </td>
   <td>Is required
   </td>
  </tr>
  <tr>
   <td>leadEventId
   </td>
   <td>Id of event in Tradedoubler system
   </td>
   <td>true
   </td>
  </tr>
  <tr>
   <td>leadId
   </td>
   <td>Id of lead (from Advertiser / Publisher)
   </td>
   <td>true
   </td>
  </tr>
</table>



### Track Sales

SDK provides a method for tracking sales:

Parameters


<table>
  <tr>
   <td>Name
   </td>
   <td>Info
   </td>
   <td>Is required
   </td>
  </tr>
  <tr>
   <td>saleEventId
   </td>
   <td>Id of event in Tradedoubler system
   </td>
   <td>true
   </td>
  </tr>
  <tr>
   <td>orderNumber
   </td>
   <td>Unique order number (from Advertiser/ Publisher)
   </td>
   <td>true
   </td>
  </tr>
  <tr>
   <td>orderValue
   </td>
   <td>value of order
   </td>
   <td>true
   </td>
  </tr>
  <tr>
   <td>currency
   </td>
   <td>Currency of the order (ISO-4217)
   </td>
   <td>false
   </td>
  </tr>
  <tr>
   <td>voucherCode
   </td>
   <td>Voucher code affiliated with organization
   </td>
   <td>false
   </td>
  </tr>
  <tr>
   <td>reportInfo
   </td>
   <td>Info about basket
   </td>
   <td>false
   </td>
  </tr>
</table>



```kotlin
fun trackSale(){
   val orderNumber = "order_id"
   val reportInfo = ReportInfo(
       listOf(
           ReportEntry("25","bike", 21.0,3),
           ReportEntry("453","doll", 3.5,12)
       )
   )
   val price = reportInfo.getOrderValue()
   TradeDoublerSdk.getInstance().trackSale(saleEventId, orderNumber, price, "PLN","13131313", reportInfo)
}
```



### Track Sales PLT

SDK provides a method for product level sales tracking:

Parameters


<table>
  <tr>
   <td>Name
   </td>
   <td>Info
   </td>
   <td>Is required
   </td>
  </tr>
  <tr>
   <td>saleEventId
   </td>
   <td>Id of event in Tradedoubler system, default 51
   </td>
   <td>true
   </td>
  </tr>
  <tr>
   <td>orderNumber
   </td>
   <td>Unique order number (from Advertiser/ Publisher)
   </td>
   <td>true
   </td>
  </tr>
  <tr>
   <td>currency
   </td>
   <td>Currency of the order (ISO-4217)
   </td>
   <td>false
   </td>
  </tr>
  <tr>
   <td>voucherCode
   </td>
   <td>Voucher code affiliated with organization
   </td>
   <td>false
   </td>
  </tr>
  <tr>
   <td>reportInfo
   </td>
   <td>Info about basket
   </td>
   <td>true
   </td>
  </tr>
</table>



```kotlin
fun trackSalePlt(){
   val orderNumber = "order_number"
   val basketInfo = BasketInfo(
       listOf(
           BasketEntry("3408","1323","cookies", 23.0,5),
           BasketEntry("3168","5389","milk", 3.0,25)
       )
   )
   TradeDoublerSdk.getInstance().trackSalePlt(saleEventId, orderNumber, "PLN","13131313", basketInfo)
}

```




## Testing and Troubleshooting

Testing some parts of flow involves interaction with external components and for security reasons there could be some restrictions.


### Installing the Application from Advertising URL

The app uses Google Play install referrer library to track referral URL:

[https://developer.android.com/google/play/installreferrer](https://developer.android.com/google/play/installreferrer)

Known limitations:


*   Referral URL is only available if a user installs an app from Google Play app. Installing an advertising URL from a web page will not provide a proper referrer URL to the app.
*   Referrer tracking for G Suite accounts (work/school) doesn’t work by design.
*   Referrer tracking information may be available up to 90 days.

You can test referral tracking for you application using Google Play beta program:

[https://support.google.com/googleplay/android-developer/answer/9845334?hl=en](https://support.google.com/googleplay/android-developer/answer/9845334?hl=en)

To install app you need to provide advertising URL with parameter **referrer:**

_[https://play.google.com/store/apps/details?id=your.packagename&referrer=tduid%3Dtduid_value&param2=1](https://play.google.com/store/apps/details?id=your.packagename&referrer=tduid%3Dtduid_value)_

Only **referrer** parameter parts from query params are available for the application. In most cases it contains encoded nested query string, so in a given example SDK will have access to a referral URL with value **tduid=tduid_value.**

SDK expects to have a TDUID parameter inside the referral URL. In case your referral URL has another format you can always extract TDUID manually and set it to the SDK instance.


### Opening the Application from Advertising URL

[https://developer.android.com/training/app-links](https://developer.android.com/training/app-links)

[https://developer.android.com/studio/write/app-link-indexing](https://developer.android.com/studio/write/app-link-indexing)

[https://developer.android.com/training/app-links/deep-linking](https://developer.android.com/training/app-links/deep-linking)

As Android provides a lot of flexibility using aplinks, creating custom schemas and providing different target components for different actions, there is no way for SDK to handle retrieving TDUID form app open URL in a fully automatic way.

For the basic usage you need to follow examples for Android documentation and create an intent filter for activity that should be opened after handling the URL by your app. In this particular example we will register activity to handle link from <code>[www.your.domain.com](http://www.your.domain.com)</code>


```
<activity android:name="com.tradedoubler.MainViewActivity">
   <intent-filter>
       <action android:name="android.intent.action.VIEW" />
       <category android:name="android.intent.category.DEFAULT" />
       <category android:name="android.intent.category.BROWSABLE" />
       <data android:scheme="https" android:host="www.your.domain.com" android:pathPattern=".*"/>
   </intent-filter>
</activity>
```


In the next step you should handle extracting parameters from intent that will start your activity.

In the following example we will use **retrieveAndSetTduidFromIntent** that extracts TDUID from intent and sets its value to SDK instance for further use. Also, we use retrieved value to show information toast with extracted TDUID value:


```kotlin
class MainViewActivity: AppCompatActivity() {

   override fun onCreate(savedInstanceState: Bundle?) {
       super.onCreate(savedInstanceState)

       TradeDoublerSdk.getInstance().retrieveAndSetTduidFromIntent(intent)?.also {
           Toast.makeText(this@MainViewActivity, "new tduid: $it",Toast.LENGTH_LONG).show()
       }
   }
}
```


If the setup is correct you should now be able to simulate opening the URL from the terminal.


```
adb shell am start -W -a android.intent.action.VIEW -d "https://www.your.domain.com?tduid=3e28242cd1c67ca5d9b19d2395e52941"
```


By default you should see a prompt about opening the URL in your browser or application.

If you don’t see a prompt and the URL is always opened in the browser, something may be misconfigured or your browser may be set as the default app for this type of URLs. In this case please check default app settings.
