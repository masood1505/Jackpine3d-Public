package  edu.toronto.cs.jackpine.benchmark.scenarios.threed;

import java.util.Properties;

public interface MicroBenchmarkQuery {
    void initialize(Properties properties) throws Exception;
    void prepare() throws Exception;
    void iterate(long iterationCount) throws Exception;
    void cleanup() throws Exception;
}