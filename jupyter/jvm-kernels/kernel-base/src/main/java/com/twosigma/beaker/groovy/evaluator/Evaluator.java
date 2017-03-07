package com.twosigma.beaker.groovy.evaluator;

import java.io.IOException;

import com.twosigma.beaker.groovy.autocomplete.AutocompleteResult;
import com.twosigma.beaker.jvm.object.SimpleEvaluationObject;

public interface Evaluator {

	void setShellOptions(String cp, String in, String od) throws IOException;
	AutocompleteResult autocomplete(String code, int caretPosition);
	void killAllThreads();
	void evaluate(SimpleEvaluationObject seo, String code);
	void startWorker();
	void exit();
	
}