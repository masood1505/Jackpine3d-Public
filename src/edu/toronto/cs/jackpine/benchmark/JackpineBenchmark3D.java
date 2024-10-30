package edu.toronto.cs.jackpine.benchmark;

import org.apache.log4j.Logger;
import com.continuent.bristlecone.benchmark.Benchmark;

public class JackpineBenchmark3D extends Benchmark
{
    static {
        logger = Logger.getLogger(JackpineBenchmark3D.class);
    }

    protected void addLoggers() {
        if (this.htmlOutputFile != null)
            addLogger(new JackpineHtml3DLogger(htmlOutputFile));
    }

	private void addLogger(JackpineHtml3DLogger jackpineHtml3DLogger) {
		// TODO Auto-generated method stub
		
	}
}