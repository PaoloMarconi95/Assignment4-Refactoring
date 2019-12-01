package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileParser {

	List<String> allLine = new ArrayList<String>();


	FileParser(File file) {
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String actual_line;
			String line_from_file = br.readLine();
			while ((actual_line = line_from_file) != null) {
				allLine.add(actual_line);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public List<String> getAllLine() {
		return allLine;
	}


	public ArrayList<String[]> parseLines(String separator) {
		ArrayList<String[]> parsedLines = new ArrayList<>();
		String[] tmpLine;
		for (String line : allLine) {
			tmpLine = line.split(separator);
			parsedLines.add(tmpLine);
		}
		return parsedLines;
	}

}