// scalastyle:off null

case class Tree(x: Int, l: Tree, r: Tree)


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
    case Some(Tree(x, null,  null)) =>
      (x :: ac) :: Nil
    case Some(Tree(x, l, r)) =>
      doFindPaths(l, x :: ac) ++
      doFindPaths(l, Nil)  ++
      doFindPaths(r, x :: ac) ++
      doFindPaths(r, Nil)
  }

  def amplitud (xs: List[Int]): Int = xs.sorted match {
    case Nil        => 0
    case x :: Nil   => 0
    case x :: tail  => tail.last - x
  }


}


// scalastyle:on null
