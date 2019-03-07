package io.findify.akka.persistence.s3.snapshot.keys

import akka.persistence.SnapshotMetadata
import com.typesafe.config.ConfigFactory
import io.findify.akka.persistence.s3.snapshot.S3SnapshotConfig
import org.scalatest.{FlatSpec, Matchers}

class DefaultSnapshotKeySupportSpec extends FlatSpec with Matchers {

  val defaultKeyController = new DefaultSnapshotKeySupport(DefaultSnapshotKeySupportSpec.settings)

  "parseKeyToMetadata" should "parse snapshot object key to SnapshotMetadata" in {
    defaultKeyController.parseKeyToMetadata("", "p-9/15-1455442923252.ss") shouldBe SnapshotMetadata("p-9", 51L, 1455442923252L)
  }
  it should "parse snapshot object key to SnapshotMetadata with prefix" in {
    assert(defaultKeyController.parseKeyToMetadata("foo/", "p-9/15-1455442923252.ss") == SnapshotMetadata("p-9", 51L, 1455442923252L))
  }

}

object DefaultSnapshotKeySupportSpec {
  val settings = new S3SnapshotConfig(ConfigFactory
    .parseString(
      """
        |bucket-name = ""
        |extension = "ss"
        |prefix = ""
        |max-load-attempts = 3
        |key-generator-class = ""
        |
        |akka.persistence.snapshot-store.plugin = "s3-snapshot-store"
        |s3-client{
        |  region = "us-west-2"
        |  endpoint = "http://127.0.0.1:4567"
        |  options {
        |    path-style-access = true
        |  }
        |}
      """.stripMargin
    )
    .withFallback(ConfigFactory.load()))
}
