package io.findify.akka.persistence.s3.snapshot.keys

import akka.persistence.SnapshotMetadata
import org.scalatest.{FlatSpec, Matchers}

class PipePathKeySupportSpec extends FlatSpec with Matchers {

  val keyController = new PipePathKeySupport(DefaultSnapshotKeySupportSpec.settings)

  "pathForPersistenceId" should "replace pipe chars with forward slashes" in {
    assert(keyController.pathForPersistenceId("","level1|level2|p9") == "||level1/level2/p9||/")
    assert(keyController.pathForPersistenceId("prefix/","level1|level2|p9") == "prefix/||level1/level2/p9||/")
  }

  "parseKeyToMetadata" should "parse snapshot object key to SnapshotMetadata" in {
    //no s3 path structure to persistenceId
    keyController.parseKeyToMetadata("", "p-9/15-1455442923252.ss") shouldBe SnapshotMetadata("p-9", 51L, 1455442923252L)

    //one sub-level of s3 path structure in persistenceId
    keyController.parseKeyToMetadata("", "||level2/p-9||/15-1455442923252.ss") shouldBe SnapshotMetadata("level2|p-9", 51L, 1455442923252L)

    //two sub-level of s3 path structure in persistenceId
    keyController.parseKeyToMetadata("", "||level1/level2/p-9||/15-1455442923252.ss") shouldBe SnapshotMetadata("level1|level2|p-9", 51L, 1455442923252L)
  }
  it should "parse snapshot object key to SnapshotMetadata with prefix" in {
    //no s3 path structure to persistenceId
    keyController.parseKeyToMetadata("foo/", "p-9/15-1455442923252.ss") shouldBe SnapshotMetadata("p-9", 51L, 1455442923252L)

    //one sub-level of s3 path structure in persistenceId
    keyController.parseKeyToMetadata("foo/", "||level2/p-9||/15-1455442923252.ss") shouldBe SnapshotMetadata("level2|p-9", 51L, 1455442923252L)

    //two sub-level of s3 path structure in persistenceId
    keyController.parseKeyToMetadata("foo/", "||level1/level2/p-9||/15-1455442923252.ss") shouldBe SnapshotMetadata("level1|level2|p-9", 51L, 1455442923252L)
  }

  "snapshotKey" should "correctly construct an S3 key" in {
    assert( keyController.snapshotKey("", SnapshotMetadata("level1|level2|p-9", 51L, 1455442923252L)) == "||level1/level2/p-9||/15-1455442923252.ss")
    assert( keyController.snapshotKey("", SnapshotMetadata("level1-level2-p-9", 51L, 1455442923252L)) == "level1-level2-p-9/15-1455442923252.ss")
  }
}
