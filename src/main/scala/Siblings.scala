object Siblings extends SiblingsLike


trait SiblingsLike {

  import scala.annotation.tailrec

  final def findLargest(x: Int):Option[Int] =
    if (x < 0) {
      None
    }
    else {
      Option(asLargestInt(toList(x)))
    }

  final def asLargestInt(xs: List[Int]): Int =
    doAsLargestInt(xs.sorted.view.zipWithIndex.toStream, 0)

  @tailrec private def doAsLargestInt(xs: Stream[(Int, Int)], ac:Int): Int =
  xs match {
    case Stream.Empty     => ac
    case (x,i) #:: tail   => doAsLargestInt(tail, ac + (pow(i) * x))
  }

  private def pow(i: Int): Int = math.pow(10, i).intValue

  final def toList(x:Int): List[Int] = doToList(x)

  @tailrec private def doToList(x:Int, ac:List[Int] = Nil): List[Int] = {
    val i = x/10
    if (i == 0){
      x :: ac
    } else {
      doToList(i, (x%10) :: ac)
    }
  }

}
