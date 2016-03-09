package alg.trees

import org.scalatest._

class TreeSpec
    extends WordSpec
    with ShouldMatchers
    with OptionValues {

  import TreeSpecFixtures._

  val stub = Tree.vInt

  "The Tree" can {

    "obtain the paths" when {
      "the tree is null" in {
        // scalastyle:off null
        stub.findPaths(null) should equal (Set.empty)
        // scalastyle:on null
      }
      "the tree has one node" in {
        stub.findPaths(singleNode) should equal (singleNodePath)
      }
      "the tree has multiple nodes" in {
        stub.findPaths(tree) should equal (treePaths)
      }
    }

    "obtain the max applitud" when {
      "the tree is null" in {
        // scalastyle:off null
        stub.maxApplitud(null) should be(empty)
        // scalastyle:on null
      }
      "the tree has multiple nodes" in {
        stub.maxApplitud(tree).value should be(treeMaxAmplitud)
      }
    }
  }
}

object TreeSpecFixtures {
  import Tree._

  val singleNode: BinaryTree[Int] = bnode(8)

  val singleNodePath = Set(List(singleNode.x))

  val treeMaxAmplitud = 8

  val tree =
    bnode(
      5,
      bnode(8, bnode(12), bnode(2)),
      bnode(
        9,
        bnode(7, bnode(1)),
        bnode(4, bnode(3))
      )
    )

  val treePaths = Set(
    List(12, 8, 5),
    List(12, 8),
    List(2, 8, 5),
    List(2, 8),
    List(1, 7, 9, 5),
    List(1, 7),
    List(3, 4, 9, 5),
    List(3, 4),
    List(1, 7, 9),
    List(1, 7),
    List(3, 4, 9),
    List(3, 4)
  )
}
