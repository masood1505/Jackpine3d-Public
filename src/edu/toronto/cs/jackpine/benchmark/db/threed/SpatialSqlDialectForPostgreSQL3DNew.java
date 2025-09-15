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
public String getSelectArealmIntersectsBuildings3Dvs3D() {
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
           "    rc.ogc_fid AS california_gml_id,\n" +
           "    rc.state AS california_state,\n" +
           "    ST_3DDifference(rr3d.geom, rc.lod1solid) AS difference_geom\n" +
           "FROM \n" +
           "    arealm3d rr3d\n" +
           "JOIN \n" +
           "    riversidecounty rc\n" +
           "ON \n" +
           "    ST_Intersects(rr3d.geom, rc.lod1solid)  -- Replace with your actual join condition\n" +
           "WHERE \n" +
           "    ST_3DDifference(rr3d.geom, rc.lod1solid) IS NOT NULL;"; // This is now correctly placed
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

@Override
public String getSelectAreaContainsBuildings3D() {
    return "SELECT \n" +
            "    rr3d.fid AS road_id,\n" +
            "    rc.ogc_fid AS california_gml_id,\n" +
            "    rc.height AS building_height\n" + // Add this line for the new column
            "FROM \n" +
            "    arealm3d rr3d\n" +
            "JOIN \n" +
            "    riversidecounty rc\n" +
            "ON \n" +
            "    geometry_contains_3d(rr3d.geom, rc.lod1solid);";
}

@Override
public String getSelectArealmOverlapsBuildings3D() {
    return "SELECT \n" +
           "    rc.ogc_fid AS california_gml_id,\n" +
           "    rr3d.fid AS road_id\n" +
           "FROM \n" +
           "    riversidecounty rc\n" +
           "JOIN \n" +
           "    arealm3d rr3d\n" +
           "ON \n" +
           "    geometry_overlaps_3d(rc.lod1solid, rr3d.geom)\n" +
           "LIMIT 100;";
}





/* begining of line queries  */  

@Override
public String getSelectLineOverlapsLine3Dvs3DLine() {
    return "SELECT \n" +
           "    rr3d.fid AS road_id,\n" +
           "    rc.ogc_fid AS california_gml_id,\n" +
           "    ST_3DIntersects(rr3d.geom, rc.lod1solid) AS overlaps\n" +
           "FROM \n" +
           "    riversideroads3d rr3d\n" +
           "JOIN \n" +
           "    riversidecounty rc\n" +
           "ON \n" +
           "    ST_3DIntersects(rr3d.geom, rc.lod1solid);";
}

@Override
public String getSelectBuildingFullyWithinArea3DLine() {
    return "SELECT \n" +
           "    rr3d.fid AS road_id,\n" +
           "    rc.ogc_fid AS california_gml_id\n" +
           "FROM \n" +
           "    riversideroads3d rr3d\n" +
           "JOIN \n" +
           "    riversidecounty rc\n" +
           "ON \n" +
           "    _st_3ddfullywithin(rr3d.geom, rc.lod1solid, 0.0);"; // Hardcoded double precision value
}

@Override
public String getSelectBuildingIntersectionArea3Dvs3DLine() {
    return "SELECT \n" +
           "    rr3d.fid AS road_id,\n" +
           "    rc.ogc_fid AS california_gml_id\n" +
           "FROM \n" +
           "    riversideroads3d rr3d\n" +
           "JOIN \n" +
           "    riversidecounty rc\n" +
           "ON \n" +
           "    ST_3DIntersection(rr3d.geom, rc.lod1solid);";
}

@Override
public String getSelectBuildingsDifferenceArea3Dvs3DLine() {
    return "SELECT \n" +
           "    rr3d.fid AS road_id,\n" +
           "    rc.ogc_fid AS california_gml_id\n" +
           "FROM \n" +
           "    riversideroads3d rr3d\n" +
           "JOIN \n" +
           "    riversidecounty rc\n" +
           "ON \n" +
           "    ST_3DDifference(rr3d.geom, rc.lod1solid);";
}

@Override
public String getClosestPointAreaQueryLine() {
    return "SELECT \n" +
           "    rr3d.fid AS road_id,\n" +
           "    rc.ogc_fid AS california_gml_id,\n" +
           "    ST_3DClosestPoint(rr3d.geom, rc.lod1solid) AS closest_point\n" +
           "FROM \n" +
           "    riversideroads3d rr3d\n" +
           "JOIN \n" +
           "    riversidecounty rc\n" +
           "ON ST_3DIntersects(rr3d.geom, rc.lod1solid);";
}

@Override
public String getBuilding3DIntersectsLineQuery() {
    return "SELECT \n" +
           " rr3d.gid AS road_id,\n" +
           " rc.ogc_fid AS california_gml_id,\n" +
           " ST_3DIntersects(rr3d.geom, rc.lod1solid) AS overlaps\n" +
           "FROM \n" +
           " riversideroads3d rr3d\n" +
           "JOIN \n" +
           " riversidecounty rc\n" +
           "ON ST_3DIntersects(rr3d.geom, rc.lod1solid);";
}

public String getBuilding3DIntersectsAreaQuery() {
    return "SELECT \n" +
           " rr3d.gid AS road_id,\n" +
           " rc.ogc_fid AS california_gml_id,\n" +
           " ST_3DIntersects(rr3d.geom, rc.lod1solid) AS overlaps\n" +
           "FROM \n" +
           " landmarkriverside3d rr3d\n" +
           "JOIN \n" +
           " riversidecounty rc\n" +
           "ON ST_3DIntersects(rr3d.geom, rc.lod1solid);";
}

public String getBuilding3DDistanceLineQuery() {
    return "EXPLAIN ANALYZE SELECT \n" +
           " a.gid AS road_id,\n" +
           " b.ogc_fid AS building_id,\n" +
           " ST_3DDistance(a.geom, b.lod1solid) AS distance\n" +
           "FROM \n" +
           " riversideroads3d a,\n" +
           " riversidecounty b\n" +
           "WHERE \n" +
           " ST_3DIntersects(a.geom, b.lod1solid);";
}



public String getBuilding3DDistanceAreaQuery() {
    return "EXPLAIN ANALYZE SELECT \n" +
           " l.gid AS landmark_id,\n" +
           " rc.ogc_fid AS california_gml_id,\n" +
           " ST_3DDistance(l.geom, rc.lod1solid) AS distance_3d\n" +
           "FROM \n" +
           " landmarkriverside3d l\n" +
           "JOIN \n" +
           " riversidenew3d rc\n" +
           "ON ST_3DIntersects(l.geom, rc.lod1solid);";
}

public String getBuilding3DDistanceWithinBuildingQuery() {
    return "EXPLAIN ANALYZE \n" +
           "SELECT a.ogc_fid as id_a, b.ogc_fid as id_b,\n" +
           "       a.gml_id as gml_id_a, b.gml_id as gml_id_b\n" +
           "FROM riversidenew3d a\n" +
           "JOIN riversidenew3d b ON a.ogc_fid < b.ogc_fid\n" +
           "WHERE ST_3DDWithin(a.lod1solid, b.lod1solid, 10);";
}

public String getBoundingBox3DQuery() {
    return "SELECT gid, Box3D(geom) AS bounding_box_3d\n" +
           "FROM landmarkriverside3d;";
}



public String getConvexHullQuery() {
    return "EXPLAIN ANALYZE\n" +
           "SELECT gid, ST_ConvexHull(geom) AS convex_hull\n" +
           "FROM landmarkriverside3d;";
}


public String getDimensionsQuery() {
    return "SELECT DISTINCT ST_NDims(geom) as dimensions\n" +
           "FROM landmarkriverside3d\n" +
           "WHERE ST_IsValid(geom)";
}

public String getLength3DQuery() {
    return "SELECT ST_3DLength(geom) as length_3d\n" +
           "FROM landmarkriverside3d\n" +
           "WHERE ST_IsValid(geom)";
}

public String getPerimeter3DQuery() {
    return "SET TIMING ON;\n" +
           "SELECT SDO_GEOM.SDO_LENGTH(geometry, 0.005) as perimeter_3d FROM arealm3d;\n" +
           "SET TIMING OFF;";
}

public String getBridgeAnalysisQuery() {
    return "EXPLAIN ANALYZE WITH selected_points AS (\n" +
           "    SELECT latitude, longitude, 75 as elevation\n" +
           "    FROM riversidecounty\n" +
           "    TABLESAMPLE SYSTEM(1) REPEATABLE(12345)\n" +
           "    LIMIT 100\n" +
           "),\n" +
           "bridge_analysis AS (\n" +
           "    SELECT s.latitude, s.longitude,\n" +
           "           COUNT(*) as connection_points,\n" +
           "           MAX(ST_3DDistance(\n" +
           "               ST_SetSRID(ST_MakePoint(r.longitude, r.latitude, 75), 4326),\n" +
           "               ST_SetSRID(ST_MakePoint(s.longitude, s.latitude, s.elevation), 4326)\n" +
           "           )) as bridge_span\n" +
           "    FROM riversidecounty r\n" +
           "    JOIN selected_points s ON ST_3DDWithin(\n" +
           "        ST_SetSRID(ST_MakePoint(r.longitude, r.latitude, 75), 4326),\n" +
           "        ST_SetSRID(ST_MakePoint(s.longitude, s.latitude, s.elevation), 4326),\n" +
           "        100\n" +
           "    )\n" +
           "    GROUP BY s.latitude, s.longitude\n" +
           ")\n" +
           "SELECT latitude, longitude, connection_points, bridge_span\n" +
           "FROM bridge_analysis\n" +
           "WHERE connection_points > 5\n" +
           "ORDER BY bridge_span DESC\n" +
           "LIMIT 5;";
}

public String getCancerousAnalysisIntersectionQuery() {
    return "EXPLAIN ANALYZE SELECT \n" +
           " a.ogc_fid as cell1_id,\n" +
           " b.ogc_fid as cell2_id,\n" +
           " ST_3DIntersects(a.wkb_geometry, b.wkb_geometry) AS intersection_3d\n" +
           "FROM \n" +
           " synthetic_cells_3d a,\n" +
           " synthetic_cells_3d b\n" +
           "WHERE \n" +
           " a.ogc_fid < b.ogc_fid\n" +
           " AND ST_3DDWithin(a.wkb_geometry, b.wkb_geometry, 10)\n" +
           "LIMIT 10000;";
}

public String getEmergencyRoutesQuery() {
    return "EXPLAIN ANALYZE WITH selected_points AS (\n" +
           "    SELECT latitude, longitude, 0 as elevation\n" +
           "    FROM riversidenew3d\n" +
           "    TABLESAMPLE SYSTEM(1) REPEATABLE(12345)\n" +
           "    LIMIT 50\n" +
           "),\n" +
           "route_analysis AS (\n" +
           "    SELECT s.latitude, s.longitude,\n" +
           "           COUNT(*) as building_density,\n" +
           "           MAX(ST_3DDistance(\n" +
           "               ST_SetSRID(ST_MakePoint(r.longitude, r.latitude, 0), 4326),\n" +
           "               ST_SetSRID(ST_MakePoint(s.longitude, s.latitude, s.elevation), 4326)\n" +
           "           )) as clearance_radius\n" +
           "    FROM riversidenew3d r\n" +
           "    JOIN selected_points s ON ST_3DDWithin(\n" +
           "        ST_SetSRID(ST_MakePoint(r.longitude, r.latitude, 0), 4326),\n" +
           "        ST_SetSRID(ST_MakePoint(s.longitude, s.latitude, s.elevation), 4326),\n" +
           "        100\n" +
           "    )\n" +
           "    GROUP BY s.latitude, s.longitude\n" +
           ")\n" +
           "SELECT latitude, longitude, building_density, clearance_radius\n" +
           "FROM route_analysis\n" +
           "ORDER BY building_density ASC, clearance_radius DESC\n" +
           "LIMIT 5;";
}

public String getFutureExpansionQuery() {
    return "EXPLAIN ANALYZE WITH selected_points AS (\n" +
           "    SELECT latitude, longitude, 0 as elevation\n" +
           "    FROM riversidenew3d\n" +
           "    TABLESAMPLE SYSTEM(1) REPEATABLE(12345)\n" +
           "    LIMIT 100\n" +
           "),\n" +
           "expansion_analysis AS (\n" +
           "    SELECT s.latitude, s.longitude, s.elevation,\n" +
           "           COUNT(*) as building_density,\n" +
           "           ST_3DDistance(\n" +
           "               ST_SetSRID(ST_MakePoint(r.longitude, r.latitude, 0), 4326),\n" +
           "               ST_SetSRID(ST_MakePoint(s.longitude, s.latitude, s.elevation), 4326)\n" +
           "           ) as distance_to_nearest,\n" +
           "           AVG(ST_3DDistance(\n" +
           "               ST_SetSRID(ST_MakePoint(r.longitude, r.latitude, 0), 4326),\n" +
           "               ST_SetSRID(ST_MakePoint(s.longitude, s.latitude, s.elevation), 4326)\n" +
           "           )) as avg_distance\n" +
           "    FROM riversidenew3d r\n" +
           "    JOIN selected_points s ON ST_3DDWithin(\n" +
           "        ST_SetSRID(ST_MakePoint(r.longitude, r.latitude, 0), 4326),\n" +
           "        ST_SetSRID(ST_MakePoint(s.longitude, s.latitude, s.elevation), 4326),\n" +
           "        500\n" +
           "    )\n" +
           "    GROUP BY s.latitude, s.longitude, s.elevation, r.longitude, r.latitude\n" +
           ")\n" +
           "SELECT latitude, longitude, building_density, distance_to_nearest, avg_distance\n" +
           "FROM expansion_analysis\n" +
           "WHERE building_density < 50\n" +
           "ORDER BY building_density ASC, avg_distance DESC\n" +
           "LIMIT 5;";
}

public String getGardenAnalysisQuery() {
    return "EXPLAIN ANALYZE WITH selected_points AS (\n" +
           "    SELECT latitude, longitude, 100 as elevation\n" +
           "    FROM riversidecounty\n" +
           "    TABLESAMPLE SYSTEM(1) REPEATABLE(12345)\n" +
           "    LIMIT 100\n" +
           "),\n" +
           "garden_analysis AS (\n" +
           "    SELECT s.latitude, s.longitude,\n" +
           "           COUNT(*) as space_score,\n" +
           "           MIN(ST_3DDistance(\n" +
           "               ST_SetSRID(ST_MakePoint(r.longitude, r.latitude, 100), 4326),\n" +
           "               ST_SetSRID(ST_MakePoint(s.longitude, s.latitude, s.elevation), 4326)\n" +
           "           )) as clear_space\n" +
           "    FROM riversidecounty r\n" +
           "    JOIN selected_points s ON ST_3DDWithin(\n" +
           "        ST_SetSRID(ST_MakePoint(r.longitude, r.latitude, 100), 4326),\n" +
           "        ST_SetSRID(ST_MakePoint(s.longitude, s.latitude, s.elevation), 4326),\n" +
           "        150\n" +
           "    )\n" +
           "    GROUP BY s.latitude, s.longitude\n" +
           ")\n" +
           "SELECT latitude, longitude, space_score, clear_space\n" +
           "FROM garden_analysis\n" +
           "ORDER BY clear_space DESC\n" +
           "LIMIT 5;";
}

public String getLength3DMedicalAnalysisQuery() {
    return "EXPLAIN ANALYZE SELECT \n" +
           " ogc_fid,\n" +
           " ST_3DLength(wkb_geometry) AS perimeter_3d\n" +
           "FROM \n" +
           " synthetic_cells_3d;";
}

public String getPerimeter3DMedicalAnalysisQuery() {
    return "EXPLAIN ANALYZE SELECT \n" +
           " ogc_fid,\n" +
           " ST_3DMaxDistance(wkb_geometry, wkb_geometry) AS max_3d_distance\n" +
           "FROM \n" +
           " synthetic_cells_3d;";
}

public String getSubwayStationLocationQuery() {
    return "EXPLAIN ANALYZE WITH selected_points AS (\n" +
           "    SELECT latitude, longitude, 0 as elevation\n" +
           "    FROM riversidenew3d\n" +
           "    TABLESAMPLE SYSTEM(1) REPEATABLE(12345)\n" +
           "    LIMIT 100\n" +
           "),\n" +
           "point_distances AS (\n" +
           "    SELECT s.latitude, s.longitude,\n" +
           "           MIN(ST_3DDistance(\n" +
           "               ST_SetSRID(ST_MakePoint(r.longitude, r.latitude, 0), 4326),\n" +
           "               ST_SetSRID(ST_MakePoint(s.longitude, s.latitude, s.elevation), 4326)\n" +
           "           )) as min_distance_meters\n" +
           "    FROM riversidenew3d r\n" +
           "    JOIN selected_points s ON ST_3DDWithin(\n" +
           "        ST_SetSRID(ST_MakePoint(r.longitude, r.latitude, 0), 4326),\n" +
           "        ST_SetSRID(ST_MakePoint(s.longitude, s.latitude, s.elevation), 4326),\n" +
           "        25\n" +
           "    )\n" +
           "    GROUP BY s.latitude, s.longitude\n" +
           ")\n" +
           "SELECT latitude, longitude, min_distance_meters\n" +
           "FROM point_distances\n" +
           "ORDER BY min_distance_meters\n" +
           "LIMIT 5;";
}



@Override
public String getLongestLineAreaQueryLine() {
    return "SELECT \n" +
           "    rr3d.fid AS road_id,\n" +
           "    rc.ogc_fid AS california_gml_id,\n" +
           "    ST_3DLongestLine(rr3d.geom, rc.lod1solid) AS longest_line\n" +
           "FROM \n" +
           "    riversideroads3d rr3d\n" +
           "JOIN \n" +
           "    riversidecounty rc\n" +
           "ON ST_3DIntersects(rr3d.geom, rc.lod1solid);";
}

@Override
public String getLineInterpolatePointAreaQueryLine() {
    return "SELECT \n" +
           "    rr3d.fid AS road_id,\n" +
           "    rc.ogc_fid AS california_gml_id,\n" +
           "    ST_3DLineInterpolatePoint(ST_ExteriorRing(rr3d.geom), 0.5) AS interpolated_point\n" +
           "FROM \n" +
           "    riversideroads3d rr3d\n" +
           "JOIN \n" +
           "    riversidecounty rc\n" +
           "ON ST_3DIntersects(rr3d.geom, rc.lod1solid);";
}

@Override
public String getSelectLineIsContainedBuilding3D() {
    return "SELECT \n" +
           "    rr3d.fid AS road_id,\n" +
           "    rc.ogc_fid AS california_gml_id\n" +
           "FROM \n" +
           "    riversideroads3d rr3d\n" +
           "JOIN \n" +
           "    riversidecounty rc\n" +
           "ON \n" +
           "    geometry_contained_3d(rr3d.geom, rc.lod1solid)\n" +
           "LIMIT 100;";
}

@Override
public String getSelectLineOverlapsBuildings3D() {
    return "SELECT \n" +
           "    rr3d.fid AS road_id,\n" +
           "    rc.ogc_fid AS california_gml_id\n" +
           "FROM \n" +
           "    riversideroads3d rr3d\n" +
           "JOIN \n" +
           "    riversidecounty rc\n" +
           "ON \n" +
           "    geometry_overlaps_3d(rr3d.geom, rc.lod1solid)\n" +
           "LIMIT 100;";
}




/*        ending of line query                               */


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
