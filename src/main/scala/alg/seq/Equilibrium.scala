package alg.seq

import scala.annotation.tailrec

object Equilibrium extends EquilibriumLike

/** Equilibrium index of an array is an index such that the sum of elements at
  * lower indexes is equal to the sum of elements at higher indexes. For example,
  * in an array
  * {{
  *  val a = Array(-7,1,5,2,-4,3,0)
  * }}
  * The index `3` is an equilibrium index, because:
  *  {{
  *    assert( a(0) + a(1) + a(2) == a(4) + a(5) + a(6) )
  *  }}
  * The index `6` is also an equilibrium index, because sum of zero elements is zero,
  * i.e.,
  * {{
  *   assert( a(0) + a(1) + a(2) + a(3) + a(4) + a(5) == 0 )
  * }}
  */
trait EquilibriumLike {

  /** Finds the first index of an element that holds the equilibrium of this Sequence. */
  def findFirst(as: Seq[Int]): Int = Option(as) match {
    case None           => -1
    case Some(Seq())    => -1
    case Some(xs)       => doFindFirst(xs.view.zipWithIndex.toStream, 0, sum(xs))
  }

  /** Adds all elements of a given sequence. */
  private def sum(as: Seq[Int]) = as.reduce(_ + _)

  /** Returns the first element of the list, `as`, that is in equilibrium.
    *
    * @param as List where the head is the candidate for equilibrium
    *           and the tail the elements at higher indexes.
    * @param ps Sum of the previous elements of the list, i.e. sum of lower indexes.
    * @param ts Total sum of the elements of the list.
    */
  @tailrec private def doFindFirst(as: Stream[(Int, Int)], ps:Int, ts:Int): Int = as match {
    case Stream.Empty             => -1 // Empty

    case x #:: Stream.Empty       =>
      // Test form Sum of Zero elements eq Zero.
      if (ps == 0) x._2 else -1

    case x #:: tail               =>
      // Test for Sum of previous elements eq Sum of Next.
      if (ps == (ts - ps - x._1)) {
        x._2
      }
      else {
        doFindFirst(tail, ps + x._1, ts)
      }
  }

  /** Finds all indexes that are in equilibrium. */
  def findAll(as: Seq[Int]): List[Int] = Option(as) match {
    case None           => Nil
    case Some(Seq())    => Nil
    case Some(xs)       => doFindAll(xs.view.zipWithIndex.toStream, 0, sum(xs))
  }

  /** Returns the index of all element of the stream, `as`, that are in equilibrium.
    *
    * @param as List where the head is the candidate for equilibrium
    *           and the tail the elements at higher indexes.
    * @param ps Sum of the previous elements of the list, i.e. sum of lower indexes.
    * @param ts Total sum of the elements of the list.
    * @param ac List that holds the indexes that were found to be in equilibrium.
    */
  @tailrec private def doFindAll(as: Stream[(Int, Int)], ps:Int, ts:Int, ac: List[Int] = Nil): List[Int] =
  as match {
    case Stream.Empty             => ac

    case x #:: Stream.Empty       =>
      // Test form Sum of Zero elements eq Zero.
      if (ps == 0) x._2 :: ac else ac

    case x #:: tail               =>
      // Test for Sum of previous elements eq Sum of Next.
      if (ps == (ts - ps - x._1)) {
        doFindAll(tail, ps + x._1, ts, x._2 :: ac)
      }
      else {
        doFindAll(tail, ps + x._1, ts, ac)
      }
  }
}
