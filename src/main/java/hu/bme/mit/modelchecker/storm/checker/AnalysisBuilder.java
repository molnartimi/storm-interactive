package hu.bme.mit.modelchecker.storm.checker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hu.bme.mit.modelchecker.storm.exception.StormException;
import hu.bme.mit.modelchecker.storm.model.InputModel;
import hu.bme.mit.modelchecker.storm.model.ModelParam;
import hu.bme.mit.modelchecker.storm.model.ModelReward;

public class AnalysisBuilder {
	private Map<ModelParam, Number> params = new HashMap<>(); 
	private List<ModelReward> rewards = new ArrayList<>();
	private InputModel model;
	private Double tolerance;
	private boolean silentMode = true;
	
	public AnalysisBuilder(InputModel model) {
		this.model = model;
	}
	
	public AnalysisBuilder withReward(ModelReward reward) throws StormException {
		if (!model.getRewards().contains(reward)) {
			throw new StormException("Model not contains reward with name " + reward.name + ".");
		}
		rewards.add(reward);
		return this;
	}
	
	public AnalysisBuilder withParam(ModelParam param, Number value) {
		params.put(param, value);
		return this;
	}
	
	public AnalysisBuilder withTolerance(Double tol) {
		this.tolerance = tol;
		return this;
	}
	
	public AnalysisBuilder withConsolLogInfo(boolean withLog) {
		this.silentMode = !withLog;
		return this;
	}
	
	public StormRunner build() {
		return (tolerance == null) ? new StormRunner(model, params, rewards, silentMode) : new StormRunner(model, params, rewards, tolerance, silentMode);
	}

}
