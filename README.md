<img src="http://www.zold.io/logo.svg" width="92px" height="92px"/>

[![Donate via Zerocracy](https://www.0crat.com/contrib-badge/CAZUREFND.svg)](https://www.0crat.com/contrib/CAZUREFND)

[![EO principles respected here](http://www.elegantobjects.org/badge.svg)](http://www.elegantobjects.org)
[![Managed by Zerocracy](https://www.0crat.com/badge/CAZUREFND.svg)](https://www.0crat.com/p/CAZUREFND)
[![DevOps By Rultor.com](http://www.rultor.com/b/zold-io/java-api)](http://www.rultor.com/p/zold-io/java-api)
[![We recommend IntelliJ IDEA](http://www.elegantobjects.org/intellij-idea.svg)](https://www.jetbrains.com/idea/)

[![Build Status](https://travis-ci.org/zold-io/java-api.svg?branch=master)](https://travis-ci.org/zold-io/java-api)
[![Javadoc](http://www.javadoc.io/badge/io.zold/java-api.svg)](http://www.javadoc.io/doc/io.zold/java-api)
[![PDD status](http://www.0pdd.com/svg?name=zold-io/java-api)](http://www.0pdd.com/p?name=zold-io/java-api)
[![Maven Central](https://img.shields.io/maven-central/v/io.zold/java-api.svg)](https://maven-badges.herokuapp.com/maven-central/io.zold/java-api)
[![License](https://img.shields.io/badge/license-MIT-green.svg)](https://github.com/zold-io/java-api/blob/master/LICENSE.txt)

[![Test Coverage](https://img.shields.io/codecov/c/github/zold-io/java-api.svg)](https://codecov.io/github/zold-io/java-api?branch=master)
[![SonarQube](https://img.shields.io/badge/sonar-ok-green.svg)](https://sonarcloud.io/dashboard?id=io.zold%3java-api)

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

First, you find a wallet in a directory of wallets:

```java
Wallets wallets = new WalletsInDir(new File("/tmp/wallets"));
Wallet wallet = wallets.find("9999888877776666");
```

Then, you pull it:

```java
wallet.pull();
```

Then, you check its balance:

```java
Amount balance = wallet.balance();
assert balance.equals(new Amount(50.0d));
```

Then, you make a payment:

```java
String key = "jfUJklaljsios....JKLJLSksjd89os"; // private RSA key
String invoice = "JhYPOKNj@bbbbccccddddeeee";
Amount amount = new Amount(19.99d);
String details = "Thank you for the services!"
int txn = wallet.pay(key, invoice, amount, details);
```

Finally, you push it:

```java
wallet.push();
```

That's it.

## How to contribute?

Just fork the repo and send us a pull request.

Make sure your branch builds without any warnings/issues:

```
mvn clean install -Pqulice
```

## License (MIT)

Copyright (c) 2018 Yegor Bugayenko

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included
in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

## Requirements

These are the requirements for this API.

**Note:** The original whitepaper on *zold* can be found [here](https://www.zold.io/). The whitepaper and the documentation found at www.zold.io serve as the highest authority on the subject of *zold*. The following requirements are condensed versions of the points expressed in the aforementioned docs as they relate to the scope of this project:

* Maintain a wallet in structured textual format, within which is a ledger that contains transactions for that wallet.
* Pull wallets from an ad-hoc network of nodes and merge the transactions returned for the wallet with the local copy
* Transactions:
  * Signed with RSA keys
  * For payments made to *other* wallets, the local node adds a transaction with the amount in negative, with a `bnf` (beneficiary wallet ID) value of the target wallet. This change to this wallet then needs to be pushed to the network.
  * To receive a payment, another node needs to make the payment as described above. The local wallet then needs to *pull* this new version of your wallet with the added transaction from the network.
* We need to pay taxes according to a formula. This transaction needs to be pushed to a network node selected according to an established criteria.
* We need to refresh our local database of network nodes by querying the network.
* We only need to implement an API for making and receiving transactions.
* We must implement with Java 8
* No non-functional requirements were made, but I expect
  * We must handle concurrency flawlessly
  * We must have "decent" performance
  * Our designs must respect the principles of [elegant objects](www.elegantobjects.org)

## Decisions and Alternatives

* `javax-json` is a Java API that can parse and also write JSON. As part of the EE7 spec, it is stable, well established, and is the standard. We can use it to read JSON data from the zold network, and write to our local database file. Alternatives are google's [gson](https://github.com/google/gson), [JSON-java](https://github.com/stleary/JSON-java), [jackson-databind](https://github.com/FasterXML/jackson-databind/), and many others.
* The standard `java.security` package contains everything we need to import keystores and sign/encrypt messages with RSA keys. There are lots of examples on the internet on how to use it. I am not aware of any other free alternative out there.
* Our API will consist of:
  * A `Wallet`
  * The wallet will 
