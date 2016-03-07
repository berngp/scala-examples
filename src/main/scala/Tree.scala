// scalastyle:off null

case class Tree(x: Int, l: Tree, r: Tree)

// TODO migrate to
// sealed trait Tree[+A]
// case class Node[A](x: A, left: Option[Tree[A]], right: Option[Tree[A]]) extends Tree[A]

trait TreeLike {

  import scala.annotation.tailrec

  def maxApplitud(t: Tree): Int =
    pathsToMaxAmplitud(findPaths(t))

  def pathsToMaxAmplitud(ps: List[List[Int]]): Int =
    ps.map(amplitud).sorted.last

  def findPaths(tree: Tree): List[List[Int]] = doFindPaths(tree)

  private def doFindPaths(tree: Tree,
                          ac: List[Int] = Nil): List[List[Int]] =
  Option(tree) match {
    case None => Nil
    // TODO migrate to Some(Node(x, None, None))
    case Some(Tree(x, null,  null)) =>
      (x :: ac) :: Nil
    // TODO migrate to Some(Node(x, left, right))
    case Some(Tree(x, l, r)) =>
      doFindPaths(l, x :: ac) ++
      doFindPaths(l, Nil)  ++
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

// scalastyle:on null
