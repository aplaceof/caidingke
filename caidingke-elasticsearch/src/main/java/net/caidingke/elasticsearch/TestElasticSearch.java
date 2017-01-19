package net.caidingke.elasticsearch;

import org.elasticsearch.action.admin.cluster.state.ClusterStateResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author bowen
 * @create 2017-01-12 15:28
 */

public class TestElasticSearch {

    private Client client;

    private XContentBuilder mapping;

    private String indexAlias = "test";
    final String newIndex = "product_v_" + System.currentTimeMillis();

    @PostConstruct
    public void setup() throws UnknownHostException {

        Settings settings = Settings.builder()
                .put("cluster.name", "my-application").build();
        // 创建client
        client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new InetSocketTransportAddress(
                        InetAddress.getByName("127.0.0.1"), 9300));
    }

    @PreDestroy
    public void release() {
        client.close();
    }


    public void rebuildIndex() throws IOException {
        client.admin().indices().prepareCreate(newIndex).get();
        Map<String, Object> json = new HashMap<String, Object>();
        json.put("user", "kimchy");
        json.put("postDate", new Date());
        json.put("message", "trying out Elasticsearch");
        json.put("bowen", "bowen");

        client.prepareIndex(newIndex, "goog", "33").setSource(json).get();
        client.prepareIndex(newIndex, "goog", "22").setSource(json).execute().actionGet();
    }

    public void flush() {
        client.admin().indices().prepareRefresh().execute().actionGet();
    }

    public void search() {
        System.out.println(client.admin().indices().prepareAliasesExist(indexAlias).execute().actionGet().exists());
        QueryBuilder matchQuery = QueryBuilders.matchQuery("bowen", "bowen");
        SearchResponse response = client.prepareSearch(newIndex)
                .setTypes("goog")
                .setSearchType(SearchType.QUERY_AND_FETCH)
                .setQuery(matchQuery)
                .execute()
                .actionGet()
                //.setQuery(QueryBuilders.termQuery("bowen", "bowen"))
                //.setQuery(QueryBuilders.te())
                // Query
                //.setPostFilter(QueryBuilders.rangeQuery("age").from(12).to(18))     // Filter
                //.setFrom(0).setSize(60).setExplain(true)
                //.get();
                ;
        System.out.println(response);
        System.out.println(response.getHits().getTotalHits());
        for (SearchHit serachHits : response.getHits()) {
            System.out.println(serachHits.getId());
        }
    }

    private SearchRequestBuilder getSearchRequestBuilder(String productSearchCriteria) {
        final SearchRequestBuilder builder = client.prepareSearch(indexAlias).setTypes("sku");
        final MultiMatchQueryBuilder queryBuilder = QueryBuilders.multiMatchQuery(productSearchCriteria,
                "name", "alias");
        queryBuilder.operator(Operator.AND);
        builder.setQuery(queryBuilder);

        List<QueryBuilder> filters = new ArrayList<QueryBuilder>();
        builder.setPostFilter(QueryBuilders.termQuery("a", "b"));
        //filters.add(QueryBuilders.termsQuery("a", "b"));

        builder.storedFields("id", "name");
        //builder.setPostFilter(QueryBuilders)
        return builder;
    }

    private void deleteIndex() {
        ClusterStateResponse response = client.admin().cluster()
                .prepareState()
                .execute().actionGet();
        String[] indexs = response.getState().getMetaData().getConcreteAllIndices();
        for (String index : indexs) {
            System.out.println(index + " delete");
            //清空所有索引。
            DeleteIndexResponse deleteIndexResponse = client.admin().indices()
                    .prepareDelete(index)
                    .execute().actionGet();

        }

    }

    public static void main(String[] args) throws IOException, InterruptedException {
        final TestElasticSearch searchTest = new TestElasticSearch();
        searchTest.setup();
        searchTest.deleteIndex();
        searchTest.rebuildIndex();
        searchTest.flush();
        searchTest.search();
        searchTest.release();
    }

}
