##WORK IS STILL IN PROGRESS!!

Below I document some of the thought processes that go into each iteration of this applicaiton. When the application is complete, I will remove the "WORK IN PROGRESS!!" banner.

## Etsy HomeWork

The following will explain the choices made throughout the application, please feel free to follow up on any questions you may have.

## Data flow

The app in question attempts to follow an MVVM structure while using a REST technique for local data caching.

# REST

The hardest problem to solve for developers is how to deal with data and the Activity life cycle. The first problem is how to resolve and synchronize a background threads running network requests after an Activity has been destroyed and another has been craeted. The other how to handle the resulting data between rotations without using too much memory and without making too many mistakes while keeping track of that data.

The most elegant solution I've come across is the use of REST. We use a local database to store the data and serve it to the views. When the views are created, they simply register for an Observable on a URI that represents their data, query the latest data and wait for notifications that let them know their data is available. At which point they re-query the data set in question.

Assuming that data retrieved from the local database is always up to date this seemingly solves the problem.

The trick is to use Loaders and Observables. Loaders are an Android construct that are designed to work with the LifeCycle. They go off of the UI thread to retrieve data from a database and return to the UI thread with the results. They also know how to attach and detach from the same Activity in case of a rotation/life cycle event.


The database allows for three things:

1. Loaders are designed to work with ContentProviders and hence Databases.

2. Databases offer a simple way of caching the data so that it is accessible between Activity life cycles.

3. More importantly, they are thread safe and can complete atomic read and writes.


Point three means that we can get as intricate as we want with the distribution of our work between processes but still have a simple and clean way to read and write data. This also means that we can create happens-before relationships between our data flow. There are two good examples of using the atomic read and writes in the code base:

1. Before we run a network call, we atomically check to see if there is already a network call running. If there is we exit, if there is not, we write (in that same atomic block) that a network call is running and then run the network call. This ensures that in case we run several threads that are trying to run the same network request, that only one gets run.

2. When we receive data, we atomically delete all of the previous data and write in the new data. This ensures that if an loader reads the database while a new set of data is coming in, that it only sees a complete set of data and not the intermediate state of the delete and write mentioned previously.


# MVVM

MVVM can be greatly beneficial if used correctly. When compared to MVC, I find that MVVM can greatly simplify the code at the Fragment and Activity level. As a rule of thumb, I only try to have logic that pertains to application flow, animations and UI/UX state at the Activity and Fragment levels. The majority, if not all, of the business logic is done inside SQL.

The way I accomplish this in the current code base is by using tables strictly for storing data that comes from the network requests. By rul of thumb I store them in such a way that if I need to recreate the entire network request structure and its relationships, I can.

SQL Views are then used to query these tables appropriately and create data that is strictly designed to be consumed by the Views that it will be binding to. More importantly, the job of this SQL view is to prepare the data in such a way that there is almost no logic to be done from the point of the View.

For example:

In the app, we have have a loader that connects to and displays the data for search results from the SearchResultsView. The SearchResultsView is set up so that it contains all of the results associated with their respective queries. The view can have search results for both dogs and cats, which gives the ability to the Loader to simply say that it wants results for cats.

The data is queried and sent down to the adapter where each View is paired with a row of the results. The adapter simply binds the data to the corresponding View and the work is done.

The beauty of this is that you are now free to modify either the Model, the View Model or the view separately from one another without breaking the flow of the application. I find that this really helps for growing projects and new comers to Android, as you can give them very confined problems to solve without having to know about the entire stack.


## General considerations when choosing libraries, flows and techniques


# Parsing

For memory and efficiency proposes I have used GSON. A library that SAX parses the network calls. JSONObject is a DOM parser that always forms contiguous Strings in memory that represent the JSON. I've seen JSON parse large sets of data and fragment memory significantly enough that it causes OutOfMemory Exceptions.

GSON allows you to build objects (with easy to use annotations) in memory without using a contiguous String to represent the JSON, which considerably cuts down on the possibility of Fragmentation.

The alternative is to parse the data manually and store it into the data base as you go, avoiding putting the entire model into memory at once. In the case of this application, and most applications, the time it would have taken to write the parsing manually would not make the trade off of less memory consumption worth it.

# Logging

In the application above, the REST / MVVM framework built proved difficult to debug as there were several threads running at the same time. The simplest way of testing the flow in a production environment was to write logs and observe their order afterwards.

A neat trick I picked up is logging the line that you are on automatically. See Debug.java for further details, but a simple Debug.log("foreceRequest: " + forceRequest); can give you far more information:

03-30 23:35:24.067  16790-16813/etsy.homework D/Etsy﹕ BG - [etsy.homework.providers.EtsyContentProvider.launchTask(EtsyContentProvider.java:168)] forceRequest: false


The benefits of logging in this manner:

1. The logging structure is the same at all times making it easy to read and understand the flow between logs
2. Greping becomes a lot more fun as you can grep per package, per class name per application etc...


# Known issues

I'm not quite happy with the way I'm using my Constants, although each constant technically is in the class that it represents, it is getting difficult, especially with URIS, SCHEMAS, and AUTHORITIES, to keep track of where each one should be.

I'm currently not using the selection and selectionArgs[] in the Loaders, nor am I using the uri paths to pass in unique keys, instead I am using query parameters and parsing them as needed. The android system recognizes unique paths when it comes time to triggering and responding to notifications, so there will be some places where I needlessly restart the content loader.

The keywords are not being saved between Activity re-creations.




