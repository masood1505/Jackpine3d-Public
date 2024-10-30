package edu.toronto.cs.jackpine.benchmark.scenarios.threed;

import java.sql.Connection;
import java.sql.Types;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.continuent.bristlecone.benchmark.db.Column;
import com.continuent.bristlecone.benchmark.db.TableSet;
import com.continuent.bristlecone.benchmark.db.TableSetHelper;

//import edu.toronto.cs.jackpine.benchmark.db.TableSetHelper3D;

public abstract class ScenarioBase3D implements MicroBenchmarkQuery {
    protected static final Logger logger = Logger.getLogger(ScenarioBase3D.class);

    // Scenario properties
    protected String url;
    protected String user;
    protected String password = "";
    protected int tables = 1;
    protected int datarows = 1;
    protected String datatype = "varchar";
    protected int datawidth = 10;
    protected String analyzeCmd = null;
    protected boolean reusedata = false;

    // Implementation data for scenario
    protected TableSet tableSet;
    protected TableSetHelper helper;
    protected Connection conn = null;

    // Setters for properties
    public void setDatarows(int datarows) {
        this.datarows = datarows;
    }

    public void setDatatype(String datatype) {
        this.datatype = datatype;
    }

    public void setDatawidth(int datawidth) {
        this.datawidth = datawidth;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setTables(int tables) {
        this.tables = tables;
    }

    public void setAnalyzeCmd(String analyzeCmd) {
        this.analyzeCmd = analyzeCmd;
    }

    public void setReusedata(boolean reusedata) {
        this.reusedata = reusedata;
    }

    @Override
    public void initialize(Properties properties) throws Exception {
        this.url = properties.getProperty("url");
        this.user = properties.getProperty("user");
        this.password = properties.getProperty("password", "");
        this.tables = Integer.parseInt(properties.getProperty("tables", "1"));
        this.datarows = Integer.parseInt(properties.getProperty("datarows", "1"));
        this.datatype = properties.getProperty("datatype", "varchar");
        this.datawidth = Integer.parseInt(properties.getProperty("datawidth", "10"));
        this.analyzeCmd = properties.getProperty("analyzeCmd");
        this.reusedata = Boolean.parseBoolean(properties.getProperty("reusedata", "false"));

        // You may need to adjust the Column array for 3D data
        Column[] columns = new Column[] {
            new Column("id", Types.INTEGER, -1, -1, true, true),
            new Column("x", Types.DOUBLE),
            new Column("y", Types.DOUBLE),
            new Column("z", Types.DOUBLE),
            new Column("payload", Types.VARCHAR, datawidth)
        };
        tableSet = new TableSet("benchmark_3d_", tables, datarows, columns);
       // helper = new TableSetHelper3D(url, user, password);
        conn = helper.getConnection();
    }

    @Override
    public void prepare() throws Exception {
        if (reusedata) {
            logger.info("Reusing existing 3D test tables...");
        } else {
            logger.info("Creating and populating 3D test tables...");
            helper.createAll(tableSet);
            helper.populateAll(tableSet);
        }

        if (analyzeCmd != null) {
            logger.info("Running analyze command: " + analyzeCmd);
            helper.execute(analyzeCmd);
        }
    }

    @Override
    public abstract void iterate(long iterationCount) throws Exception;

    @Override
    public void cleanup() throws Exception {
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
        if (helper != null) {
        //    helper.close();
        }
    }
}