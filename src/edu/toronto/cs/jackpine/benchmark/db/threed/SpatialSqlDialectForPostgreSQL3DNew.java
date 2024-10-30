package edu.toronto.cs.jackpine.benchmark.db.threed;

import java.util.ResourceBundle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

import com.continuent.bristlecone.benchmark.db.Column;
import com.continuent.bristlecone.benchmark.db.SqlDialectForPostgreSQL;
import com.continuent.bristlecone.benchmark.db.Table;

import edu.toronto.cs.jackpine.benchmark.db.SpatialSqlDialect;
import edu.toronto.cs.jackpine.benchmark.scenarios.macroscenario.VisitScenario;

import java.io.InputStream;
import java.util.Properties;

/**
 * PostgreSQL spatial DBMS dialect information for 3D queries.
 * 
 */
public class SpatialSqlDialectForPostgreSQL3DNew implements SpatialSqlDialect3D {
    private String SRID;
    private Properties props;

    public SpatialSqlDialectForPostgreSQL3DNew(Properties props) {
        this.props = props;
        this.SRID = props.getProperty("POSTGRESQL_SRID", "").trim();
        
        if (this.SRID.isEmpty()) {
            throw new RuntimeException("POSTGRESQL_SRID property is missing or empty");
        }
    }
  
  @Override
  public SupportedSqlDialect getSqlDialectType() {
    return SupportedSqlDialect.PostgreSQL;
  }
  
  

  
  
  @Override
  public String getSelectBuffer3D() {
    return "SELECT ST_3DBuffer(a.geom, 5280) FROM arealm_merge a";
  }

  @Override
  public String getSelectClosestPoint3D() {
    return "SELECT ST_3DClosestPoint(geom1, geom2) FROM your_table WHERE id1 = ? AND id2 = ?";
  }

  @Override
  public String getSelectContains3D() {
    return "SELECT ST_3DContains(geom1, geom2) FROM your_table WHERE id1 = ? AND id2 = ?";
  }

  @Override
  public String getSelectConvexHull3D() {
    return "SELECT ST_3DConvexHull(a.geom) FROM arealm_merge a";
  }

  @Override
  public String getSelectCrosses3D() {
    return "SELECT ST_3DCrosses(geom1, geom2) FROM your_table WHERE id1 = ? AND id2 = ?";
  }

  @Override
  public String getSelectDistance3D() {
    return "SELECT ST_3DDistance(geom1, geom2) FROM your_table WHERE id1 = ? AND id2 = ?";
  }

  @Override
  public String getSelectDistanceWithin3D() {
    return "SELECT ST_3DWithin(geom1, geom2) FROM your_table WHERE id1 = ? AND id2 = ?";
  }

  @Override
  public String getSelectDWithin3D() {
    return "SELECT ST_3DDWithin(geom1, geom2, ?) FROM your_table WHERE id1 = ? AND id2 = ?";
  }

  @Override
  public String getSelectFullyWithin3D() {
    return "SELECT ST_3DFullyWithin(geom1, geom2, ?) FROM your_table WHERE id1 = ? AND id2 = ?";
  }

  @Override
  public String getSelectIntersects3D() {
    return "SELECT ST_3DIntersects(geom1, geom2) FROM your_table WHERE id1 = ? AND id2 = ?";
  }

  @Override
  public String getSelectIs3D() {
    return "SELECT ST_Is3D(geom) FROM your_table WHERE id = ?";
  }

  /*@Override
  public String getSelectLength3D() {
    return "SELECT ST_3DLength(geom) FROM your_table WHERE id = ?";
  }*/

  @Override
  public String getSelectLongestLine3D() {
    return "SELECT ST_3DLongestLine(geom1, geom2) FROM your_table WHERE id1 = ? AND id2 = ?";
  }

  @Override
  public String getSelectMaxDistance3D() {
    return "SELECT ST_3DMaxDistance(geom1, geom2) FROM your_table WHERE id1 = ? AND id2 = ?";
  }

  @Override
  public String getSelectShortestLine3D() {
    return "SELECT ST_3DShortestLine(geom1, geom2) FROM your_table WHERE id1 = ? AND id2 = ?";
  }

  @Override
  public String getSelectReadSpatialBufferPolygon3D() {
    return "SELECT ST_3DBuffer(geom, 100) FROM your_table WHERE id = ?";
  }

  @Override
  public String getSelectReadSpatialLongestLine3D() {
    return "SELECT ST_3DLongestLine(geom1, geom2) FROM your_table WHERE id1 = ? AND id2 = ?";
  }

  @Override
  public String getSelectAreaContainsArea() {
    return "SELECT ST_Contains(a.geom, b.geom) FROM your_table a, your_table b WHERE a.id = ? AND b.id = ?";
  }

 /* public String[] getLandUseQueries() {
    return "SELECT land_use, COUNT(*) FROM land_use_table GROUP BY land_use";
  }*/

public String getSelectAllFeaturesWithinADistanceFromPoint() {
	// TODO Auto-generated method stub
	return null;
}



@Override
public String getSelectBoundingBoxSearch() {
	// TODO Auto-generated method stub
	return null;
}


@Override
public String getSelect3DPointIntersectsLine() {
    return "SELECT id FROM geometries WHERE ST_3DIntersects(point_geom, line_geom)";
}

@Override
public String getSelect3DPointEqualsPoint() {
    return "SELECT id FROM geometries WHERE ST_3DEquals(point_geom1, point_geom2)";
}

public String getSelect3DLineCrossesLine() {
    return "SELECT a.gid FROM camb3d_bldg_active_mp a, camb3d_bldg_active_mp b WHERE _st_3dintersects(a.geom1, b.geom2)";
}



@Override
public String getSelect3DLineEqualsLine() {
    return "SELECT a.ogc_fid AS building_id_1,\n"
         + "       b.ogc_fid AS building_id_2,\n"
         + "       a.gml_id AS building_name_1,\n"
         + "       b.gml_id AS building_name_2\n"
         + "FROM public.building a\n"
         + "JOIN public.building b ON a.ogc_fid < b.ogc_fid\n"
         + "WHERE a.ogc_fid < 2000\n"
         + "  AND b.ogc_fid < 2000\n"
         + "  AND ST_Equals(a.lod1solid, b.lod1solid);";
}

@Override
public String getSelect3DLineEqualsLinevs3D() {
    return "SELECT ca_gml.ogc_fid AS california_gml_id_1,\n" +
           "       tr3d.fid AS road_id,\n" +
           "       ca_gml.gml_id AS california_gml_name,\n" +
           "       tr3d.gml_id AS road_name\n" +
           "FROM california_gml ca_gml\n" +
           "JOIN tiger_roads_3d_all_local2 tr3d ON ca_gml.ogc_fid < tr3d.fid\n" +
           "WHERE ca_gml.ogc_fid < 2000\n" +
           "  AND tr3d.fid < 2000\n" +
           "  AND ST_Equals(ca_gml.geom, tr3d.geom);";
}


@Override
public String getSelect3DPointWithinRegion() {
    return "SELECT a.gid\n"
    		+ "FROM camb3d_bldg_active_mp a, camb3d_bldg_active_mp b\n"
    		+ "WHERE ST_3DDWithin(a.geom1, b.geom2, 1000); -- Replace 1000 with a relevant distance\n"
    		+ "";
}


public String getSelect3DLineWithinRegion() {
    return "SELECT id FROM geometries WHERE ST_3DWithin(line_geom, region_geom)";
}

@Override
public String getSelect3DLineCrossesRegion() {
    return "SELECT id FROM geometries WHERE ST_3DCrosses(line_geom, region_geom)";
}
/*
@Override
public String getSelect3DRegionContainsLine() {
    return "SELECT id FROM geometries WHERE ST_3DContains(region_geom, line_geom)";
}*/

public String getSelect3DRegionContainsLine() {
    return "SELECT a.gid FROM camb3d_bldg_active_mp a, camb3d_bldg_active_mp b " +
           "WHERE ST_3DIntersects(a.geom, b.geom2) " +
           "AND ST_ZMax(b.geom2) <= ST_ZMax(a.geom) " +
           "AND ST_ZMin(b.geom2) >= ST_ZMin(a.geom)";
}




public String getSelect3DRegionEqualsRegion() {
    return "SELECT id FROM geometries WHERE ST_3DEquals(region_geom, another_region_geom)";
}


@Override
public String getSelect3DRegionContainsRegion() {
    return "SELECT a.ogc_fid, a.gml_id, a.ubid " +
           "FROM building a, building b " +
           "WHERE ST_3DIntersects(a.lod1solid, b.lod1solid) " +
           "AND ST_Volume(ST_3DIntersection(a.lod1solid, b.lod1solid)) = ST_Volume(b.lod1solid) " +
           "AND a.ogc_fid != b.ogc_fid";
}




@Override
public String getSelectLineContainsVolume3D() {
    return "SELECT * FROM your_table WHERE ST_Contains(line_geom, volume_geom)";
}
@Override
public String getSelectLineWithinVolume3D() {
    return "SELECT * FROM your_table WHERE ST_Within(line_geom, volume_geom)";
}

@Override
public String getSelectVolumeContainsLine3D() {
    return "SELECT * FROM your_table WHERE ST_Contains(volume_column, line_column)";
}

@Override
public String getSelectLineIntersectsVolume3D() {
    return "SELECT a.gid FROM camb3d_bldg_active_mp a, camb3d_bldg_active_mp b WHERE ST_3DIntersects(a.geom, b.geom)";
}



public String getSelectVolumeContainsRegion3D() {
    return "SELECT volume_id FROM volume_table WHERE ST_Contains(volume_geom, region_geom)";
}

@Override
public String getSelectRegionInsideVolume3D() {
    return "SELECT a.ogc_fid " +
           "FROM building a, building b " +
           "WHERE ST_3DFullyWithin(a.lod1solid, b.lod1solid, 0.0001)";
}

@Override
public String getSelectRegionCrossesVolume3D() {
    return "SELECT region_id FROM region_table, volume_table WHERE ST_Crosses(region_table.region_geom, volume_table.volume_geom)";
}

@Override
public String getSelectRegionIntersectsVolume3D() {
    return "SELECT region_id FROM region_table, volume_table WHERE ST_Intersects(region_table.region_geom, volume_table.volume_geom)";
}

@Override
public String getSelectRegionContainsPoint3D() {
    return "SELECT region_id FROM region_table, point_table WHERE ST_3DContains(region_table.region_geom, point_table.point_geom)";
}

public String getSelectRegionCoversPoint3D() {
    // Adjust the query as needed to match your spatial requirements
    return "SELECT a.gid FROM camb3d_bldg_active_mp a, camb3d_bldg_active_mp b WHERE ST_3DCovers(a.geom, b.geom)";
}


@Override
public String getSelectRegionCoversLine3D() {
    return "SELECT region_id FROM region_table, line_table WHERE ST_3DCovers(region_table.region_geom, line_table.line_geom)";
}

public String getSelectRegionCoversRegion3D() {
    return "SELECT region1_id FROM region_table AS r1, region_table AS r2 WHERE ST_3DCovers(r1.region_geom, r2.region_geom)";
}

public String getSelectVolumeCoversLine3D() {
    return "SELECT volume_id FROM volume_table AS v, line_table AS l WHERE ST_3DCovers(v.volume_geom, l.line_geom)";
}
@Override
public String getSelectVolumeCoversRegion3D() {
    return "SELECT volume_id FROM volume_table AS v, region_table AS r WHERE ST_3DCovers(v.volume_geom, r.region_geom)";
}

public String getSelectPointCoveredByRegion3D() {
    return "SELECT point_id FROM point_table AS p, region_table AS r WHERE r.region_geom.STIntersects(p.point_geom) = 1";
}

public String getSelectRegionCoveredByRegion3D() {
    return "SELECT region_id FROM region_table1 AS r1, region_table2 AS r2 WHERE r1.region_geom.STIntersects(r2.region_geom) = 1";
}

public String getSelectLineCoveredByVolume3D() {
    return "SELECT line_id FROM line_table AS l, volume_table AS v WHERE v.volume_geom.STIntersects(l.line_geom) = 1";
}

public String getSelectRegionCoveredByVolume3D() {
    return "SELECT region_id FROM region_table AS r, volume_table AS v WHERE v.volume_geom.STIntersects(r.region_geom) = 1";
}

/*@Override
public String getSelectLineOverlapsLine3D() {
    return "SELECT a.ogc_fid AS building_id_1,\n"
    		+ "       b.ogc_fid AS building_id_2,\n"
    		+ "       a.gml_id AS building_name_1,\n"
    		+ "       b.gml_id AS building_name_2\n"
    		+ "FROM public.building a\n"
    		+ "JOIN public.building b ON a.ogc_fid < b.ogc_fid\n"
    		+ "WHERE a.ogc_fid < 2000\n"
    		+ "  AND b.ogc_fid < 2000\n"
    		+ "  AND ST_3DIntersects(ST_Envelope(a.lod1solid), ST_Envelope(b.lod1solid));";
}	*/

@Override
public String getSelectLineOverlapsLine3D() {
    return "SELECT \n" +
           "    ca.gid AS california_id,\n" +
           "    ca_gml.ogc_fid AS california_gml_id,\n" +
           "    ST_3DIntersection(\n" +
           "        ST_Force3D(ST_Transform(ca.geom, 4979)),\n" +
           "        ca_gml.geom\n" +
           "    ) AS intersection_geom\n" +
           "FROM \n" +
           "    california ca\n" +
           "JOIN \n" +
           "    california_gml ca_gml\n" +
           "ON \n" +
           "    ST_3DIntersects(\n" +
           "        ST_Force3D(ST_Transform(ca.geom, 4979)),\n" +
           "        ca_gml.geom\n" +
           "    );";
}
/*
@Override
public String getSelectLineOverlapsLine3Dvs3D() {
    return "SELECT \n" +
           "    tr3d.fid AS road_id,\n" +
           "    ca_gml.ogc_fid AS california_gml_id,\n" +
           "    ST_3DIntersection(\n" +
           "        ST_Transform(tr3d.geom, 4979),\n" +
           "        ca_gml.geom\n" +
           "    ) AS intersection_geom\n" +
           "FROM \n" +
           "    tiger_roads_3d_all_local2 tr3d\n" +
           "JOIN \n" +
  //         "    california_gml ca_gml\n" +
  			"    allthefourblueregions lod1solid\n" +

           "ON \n" +
           "    ST_3DIntersects(\n" +
           "        ST_Transform(tr3d.geom, 4979),\n" +
           "        ca_gml.geom\n" +
           "    );";
}*/

/*@Override
public String getSelectLineOverlapsLine3Dvs3D() {
    return "SELECT \n" +
           "    tr3d.fid AS road_id,\n" +
           "    afbr.ogc_fid AS california_gml_id\n" +
           "FROM \n" +
           "    tiger_roads_3d_all_local2 tr3d\n" +
           "JOIN \n" +
           "    allthefourblueregions afbr\n" +
           "ON \n" +
           "    ST_3DIntersects(tr3d.geom, afbr.lod1solid);";
}*/

/*@Override
public String getSelectLineOverlapsLine3Dvs3D() {
    return "SELECT \n" +
           "    rr3d.fid AS road_id,\n" +
           "    rc.ogc_fid AS california_gml_id\n" +
           "FROM \n" +
           "    arealm3d rr3d\n" +
           "JOIN \n" +
           "    riversidecounty rc\n" +
           "ON \n" +
           "    ST_3DIntersects(rr3d.geom, rc.lod1solid);";
}*/

@Override
public String getSelectLineOverlapsLine3Dvs3D() {
    return "SELECT \n" +
           "    rr3d.fid AS road_id,\n" +
           "    rc.ogc_fid AS california_gml_id,\n" +
           "    ST_3DIntersects(rr3d.geom, rc.lod1solid) AS overlaps\n" +
           "FROM \n" +
           "    arealm3d rr3d\n" +
           "JOIN \n" +
           "    riversidecounty rc\n" +
           "ON \n" +
           "    ST_3DIntersects(rr3d.geom, rc.lod1solid);";
}


@Override
public String getSelectBuildingFullyWithinArea3d() {
    return "SELECT \n" +
           "    rr3d.fid AS road_id,\n" +
           "    rc.ogc_fid AS california_gml_id\n" +
           "FROM \n" +
           "    arealm3d rr3d\n" +
           "JOIN \n" +
           "    riversidecounty rc\n" +
           "ON \n" +
           "    _st_3ddfullywithin(rr3d.geom, rc.lod1solid, 0.0);"; // Hardcoded double precision value
}


public String getSelectBuildingIntersectionArea3Dvs3D() {
    return "SELECT \n" +
           "    rr3d.fid AS road_id,\n" +
           "    rc.ogc_fid AS california_gml_id\n" +
           "FROM \n" +
           "    arealm3d rr3d\n" +
           "JOIN \n" +
           "    riversidecounty rc\n" +
           "ON \n" +
           "    ST_3DIntersection(rr3d.geom, rc.lod1solid);";
}


@Override
public String getSelectBuildingsDifferenceArea3Dvs3D() {
    return "SELECT \n" +
           "    rr3d.fid AS road_id,\n" +
           "    rc.ogc_fid AS california_gml_id\n" +
           "FROM \n" +
           "    arealm3d rr3d\n" +
           "JOIN \n" +
           "    riversidecounty rc\n" +
           "ON \n" +
           "    ST_3DDifference(rr3d.geom, rc.lod1solid);";
}

@Override
public String get3DConvexHullAreaQuery() {
    return "SELECT \n" +
           "    rr3d.fid AS road_id,\n" +
           "    ST_3DConvexHull(rr3d.geom) AS convex_hull\n" +
           "FROM \n" +
           "    arealm3d rr3d;";
}

@Override
public String get3DUnionSAreaQuery() {
    return "SELECT \n" +
           "    ST_3DUnion(rr3d.geom) AS unified_geom\n" +
           "FROM \n" +
           "    arealm3d rr3d;";
}





@Override
public String getClosestPointAreaQuery() {
    return "SELECT \n" +
           "    rr3d.fid AS road_id,\n" +
           "    rc.ogc_fid AS california_gml_id,\n" +
           "    ST_3DClosestPoint(rr3d.geom, rc.lod1solid) AS closest_point\n" +
           "FROM \n" +
           "    arealm3d rr3d\n" +
           "JOIN \n" +
           "    riversidecounty rc\n" +
           "ON ST_3DIntersects(rr3d.geom, rc.lod1solid);";
}




@Override
public String getShortestLineAreaQuery() {
    return "SELECT \n" +
           "    rr3d.fid AS road_id,\n" +
           "    rc.ogc_fid AS california_gml_id,\n" +
           "    ST_3DShortestLine(rr3d.geom, rc.lod1solid) AS shortest_line\n" +
           "FROM \n" +
           "    arealm3d rr3d\n" +
           "JOIN \n" +
           "    riversidecounty rc\n" +
           "ON ST_3DIntersects(rr3d.geom, rc.lod1solid);";
}

@Override
public String getLongestLineAreaQuery() {
    return "SELECT \n" +
           "    rr3d.fid AS road_id,\n" +
           "    rc.ogc_fid AS california_gml_id,\n" +
           "    ST_3DLongestLine(rr3d.geom, rc.lod1solid) AS longest_line\n" +
           "FROM \n" +
           "    arealm3d rr3d\n" +
           "JOIN \n" +
           "    riversidecounty rc\n" +
           "ON ST_3DIntersects(rr3d.geom, rc.lod1solid);";
}


@Override
public String getLineInterpolatePointAreaQuery() {
    return "SELECT \n" +
           "    rr3d.fid AS road_id,\n" +
           "    rc.ogc_fid AS california_gml_id,\n" +
           "    ST_3DLineInterpolatePoint(ST_ExteriorRing(rr3d.geom), 0.5) AS interpolated_point\n" +
           "FROM \n" +
           "    arealm3d rr3d\n" +
           "JOIN \n" +
           "    riversidecounty rc\n" +
           "ON ST_3DIntersects(rr3d.geom, rc.lod1solid);";
}






public String getSelectArea3D() {
    return "SELECT ST_3DArea(geom) \n" +
           "FROM arealm3d \n" +
           "WHERE ST_IsValid(geom);";
}

public String getSelectPerimeter3D() {
    return "SELECT ST_3DPerimeter(geom) \n" +
           "FROM arealm3d \n" +
           "WHERE ST_IsValid(geom);";
}

public String getSelectLength3D() {
    return "SELECT ST_3DLength(geom) \n" +
           "FROM arealm3d \n" +
           "WHERE ST_IsValid(geom);";
}

public String getSelect3DDistance3D() {
    return "SELECT a.fid, r.ogc_fid, ST_3DDistance(a.geom, r.lod1solid) as distance_3d \n" +
           "FROM arealm3d a \n" +
           "CROSS JOIN riversidecounty r \n" +
           "WHERE ST_IsValid(a.geom) \n" +
           "AND ST_IsValid(r.lod1solid);";
}










/*@Override
public String getSelectLineLongestLine3D() {
    return "SELECT \n" +
           "    ca.gid AS california_id,\n" +
           "    ca_gml.ogc_fid AS california_gml_id,\n" +
           "    ST_3DLongestLine(\n" +
           "        ST_Force3D(ST_Transform(ca.geom, 4979)),\n" +
           "        ca_gml.geom\n" +
           "    ) AS longest_line_geom\n" +
           "FROM \n" +
           "    california ca\n" +
           "JOIN \n" +
           "    california_gml ca_gml\n" +
           "ON \n" +
           "    ST_3DIntersects(\n" +
           "        ST_Force3D(ST_Transform(ca.geom, 4979)),\n" +
           "        ca_gml.geom\n" +
           "    );";
}*/



//////////////////////////////////////////MACRO BENCHMARK /////////////////////////////////


public String getGeocodingQuery3D() {
    String sql = "SELECT t.tlid, t.fraddr, t.fraddl, t.toaddr, t.toaddl," +
        " t.zipL, t.zipR, t.tolat, t.tolong, t.frlong, t.frlat," +
        " t.long1, t.lat1, t.long2, t.lat2, t.long3, t.lat3, t.long4, t.lat4," +
        " t.long5, t.lat5, t.long6, t.lat6, t.long7, t.lat7, t.long8, t.lat8," +
        " t.long9, t.lat9, t.long10, t.lat10, t.fedirp, t.fetype, t.fedirs," +
        " t.elevation, t.building_height, t.num_floors, t.building_type" +
        " FROM geocoder_address t" +
        " WHERE t.fename = ? AND " +
        "(" +
        " (t.fraddL <= ? AND t.toaddL >= ?) OR (t.fraddL >= ? AND t.toaddL <= ?) " +
        " OR (t.fraddR <= ? AND t.toaddR >= ?) OR (t.fraddR >= ? AND t.toaddR <= ?) " +
        ")" +
        " AND (t.zipL = ? OR t.zipR = ?)";

    return sql;
}

public  String getCityStateForReverseGeocoding(){ 
	  StringBuffer sb = new StringBuffer();

	  // Generate the join SQL statement.
	  sb.append("SELECT name, st, distance(geom, GeomFromText(?, "+SRID+" )) as dist FROM cityinfo order by dist limit 1 ");
	  String sql = sb.toString();

	  return sql;
}


public String getStreetAddressForReverseGeocoding3D() {
    StringBuffer sb = new StringBuffer();

    sb.append("SELECT fullname, lfromadd, ltoadd, rfromadd, rtoadd, zipl, zipr, " +
               "ST_3DDistance(geom, ST_GeomFromText(?, " + SRID + ")) as d " +
               "FROM edges_merge_3d " +
               "WHERE ST_3DIntersects(geom, ST_GeomFromText(?, " + SRID + ")) " +
               "AND ST_3DDistance(geom, ST_GeomFromText(?, " + SRID + ")) < 0.1 " +
               "AND roadflg = 'Y' " +
               "ORDER BY d LIMIT 1");

    return sb.toString();
}


public void performSpatialJoin(Connection conn) throws Exception {
    String sql = "SELECT g.id AS green_id, r.id AS red_id " +
                 "FROM green_table g " +
                 "JOIN red_table r " +
                 "ON ST_Intersects(g.geometry_column, r.geometry_column)";
    
    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
        long startTime = System.currentTimeMillis();
        ResultSet rs = pstmt.executeQuery();
        long endTime = System.currentTimeMillis();
        System.out.println("PostGIS Spatial Join executed in " + (endTime - startTime) + " ms");
        
        while (rs.next()) {
            System.out.println("Green ID: " + rs.getInt("green_id") + ", Red ID: " + rs.getInt("red_id"));
        }
    }
}

public void findClosestBloodVessels(Connection conn) throws Exception {
    String sql = "SELECT p.id AS purple_id, g.id AS green_id " +
                 "FROM purple_table p " +
                 "JOIN green_table g " +
                 "ON g.geometry_column = ( " +
                 "    SELECT g_inner.geometry_column " +
                 "    FROM green_table g_inner " +
                 "    ORDER BY g_inner.geometry_column <-> p.geometry_column " +
                 "    LIMIT 1 " +
                 ")";
    
    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
        long startTime = System.currentTimeMillis();
        ResultSet rs = pstmt.executeQuery();
        long endTime = System.currentTimeMillis();
        System.out.println("PostGIS Nearest Neighbor Query executed in " + (endTime - startTime) + " ms");
        
        while (rs.next()) {
            System.out.println("Purple ID: " + rs.getInt("purple_id") + ", Closest Green ID: " + rs.getInt("green_id"));
        }
    }
}

@Override
public boolean supportsJdbcUrl(String url) {
    return url != null && url.startsWith("jdbc:postgresql:");
}

@Override
public String getLandUseQueries() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public String getCityStateForReverseGeocoding3D() {
	// TODO Auto-generated method stub
	return null;
}


/*

@Override
public String getCreateIndex(Table arg0, Column arg1) {
	// TODO Auto-generated method stub
	return null;
}

@Override
public String getCreateTable(Table arg0) {
	// TODO Auto-generated method stub
	return null;
}

@Override
public String getDeleteAll(Table arg0) {
	// TODO Auto-generated method stub
	return null;
}
/*
@Override
public String getDeleteByKey(Table arg0) {
	// TODO Auto-generated method stub
	return null;
}

@Override
public String getDeleteByKey(Table arg0, Column arg1) {
	// TODO Auto-generated method stub
	return null;
}

@Override
public String getDeleteByKeyLike(Table arg0, Column arg1) {
	// TODO Auto-generated method stub
	return null;
}

@Override
public String getDriver() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public String getDropTable(Table arg0) {
	// TODO Auto-generated method stub
	return null;
}

@Override
public String getInsert(Table arg0) {
	// TODO Auto-generated method stub
	return null;
}

@Override
public String getSelectAll(Table arg0) {
	// TODO Auto-generated method stub
	return null;
}

@Override
public String getSelectAllSorted(Table arg0) {
	// TODO Auto-generated method stub
	return null;
}

@Override
public String getSelectByColumn(Table arg0, Column arg1) {
	// TODO Auto-generated method stub
	return null;
}

@Override
public String getSelectByColumnWithLimit(Table arg0, Column arg1, int arg2) {
	// TODO Auto-generated method stub
	return null;
}

@Override
public String getSelectByKey(Table arg0) {
	// TODO Auto-generated method stub
	return null;
}

@Override
public String getSelectCrossProduct(Table arg0) {
	// TODO Auto-generated method stub
	return null;
}

@Override
public String getSelectCrossProductCount(Table arg0) {
	// TODO Auto-generated method stub
	return null;
}

@Override
public String getSelectCrossProductCount(Table[] arg0) {
	// TODO Auto-generated method stub
	return null;
}

@Override
public String getUpdateByKey(Table arg0) {
	// TODO Auto-generated method stub
	return null;
}

@Override
public String getUpdateByKey(Table arg0, Column arg1) {
	// TODO Auto-generated method stub
	return null;
}

@Override
public String getUpdateByKeyLike(Table arg0, Column arg1) {
	// TODO Auto-generated method stub
	return null;
}

@Override
public String implementationColumnSpecification(Column arg0) {
	// TODO Auto-generated method stub
	return null;
}

@Override
public int implementationConvertFetchSize(int arg0) {
	// TODO Auto-generated method stub
	return 0;
}

@Override
public boolean implementationSupportsLimitClause() {
	// TODO Auto-generated method stub
	return false;
}

@Override
public String implementationTypeName(int arg0) {
	// TODO Auto-generated method stub
	return null;
}

@Override
public boolean implementationTypeNeedsLength(int arg0) {
	// TODO Auto-generated method stub
	return false;
}

@Override
public boolean implementationTypeNeedsPrecision(int arg0) {
	// TODO Auto-generated method stub
	return false;
}

@Override
public boolean implementationUpdateRequiresTransaction(int arg0) {
	// TODO Auto-generated method stub
	return false;
}

@Override
public String getSelectTotalLength() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public String getSelectTotalArea() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public String getSelectLongestLine() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public String getSelectLargestArea() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public String getSelectDimensionPolygon() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public String getSelectBufferPolygon() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public String getSelectConvexHullPolygon() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public String getSelectEnvelopeLine() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public String[] getSelectLongestLineIntersectsArea() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public String[] getSelectLineIntersectsLargestArea() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public String[] getSelectAreaOverlapsLargestArea() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public String[] getSelectLargestAreaContainsPoint() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public String getSelectAreaOverlapsArea() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public String getSelectAreaWithinArea() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public String getSelectAreaTouchesArea() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public String getSelectAreaEqualsArea() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public String getSelectAreaDisjointArea() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public String getSelectLineIntersectsArea() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public String getSelectLineCrossesArea() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public String getSelectLineWithinArea() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public String getSelectLineTouchesArea() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public String getSelectLineOverlapsArea() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public String getSelectLineOverlapsLine() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public String getSelectLineCrossesLine() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public String getSelectPointEqualsPoint() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public String getSelectPointWithinArea() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public String getSelectPointIntersectsArea() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public String getSelectPointIntersectsLine() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public String getMaxRowidFromSpatialTableEdgesMerge() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public String getMaxRowidFromSpatialTableArealmMerge() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public String getInsertIntoEdgesMerge() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public String getInsertIntoArealmMerge() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public String getSpatialWriteCleanupEdgesMerge() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public String getSpatialWriteCleanupArealmMerge() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public String getStreetAddressForReverseGeocoding() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public String getGeocodingQuery() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public String getMapSearchSiteSearchQuery(VisitScenario visitScenario) {
	// TODO Auto-generated method stub
	return null;
}

@Override
public String[] getMapSearchScenarioQueries(VisitScenario visitScenario) {
	// TODO Auto-generated method stub
	return null;
}

@Override
public String[] getMapBrowseBoundingBoxQueries() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public String[] getEnvHazardQueries() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public String getPoint3DString(double x, double y, double z) {
	// TODO Auto-generated method stub
	return null;
}

@Override
public String getBox3DString(double d, double e, double f, double g, double h, double i) {
	// TODO Auto-generated method stub
	return null;
}




*/

@Override
public String getSelectBodyEqualsBody() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public String getSelectBodyOverlapsBody() {
	// TODO Auto-generated method stub
	return null;
}

}
