package pruebas

object Ejercicio01Contador extends App {

  /**
   * Implementa en Scala un contador utilizando estado local que, cada vez que se invoque, devuelva el resultado de aplicar una función al valor del resultado obtenido la vez anterior.
   * Tanto la función a aplicar como el valor de inicio se determinan en el momento de la creación del contador.
   *
   */
  def makeContador(x: Int, fn: (Int) => Int): () => Int = {
    var acc: Int = x
    //Definimos una función anónima dentro del método makeContador
    () => {
      acc = fn(acc)
      acc
    }
  }

  def cuadrado(x: Int) = x * x

  // Llamamos al metodo makeContador, que devuelve una función
  val cont = makeContador(2, cuadrado)
  //Llamamos a la función que se ejecuta dentro de makeContador
  println(cont())
  println(cont())
}
