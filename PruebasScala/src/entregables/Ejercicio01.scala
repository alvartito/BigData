package pruebas

/**
 * @author Álvaro Sánchez Blasco
 */
class Ejercicio01Entregable extends App {

  /**
   * Define en Scala la función recursiva pura (sin tail recursion) aplica3D,
   * que reciba una lista de coordenadas 3D en forma de tupla, una función unaria y una coordenada (“x”, “y” ó “z”). 
   * Deberá aplicar la función a los elementos correspondientes a esa coordenada y devolver las nuevas coordenadas. 
   * Ejemplo:
   * val lista=List((1,2,3),(4,5,6),(7,8,9),(10,11,12))
   * def suma(x:Int)=x+2
   * aplica3D(lista,suma_,“y”)→ ((1,4,3),(4,7,6),
   * (7,10,9),(10,13,12))
   *
   */
  def aplica3D(lista: List[(Int, Int, Int)], f: (Int) => Int, c: String): List[(Int, Int, Int)] =
    {
      if (lista.isEmpty) Nil else
        c match {
          case "x" => (f(lista.head._1), lista.head._2, lista.head._3) :: aplica3D(lista.tail, f, c)
          case "y" => (lista.head._1, f(lista.head._2), lista.head._3) :: aplica3D(lista.tail, f, c)
          case "z" => (lista.head._1, lista.head._2, f(lista.head._3)) :: aplica3D(lista.tail, f, c)
        }
    }

  def suma(x: Int) = x + 2

  val listaE1a = List((1, 2, 3), (4, 5, 6), (7, 8, 9), (10, 11, 12))

  println(aplica3D(listaE1a, suma _, "y")) // => ((1,4,3), (4,7,6), (7,10,9), (10,13,12))
  println(aplica3D(listaE1a, suma _, "x")) // => ((3,2,3), (6,5,6), (9,8,9), (12,11,12))
  println(aplica3D(listaE1a, suma _, "z")) // => ((1,2,5), (4,5,8), (7,8,11), (10,11,14))
}