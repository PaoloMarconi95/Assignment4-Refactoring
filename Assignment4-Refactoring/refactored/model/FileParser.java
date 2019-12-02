package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileParser {

	List<String> allLine = new ArrayList<>();


	FileParser(File file) {
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line = br.readLine();
			while ((line = br.readLine()) != null) {
				allLine.add(line);
			}
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
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