package pruebas

/**
 * Implementa en Scala un programa que permita construir parkímetros
 * utilizando estado local (al estilo del ejemplo del contador con estado y mensajes).
 * La clausura devuelta tendrá el perfil (String,Double)=>Double, siendo el String
 * el mensaje que especifica la acción a hacer por el parkímetro, el Double el
 * parámetro necesario en algún mensaje. En el caso que no necesite el parámetro,
 * se pasará un valor por defecto como 0.0.
 * Mensajes y acciones a realizar:
 * <p>"pagar": se añade dinero al parkímetro. Cuesta 0.01 euros/minuto.
 * <p>"tiempo": paraconsultar el tiempo que le queda
 * <p>"total": para saber cuánto dinero ha recogido un determinado parkimetro.
 * <p>"tick": El tiempo se decrementa manualmente simulando el tick de reloj.
 *
 */
object Ejercicio02Parkimetro extends App {
  
  def makeParkimetro(): (String, Double) => Double = {
    var dineroAcc: Double = 0
    var tiempo: Double = 0
    (accion: String, cantidad: Double) => {
      def pagar() = {
        tiempo + cantidad / 0.01
        dineroAcc += cantidad
        tiempo
      }
      accion match {
        case "pagar"  => pagar()
        case "tiempo" => tiempo
        case "total"  => dineroAcc
        case "tick" => {
          if (tiempo > 0) {
            tiempo = tiempo - 1
          }
          tiempo
        }
      }
    }
  }

  val part1 = makeParkimetro()
  println("pagar "+part1("pagar", 1))
  println("tick "+part1("tick", 1))
  println(part1("tiempo", 1))
  println(part1("tiempo", 1))
  println(part1("tiempo", 1))
  println(part1("tiempo", 1))
  println( part1("total", 1))

}