# Tasty Search

It is a search engine for searching gourmet food reviews. The data set for the reviews is taken from [Amazon Fine Foods reviews](http://snap.stanford.edu/data/web-FineFoods.html) which has 568,454 reviews given by 256,059 users.
You can checkout the live demo [here](http://prashkr.com/tastysearch/).

### Running instructions

Clone this repo:
```sh
$ git clone https://github.com/prashkr/tasty-search.git
```
Before running the following command make sure you have the dataset file (Amazon reviews) in the resources folder.

Now move into the cloned directory and run:
```sh
$ ./gradlew run
```
**Make sure you have JDK 8 installed**

Your application will be up and running on the port specified in the tastysearch.yml file which is the global configuration file for our application. Access your application as follows:
```sh
http://localhost:[port]/tastysearch
```

### Dropwizard Configuration
The configuration has the following fields which can be changed according to needs.
* **port** : port on which you want to run your application
* **sampleSize** : number of samples to collect out of all the reviews
* **indexType** : type of index to use. 1 for Hash map index. 2 for Radix tree index
* **resultSize** : number of results to emit
* **fileName** : name of the data file
*  **testSetSize** : number of queries to generate for load test
*   **maxTestTokens** : max number of tokens in the generated query (for load test)
*   **testSetFileName** : name of the test set file

### Tools, Libraries and Frameworks Used
* [Dropwizard](https://github.com/dropwizard/dropwizard)
* [AngularJS](https://github.com/angular/angular.js)
* [Concurrent-Trees](https://github.com/npgall/concurrent-trees)

### Load Test Results

Entries in the table correspond to the tuple (Average Latency, Throughput in requests/sec). Ran the tests for 10 minutes each.

|              **Structure \ Threads**             |  **1 Thread**  |  **10 Threads** |  **20 Threads**  |
|:------------------------------------------------:|:--------------:|:---------------:|:----------------:|
|                  **Radix Tree**                  | (2ms, 452/sec) | (9ms, 1064/sec) | (16ms, 1177/sec) |
|                   **Hash Map**                   | (1ms, 470/sec) | (8ms, 1152/sec) | (15ms, 1295/sec) |
|              **Concurrent Hash Map**             | (1ms, 578/sec) | (7ms, 1317/sec) | (13ms, 1418/sec) |
| **Hash Map with Heap Optimization during query** | (1ms, 640/sec) | (6ms, 1403/sec) | (10ms, 1837/sec) |
|              **Brute Force Search**              | (167ms, 6/sec) |        NA       |        NA        |

License
----
MIT