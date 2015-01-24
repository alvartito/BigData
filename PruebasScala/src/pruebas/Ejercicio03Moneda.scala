package pruebas

/**
   * Define en Scala un procedimiento que simule una moneda que, cada vez que se invoque,
   * devuelva "cara" o "cruz" alternativamente.
   * <p>Cada x tiradas, caerá de "canto", y a la siguiente tirada volverá a caer del lado que tocaba.
   * <p>El valor x se determinará en la creación de la moneda.
   */
object Ejercicio03Moneda extends App {
  
  def makeMoneda(num:Int) :() => String= {
    var last:String="cara"
    var it:Int=0
    ()=>{
      if(it==num){
        it=0
        "canto"
      } else {
        it+=1
        if(last=="cara"){
          last="cruz"
        } else {
          last="cara"
        }
        last
      }
    }
  }

  //Llamamos a la función que se ejecuta dentro de makeContador
  println( makeMoneda(5)())
  println( makeMoneda(5)())
  println( makeMoneda(5)())
  println( makeMoneda(5)())
  println( makeMoneda(5)())

}