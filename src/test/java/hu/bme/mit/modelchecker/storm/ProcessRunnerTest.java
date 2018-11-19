package hu.bme.mit.modelchecker.storm;

import java.util.Iterator;
import java.util.Map;

import org.junit.Test;

import hu.bme.mit.modelchecker.storm.checker.StormRunner;
import hu.bme.mit.modelchecker.storm.exception.NotSupportedModelTypeException;
import hu.bme.mit.modelchecker.storm.model.InputModel;
import hu.bme.mit.modelchecker.storm.model.ModelReward;

public class ProcessRunnerTest {
	
	@Test
	public void runStatic() {
		try {
			Iterator<Map.Entry<ModelReward, Double>> it = StormRunner.run(new InputModel("/home/storm/onlab/smpl.prism")).entrySet().iterator();
		    while (it.hasNext()) {
		        Map.Entry pair = (Map.Entry)it.next();
		        System.out.println(((ModelReward)pair.getKey()).name + " = " + pair.getValue());
		    }
			;
		} catch (NotSupportedModelTypeException e) {
			e.printStackTrace();
		}
	}

}
