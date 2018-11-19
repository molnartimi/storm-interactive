package hu.bme.mit.modelchecker.storm.checker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hu.bme.mit.modelchecker.storm.model.InputModel;
import hu.bme.mit.modelchecker.storm.model.InputModelType;
import hu.bme.mit.modelchecker.storm.model.ModelReward;

public class StormRunner {
	
	public static Map<ModelReward, Double> run(InputModel model) {
		Map<ModelReward, Double> results = new HashMap<>();
		try {
			for (ModelReward reward: model.getRewards()) {
				Process process = new ProcessBuilder()
						.command(getArgs(model.filePath, model.type, reward)).start();
			    BufferedReader reader = 
			         new BufferedReader(new InputStreamReader(process.getInputStream()));

			    Double rewardResult = readResult(reader);
			    if (rewardResult != null) {
			    	results.put(reward, rewardResult);
			    }
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return results;
	}

	private static List<String> getArgs(String modelPath, InputModelType type, ModelReward reward) {
		List<String> args = new ArrayList<>();
		args.add("storm");
		args.add("--buildfull");
		
		switch (type) {
		case PRISM:
			args.add("--prismcompat");
			args.add("--prism");
		}
		
		args.add(modelPath);
		
		args.add("--prop");
		args.add("R{\"" + reward.name + "\"}=? [S]");
		return args;
	}

	private static Double readResult(BufferedReader reader) throws IOException {
		String line;
		while ((line = reader.readLine()) != null) {
			if (line.indexOf("Result") == 0) {
				String value = line.substring(line.indexOf(":") + 1).trim();
				return Double.valueOf(value);
			}
		}
		return null;
	}
}
