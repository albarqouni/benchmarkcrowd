package tools.io;

import java.util.List;

import CrowdBenchmark.models.Answer;

public abstract class Writer {
	public Writer() {

	}

	public abstract String WriteToFile(String input, String FileName);
	//public abstract String WriteToFile(List<Answer> input, String FileName);
}
