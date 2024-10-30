package edu.toronto.cs.jackpine.benchmark.scenarios;

import java.sql.Connection;
import java.sql.Types;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.continuent.bristlecone.benchmark.Scenario;
import com.continuent.bristlecone.benchmark.db.Column;
import com.continuent.bristlecone.benchmark.db.SpatialTableSet;

import edu.toronto.cs.jackpine.benchmark.db.SpatialTableHelper;

public abstract class SpatialScenarioBase implements Scenario {
    private static final Logger logger = Logger.getLogger(SpatialScenarioBase.class);
    
    private int iterations;


    protected String url;
    protected String user;
    protected String password = "";
    protected int tables = 1;
    protected String datatype = "varchar";
    protected int datawidth = 10;
    protected String analyzeCmd = null;
    protected boolean reusedata = false;

    protected SpatialTableSet tableSet;
    protected SpatialTableHelper helper;
    protected Connection conn = null;

    public void setDatarows(int datarows) {
        // this.datarows = datarows;
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
    
    public void setIterations(int iterations) {
        this.iterations = iterations;
    }

    public void setAnalyzeCmd(String analyzeCmd) {
        this.analyzeCmd = analyzeCmd;
    }

    public void setReusedata(boolean reusedata) {
        this.reusedata = reusedata;
    }

    public void initialize(Properties properties) throws Exception {
        this.url = properties.getProperty("url");
        this.user = properties.getProperty("user");
        this.password = properties.getProperty("password");
        this.tables = Integer.parseInt(properties.getProperty("tables", "1"));
        this.datatype = properties.getProperty("datatype", "varchar");
        this.datawidth = Integer.parseInt(properties.getProperty("datawidth", "10"));
        this.analyzeCmd = properties.getProperty("analyzeCmd");
        this.reusedata = Boolean.parseBoolean(properties.getProperty("reusedata", "false"));

        tableSet = new SpatialTableSet(SPATIAL_TABLES.values().length);

        Column[] arealmColumns = new Column[] { new Column("OGR_FID", Types.INTEGER, -1, -1, true, true),
                new Column("SHAPE", Types.STRUCT), // geometry
                new Column("statefp", Types.VARCHAR, 3), new Column("countyfp", Types.VARCHAR, 3),
                new Column("ansicode", Types.VARCHAR, 8), new Column("areaid", Types.VARCHAR, 22),
                new Column("fullname", Types.VARCHAR, 100), new Column("mtfcc", Types.VARCHAR, 5), };

        tableSet.addTable(SPATIAL_TABLES.arealm.toString(), SPATIAL_TABLES.arealm.ordinal(), arealmColumns);

        Column[] areawaterColumns = new Column[] { new Column("OGR_FID", Types.INTEGER, -1, -1, true, true),
                new Column("SHAPE", Types.STRUCT), // geometry
                new Column("statefp", Types.VARCHAR, 3), new Column("countyfp", Types.VARCHAR, 3),
                new Column("ansicode", Types.VARCHAR, 8), new Column("hydroid", Types.VARCHAR, 22),
                new Column("fullname", Types.VARCHAR, 100), new Column("mtfcc", Types.VARCHAR, 5), };

        tableSet.addTable(SPATIAL_TABLES.areawater.toString(), SPATIAL_TABLES.areawater.ordinal(), areawaterColumns);

        Column[] edgesColumns = new Column[] { new Column("OGR_FID", Types.INTEGER, -1, -1, true, true),
                new Column("SHAPE", Types.STRUCT), // geometry
                new Column("statefp", Types.VARCHAR, 3), new Column("countyfp", Types.VARCHAR, 3),
                new Column("tlid", Types.DECIMAL, 10), new Column("tfidl", Types.DECIMAL, 10),
                new Column("tfidr", Types.DECIMAL, 10), new Column("mtfcc", Types.VARCHAR, 5),
                new Column("fullname", Types.VARCHAR, 100), new Column("smid", Types.VARCHAR, 22),
                new Column("lfromadd", Types.VARCHAR, 12), new Column("ltoadd", Types.VARCHAR, 12),
                new Column("rfromadd", Types.VARCHAR, 12), new Column("rtoadd", Types.VARCHAR, 12),
                new Column("zipl", Types.VARCHAR, 5), new Column("zipr", Types.VARCHAR, 5),
                new Column("featcat", Types.VARCHAR, 1), new Column("hydroflg", Types.VARCHAR, 1),
                new Column("railflg", Types.VARCHAR, 1), new Column("roadflg", Types.VARCHAR, 1),
                new Column("olfflg", Types.VARCHAR, 1), new Column("passflg", Types.VARCHAR, 1),
                new Column("divroad", Types.VARCHAR, 1), new Column("exttyp", Types.VARCHAR, 1),
                new Column("ttyp", Types.VARCHAR, 1), new Column("deckedroad", Types.VARCHAR, 1),
                new Column("artpath", Types.VARCHAR, 1), new Column("persist", Types.VARCHAR, 1),
                new Column("gcseflg", Types.VARCHAR, 1), new Column("offsetl", Types.VARCHAR, 1),
                new Column("offsetr", Types.VARCHAR, 1), new Column("tnidf", Types.DECIMAL, 10),
                new Column("tnidt", Types.DECIMAL, 10),

        };

        tableSet.addTable(SPATIAL_TABLES.edges.toString(), SPATIAL_TABLES.edges.ordinal(), edgesColumns);

        Column[] pointlmColumns = new Column[] { new Column("OGR_FID", Types.INTEGER, -1, -1, true, true),
                new Column("SHAPE", Types.STRUCT), // geometry
                new Column("statefp", Types.VARCHAR, 3), new Column("countyfp", Types.VARCHAR, 3),
                new Column("ansicode", Types.VARCHAR, 8), new Column("areaid", Types.VARCHAR, 22),
                new Column("fullname", Types.VARCHAR, 100), new Column("mtfcc", Types.VARCHAR, 5), };

        tableSet.addTable(SPATIAL_TABLES.pointlm.toString(), SPATIAL_TABLES.pointlm.ordinal(), pointlmColumns);

        helper = new SpatialTableHelper(url, user, password);
        conn = helper.getConnection();
    }

    public void globalPrepare() throws Exception {
        if (analyzeCmd != null) {
            logger.info("Running analyze command: " + analyzeCmd);
            helper.execute(analyzeCmd);
        }
    }

    public abstract void prepare() throws Exception;

    public abstract void iterate(long iterationCount) throws Exception;

    public abstract void cleanup() throws Exception;

    public void globalCleanup() {
    }

    public void iterate(long iterationCount, int statementIndex) {
    }

    public void prepare(Connection conn) throws Exception {
    }

    public enum SPATIAL_TABLES {
        arealm,
        areawater,
        edges,
        pointlm
    }
}
