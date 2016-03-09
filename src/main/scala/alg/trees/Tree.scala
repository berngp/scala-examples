package alg.trees

/** Sealed Trait labels a tree. */
sealed trait Tree[+A]

object Tree {
  /** Convenience type for Int Trees. */
  type IntTree = Tree[Int]

  /**
    * A Tree factory which returns `NoneTree` in a manner consistent with
    * the collections hierarchy.
    */
  def empty[A]: Tree[A] = NoneTree

  /**
    * A Tree factory method that creates a node with a value `x` with
    * no left and no right.
    */
  def bnode[A](
    x: A,
    l: Tree[A] = NoneTree,
    r: Tree[A] = NoneTree
  ): BinaryTree[A] = BinaryTree(x, l, r)

  def vInt: TreeLike[Int, Int] = intBinaryTreeVisitor
  def vLong: TreeLike[Long, Long] = longBinaryTreeVisitor
  def vDouble: TreeLike[Double, Double] = doubleBinaryTreeVisitor

  private abstract class NumericTreeLike[T] extends TreeLike[T, T]

  private object intBinaryTreeVisitor extends NumericTreeLike[Int] {
    protected def diff(a: Int, b: Int): Int = a - b
  }
  private object doubleBinaryTreeVisitor extends NumericTreeLike[Double] {
    protected def diff(a: Double, b: Double): Double = a - b
  }
  private object longBinaryTreeVisitor extends NumericTreeLike[Long] {
    protected def diff(a: Long, b: Long): Long = a - b
  }

}

/** Case object that represents an non-existent value. */
case object NoneTree extends Tree[Nothing]

/** Class `BinaryTree[A]` represents a tree node with a value of type `A`. */
case class BinaryTree[A](
  x:     A,
  left:  Tree[A] = NoneTree,
  right: Tree[A] = NoneTree
) extends Tree[A]

trait TreeLike[A, N] { self =>

  def size(tree: Tree[A]): Int = ???

  def max(t: Tree[A])(implicit ord: Ordering[A]): A = ???

  def depth: (Int, List[A]) = ???

  def map[B](f: (A) => B): Tree[B] = ???

  protected def diff(a: A, b: A): N

  /**
    * Calculates the max amplitude of a `Tree[Int]`.
    * In a binary tree `T`, a path `P` is a non-empty sequence of nodes of tree such that,
    * each consecutive node in the sequence is a subtree of its preceding node. In the
    * example tree, the sequences `[9, 7, 1]` and `[5, 8, 12]` are two paths, while `[12, 8, 2]` is not.
    * The amplitude of path P is the maximum difference among values of nodes on path P.
    * The amplitude of tree T is the maximum amplitude of all paths in T. When the tree is empty,
    * it contains no path, and its amplitude is treated as 0.
    * For example.
    *
    *
    * Input where the Tree is an Tree[Int]:
    * {{
    *          5
    *        /   \
    *     8        9
    *    /  \     /  \
    * 12    2    7   4
    *           /    /
    *         1    3
    * }}
    *
    * Output:
    * `8`
    * Explanation:
    * The paths `[5, 8, 12]` has `7` but the path `[9, 7, 1]` has the maximum amplitude of `8`.
    *
    */
  def maxApplitud(tree: Tree[A])(implicit ordA: Ordering[A], ordN: Ordering[N]): Option[N] =
    Option(tree) match {
      case None => None
      case Some(t) =>
        amplituds(t) match {
          case Nil    => None
          case xs @ _ => Some(xs.max)
        }
    }

  private def amplituds(
    t:   Tree[A],
    max: Option[A] = None,
    min: Option[A] = None
  )(implicit ord: Ordering[A]): List[N] = t match {
    case NoneTree =>
      if (max.isDefined && min.isDefined) List(diff(max.get, min.get)) else Nil
    case BinaryTree(x, NoneTree, NoneTree) =>
      amplituds(NoneTree, fmax(x, max), fmin(x, min))
    case BinaryTree(x, l, r) =>
      amplituds(l, fmax(x, max), fmin(x, min)) ++ amplituds(r, fmax(x, max), fmin(x, min))
  }

  private def fmin[B >: A](a: A, b: Option[A])(implicit ord: Ordering[B]): Option[A] = b map { x =>
    if (ord.compare(a, x) <= 0) a else x
  } orElse Some(a)

  private def fmax[B >: A](a: A, b: Option[A])(implicit ord: Ordering[B]): Option[A] = b map { x =>
    if (ord.compare(a, x) >= 0) a else x
  } orElse Some(a)

  /**
    * Generates a set with the paths and sub paths of the given tree.
    * A path `P` is a non-empty sequence of nodes of the tree such that,
    * each consecutive node in the sequence is a subtree of its preceding node. In the
    * example tree, the sequences `[9, 8, 2]` and `[5, 8, 12]` are two paths, while `[12, 8, 2]` is not.
    * For example.
    *
    * Input where the Tree is an Tree[Int]:
    * {{
    *          5
    *        /   \
    *     8        9
    *    /  \     /  \
    * 12     2   7   4
    *           /    /
    *          1    3
    * }}
    *
    * The paths are:
    * {{
    *     Set(List(12, 8, 5),   List(12, 8),
    *         List(2, 8, 5),    List(2, 8),
    *         List(1, 7, 9, 5), List(1, 7, 9), List(1, 7),
    *         List(3, 4, 9, 5), List(3, 4, 9), List(3, 4))
    * }}
    */
  def findPaths(tree: Tree[A]): Set[List[A]] = doFindPaths(tree)

  private def doFindPaths(
    tree: Tree[A],
    ac:   List[A] = Nil
  ): Set[List[A]] =
    Option(tree) match {
      case None =>
        Set.empty
      case Some(NoneTree) =>
        Set.empty
      case Some(BinaryTree(x, NoneTree, NoneTree)) =>
        Set(x :: ac)
      case Some(BinaryTree(x, l, r)) =>
        doFindPaths(l, x :: ac) ++
          doFindPaths(l, x :: Nil) ++
          doFindPaths(r, x :: ac) ++
          doFindPaths(r, x :: Nil)
    }
}
