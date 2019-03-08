package io.findify.akka.persistence.s3.snapshot

import com.typesafe.config.Config

class S3SnapshotConfig(config: Config) {
  val bucketName = config getString "bucket-name"
  val extension = config getString "extension"
  val prefix = config getString "prefix"
  val maxLoadAttempts = config getInt "max-load-attempts"
  val keySupportClass = config getString "key-generator-class"
}
