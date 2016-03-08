
/** Sealed Trait labels a tree. */
sealed trait Tree[+A]

object Tree {
  /** Convenience type for Int Trees. */
  type IntTree = Tree[Int]

  /** A Tree factory which returns `NoneTree` in a manner consistent with
    * the collections hierarchy.
    */
  def empty[A] : Tree[A] = NoneTree

  /** A Tree factory method that creates a node with a value `x` with
    * no left and no right.
    */
  def bnode[A](x: A,
               l: Tree[A]= NoneTree,
               r: Tree[A]= NoneTree): BinaryTree[A] = BinaryTree(x,l,r)
}

/** Case object that represents an non-existent value. */
case object NoneTree extends Tree[Nothing]

/** Class `BinaryTree[A]` represents a tree node with a value of type `A`. */
case class BinaryTree[A](
  x: A,
  left: Tree[A]  = NoneTree,
  right: Tree[A] = NoneTree
) extends Tree[A]



trait TreeLike {

  /** Calculates the max amplitude of a `Tree[Int]`.
    * In a binary tree `T`, a path `P` is a non-empty sequence of nodes of tree such that,
    * each consecutive node in the sequence is a subtree of its preceding node. In the
    * example tree, the sequences `[9, 8, 2]` and `[5, 8, 12]` are two paths, while `[12, 8, 2]` is not.
    * The amplitude of path P is the maximum difference among values of nodes on path P.
    * The amplitude of tree T is the maximum amplitude of all paths in T. When the tree is empty,
    * it contains no path, and its amplitude is treated as 0.
    * For example.
    *
    *
    * Input:
    * {{
    *          5
    *        /   \
    *     8        9
    *    /  \     /  \
    * 12   2  8   4
    *           /    /
    *         2    5
    * }}
    *
    * Output:
    * `7`
    * Explanation:
    * The paths `[5, 8, 12]` and `[9, 8, 2]` have the maximum amplitude `7`.
    *
    */
  def maxApplitud(t: Tree.IntTree): Int =
    doMaxApplitud(t) match {
      case Nil   => 0
      case xs@_  => xs.max
    }

  private def doMaxApplitud(t: Tree[Int],
                            max:Int = Int.MinValue,
                            min:Int = Int.MaxValue): List[Int] = t match {
    case NoneTree                          =>
      List(max - min)
    case BinaryTree(x, NoneTree, NoneTree) =>
      doMaxApplitud(NoneTree, math.max(x, max), math.min(x, min))
    case BinaryTree(x, l, r)               =>
      doMaxApplitud(l, math.max(x, max), math.min(x, min)) ++
      doMaxApplitud(r, math.max(x, max), math.min(x, min))
  }

  def findPaths(tree: Tree.IntTree): Set[List[Int]] = doFindPaths(tree)

  private def doFindPaths(tree: Tree.IntTree,
                          ac: List[Int] = Nil): Set[List[Int]] =
  Option(tree) match {
    case None           =>
      Set.empty
    case Some(NoneTree) =>
      Set.empty
    case Some(BinaryTree(x, NoneTree,  NoneTree)) =>
      Set(x :: ac)
    case Some(BinaryTree(x, l, r)) =>
      doFindPaths(l, x :: ac) ++
      doFindPaths(l, Nil)     ++
      doFindPaths(r, x :: ac) ++
      doFindPaths(r, Nil)
  }

  def amplitud (xs: List[Int]): Int = xs.sorted match {
    case Nil        => 0
    case x :: Nil   => 0
    case _  =>
      val (min, max) = minMax(xs)
      max - min
  }

  private def minMax(xs: List[Int]) : (Int, Int) =
    xs.foldLeft((xs(0), xs(0))) {
      case ((min, max), e) => (math.min(min, e), math.max(max, e))
    }
}
