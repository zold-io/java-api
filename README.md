<img src="https://www.zold.io/logo.svg" width="92px" height="92px"/>

[![EO principles respected here](https://www.elegantobjects.org/badge.svg)](https://www.elegantobjects.org)
[![DevOps By Rultor.com](https://www.rultor.com/b/zold-io/java-api)](https://www.rultor.com/p/zold-io/java-api)
[![We recommend IntelliJ IDEA](https://www.elegantobjects.org/intellij-idea.svg)](https://www.jetbrains.com/idea/)

[![mvn](https://github.com/zold-io/java-api/actions/workflows/mvn.yml/badge.svg)](https://github.com/zold-io/java-api/actions/workflows/mvn.yml)
[![Javadoc](https://www.javadoc.io/badge/io.zold/java-api.svg)](https://www.javadoc.io/doc/io.zold/java-api)
[![PDD status](https://www.0pdd.com/svg?name=zold-io/java-api)](https://www.0pdd.com/p?name=zold-io/java-api)
[![Maven Central](https://img.shields.io/maven-central/v/io.zold/java-api.svg)](https://maven-badges.herokuapp.com/maven-central/io.zold/java-api)
[![License](https://img.shields.io/badge/license-MIT-green.svg)](https://github.com/zold-io/java-api/blob/master/LICENSE.txt)
[![Test Coverage](https://img.shields.io/codecov/c/github/zold-io/java-api.svg)](https://codecov.io/github/zold-io/java-api?branch=master)
[![SonarQube](https://img.shields.io/badge/sonar-ok-green.svg)](https://sonarcloud.io/dashboard?id=io.zold%3java-api)
[![Hits-of-Code](https://hitsofcode.com/github/zold-io/java-api)](https://hitsofcode.com/view/github/zold-io/java-api)

Java API for Zold. Ruby API is in [zold-io/zold](https://github.com/zold-io/zold).

All you need is this:

```xml
<dependency>
  <groupId>io.zold</groupId>
  <artifactId>java-api</artifactId>
  <version><!-- Get it here: https://github.com/zold-io/java-api/releases --></version>
</dependency>
```

Java version required: 1.8+.

**We recommend you read the Zold [whitepaper](https://papers.zold.io/wp.pdf) in order to understand the concepts behind this API.**

First, you find a wallet in a directory of wallets:

```java
final Wallets wallets = new WalletsIn(new File("/tmp/wallets"));
final Wallet wallet = new Filtered<>(
    w -> "9999888877776666".equals(w.id()),
    wallets
).iterator().next();
```

Then, you pull the network's version of the wallet and merge it into yours:

```java
final Wallet merged = wallet.merge(
    network.pull(wallet.id())
);
```

Then, you make a payment:

```java
final long amount = 1999L;  //zents
final char bnf = (char) 12345; //beneficiary wallet's ID
wallet.pay(amount, bnf);
```

Finally, you push it:

```java
network.push(wallet);
```

That's it.

## How to contribute?

Just fork the repo and send us a pull request.

Make sure your branch builds without any warnings/issues:

```
mvn clean install -Pqulice
```

## Requirements

These are the requirements for this API.

**Note:** The original whitepaper on *zold* can be found [here](https://www.zold.io/). The whitepaper and the documentation found at www.zold.io serve as the highest authority on the subject of *zold*. The following requirements are condensed versions of the points expressed in the aforementioned docs as they relate to the scope of this project:

* Maintain a wallet in structured textual format, within which is a ledger that contains transactions for that wallet.
* Make payments:
  * Taxes according to fixed formula
  * Payments to other wallets
  * Sign with RSA key
  * Push to network
* Receive payments:
  * Pull the paying wallet from the network
  * Merge the copies of the paying wallet with our own copy
* We need to refresh our local database of network nodes by querying the network.
* We must implement with Java 8
* Non-functional requirements were not made, but we expect
  * Flawless concurrency
  * "Decent" performance
  * Design must respect the principles of [elegant objects](www.elegantobjects.org)
  * Design must resemble the design of the original [ruby API](https://github.com/zold-io/zold)


## Decisions and Alternatives

* `javax-json` is a Java API that can parse and also write JSON. As part of the EE7 spec, it is stable, well established, and is the standard. We can use it to write to our local database file. Alternatives are google's [gson](https://github.com/google/gson), [JSON-java](https://github.com/stleary/JSON-java), [jackson-databind](https://github.com/FasterXML/jackson-databind/), and many others.
* The standard `java.security` package contains everything we need to import keystores and sign/encrypt messages with RSA keys. There are lots of examples on the internet on how to use it. I am not aware of any other popular alternative out there.
* `cactoos-http` is a new object-oriented HTTP API under current development as part of project `cactoos` in Zerocracy. It is expected to be ready by the time this API goes to production. Other alternatives are Apache's [http client](https://hc.apache.org/httpcomponents-client-4.5.x/index.html) (not object-oriented), `jcabi-http` (too many dependencies), and many others.
* Our API will consist of our core classes that will communicate with the network using cactoos-http + javax.json, a local storage (`nodes.json`) for persistence of remote node data, will use the `java.security` package when signing the transactions, and will expect wallet files to have the `.z` extension ([discuss](https://github.com/zold-io/zold/issues/164)) and conform to the format specified in the whitepaper:

![architecture](http://www.plantuml.com/plantuml/proxy?src=https://raw.githubusercontent.com/zold-io/java-api/master/src/site/resources/plantuml/architecture.plantuml)

## Concerns

* `cactoos-http` is designed according to the principles of EO, although it still has mayor hurdles to overcome (eg. see [#62](https://github.com/yegor256/cactoos-http/issues/62)).
* `javax.json` will parse data from the zold network and write our local database file in structured JSON format. Its usage is very simple, although we will probably have to be careful with regards to concurrent access to the file.
* `java.security` runtimes can handle RSA and MD5 on all platforms.

## Assumptions

* `cactoos-http` will reach the maturity level necessary to support our requirements
* We can flawlessly manage synchronized access to our local database file

## Risks

* The `cactoos-http` project might not obtain the resources to reach maturity, or may not reach maturity for some other reason.
* Our bottleneck will be reading/writing our local database file. We might not be able to manage a "heavy" throughput.
