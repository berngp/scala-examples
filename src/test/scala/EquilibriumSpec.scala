import org.scalatest._

class EquilibriumSpec 
  extends WordSpec
    with ShouldMatchers {

  "The Equilibrium of an array" can {
    "when null" should {
      "no indexes should be found" in {
        Equilibrium.findFirst(null) should be (-1)
        Equilibrium.findAll(null) should be ('empty)
      }
    }
    "when empty" should {
      "no indexes should be found" in {
        Equilibrium.findFirst(Nil) should be (-1)
        Equilibrium.findAll(Nil) should be ('empty)
      }
    }
    "when non-empty" should {
      "return the first index" in {
        val input = Array(-7,1,5,2,-4,3,0)
        Equilibrium.findFirst(input) should be (3)
      }
      "return all indexes" in {
        val input = Array(-7,1,5,2,-4,3,0)
        Equilibrium.findAll(input) should contain allOf(6, 3)
      }
    }
  }

}
