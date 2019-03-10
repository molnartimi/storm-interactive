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
		InputModel smpl = new InputModelBuilder("/home/meres/git/Stochastic-Optimization/STORM/models/simple-server.prism")
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
					.withConsolLogInfo(true)
					.build()
					.runSteadyStateCheck();
			
			assertEquals(result.getResultOf(Idle), 0.7272727273, 1e-5);
			assertEquals(result.getResultOf(ServedRequests), 1.090909091, 1e-5);
		} catch (IOException | StormException e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void Phil3Test() {
		ModelParam phil1_eatingRate = new ModelParam("phil1_eatingRate");
		ModelParam phil2_eatingRate = new ModelParam("phil2_eatingRate");
		ModelParam phil3_eatingRate = new ModelParam("phil3_eatingRate");
		ModelReward phil1_thinkingTime = new ModelReward("phil1_thinkingTime");
		ModelReward phil2_thinkingTime = new ModelReward("phil2_thinkingTime");
		ModelReward phil3_thinkingTime = new ModelReward("phil3_thinkingTime");
		ModelReward total_thinkingTime = new ModelReward("Table_totalThinkingTime");
		InputModel fil3 = new InputModelBuilder("/home/meres/git/Stochastic-Optimization/STORM/models/philosophers_3.prism")
				.withParam(phil1_eatingRate)
				.withParam(phil2_eatingRate)
				.withParam(phil3_eatingRate)
				.withReward(phil1_thinkingTime)
				.withReward(phil2_thinkingTime)
				.withReward(phil3_thinkingTime)
				.withReward(total_thinkingTime)
				.build();
		try {
			AnalysisResult result = new AnalysisBuilder(fil3)
					.withParam(phil1_eatingRate, 7.33569408796258)
					.withParam(phil2_eatingRate, 2.156376928966199)
					.withParam(phil3_eatingRate, 9.680783503298795)
					.withReward(phil1_thinkingTime)
					.withReward(phil2_thinkingTime)
					.withReward(phil3_thinkingTime)
					.withReward(total_thinkingTime)
					.withConsolLogInfo(true)
					.build()
					.runSteadyStateCheck();
			
			assertEquals(result.getResultOf(phil1_thinkingTime), 0.612806126890481, 1e-5);
			assertEquals(result.getResultOf(phil2_thinkingTime), 0.539690629168788, 1e-5);
			assertEquals(result.getResultOf(phil3_thinkingTime), 0.971340613831368, 1e-5);
			assertEquals(result.getResultOf(total_thinkingTime), 2.12383736989064, 1e-5);
		} catch (IOException | StormException e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void Phil3JaniTest() {
		ModelParam phil1_eatingRate = new ModelParam("phil1_eatingRate");
		ModelParam phil2_eatingRate = new ModelParam("phil2_eatingRate");
		ModelParam phil3_eatingRate = new ModelParam("phil3_eatingRate");
		ModelReward phil1_thinkingTime = new ModelReward("phil1_thinkingTime");
		ModelReward phil2_thinkingTime = new ModelReward("phil2_thinkingTime");
		ModelReward phil3_thinkingTime = new ModelReward("phil3_thinkingTime");
		InputModel fil3 = new InputModelBuilder("/home/meres/git/Stochastic-Optimization/STORM/models/philosophers_3_bounded_mod.jani")
				.withParam(phil1_eatingRate)
				.withParam(phil2_eatingRate)
				.withParam(phil3_eatingRate)
				.withReward(phil1_thinkingTime)
				.withReward(phil2_thinkingTime)
				.withReward(phil3_thinkingTime)
				.build();
		try {
			AnalysisResult result = new AnalysisBuilder(fil3)
					.withParam(phil1_eatingRate, 7.33569408796258)
					.withParam(phil2_eatingRate, 2.156376928966199)
					.withParam(phil3_eatingRate, 9.680783503298795)
					.withReward(phil1_thinkingTime)
					.withReward(phil2_thinkingTime)
					.withReward(phil3_thinkingTime)
					.withConsolLogInfo(true)
					.build()
					.runSteadyStateCheck();
			
			assertEquals(result.getResultOf(phil1_thinkingTime), 0.612806126890481, 1e-5);
			assertEquals(result.getResultOf(phil2_thinkingTime), 0.539690629168788, 1e-5);
			assertEquals(result.getResultOf(phil3_thinkingTime), 0.971340613831368, 1e-5);
		} catch (IOException | StormException e) {
			e.printStackTrace();
		}
		
	}
}
