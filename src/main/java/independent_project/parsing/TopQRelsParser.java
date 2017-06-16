package independent_project.parsing;

import independent_project.config.Config;
import independent_project.model.interest_profile.Topic;
import independent_project.model.twitter.Tweet;

import java.io.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TopQRelsParser {
    public static Map<String, Map<String, Tweet>> parseFile(Config config) throws IOException {
        Map<String, Map<String, Tweet>> map = new HashMap();
        File f = new File(config.getTopQRelsPath());
        if(f.exists() && !f.isDirectory()){
            BufferedReader br = new BufferedReader(new FileReader(config.getTopQRelsPath()));
            String line;
            boolean header = true;
            while ((line = br.readLine()) != null) {
                if (header) {
                    header = false;
                    continue;
                }
                String[] split = line.split("\t");
                map.put(split[0], new HashMap());
            }
        }else {
            List<Topic> values = QRelsParser.parseFile(config.getQRelsPath());
            PrintWriter pw = new PrintWriter(config.getTopQRelsPath());
            pw.println("Topic   Cumulative Interest");
            for (Topic t : values) {
                map.put(t.topid, new HashMap<>());
                pw.println(t.topid + "\t" + t.interest);
            }
            pw.close();
        }
        return map;
    }
}
