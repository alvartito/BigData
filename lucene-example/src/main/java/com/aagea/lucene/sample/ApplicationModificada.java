package com.aagea.lucene.sample;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;


/**
 * map-reduce casero. Primero un map, un shuffle en memoria, y un reduce al
 * realizar de nuevo la busqueda.
 */
public class ApplicationModificada {
	// Directorio en memoria.
	private static RAMDirectory ramDirectory = new RAMDirectory();
	// Si añaden * tiene que devolver los 10 más relevantes. Hacer método o algo
	// para realizar la búsqueda.
	/**
	 * Para los mas relevantes, tomar los 10 más relevantes de cada indice, con
	 * lo que me aseguro que en los 40 recuperados, estan los mas relevantes.
	 * 
	 * Indexo los documentos recuperados en un RAMDirectory.
	 * 
	 * Por arte de magia, esto funcionará...
	 * 
	 * Top-K queries
	 */

	private static SimpleFSDirectory fsDirectory = null;

	private static String path = "data/";
	private static String directorio = "";

	// Utilizar otro analyzer, StandardAnalyzer
	private static StandardAnalyzer sAnalyzer = new StandardAnalyzer(
			Version.LUCENE_47);

	private static IndexWriterConfig config = new IndexWriterConfig(
			Version.LUCENE_47, sAnalyzer);

	private static ArrayList<String> tiposValidos = new ArrayList<String>();

	private static void setTiposValidos() {
		tiposValidos.add("A");
		tiposValidos.add("B");
		tiposValidos.add("C");
		tiposValidos.add("D");
		tiposValidos.add("*");
	}

	public static void main(String[] args) throws IOException {
		setTiposValidos();
		readCommand();
	}

	private static void readCommand() throws IOException {
		String command = "";
		String res = "";
		System.out.println("\nPath:");
		command = readLine();

		setDirectorio(command);

		// Para salir del sistema
		while (!res.equals("e")) {

			System.out
					.println("\n(A)dd information - (S)earch information - (E)xit");
			res = readLine();

			if (res.toLowerCase().equals("a")) {
				// Añadir informacion
				addDocument();
			} else if (res.toLowerCase().equals("s")) {
				// Buscar
				searchDocument();
			} else if (res.toLowerCase().equals("e")) {
				// Salir
				endApplication();
			}
		}
	}

	private static String readTipo() throws IOException {
		String tipoUsuario = "";
		while (!tiposValidos.contains(tipoUsuario.toUpperCase())) {
			System.out.println("Seleccione tipo de usuario: A, B, C o D");
			System.out.println("Type:");
			tipoUsuario = readLine();
		}
		return tipoUsuario;
	}

	private static void endApplication() {
		System.out.println("Goodbye!!");
	}

	/**
	 * Lee un documento, si existe, y muestro la informacion por pantalla
	 * 
	 * Pedir el tipo de usuario antes de realizar la búsqueda.
	 * 
	 * */
	private static void searchDocument() throws IOException {
		String tipoUsuario = readTipo();
		System.out.println("Query:");
		String queryStr = readLine();

		if("*".equals(tipoUsuario)){
			getTopTen(queryStr);
		} else {
			setDirectorioTipo(tipoUsuario);
			
			IndexReader reader = DirectoryReader.open(fsDirectory);
			IndexSearcher searcher = new IndexSearcher(reader);
			Query query = new WildcardQuery(new Term("name", queryStr));
			//Numero de registros
			TopDocs search = searcher.search(query, 10);
			Document document;
			for (ScoreDoc scoreDoc : search.scoreDocs) {
				document = searcher.doc(scoreDoc.doc);
				System.out.println("Name ==>" + document.get("name"));
				System.out.println("Data ==>" + document.get("data"));
				System.out.println("Type ==>" + document.get("type"));
			}
			reader.close();
		}
	}

	private static void getTopTenPorUsuario(String tipoUsuario, String queryStr) throws IOException {
		setDirectorioTipo(tipoUsuario);

		IndexReader reader = DirectoryReader.open(fsDirectory);
		IndexWriter writer = new IndexWriter(fsDirectory, config.clone());
		IndexSearcher searcher = new IndexSearcher(reader);
		Query query = new WildcardQuery(new Term("name", queryStr));
		//Numero de registros
		TopDocs search = searcher.search(query, 10);
		Document document;
		for (ScoreDoc scoreDoc : search.scoreDocs) {
			document = searcher.doc(scoreDoc.doc);

			document.add(new StringField("name", document.get("name"), Field.Store.YES));
			document.add(new TextField("data", document.get("data"), Field.Store.YES));
			document.add(new TextField("type", document.get("type"), Field.Store.YES));

			writer.addDocument(document);
			writer.commit();

		}
		reader.close();
		writer.close();
	}
	
	private static void getTopTen(String queryTopTen) throws IOException {
		
		//recuperar los 10 más relevantes de cada tipo (de cada indice a, b, c o d)
		getTopTenPorUsuario("A", queryTopTen);
		getTopTenPorUsuario("B", queryTopTen);
		getTopTenPorUsuario("C", queryTopTen);
		getTopTenPorUsuario("D", queryTopTen);

		IndexReader reader = DirectoryReader.open(ramDirectory);
		IndexSearcher searcher = new IndexSearcher(reader);
		Query query = new WildcardQuery(new Term("name", queryTopTen));
		//Numero de registros
		TopDocs search = searcher.search(query, 10);
		Document document;
		for (ScoreDoc scoreDoc : search.scoreDocs) {
			document = searcher.doc(scoreDoc.doc);
			System.out.println("Name ==>" + document.get("name"));
			System.out.println("Data ==>" + document.get("data"));
			System.out.println("Type ==>" + document.get("type"));
		}
		reader.close();
	}
	
	
	/**
	 * Lee un documento, lo añade, hace commit
	 * */
	private static void addDocument() throws IOException {
		// El campo tipoUsuario es nuevo.
		IndexWriter writer = new IndexWriter(fsDirectory, config.clone());
		System.out.println("Name:");
		String name = readLine();
		System.out.println("Data:");
		String data = readLine();
		String tipoUsuario = readTipo();
		setDirectorioTipo(tipoUsuario);
		Document document = new Document();
		document.add(new StringField("name", name, Field.Store.YES));
		document.add(new TextField("data", data, Field.Store.YES));
		document.add(new TextField("type", tipoUsuario, Field.Store.YES));
		writer.addDocument(document);
		writer.commit();
		writer.close();
	}

	private static String readLine() throws IOException {
		if (System.console() != null) {
			return System.console().readLine();
		}
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				System.in));
		return reader.readLine();
	}

	private static void setDirectorio(String dir) throws IOException {
		// Cambiar el directorio por uno de disco.
		// Crear archivo de configuracion por si queremos trabajar en varios
		// directorios.
		// El directorio donde se vaya a guardar, se pasa por consola.
		directorio = dir;
		File f = new File(path + directorio);
		if (f.exists()) {
			fsDirectory = new SimpleFSDirectory(f);
		} else {
			f.mkdir();
			fsDirectory = new SimpleFSDirectory(f);
		}
	}

	private static void setDirectorioTipo(String tipoUsuarioSeleccionado)
			throws IOException {
		// El directorio donde se vaya a guardar, se pasa por consola.
		File f = new File(path + directorio + "/" + tipoUsuarioSeleccionado);
		if (f.exists()) {
			fsDirectory = new SimpleFSDirectory(f);
		} else {
			f.mkdir();
			fsDirectory = new SimpleFSDirectory(f);
		}
	}


	
}
