package hu.bme.mit.modelchecker.storm;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import hu.bme.mit.modelchecker.storm.checker.AnalysisBuilder;
import hu.bme.mit.modelchecker.storm.checker.AnalysisResult;
import hu.bme.mit.modelchecker.storm.exception.StormException;
import hu.bme.mit.modelchecker.storm.model.InputModel;
import hu.bme.mit.modelchecker.storm.model.InputModelBuilder;
import hu.bme.mit.modelchecker.storm.model.ModelParam;
import hu.bme.mit.modelchecker.storm.model.ModelReward;

public class ProcessRunnerTest {
	
	@Test
	public void SimpleServerTest() {
		ModelParam serviceTime = new ModelParam("serviceTime");
		ModelParam requestRate = new ModelParam("requestRate");
		ModelReward Idle = new ModelReward("Idle");
		ModelReward ServedRequests = new ModelReward("ServedRequests");
		InputModel smpl = new InputModelBuilder("/home/storm/onlab/smpl.prism")
				.withParam(serviceTime)
				.withParam(requestRate)
				.withReward(Idle)
				.withReward(ServedRequests)
				.build();
		try {
			AnalysisResult result = new AnalysisBuilder(smpl)
					.withParam(serviceTime, 0.25)
					.withParam(requestRate, 1.5)
					.withReward(Idle)
					.withReward(ServedRequests)
					.build()
					.runSteadyStateCheck();
			
			assertEquals(result.getResultOf(Idle), 0.7272727273, 1e-5);
			assertEquals(result.getResultOf(ServedRequests), 1.090909091, 1e-5);
		} catch (IOException | StormException e) {
			e.printStackTrace();
		}
		
	}

}
