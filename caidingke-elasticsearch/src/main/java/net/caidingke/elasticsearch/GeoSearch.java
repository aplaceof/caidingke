package net.caidingke.elasticsearch;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.admin.cluster.state.ClusterStateResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.geo.GeoDistance;
import org.elasticsearch.common.logging.ESLoggerFactory;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.GeoDistanceQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

/**
 * elasticsearch geo
 *
 * @author bowen
 * @create 2017-04-11 16:04
 */

public class GeoSearch {

    private Client client;

    protected static final Logger logger = ESLoggerFactory.getLogger(GeoSearch.class.getName());

    private static final int NB_MAX_RESULTS = 100;
    private static final double ORIGIN_CITY_LON = 116.293557;  // 南邵地铁站 longitude(116.293557)
    private static final double ORIGIN_CITY_LAT = 40.213554; // 南邵地铁站 lattitude(40.213554)
    private static final int DISTANCE_FROM_ORIGIN = 1;
    private static final DistanceUnit DISTANCE_UNIT = DistanceUnit.KILOMETERS;

    @PostConstruct
    public void setup() throws UnknownHostException {

        Settings settings = Settings.builder()
                .put("cluster.name", "my-application").build();

        client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new InetSocketTransportAddress(
                        InetAddress.getByName("127.0.0.1"), 9300));
    }

    @PreDestroy
    public void release() {
        client.close();
    }

    private void deleteIndex() {
        ClusterStateResponse response = client.admin().cluster()
                .prepareState()
                .execute().actionGet();
        String[] indexs = response.getState().getMetaData().getConcreteAllIndices();
        for (String index : indexs) {
            System.out.println(index + " delete");
            client.admin().indices()
                    .prepareDelete(index)
                    .execute().actionGet();

        }

    }

    public void rebuildIndex() throws Exception {

        client.admin().indices().prepareCreate("location").execute().actionGet();
        XContentBuilder contentBuilder = buildMapping();
        logger.info("Mapping is : {}", contentBuilder.string());
        PutMappingResponse response = client
                .admin()
                .indices()
                .preparePutMapping("location")
                .setType("site")
                .setSource(contentBuilder)
                .execute()
                .actionGet();
        if (!response.isAcknowledged()) {
            throw new Exception("Could not define mapping.");
        }
        client.admin().cluster().prepareHealth().setWaitForYellowStatus().execute().actionGet();
        ObjectMapper mapper = new ObjectMapper();
        BulkRequestBuilder bulkRequestBuilder = client.prepareBulk();
        List<Site> sites = SiteHelper.getSites();
        int i = 0;
        for (Site site : sites) {
            IndexRequest indexRequest = new IndexRequest("location", "site", "site" + i);
            String jsonString = mapper.writeValueAsString(site);
            indexRequest.source(jsonString, XContentType.JSON);
            bulkRequestBuilder.add(indexRequest);
            i++;
        }
        logger.info("------------------------------------------------------------------------------");
        logger.info("Starting GeoDistanceSearch test...");
        logger.info("------------------------------------------------------------------------------");
        logger.info(sites.size() + " sites indexed");
        BulkResponse br = bulkRequestBuilder.execute().actionGet();
        if (!br.hasFailures()) {
            client.admin().indices().prepareRefresh().execute().actionGet();
        }
    }

    public void performGeoDistanceSearch() throws Exception {
        logger.info("------------------------------------------------------------------------------");
        logger.info("Searching sites...");
        logger.info("------------------------------------------------------------------------------");
        logger.info("Origin lattitude: " + ORIGIN_CITY_LAT);
        logger.info("Origin longitude: " + ORIGIN_CITY_LON);
        logger.info("Distance from origin: " + DISTANCE_FROM_ORIGIN + " " + DISTANCE_UNIT);
        logger.info("Max results limit: " + NB_MAX_RESULTS);
        GeoDistanceQueryBuilder geoDistanceQueryBuilder = QueryBuilders.geoDistanceQuery("coordinates")
                .point(ORIGIN_CITY_LAT, ORIGIN_CITY_LON)
                .distance(DISTANCE_FROM_ORIGIN, DISTANCE_UNIT)
                .geoDistance(GeoDistance.ARC);

        SearchRequestBuilder searchRequestBuilder = client.prepareSearch("location").setTypes("site")
                .setQuery(QueryBuilders.matchAllQuery())
                .setQuery(geoDistanceQueryBuilder)
                .setFrom(0)
                .setSize(NB_MAX_RESULTS)
                .addSort(SortBuilders.geoDistanceSort("coordinates", ORIGIN_CITY_LAT, ORIGIN_CITY_LON)
                        .order(SortOrder.ASC)
                        .point(ORIGIN_CITY_LAT, ORIGIN_CITY_LON)
                        .unit(DISTANCE_UNIT));
        SearchResponse resp = searchRequestBuilder.execute().actionGet();
        for (SearchHit hit : resp.getHits()) {
            logger.info(hit.getId());
        }
    }

    private static XContentBuilder buildMapping() throws Exception {
        return jsonBuilder().prettyPrint()
                .startObject()
                .startObject("site")
                .startObject("properties")
                .startObject("coordinates")
                .field("type", "geo_point")
                .endObject()
                .startObject("name")
                .field("type", "string")
                .field("index", "not_analyzed")//name 建议用ik分词
                .endObject()
                .endObject()
                .endObject()
                .endObject();
    }

    public static void main(String[] args) throws Exception {
        GeoSearch geoSearch = new GeoSearch();
        geoSearch.setup();
        geoSearch.deleteIndex();
        geoSearch.rebuildIndex();
        geoSearch.performGeoDistanceSearch();
        geoSearch.release();
    }
}
