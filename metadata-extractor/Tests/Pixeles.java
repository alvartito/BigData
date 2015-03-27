import java.awt.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import java.io.*;
import javax.imageio.ImageIO;

//Autor Jose abel de la Fuente Arriaga
//CyberServer


/*PERO para que sirve agrupar los patrones???????????

BIEN FACIL PARA FORMAR FIGURAS COMPLETAS, Y EN LUGAR DE QUE TE DIGA QUE EXISTEN 200 PIXELES DE ROJOS... TE DIRA QUE EXISTEN DOS MANZANAZ YA QUE ABRA AGRUPADO LOS PIXELES..... :p :p

http://foro.elhacker.net/java/agrupamiento_de_nodos_vecino_en_un_arreglo_mineria_de_datos-t298383.0.html
*/
public class Pixeles {
	public static void main(String[] args) {
		JLabel Resultado = new JLabel();

		String arreglo[][];

		JOptionPane.showMessageDialog(null,
				"Proyecto: Reconocimiento de Patrones en imagenes \n"
						+ "Autor: Jose Abel de la fuente \n"
						+ "Correo: crow_15@hotmail.com \n"
						+ "Lenguaje: Java \n"
						+ "Arriva atlacomulco, atte: cyberServer");

		String PixelBuscar = JOptionPane
				.showInputDialog(
						"Color en Hexadecimal del pixel a Buscar \n El Default es Blanco - ffffff",
						"ffffff");
		String Archivo = JOptionPane
				.showInputDialog("Nombre de la imagen para Buscar el pixel \n Tiene que se png");
		int contador = 0;
		String Cordenadas = null;
		ImageIcon Imagen = new ImageIcon(Archivo + ".png");

		Coversion C = new Coversion();
		BufferedImage Nueva = C.toBufferedImage(Imagen.getImage());
		Graphics g = Nueva.getGraphics();

		// Agrupar
		arreglo = new String[Nueva.getWidth()][Nueva.getHeight()];
		for (int x = 0; x <= Nueva.getWidth() - 1; x++) {
			for (int y = 0; y <= Nueva.getHeight() - 1; y++) {
				String val = Integer.toHexString(
						Nueva.getRGB(x, y) & 0x00ffffff).toString();
				arreglo[x][y] = val;
			}
		}
		AgrupamientoVecinos objeto = new AgrupamientoVecinos();
		int GruposTotales = objeto.Agrupar(arreglo, PixelBuscar, true, null);
		JOptionPane.showMessageDialog(null,
				"Termino de Buscar en total fueron " + GruposTotales
						+ " grupos");

		// fin de Agrupar
		for (int x = 0; x <= Nueva.getWidth() - 1; x++) {
			for (int y = 0; y <= Nueva.getHeight() - 1; y++) {
				String val = Integer.toHexString(
						Nueva.getRGB(x, y) & 0x00ffffff).toString();
				if (val.equals(PixelBuscar)) {
					Cordenadas = Cordenadas + " (" + x + "," + y + ") ";
					contador++;
					g.setColor(Color.blue);
					g.fillOval(x, y, 10, 10);
				}
			}
		}

		// JOptionPane.showMessageDialog(null,"Tiene " + contador +
		// " elementos que buscas");
		// JOptionPane.showMessageDialog(null,"Cordenadas " + Cordenadas);

		if (contador != 0) {
			try {
				ImageIO.write(Nueva, "jpg", new File("Resultado.png"));
			} catch (IOException e) {
				System.out.println("Error de escritura");
			}
			Resultado.setIcon(new ImageIcon("Resultado.png"));
			JOptionPane.showMessageDialog(null,
					"Numero de Pixeles involucrados " + contador);

			JOptionPane.showMessageDialog(null, Resultado);
		} else {
			JOptionPane.showMessageDialog(null, "No se encontro ningun patron");
		}
	}

}

class Coversion {

	BufferedImage toBufferedImage(Image image) {
		if (image instanceof BufferedImage) {
			return ((BufferedImage) image);
		} else {
			image = new ImageIcon(image).getImage();
			BufferedImage bufferedImage = new BufferedImage(
					image.getWidth(null), image.getHeight(null),
					BufferedImage.TYPE_INT_RGB);
			Graphics g = bufferedImage.createGraphics();
			g.drawImage(image, 0, 0, null);
			g.dispose();

			return (bufferedImage);
		}
	}
}

class AgrupamientoVecinos {
	public String ValorNuevo;
	public String ValorViejo;
	public String arreglo[][];

	public int Agrupar(String ArregloDeBusqueda[][], String ValorABuscar,
			boolean RenombradoAutomaticoGrupos, String ValorAModificar) {
		int grupos = 0;
		arreglo = ArregloDeBusqueda;
		ValorNuevo = ValorAModificar;
		ValorViejo = ValorABuscar;

		for (int yx = 0; yx < ArregloDeBusqueda.length; yx++) {
			for (int yy = 0; yy < ArregloDeBusqueda[0].length; yy++) {
				if (ArregloDeBusqueda[yx][yy].equals(ValorViejo)) {
					if (RenombradoAutomaticoGrupos == false) {
						grupos++;
						this.BuscarVecino(yx, yy);
					} else {
						grupos++;
						ValorNuevo = Integer.toString(grupos);
						this.BuscarVecino(yx, yy);
					}
				}
			}
		}
		return grupos;
	}

	private void BuscarVecino(int x, int y) {
		arreglo[x][y] = ValorNuevo;
		if (arreglo[x][y - 1].equals(ValorViejo)) {
			BuscarVecino(x, y - 1);
		}
		if (arreglo[x - 1][y - 1].equals(ValorViejo)) {
			BuscarVecino(x - 1, y - 1);
		}
		if (arreglo[x - 1][y].equals(ValorViejo)) {
			BuscarVecino(x - 1, y);
		}
		if (arreglo[x - 1][y + 1].equals(ValorViejo)) {
			BuscarVecino(x - 1, y + 1);
		}
		if (arreglo[x][y + 1].equals(ValorViejo)) {
			BuscarVecino(x, y + 1);
		}
		if (arreglo[x + 1][y + 1].equals(ValorViejo)) {
			BuscarVecino(x + 1, y + 1);
		}
		if (arreglo[x + 1][y].equals(ValorViejo)) {
			BuscarVecino(x + 1, y);
		}
		if (arreglo[x + 1][y - 1].equals(ValorViejo)) {
			BuscarVecino(x + 1, y - 1);
		}
	}

}
