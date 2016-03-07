import org.scalatest._

// scalastyle:off null

class TreeSpec
  extends WordSpec
    with ShouldMatchers
    with OptionValues {

  import Fixtures._

  val stub = TreeStub

  "The Tree" can {

    "obtain the amplitud of a path" when {
      "the path is empty" in {
        stub.amplitud(Nil) should equal (0)
      }
      "the path has one element" in {
        stub.amplitud(List(10)) should equal(0)
      }
      "the path has n elements" in {
        stub.amplitud(List(5,9,4,3)) should equal(6)
      }
    }

    "obtain the paths" when {
      "the tree is empty" in {
        stub.findPaths(null) should equal (Nil)
      }
      "the tree has one node" in {
        stub.findPaths(singleNode) should equal (singleNodePath)
      }
      "the tree has multiple nodes" in {
        stub.findPaths(tree) should equal (treePaths)
      }
    }

    "obtain the max applitud" when {
      "the tree has multiple nodes" in {
        stub.maxApplitud(tree) should be(treeMaxAmplitud)
      }
    }
  }
}


object TreeStub extends TreeLike

object Fixtures {

  val singleNode = Tree(8,null, null)

  val singleNodePath = List(List(singleNode.x))

  val treeMaxAmplitud = 8

  val tree = Tree(5,
      Tree(8,
        Tree(12, null, null),
        Tree(2, null, null)),
      Tree(9,
        Tree(7,
          Tree(1, null, null), null),
        Tree(4,
          Tree(3, null, null), null)))

  val treePaths = List(
      List(12, 8, 5),
      List(12),
      List(2, 8, 5),
      List(2),
      List(12, 8),
      List(12),
      List(2, 8),
      List(2),
      List(1, 7, 9, 5),
      List(1),
      List(1, 7),
      List(1),
      List(3, 4, 9, 5),
      List(3),
      List(3, 4),
      List(3),
      List(1, 7, 9),
      List(1),
      List(1, 7),
      List(1),
      List(3, 4, 9),
      List(3),
      List(3, 4),
      List(3)
    )

}
// scalastyle:on null
