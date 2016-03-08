package alg.seq

import org.scalatest._

class EquilibriumSpec
    extends WordSpec
    with ShouldMatchers {

  val stub = EquilibriumStub

  "The Equilibrium of an array" can {
    "when null" should {
      "no indexes should be found" in {
        // scalastyle:off null
        stub.findFirst(null) should be (-1)
        stub.findAll(null) should be ('empty)
        // scalastyle:on null
      }
    }
    "when empty" should {
      "no indexes should be found" in {
        stub.findFirst(Nil) should be (-1)
        stub.findAll(Nil) should be ('empty)
      }
    }
    "when non-empty" should {
      "return the first index" in {
        val input = Array(-7, 1, 5, 2, -4, 3, 0)
        stub.findFirst(input) should be (3)
      }
      "return all indexes" in {
        val input = Array(-7, 1, 5, 2, -4, 3, 0)
        stub.findAll(input) should contain allOf (6, 3)
      }
    }
  }
}

object EquilibriumStub extends EquilibriumLike
