package org.example;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.*;
import org.apache.solr.client.solrj.response.DelegationTokenResponse;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.SolrPingResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.CursorMarkParams;

import java.util.List;
import java.util.UUID;

public class Main {
    public static void main(String[] args) throws Exception {


        String urlString = "http://localhost:8983/solr/gettingstarted";

        try (HttpSolrClient solrClient = new HttpSolrClient.Builder(urlString).build()) {

            //solr.setParser(new DelegationTokenResponse.JsonMapResponseParser());

            SolrPingResponse pingResponse = solrClient.ping();

            //Delete all

            //UpdateResponse deleteResponse = solrClient.deleteByQuery("*:*");
            //solrClient.commit();



            System.out.println(pingResponse);
            //Indexing - Adding document.
            SolrInputDocument document = new SolrInputDocument();
            document.addField("id", "123456");
            document.addField("name", "Kenmore Dishwasher");
            document.addField("price", "599.99");
            solrClient.add(document);
            solrClient.commit();

            //Indexing With Beans
            solrClient.addBean(new Sample("987199", List.of("Name")));
            solrClient.commit();

            //Querying Indexed Documents by Field and Id
            SolrQuery query = new SolrQuery();
            query.set("q", "*:*");
            query.setRows(500);

            QueryResponse response = solrClient.query(query);

            SolrDocumentList docList = response.getResults();


            for (SolrDocument doc : docList) {
               // System.out.println(doc.getFieldValue("id") + " : " + doc);

                SolrInputDocument newDoc = new SolrInputDocument();
                newDoc.addField("id", doc.getFieldValue("id"));
                newDoc.addField("newField3", UUID.randomUUID().toString());
                newDoc.addField("newField2", UUID.randomUUID().toString());
                solrClient.add(newDoc);
            }


            //SOLR pagination

            SolrQuery solrQuery = new SolrQuery();
            solrQuery.setRows(5);
            solrQuery.setQuery("*:*");
            solrQuery.addSort("id", SolrQuery.ORDER.asc);  // Pay attention to this line
            String cursorMark = CursorMarkParams.CURSOR_MARK_START;
            boolean done = false;
            int a = 1;
            while (!done) {
                solrQuery.set(CursorMarkParams.CURSOR_MARK_PARAM, cursorMark);
                QueryResponse rsp = solrClient.query(solrQuery);
                String nextCursorMark = rsp.getNextCursorMark();
                for (SolrDocument doc : rsp.getResults()) {
                    System.out.println(doc);
                }
                if (cursorMark.equals(nextCursorMark)) {
                    done = true;
                }
                cursorMark = nextCursorMark;
                System.out.println(a*5);
                a++;
            }



            solrClient.commit();
        }
    }
}