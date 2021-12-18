package input_sources;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import application.Application;
import data.Dataset;
import data.Type;

public class FileManager {
	private static final Integer TXT_FILETYPE = 1;
	private static final Integer CSV_FILETYPE = 2;
	private static final Integer DEFAULT_FILETYPE = TXT_FILETYPE;
	public static String default_dataset_file = "resources/seeds_dataset.txt";
	public static String default_dataset_save_file = "saved_dataset.txt";
	
	public static Dataset extract_dataset(String dataset_file) throws FileNotFoundException {
		if (dataset_file==null) dataset_file = default_dataset_file;
		
		BufferedReader reader = new BufferedReader(new FileReader(dataset_file));
		String line;
		Integer format = detect_format(dataset_file);
		String separator = "\t";
		if (format == CSV_FILETYPE)
			separator = ",";
		else if (format == TXT_FILETYPE)
			separator = "[\\s\\t]+";
		try {
			ArrayList<String> datalines = new ArrayList<>();
			while (true) {
				line = reader.readLine();if (line == null) break;
				datalines.add(line);
			}
			reader.close();
			return interpretLines(datalines, separator);
			
			
		} catch (IOException e) {
			try {
				reader.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		return null;
		
	}
	
	private static Dataset interpretLines(ArrayList<String> datalines, String separator) {
		/** extracts rows from space-separated lines (Strings)
		 * eg: 1.5 1.45 156.2 --> [1.5, 1.45, 156.2]
		 * */
		// detect data dimensions
		int n = datalines.size();
		int m = datalines.get(0).split(separator).length;
		// create JTable
		
		Type[] types = new Type[m];
		// allocate data matrix
		Double data[][] = new Double[n][m];
		// fill data matrix with values
		int i = 0;
		for (String line : datalines) {
			int j = 0;
			for (String str_value : line.split(separator)) {
				Double value = null;
				try {value = Double.parseDouble(str_value);} catch(Exception e) {};
				types[j] = Type.parse(str_value).combine(types[j]);
				data[i][j++] = value;
			}i++;
		}

		return new Dataset(null,n,m, data, types);
	}

	public static void save_dataset(Dataset dataset, String dest_file) throws FileNotFoundException {
		if (dest_file==null) dest_file = default_dataset_save_file;
		Integer format = detect_format(dest_file);
		String separator = "\t";
		if (format == CSV_FILETYPE)
			separator = ",";
		else if (format == TXT_FILETYPE)
			separator = "\t";
		PrintWriter writer = new PrintWriter(dest_file);
		for (int i=0; i<dataset.n; i++) {
			for (int j=0; j<dataset.m; j++) {
				writer.print(dataset.data[i][j]);
				if (j<dataset.m-1) writer.print(separator); 
			}
			if (i<dataset.n-1) writer.print("\n");
		}
		writer.close();
	}
	
	public static Integer detect_format(String file_path) {
		//file_path.replaceAll("\\", "/"); // using the / as separator
		String fileparts[] = file_path.split("/");
		String filename = fileparts[fileparts.length-1]; // last element is file name
		Matcher m = match(".*?\\.(.+)", filename);
		if (m != null) {
			if (m.group(1).equals("csv")) {
				return CSV_FILETYPE;
			} else if (m.group(1).equals("txt")) {
				return TXT_FILETYPE;
			}
		}
		return DEFAULT_FILETYPE;
	}
	
	public static Matcher match(String pattern,String text) {
	    Pattern p = Pattern.compile(pattern);
		Matcher matcher = p.matcher(text);
		return matcher.find() ? matcher : null;
	}
	
	// file chooser
	public static String ChooseFileWindow() {
		JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("ficher texte (.txt) ou CSV (.csv)", new String[] { "txt", "csv" }));
        fileChooser.showOpenDialog(null);
        return fileChooser.getSelectedFile().getAbsolutePath();
	}
	
	public static String SaveFileWindow() {
		JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setDialogTitle("Save");
        fileChooser.setApproveButtonText("Save");
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("ficher texte (.txt) ou CSV (.csv)", new String[] { "txt", "csv" }));
        fileChooser.showOpenDialog(null);
        return fileChooser.getSelectedFile().getAbsolutePath();
	}
	
	public static String ChooseFileWindow(Application application) {
		try {
			return ChooseFileWindow();
		} catch (Exception e) {
			application.afficherMessage("Le fichier ou l'URL que vous avez introduit est érroné");
			return null;
		}
	}
}
