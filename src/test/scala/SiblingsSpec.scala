import org.scalatest._

class SiblingsSpec
  extends WordSpec
    with ShouldMatchers
    with OptionValues {

  val stub = SiblingsStub

  "The Siblings " can {

    "transform a number `x` into a list" when {
      "x is a unit " in {
        stub.toList(3) should equal (List(3))
      }
      "x is greater than 10" in {
        stub.toList(100) should equal (List(1,0,0))
        stub.toList(123) should equal (List(1,2,3))
      }
    }

    "transform a list `xs` into the largest number" when {
      "xs has zeros" in {
        stub.asLargestInt(List(0,1,0)) should be (100)
      }
      "xs has 123 " in {
        stub.asLargestInt(List(1,2,3)) should be (321)
      }
      "xs has 535" in {
        stub.asLargestInt(List(5,3,5)) should be (553)
      }
    }

    "is able to find the largest sibling" when {
      "x is zero" in {
        stub.findLargest(0).value should be (0)
      }
      "x has a unit" in {
        stub.findLargest(9).value should be (9)
      }
      "x has 123" in {
        stub.findLargest(123).value should be (321)
      }
      "x has 535" in {
        stub.findLargest(535).value should be (553)
      }
    }
  }

}


object SiblingsStub extends SiblingsLike
