package alg.trees

import org.scalatest._

class TreeSpec
    extends WordSpec
    with ShouldMatchers
    with OptionValues {

  import TreeSpecFixtures._

  val stub = Tree.vInt

  "The Tree" can {
    "be mapped " when {
      "the tree is null" in {
        // scalastyle:off null
        stub.map(null)(x => x) should be(NoneTree)
        // scalastyle:on null
      }
      "the tree has one node" in {
        stub.map(singleNode)(_ * 2).get should be(singleNode.get * 2)
      }
      "the tree has multiple nodes" in {
        stub.paths(tree) should equal (treePaths)
      }
    }

    "obtain the paths" when {
      "the tree is null" in {
        // scalastyle:off null
        stub.paths(null) should equal (Set.empty)
        // scalastyle:on null
      }
      "the tree has one node" in {
        stub.paths(singleNode) should equal (singleNodePath)
      }
      "the tree has multiple nodes" in {
        stub.paths(tree) should equal (treePaths)
      }
    }

    "obtain the root-paths" when {
      "the tree is null" in {
        // scalastyle:off null
        stub.rootPaths(null) should equal (Set.empty)
        // scalastyle:on null
      }
      "the tree has one node" in {
        stub.rootPaths(singleNode) should equal (singleNodePath)
      }
      "the tree has multiple nodes" in {
        stub.rootPaths(tree) should equal (treeRootPaths)
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

    "obtain the max value in a tree" when {
      "the tree is null" in {
        // scalastyle:off null
        stub.max(null) should be(empty)
        // scalastyle:on null
      }
      "the tree has one node" in {
        stub.max(singleNode).value should be(singleNode.get)
      }
      "the tree has multiple nodes" in {
        stub.max(tree).value should be(treeMaxValue)
      }
    }

    "obtain the size of a tree" when {
      "the tree is null" in {
        // scalastyle:off null
        stub.size(null) should be(0)
        // scalastyle:on null
      }
      "the tree has one node" in {
        stub.size(singleNode) should be(1)
      }
      "the tree has multiple nodes" in {
        stub.size(tree) should be(treeSize)
      }
    }
  }
}

object TreeSpecFixtures {
  import Tree._

  val singleNode: BinaryTree[Int] = bnode(8)

  val singleNodePath = Set(List(singleNode.v))

  val treeMaxAmplitud = 8

  val treeMaxValue = 12

  val treeSize = 9

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

  val treeRootPaths = Set(
    List(12, 8, 5),
    List(2, 8, 5),
    List(1, 7, 9, 5),
    List(3, 4, 9, 5)
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
