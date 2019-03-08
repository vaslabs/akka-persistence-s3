package io.findify.akka.persistence.s3.snapshot.keys

import akka.persistence.SnapshotMetadata
import io.findify.akka.persistence.s3.snapshot.S3SnapshotConfig

/**
  * Default implementation of SnapshotKeySupport.
  *
  * Constructs S3 keys with the format of <prefix>/<sequence-nbr>-<timestamp>
  *
  * @param settings settings taken from the config file
  */
class DefaultSnapshotKeySupport(settings: S3SnapshotConfig) extends SnapshotKeySupport(settings) {
  private val extensionName = settings.extension

  private lazy val Pattern = ("""^(.*/)?(.+)/(\d+)-(\d+)\.""" + extensionName + "$").r

  def snapshotKey(prefix: String, metadata: SnapshotMetadata): String = {
    s"$prefix${metadata.persistenceId}/${metadata.sequenceNr.toString.reverse}-${metadata.timestamp}.$extensionName"
  }

  def parseKeyToMetadata(prefix: String, key: String): SnapshotMetadata = {
    key match {
      case Pattern(pref, persistenceId: String, sequenceNr: String, timestamp: String) =>
        SnapshotMetadata(persistenceId, sequenceNr.reverse.toLong, timestamp.toLong)
    }
  }

  def pathForPersistenceId(prefix: String, persistenceId: String): String = s"$prefix$persistenceId/"
}
