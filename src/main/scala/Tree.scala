
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

  import scala.annotation.tailrec

  def maxApplitud(t: Tree.IntTree): Int =
    pathsToMaxAmplitud(findPaths(t))

  def pathsToMaxAmplitud(ps: List[List[Int]]): Int =
    ps.map(amplitud).sorted.last

  def findPaths(tree: Tree.IntTree): List[List[Int]] = doFindPaths(tree)

  private def doFindPaths(tree: Tree.IntTree,
                          ac: List[Int] = Nil): List[List[Int]] =
  Option(tree) match {
    case None => Nil
    case Some(NoneTree) => Nil
    case Some(BinaryTree(x, NoneTree,  NoneTree)) =>
      (x :: ac) :: Nil
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
