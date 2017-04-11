package net.caidingke.elasticsearch;

import com.google.common.collect.Lists;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.common.logging.ESLoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.List;

public final class SiteHelper {

    protected static final Logger logger = ESLoggerFactory.getLogger(SiteHelper.class.getName());

    private SiteHelper() {
    }

    public static List<Site> getSites() {
        List<Site> sites = Lists.newArrayList();
        LineNumberReader reader = null;
        try {
            reader = new LineNumberReader(new BufferedReader(new FileReader("caidingke-elasticsearch/src/main/resources/sites.properties")));
            String line = null;
            while ((line = reader.readLine()) != null) {
                String[] siteParams = line.split(";");
                sites.add(new Site(siteParams[0], siteParams[1] + "," + siteParams[2]));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e, "");
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e, "");
                }
            }
        }
        return sites;
    }
}
