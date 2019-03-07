package io.findify.akka.persistence.s3.snapshot.keys

import akka.persistence.SnapshotMetadata
import io.findify.akka.persistence.s3.snapshot.S3SnapshotConfig

/**
  * KeySupport that can handle pipe characters '|' in a persistenceId and translate the pipes into
  * implicit name spaces in S3.
  *
  * For example: a persistenceId defined as "customerX|projectY|orders" which would normally be translated to the
  * S3 key of "bucketName/prefix/customerX|projectY|orders/snapshot.ss" now becomes the
  * S3 key of "bucketName/prefix/||customerX/projectY/orders||/snapshot.ss"
  *
  * @param settings required to define the snapshot file extension used
  */
class PipePathKeySupport(settings: S3SnapshotConfig) extends SnapshotKeySupport(settings) {
  private val extensionName = settings.extension

  private lazy val Pattern = ("""^(.*/)?(.+)/(\d+)-(\d+)\.""" + extensionName + "$").r

  private def convertPersistIdToS3Format(persistenceId: String) = {
    if (persistenceId.contains('|'))
      s"||${persistenceId.replaceAll("\\|", "/")}||"
    else
      persistenceId
  }

  private def extractSnapshotMeta(key: String) = {
    key match {
      case Pattern(pref, persistenceId: String, sequenceNr: String, timestamp: String) =>
        SnapshotMetadata(persistenceId, sequenceNr.reverse.toLong, timestamp.toLong)
    }
  }

  def snapshotKey(prefix: String, metadata: SnapshotMetadata): String = {
    s"$prefix${convertPersistIdToS3Format(metadata.persistenceId)}/${metadata.sequenceNr.toString.reverse}-${metadata.timestamp}.$extensionName"
  }

  def parseKeyToMetadata(prefix: String, key: String): SnapshotMetadata = {
    key.split("\\|\\|").toSeq match {
      case Seq(prefixPart, persistenceId, snapshot) =>
        extractSnapshotMeta(s"${persistenceId.replaceAll("\\/","|")}$snapshot")
      case Seq(_) =>
        extractSnapshotMeta(key)
    }
  }

  def pathForPersistenceId(prefix: String, persistenceId: String): String = {
    s"$prefix${convertPersistIdToS3Format(persistenceId)}/"
  }
}
