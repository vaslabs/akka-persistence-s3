# Amazon S3 Snapshot Store Plugin for Akka Persistence

An [Akka Persistence](http://doc.akka.io/docs/akka/2.4.1/scala/persistence.html) snapshot store backed by
[Amazon S3](https://aws.amazon.com/s3/)

**Please note that this module only provides Snapshot plugin.**


# Installation

This package is available for Scala 2.11/2.12 (on Java 8). To install using SBT, add these
 statements to your `build.sbt`:

    libraryDependencies += "io.findify" %% "akka-persistence-s3" % "0.1.1"

On maven, update your `pom.xml` in the following way:
```xml
    // add this entry to <dependencies/>
    <dependency>
        <groupId>io.findify</groupId>
        <artifactId>akka-persistence-s3_2.12</artifactId>
        <version>0.1.1</version>
        <type>pom</type>
    </dependency>
```


# Configuration

```
akka.persistence.snapshot-store.plugin = "s3-snapshot-store"

s3-client{
  aws-access-key-id = <your key>
  aws-secret-access-key = <your secret>
  region = "us-west-2"
  endpoint = "default"
  options {
    path-style-access = false
    chunked-encoding-disabled = false
  }
}

s3-snapshot-store {
  bucket-name = "snapshot"
  prefix = "some/dir/" 
  extension = "ss"
  max-load-attempts = 3
}
```

# S3 key structure

A snapshot object is saved with following key format.

```
<persistenceId>/<reverse sequenceNr>-<timestamp>.<extension>
```

Note that sequenceNr is reversed to optimize performance for partitioning. See http://docs.aws.amazon.com/AmazonS3/latest/dev/request-rate-perf-considerations.html