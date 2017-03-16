package io.findify.akka.persistence.s3.snapshot

import akka.persistence.SnapshotMetadata
import org.scalatest.{DiagrammedAssertions, FlatSpec, Matchers, WordSpecLike}

class SnapshotKeySpec extends FlatSpec with Matchers with SnapshotKeySupport {

  val extensionName = "ss"

  "parseKeyToMetadata" should "parse snapshot object key to SnapshotMetadata" in {
    parseKeyToMetadata("", "p-9/15-1455442923252.ss") shouldBe SnapshotMetadata("p-9", 51L, 1455442923252L)
  }
  it should "parse snapshot object key to SnapshotMetadata with prefix" in {
    assert(parseKeyToMetadata("foo/", "p-9/15-1455442923252.ss") == SnapshotMetadata("p-9", 51L, 1455442923252L))
  }

}
