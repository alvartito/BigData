package pruebas

/**
 * Implementa en Scala un contador utilizando estado local que, cada vez
 * que se invoque, devuelva el resultado de aplicar una función al valor
 * del resultado obtenido la vez anterior.
 * <p>Tanto la función a aplicar como el valor de inicio se determinan
 * en el momento de la creación del contador.
 */
object Ejercicio01Contador extends App {

  def cuadrado(x: Int) = x * x

  def suma(x: Int) = x + x

  //Devuelve "slskfd"---"slskfd"
  def concat(x: String) = x+"---"+x
  
  /**
   * Recibe un entero, valor inicial (x:Int) y
   * <p>una función que recibe un entero y devuelve un entero fn: (Int) => Int
   * <p> La función makeContador, llama a otra función que se define dentro, y
   * que no devuelve nada (función vacía) que devuelve el valor del valor de la
   * función mañeCuadrado con el valor inicial.
   */
  def makeContador(x: Int, fn: (Int) => Int): () => Int = {
    //Lo que definimos como var puede cambiar de valor, no es final
    var acc: Int = x
    //Definimos una función anónima dentro del método makeContador
    () => {
      acc = fn(acc)
      acc
    }
  }

  def make[T](init:T,fn:(T)=>T):()=>T={
    var acc=init
    () => {
      acc=fn(acc)
      acc
    }
  }
  
  
  // Llamamos al metodo makeContador, que devuelve una función
  // Lo que definimos como val es final, no muta
  val cont = makeContador(2, cuadrado)
  
  val sum = makeContador(2, suma)
  //Llamamos a la función que se ejecuta dentro de makeContador
  println(cont())
  println(sum())
  println(cont())
  println(sum())
  println(make("Pepe",concat)())
  println(make("Garcia",concat)())
  println(make("Jose",concat)())
  println(make("Tema",concat)())
  println(make("Leche",concat)())
  println(make("Pepito",concat)())

}
