package alg.trees

/**
  * Referenced from [Wikipedia: Tree Data Structure](https://en.wikipedia.org/wiki/Tree_\(data_structure\))
  * <blockquote cite="https://en.wikipedia.org/wiki/Tree_(data_structure)">
  *   Data type vs. data structure
  *    There is a distinction between a tree as an abstract data type and as a concrete data structure,
  *    analogous to the distinction between a list and a linked list. As a data type, a tree has a value
  *    and children, and the children are themselves trees; the value and children of the tree are
  *    interpreted as the value of the root node and the subtrees of the children of the root node.
  *    To allow finite trees, one must either allow the list of children to be empty (in which case trees
  *    can be required to be non-empty, an "empty tree" instead being represented by a forest of zero trees),
  *    or allow trees to be empty, in which case the list of children can be of fixed size (branching factor,
  *    especially 2 or "binary"), if desired.
  *
  *    As a data structure, a linked tree is a group of nodes, where each node has a value and a list of
  *    references to other nodes (its children). This data structure actually defines a directed graph,[a]
  *    because it may have loops or several references to the same node, just as a linked list may have a loop.
  *    Thus there is also the requirement that no two references point to the same node (that each node has at
  *    most a single parent, and in fact exactly one parent, except for the root), and a tree that violates
  *    this is "corrupt".
  *
  *    Due to the use of references to trees in the linked tree data structure, trees are often discussed
  *    implicitly assuming that they are being represented by references to the root node, as this is often
  *    how they are actually implemented. For example, rather than an empty tree, one may have a null
  *    reference: a tree is always non-empty, but a reference to a tree may be null.
  * </blockquote>
  */

/** Sealed Trait labels a tree. */
sealed trait Tree[+A] {
  def isEmpty: Boolean
  def get: A
}

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
    v: A,
    l: Tree[A] = NoneTree,
    r: Tree[A] = NoneTree
  ): BinaryTree[A] = BinaryTree(v, l, r)

  def vInt: TreeVisitor[Int, Int] = intBinaryTreeVisitor
  def vLong: TreeVisitor[Long, Long] = longBinaryTreeVisitor
  def vDouble: TreeVisitor[Double, Double] = doubleBinaryTreeVisitor

  private abstract class NumericTreeVisitor[T] extends TreeVisitor[T, T]

  private object intBinaryTreeVisitor extends NumericTreeVisitor[Int] {
    protected def diff(a: Int, b: Int): Int = a - b
  }
  private object doubleBinaryTreeVisitor extends NumericTreeVisitor[Double] {
    protected def diff(a: Double, b: Double): Double = a - b
  }
  private object longBinaryTreeVisitor extends NumericTreeVisitor[Long] {
    protected def diff(a: Long, b: Long): Long = a - b
  }

}

/** Case object that represents an non-existent value. */
case object NoneTree extends Tree[Nothing] {
  override def isEmpty: Boolean = true
  override def get: Nothing = throw new NoSuchElementException("NoneTree.get")
}

/** Class `BinaryTree[A]` represents a tree node with a value of type `A`. */
case class BinaryTree[A](
    v:     A,
    left:  Tree[A] = NoneTree,
    right: Tree[A] = NoneTree
) extends Tree[A] {
  override def isEmpty: Boolean = false
  override def get: A = v
}

trait TreeVisitor[A, N] { self =>

  protected def diff(a: A, b: A): N

  def map[B](t: Tree[A])(f: (A) => B): Tree[B] =
    Option(t).getOrElse(NoneTree) match {
      case NoneTree                          => NoneTree
      case BinaryTree(x, NoneTree, NoneTree) => BinaryTree(f(x), NoneTree, NoneTree)
      case BinaryTree(x, l, r)               => BinaryTree(f(x), map(l)(f), map(r)(f))
    }

  def size(tree: Tree[A]): Int = doSize(tree)

  private def doSize(t: Tree[A], cx: Int = 0): Int =
    Option(t).getOrElse(NoneTree) match {
      case NoneTree                          => 0
      case BinaryTree(x, NoneTree, NoneTree) => 1
      case BinaryTree(x, l, r)               => doSize(l) + 1 + doSize(r)
    }

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
  } orElse Option(a)

  private def fmax[B >: A](a: A, b: Option[A])(implicit ord: Ordering[B]): Option[A] = b map { x =>
    if (ord.compare(a, x) >= 0) a else x
  } orElse Option(a)

  def max(t: Tree[A])(implicit ord: Ordering[A]): Option[A] = doFindMax(Option(t).getOrElse(NoneTree))

  private def doFindMax(
    t:    Tree[A],
    cmax: Option[A] = None
  )(implicit ord: Ordering[A]): Option[A] = t match {
    case NoneTree                          => None
    case BinaryTree(x, NoneTree, NoneTree) => fmax(x, cmax)
    case BinaryTree(x, l, r)               => List(Option(x), doFindMax(l, cmax), doFindMax(r, cmax)).max
  }

  /**
    * Generates a set with the paths and sub paths of the given tree.
    * A path `P` is a non-empty sequence of nodes of the tree such that,
    * each consecutive node in the sequence is a subtree of its preceding node. In the
    * example tree, the sequences `[9, 7, 2]` and `[5, 8, 12]` are two paths, while `[12, 8, 2]` is not.
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
  def paths(tree: Tree[A]): Set[List[A]] = doFindPaths(tree)

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

  /**
    * Generates a set with the paths of the given tree.
    * A root-path `P` is a non-empty sequence of nodes of the tree starting from its such that
    * each consecutive node in the sequence is a subtree of its preceding node. In the
    * example tree, the sequences `[5,9,7,1]` and `[5, 8, 12]` are two root-paths, while
    * `[9, 8, 2]` and `[12, 8, 2]` are not.
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
    *     Set(List(12, 8, 5),
    *         List(2, 8, 5),
    *         List(1, 7, 9, 5),
    *         List(3, 4, 9, 5))
    * }}
    */
  def rootPaths(tree: Tree[A]): Set[List[A]] = doFindRootPaths(tree)

  private def doFindRootPaths(
    tree: Tree[A],
    ac:   List[A] = Nil
  ): Set[List[A]] =
    Option(tree).getOrElse(NoneTree) match {
      case NoneTree => Set.empty
      case BinaryTree(x, NoneTree, NoneTree) =>
        Set(x :: ac)
      case BinaryTree(x, l, r) =>
        doFindRootPaths(l, x :: ac) ++ doFindRootPaths(r, x :: ac)
    }

  def depth(tree: Tree[A]): (Int, List[A]) = {
    val last = rootPaths(tree).toList.sortBy(_.length).last
    (last.size, last)
  }
}
