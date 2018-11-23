package hu.bme.mit.modelchecker.storm.model;

import java.util.ArrayList;
import java.util.List;

public class InputModelBuilder {
	protected String filePath;
	protected List<ModelParam> params = new ArrayList<>();
	protected List<ModelReward> rewards = new ArrayList<>();
	
	public InputModelBuilder(String filePath) {
		this.filePath = filePath;
	}
	
	public InputModelBuilder withParam(ModelParam param) {
		params.add(param);
		return this;
	}
	
	public InputModelBuilder withReward(ModelReward reward) {
		rewards.add(reward);
		return this;
	}
	
	public InputModel build() {
		return new InputModel(filePath, params, rewards);
	}

}
