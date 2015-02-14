package pruebas

/**
 * @author cloudera
 */
class Solucionp8 {
  //----------------------------------------------------
// Solución práctica 7
//// Fecha: 7/04/2014
//----------------------------------------------------


//------------------------------------
// Ejercicio 1
//------------------------------------

def suma(x: Int) = x + 2
// a)

def aplica3Da(lista:List[(Int,Int,Int)], f:(Int)=>Int, c:String):List[(Int,Int,Int)] =
{
    if (lista.isEmpty) Nil else
    c match {
      case "x" => (f(lista.head._1), lista.head._2, lista.head._3) :: aplica3Da(lista.tail, f, c)
      case "y" => (lista.head._1, f(lista.head._2), lista.head._3) :: aplica3Da(lista.tail, f, c)
      case "z" => (lista.head._1, lista.head._2, f(lista.head._3)) :: aplica3Da(lista.tail, f, c)
    }

}

//// Pruebas
println("---Ejercicio 1a")
//
val listaE1a = List((1,2,3), (4,5,6), (7,8,9), (10,11,12))

println(aplica3Da(listaE1a, suma _, "y"))  // => ((1,4,3), (4,7,6), (7,10,9), (10,13,12))
println(aplica3Da(listaE1a, suma _, "x"))  // => ((3,2,3), (6,5,6), (9,8,9), (12,11,12))
println(aplica3Da(listaE1a, suma _, "z"))  // => ((1,2,5), (4,5,8), (7,8,11), (10,11,14))

//TODO esta parte b no se pide en el enunciado del ejercicio.
// b)

def transforma(t: (Int,Int,Int), f:(Int)=>Int, c:String): (Int,Int,Int) =
   c match {
    case "x" => (f(t._1), t._2, t._3)
    case "y" => (t._1, f(t._2), t._3)
    case "z" => (t._1, t._2, f(t._3))
   }

def aplica3Db(lista:List[(Int,Int,Int)], f:(Int)=>Int, c:String):List[(Int,Int,Int)] =
   lista.map((x)=>transforma(x,f,c))


println("---Ejercicio 1b")

val listaE1b = List((1,2,3), (4,5,6), (7,8,9), (10,11,12))

println(aplica3Db(listaE1b, suma _, "y"))  // => ((1,4,3), (4,7,6), (7,10,9), (10,13,12))
println(aplica3Db(listaE1b, suma _, "x"))  // => ((3,2,3), (6,5,6), (9,8,9), (12,11,12))
println(aplica3Db(listaE1b, suma _, "z"))  // => ((1,2,5), (4,5,8), (7,8,11), (10,11,14))

////------------------------------------
//// Ejercicio 2
////------------------------------------
//
//// a)
//def intercambia(lista: List[(Int,Int)]): List[(Int,Int)] = 
//{
//  if (lista.isEmpty) Nil else
//  (lista.head._2,lista.head._1) :: intercambia(lista.tail)
//}
//
//// Pruebas
//println("---Ejercicio 2a")
//val lista = List((1,2), (3,4), (5,6))  
//intercambia(lista) 
//
//// b)
//
//def asocia(f: (Int)=>Int, lista: List[Int]) : List[(Int,Int)] = 
//{
//  if (lista.isEmpty) Nil
//  else if (lista.tail.isEmpty) Nil
//  else if (f(lista.head) == lista.tail.head) (lista.head,lista.tail.head) :: asocia(f, lista.tail)
//  else asocia(f, lista.tail)
//}
//
//// Pruebas
//println("---Ejercicio 2b")
//def cuadrado(x:Int) = x*x  
//val lista = List(2, 4, 16, 5, 10, 100, 105)  
//asocia(cuadrado _, lista)   
//
//------------------------------------
// Ejercicio 3
//------------------------------------

// a)
def numTests(lista: List[(Int) => Boolean], n : Int) : Int = 
{
   if (lista.isEmpty)  0
   else if (lista.head(n))  1 + numTests(lista.tail, n)
   else numTests(lista.tail, n)
}

// Pruebas
println("---Ejercicio 3")

def mayorQue8 (n : Int) = n > 8
def par(n:Int) = n % 2 == 0
def impar(n:Int) = n % 2 != 0
val listaTests = List(mayorQue8 _, par _, impar _) 
println(numTests (listaTests, 12))  // => 2
println(numTests (listaTests, 3))   // => 1

//// b)
//def makeTest(p:String, n:Int) : (Int) => Boolean = 
//{
//   p match {
//      case "<" => (x:Int) => x < n
//      case ">" => (x:Int) => x > n
//      case "=" => (x:Int) => x == n
//   }
//}
//
//def generaTests(lista: List[String]) : List[(Int) => Boolean] = 
//{
//   if (lista.length < 2)  Nil
//   else makeTest(lista.head, lista.tail.head.toInt) :: generaTests(lista.tail.tail)
//}
//
//// Pruebas
//val listTest = generaTests(List(">","3","<","5",">","8","=","10")) 
//println(numTests(listTest, 10))  // => 3
//
////------------------------------------
//// Ejercicio 4
////------------------------------------
//
//// Imagen .PNG
//
////------------------------------------
//// Ejercicio 5
////------------------------------------
//
//println("---Ejercicio 5")
//
//val x = 10
//val z = 10
//def h(x:Int, y:Int, f:(Int)=>Int) : (Int)=>Int = {
//  (y:Int) => f(x+y+z)
//}
//
//val a = h(20,20,(x:Int) => x*x)
//a(10)
}