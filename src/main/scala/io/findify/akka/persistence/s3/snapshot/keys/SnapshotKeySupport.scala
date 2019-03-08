package io.findify.akka.persistence.s3.snapshot.keys

import akka.persistence.SnapshotMetadata
import io.findify.akka.persistence.s3.snapshot.S3SnapshotConfig

/**
  * Template for Class responsible for generating and parsing the S3 keys of
  * the objects stored on S3 to represent snapshots
  *
  * The instance used by the plugin can be configured by setting the following
  * key in the application.conf file:
  *
  * s3-snapshot-store.key-generator-class = "io.findify.akka.persistence.s3.snapshot.keys.DefaultSnapshotKeySupport"
  *
  * @param settings settings taken from the config file
  */
abstract class SnapshotKeySupport(settings: S3SnapshotConfig) {

  /**
    * Generate an fully qualified s3 key (with full path structure)
    *
    * @param prefix the key prefix to use for all snapshots
    * @param metadata the meta data for this particular snapshot
    * @return a fully qualified S3 key
    */
  def snapshotKey(prefix: String, metadata: SnapshotMetadata): String

  /**
    * Deconstruct a snapshot's S3 key back into a SnapshotMetadata object
    *
    * @param prefix the key prefix to use for all snapshots
    * @param key the fully qualified S3 key for a snapshot
    * @return the meta data for this particular snapshot
    */
  def parseKeyToMetadata(prefix: String, key: String): SnapshotMetadata

  /**
    * Construct the S3 path where all snapshot objects will be stored
    *
    * @param prefix the key prefix to use for all snapshots
    * @param persistenceId the akka persistenceId of the entity being snapshotted
    * @return a string representing the path where all snapshot objects are stored
    */
  def pathForPersistenceId(prefix: String, persistenceId: String): String
}
