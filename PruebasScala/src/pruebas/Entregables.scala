package pruebas

/**
 * @author cloudera
 */
class Entregables {
  /*Ejercicio 1
Define en Scala la función recursiva pura (sin tail recursion) aplica3D que reciba una lista de coordenadas 3D en forma de tupla, una función unaria y una coordenada (“x”, “y” ó “z”). Deberá aplicar la función a los elementos correspondientes a esa coordenada y devolver las nuevas coordenadas. Ejemplo:
val lista=List((1,2,3),(4,5,6),(7,8,9),(10,11,12))
def suma(x:Int)=x+2
aplica3D(lista,suma_,“y”)→ ((1,4,3),(4,7,6),
(7,10,9),(10,13,12))


* Ejercicio 2
*
Escribe en Scala el procedimiento numTests(List[(Int)=>Boolean],Int):Int que toma una lista de tests y un número n y que devuelva el número de tests de la lista que pasa el número n. Por ejemplo, supongamos los tests: mayorQue8(x),par(x),impar(x)
vallistaTests=List(mayorQue8_, par_, impar_)
numTests(listaTests,12) —> 2
numTests(listaTests,3) —> 1

* Ejercicio 3
*
Implementa en Scala el procedimiento: 
generaTest(List[String]):List[(Int)=>Boolean] 
Devuelve una lista de tests a partir de una lista de expresiones. La lista de expresiones tendrá la forma de ("op1" "n1" … "opn" "nn"), donde los operadores pueden ser las cadenas “>", "<" o "=". Puedes definir funciones auxiliares.
Por ejemplo: generaTests(List(">","3","<","5",">","8","=","10"))devolverá una lista con 4 tests: la comprobación de si un número es mayor que 3, menor que 5, mayor que 8 y igual que 10. Esta lista se podría utilizar en el ejercicio anterior:
vallistTest=generaTests(
List(“>","3","<","5",">","8","=","10"))
numTests(listTest,10)→ 3
Nota: Puedes utilizar el método de cadenas toIntpara transformar una cadena a un entero.*/
}