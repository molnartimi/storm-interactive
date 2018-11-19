package hu.bme.mit.modelchecker.storm.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import hu.bme.mit.modelchecker.storm.exception.NotSupportedModelTypeException;

public class InputModel {
	public final String filePath;
	public final InputModelType type;
	protected List<ModelConst> constants;
	protected List<ModelReward> rewards;
	
	public InputModel(String filePath) throws NotSupportedModelTypeException {
		this.filePath = filePath;
		constants = new ArrayList<>();
		rewards = new ArrayList<>();
		
		String fileType = filePath.substring(filePath.indexOf(".") + 1);
		switch (fileType) {
		case "prism":
			this.type = InputModelType.PRISM;
			parsePrismFile();
			break;
		default:
			throw new NotSupportedModelTypeException();
		}
		
		constants = Collections.unmodifiableList(constants);
		rewards = Collections.unmodifiableList(rewards);
	}
	
	public List<ModelReward> getRewards() {
		return rewards;
	}

	private void parsePrismFile() {
		File file = new File(filePath); 
		
		try {
			Scanner sc = new Scanner(file);
			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				
				if (line.indexOf("const") == 0) {
					if (line.contains("=")) {
						line.replace("=*", "");
					}
					String[] words = line.split(" ");
					constants.add(new ModelConst(words[words.length - 1].trim()));
					
				} else if (line.indexOf("rewards") == 0) {
					
					line = line.trim();
					String[] words = line.split(" ");
					rewards.add(new ModelReward(words[words.length - 1].replaceAll("\"", "")));
				}
			} 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
	}

}
