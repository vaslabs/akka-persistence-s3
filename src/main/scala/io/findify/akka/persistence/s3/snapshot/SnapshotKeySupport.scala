package io.findify.akka.persistence.s3.snapshot

import akka.persistence.SnapshotMetadata

trait SnapshotKeySupport {

  val extensionName: String

  lazy val Pattern = ("""^(.*/)?(.+)/(\d+)-(\d+)\.""" + extensionName + "$").r

  final def snapshotKey(prefix: String, metadata: SnapshotMetadata): String = {
    s"$prefix${metadata.persistenceId}/${metadata.sequenceNr.toString.reverse}-${metadata.timestamp}.$extensionName"
  }

  def parseKeyToMetadata(prefix: String, key: String): SnapshotMetadata = {
    val br=1
    key match {
      case Pattern(pref, persistenceId: String, sequenceNr: String, timestamp: String) =>
        SnapshotMetadata(persistenceId, sequenceNr.reverse.toLong, timestamp.toLong)
    }
  }

  def prefixFromPersistenceId(prefix: String, persistenceId: String): String = s"$prefix$persistenceId/"
}
