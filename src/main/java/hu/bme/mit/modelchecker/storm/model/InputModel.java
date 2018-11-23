package hu.bme.mit.modelchecker.storm.model;

import java.util.List;


public class InputModel {
	public final String filePath;
	protected List<ModelParam> params;
	protected List<ModelReward> rewards;
	
	public InputModel(String filePath, List<ModelParam> params, List<ModelReward> rewards) {
		this.filePath = filePath;
		this.params = params;
		this.rewards = rewards;
	}
	
	public List<ModelReward> getRewards() {
		return rewards;
	}

	public List<ModelParam> getParams() {
		return params;
	}

}
