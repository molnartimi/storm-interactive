package hu.bme.mit.modelchecker.storm.checker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hu.bme.mit.modelchecker.storm.exception.StormException;
import hu.bme.mit.modelchecker.storm.model.InputModel;
import hu.bme.mit.modelchecker.storm.model.ModelParam;
import hu.bme.mit.modelchecker.storm.model.ModelReward;

public class StormRunner {
	private Map<ModelParam, Number> params = new HashMap<>(); 
	private List<ModelReward> rewards = new ArrayList<>();
	private InputModel model;
	private Logger logger;
	private Double tolerance;
	private boolean silent;

	public StormRunner(InputModel model, Map<ModelParam, Number> params, List<ModelReward> rewards) {
		this(model, params, rewards, true);
	}
	
	public StormRunner(InputModel model, Map<ModelParam, Number> params, List<ModelReward> rewards, boolean silent) {
		this.params = params;
		this.rewards = rewards;
		this.model = model;
		logger = LoggerFactory.getLogger("StormRunner");
		this.silent = silent;
	}

	public StormRunner(InputModel model, Map<ModelParam, Number> params, List<ModelReward> rewards, Double tolerance) {
		this(model, params, rewards, true);
		this.tolerance = tolerance;
	}
	
	public StormRunner(InputModel model, Map<ModelParam, Number> params, List<ModelReward> rewards, Double tolerance, boolean silent) {
		this(model, params, rewards, silent);
		this.tolerance = tolerance;
	}

	public AnalysisResult runSteadyStateCheck() throws IOException, StormException {
		Process process = new ProcessBuilder().command(createCommandArgs()).start();
		return parseResponse(process.getInputStream());
	}

	private List<String> createCommandArgs() throws StormException {
		List<String> args = new ArrayList<>();
		
		args.add("storm");
		args.addAll(getExtraArgsBeforeModelPath());
		args.add(model.filePath);
		args.add("--buildfull");
		args.add("-const");
		args.add(getParamValues());
		args.add("--prop");
		args.add(getPropertyString());
		if (tolerance != null) {
			args.add("--precision");
			args.add(Double.toString(tolerance));
		}
		
		return args;
	}

	private String getPropertyString() {
		String rewardProperties = "";
		for (ModelReward reward: rewards) {
			rewardProperties += "R{\"" + reward.name + "\"}=? [ S ];";
		}
		return rewardProperties;
	}

	private String getParamValues() {
		List<String> paramsWithValues = new ArrayList<>();
		for (Map.Entry<ModelParam, Number> paramEntry: params.entrySet()) {
			paramsWithValues.add(paramEntry.getKey().name + "=" + paramEntry.getValue());
		}
		return String.join(",", paramsWithValues);
	}

	private List<String> getExtraArgsBeforeModelPath() throws StormException {
		String fileExtension = model.filePath;
		while (fileExtension.contains(".")) {
			fileExtension = fileExtension.substring(model.filePath.indexOf(".") + 1);
		}
		switch (fileExtension) {
		case "jani":
			return Arrays.asList(new String[] {"--jani"});
		case "prism":
		case "sm":
			return Arrays.asList(new String[] {"-pc", "--prism"});
		default: throw new StormException(fileExtension + " file format not supported by storm-interactive");
		}
	}
	
	private AnalysisResult parseResponse(InputStream inputStream) throws IOException, StormException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		Map<ModelReward, Double> result = new HashMap<>();
		
		String line = "";
		ModelReward checkingReward = null;
		while ((line = reader.readLine()) != null) {
			if (!silent) logger.info(line);
			
			if (line.contains("ERROR")) {
				while ((line = reader.readLine()) != null) {
					logger.info(line);
				}
				throw new StormException("Error occured at running storm");
			} else if (line.indexOf("Model checking property") == 0) {
				String rewardName = line.substring(line.indexOf("{") + 2, line.indexOf("}") - 1);
				checkingReward = rewards.stream()
						  .filter(reward -> reward.name.equals(rewardName))
						  .findAny()
						  .orElse(null);
			} else if (line.indexOf("Result") == 0 && checkingReward != null) {
				result.put(checkingReward, Double.valueOf(line.substring(line.indexOf(":") + 1).trim()));
			}
		}
		
		return new AnalysisResult(result);
	}
}
