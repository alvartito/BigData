package entregables

/**
 * @author cloudera
 */
class Ejercicio02 {
  def numTests(lista: List[(Int) => Boolean], n: Int): Int =
    {
      if (lista.isEmpty) 0
      else if (lista.head(n)) 1 + numTests(lista.tail, n)
      else numTests(lista.tail, n)
    }

  // Pruebas
  println("---Ejercicio 3")

  def mayorQue8(n: Int) = n > 8
  def par(n: Int) = n % 2 == 0
  def impar(n: Int) = n % 2 != 0
  val listaTests = List(mayorQue8 _, par _, impar _)
  println(numTests(listaTests, 12)) // => 2
  println(numTests(listaTests, 3)) // => 1
  
  def makeTest(p: String, n: Int): (Int) => Boolean =
    {
      p match {
        case "<" => (x: Int) => x < n
        case ">" => (x: Int) => x > n
        case "=" => (x: Int) => x == n
      }
    }

  def generaTests(lista: List[String]): List[(Int) => Boolean] =
    {
      if (lista.length < 2) Nil
      else makeTest(lista.head, lista.tail.head.toInt) :: generaTests(lista.tail.tail)
    }

  // Pruebas
  val listTest = generaTests(List(">", "3", "<", "5", ">", "8", "=", "10"))
  println(numTests(listTest, 10)) // => 3
}