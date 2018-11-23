package hu.bme.mit.modelchecker.storm.checker;

import java.util.Map;

import hu.bme.mit.modelchecker.storm.model.ModelReward;

public class AnalysisResult {
	private Map<ModelReward, Double> resultValues;

	public AnalysisResult(Map<ModelReward, Double> result) {
		this.resultValues = result;
	}
	
	public Double getResultOf(ModelReward reward) {
		return resultValues.get(reward);
	}
}
