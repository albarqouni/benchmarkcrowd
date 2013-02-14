package tools.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import CrowdBenchmark.models.Answer;

public class ConfigWriter extends Writer{
	private static ConfigWriter singleton;
	private static String outputPath;
	
	private ConfigWriter() {
		//outputPath = Mainconfig.getInstance().getListConfig().get("Output");
		outputPath = "./";
	}

	public static ConfigWriter getInstance() {
		if (singleton == null) {
			singleton = new ConfigWriter();
		}
		return singleton;
	}
	
	public String WriteToFile(HashMap<String, String> input, String filename) {
		File outfile = new File(filename);
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(outfile));
			String configPart = ConfigurationPart(input);
			String algoPart = AlgorithmsPart(input);
			String datasets = Datasets(input);
			String workersPart = WorkersPart(input);
			String questionsPart = QuestionsPart(input);
			String feedbacksPart = FeedBacksPart(input);
			bw.write(configPart);
			bw.write(algoPart);
			bw.write(datasets);
			bw.write(workersPart);
			bw.write(questionsPart);
			bw.write(feedbacksPart);
			bw.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return outfile.getAbsolutePath();
	}
	private String ConfigurationPart(HashMap<String, String> input){
		String content ="[Random]\n";
		content += "seed = " + input.get("seed") + "\n";
		content += "\n";
		content +="[Path]\n";
		content += "Output = " + input.get("Output") + "\n";
		content += "CSVoutput = " + input.get("CSVoutput") + "\n";
		content += "\n";
		content +="[MainIterator]\n";
		content += "Iterator = " + input.get("Iterator") + "\n";
		content += "\n";
		return content;
	}
	private String FeedBacksPart(HashMap<String, String> input){
		String content ="[FeedBacks]\n";
		content +="//FeedBacksPerQuestionDistributor, FeedBacksPerWorkerDistributor, FeedBacksConstraintDistributor\n";
		content += "feedbacksDistributor = " + input.get("feedbacksDistributor") + "\n";
		content +="// set 0 for unlimited\n";
		content += "feedbackRatio = " + input.get("feedbackRatio") + "\n";
		content += "feedbacksPerWorker = " + input.get("feedbacksPerWorker") + "\n";
		content += "feedbackRatioQuestion = " + input.get("feedbackRatioQuestion") + "\n";
		content += "feedbacksPerQuestion = " + input.get("feedbacksPerQuestion") + "\n";
		content +="// Normal, TwoCoin\n";
		content += "feedbackModel = " + input.get("feedbackModel") + "\n";
		content += "\n";
		return content;
	}
	private String QuestionsPart(HashMap<String, String> input){
		String content ="[Questions]\n";
		content += "QuestionDistributor = " + input.get("QuestionDistributor") + "\n";
		content += "HoneyPotRatio = " + input.get("HoneyPotRatio") + "\n";
		content += "NumOfQuestion = " + input.get("NumOfQuestion") + "\n";
		content +="// QuestionRatio = 0.6\n";
		content +="// constrains of number Yes/No\n";
		content += "InputDataConstrains = " + input.get("InputDataConstrains") + "\n";
		content += "InputDataRatio = " + input.get("InputDataRatio") + "\n";
		content +="//Binary, Multiple\n";
		content += "InputDataType = " + input.get("InputDataType") + "\n";
		content +="// if Multiple\n";
		content += "InputNumberLabels = " + input.get("InputNumberLabels") + "\n";
		content +="// % or fix(n)\n";
		content += "InputRatioLabels = " + input.get("InputRatioLabels") + "\n";
		content += "\n";
		return content;
	}
	private String WorkersPart(HashMap<String, String> input){
		String content ="[Workers]\n";
		content += "total = " + input.get("total") + "\n";
		content +="// spammers = total - WorkersTruthful\n";
		content +="// WorkersTruthful: % or fix(n)\n";
		content += "WorkerTypeRatio = " + input.get("WorkerTypeRatio") + "\n";
		content += "WorkerType = " + input.get("WorkerType") + "\n";
		content +="// random, uniform, semi\n";
		content +="//use fix(n) for exactly n worker\n";
		content += "spammersRatio = " + input.get("spammersRatio") + "\n";
		content += "spammersType = " + input.get("spammersType") + "\n";
		content +="//semi spammer\n";
		content += "semiSpammerRatio = " + input.get("semiSpammerRatio") + "\n";
		content +="//thresholdSpammers\n";
		content += "SpammerThreshold = " + input.get("SpammerThreshold") + "\n";
		content +="//acceptable sd\n";
		content += "SDAcceptable = " + input.get("SDAcceptable") + "\n";
		content +="//NormalDistributor, FixedDistributor, UniformDistributor, MixedDistributor\n";
		content += "workerDistributor = " + input.get("workerDistributor") + "\n";
		content += "reliabilityThreshold = " + input.get("reliabilityThreshold") + "\n";
		content += "default_reliability = " + input.get("default_reliability") + "\n";
		content += "default_sensitivity = " + input.get("default_sensitivity") + "\n";
		content += "default_specificity = " + input.get("default_specificity") + "\n";
		content += "// MixedDistributor\n";
		content += "/* for a fixed number, use this fix(a), the rest will scale base on its percentages.\n";
		content += "* for all cases , the last number will be adjusted to ensure that the size of workersRatio and totalWorkers declare above are the same.\n";
		content += "* NormalDistribution(a, b) a: mean, b: sd\n";
		content += "* UniformDistribution(a, b) a:lowBound, b: maxBound\n";
		content += "/* FixedDistribution(a) or a , which a is reliability degree\n";
		content += "workersRatio = " + input.get("workersRatio") + "\n";
		content += "typeOfDistributor = " + input.get("typeOfDistributor") + "\n";
		content +="//TwoCoin\n";
		content += "typeOfDistributorSensitivity = " + input.get("typeOfDistributorSensitivity") + "\n";
		content += "typeOfDistributorSpecificity = " + input.get("typeOfDistributorSpecificity") + "\n";
		content +="//NormalDistributor\n";
		content += "mean = " + input.get("mean") + "\n";
		content += "sd = " + input.get("sd") + "\n";
		content +="//UniformDistributor\n";
		content += "lowBound = " + input.get("lowBound") + "\n";
		content += "upBound = " + input.get("upBound") + "\n";
		content +="//FixedDistributor\n";
		content += "fixed = " + input.get("fixed") + "\n";
		content += "\n";
		return content;
	}
	private String Datasets(HashMap<String, String> input){
		String content = "// \"PURCHASE_ORDER, BUSINESS_PARTNER_OLD, UNIVERSITY_APPLICATION_FORM\"\n";
		content += "datasets = " + input.get("datasets") + "\n";
		content += "\n";
		return content;
	}
	private String AlgorithmsPart(HashMap<String, String> input){
		String content = "//\"Honey Pot, Majority Decision, EM, ELICE, SLME, IterativeLearning, GLAD\"\n";
		content += "[Algorithms]\n";
		content += "algorithms = " + input.get("algorithms") + "\n";
		content += "//EM\n";
		content += "Em_iter = " + input.get("Em_iter") + "\n";
		content += "//Honey Pot\n";
		content += "evaluatedMinimum = " + input.get("evaluatedMinimum") + "\n";
		content += "evaluatedThreshold = " + input.get("evaluatedThreshold") + "\n";
		content += "checkHPConstraint = " + input.get("checkHPConstraint") + "\n";		
		content += "//ELICE\n";
		content += "ratioSubset = " + input.get("ratioSubset") + "\n";
		content += "checkRatioSubset = " + input.get("checkRatioSubset") + "\n";
		content += "//SLME\n";
		content += "SLME_iter = " + input.get("SLME_iter") + "\n";
		content += "SLME_threshold = " + input.get("SLME_threshold") + "\n";
		content += "//IterativeLearning\n";
		content += "IterativeLearning_iter = " + input.get("IterativeLearning_iter") + "\n";
		content += "//GLAD\n";
		content += "GLAD_threshold = " + input.get("GLAD_threshold") + "\n";
		content += "\n";
		return content;
	}

	@Override
	public String WriteToFile(String input, String filename) {
		return null;
	}

}
