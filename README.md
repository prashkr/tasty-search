# Tasty Search

It is a search engine for searching gourmet food reviews. The data set for the reviews is taken from [Amazon Fine Foods reviews](http://snap.stanford.edu/data/web-FineFoods.html) which has 568,454 reviews given by 256,059 users.

### Running instructions

Clone this repo:
```sh
$ git clone repo-url
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

License
----
MIT