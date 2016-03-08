import org.scalatest._

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
        stub.findPaths(null) should equal (Set.empty)
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
  import Tree._

  val singleNode: BinaryTree[Int] = bnode(8)

  val singleNodePath = Set(List(singleNode.x))

  val treeMaxAmplitud = 8

  val tree =
    bnode(5,
      bnode(8, bnode(12), bnode(2)),
      bnode(9,
        bnode(7, bnode(1)),
        bnode(4, bnode(3))))

  val treePaths = Set(
      List(12, 8, 5),
      List(12),
      List(2, 8, 5),
      List(2),
      List(12, 8),
      List(12),
      List(2, 8),
      List(1, 7, 9, 5),
      List(1),
      List(1, 7),
      List(3, 4, 9, 5),
      List(3),
      List(3, 4),
      List(1, 7, 9),
      List(1),
      List(1, 7),
      List(3, 4, 9),
      List(3),
      List(3, 4)
    )
}
