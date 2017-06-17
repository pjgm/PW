package independent_project;

import independent_project.config.Config;
import independent_project.config.ConfigParser;
import org.apache.lucene.queryparser.classic.ParseException;

import java.io.*;

import static java.lang.System.exit;

public class Tester {

    public static final String PYTHON_PATH = "/home/gustavo/miniconda2/bin/python2.7";
    public static final String EVAL_PATH = "/home/gustavo/IdeaProjects/PW/src/main/java/independent_project/evaluation/";

    public static void main(String args[]) throws IOException, ParseException {
        Config config = ConfigParser.parseFile("src/main/java/independent_project/config/cfg.txt");

        BufferedReader br = new BufferedReader(new FileReader(config.getInputPath()));

        for(String line; (line = br.readLine()) != null; ) {
            String[] split = line.split(" ");
            new InterestProfileSearcher(config, split[0], split[1], split[2], split[3], split[4]);
        }

        br.close();

        File resultsFolder = new File(config.getResultsPath());
        if(!resultsFolder.exists() || !resultsFolder.isDirectory()) {
            System.err.println("Check if results folder exists.");
            exit(-1);
        }

        for (File fileEntry : resultsFolder.listFiles()) {
            Process p = Runtime.getRuntime().exec(PYTHON_PATH+" "+EVAL_PATH+"rts2016-batchB-eval.py"+" -q "+EVAL_PATH+"rts2016-qrels.txt"+" -c "+EVAL_PATH+"rts2016-batch-clusters.json"+" -t "+EVAL_PATH+"rts2016-batch-tweets2dayepoch.txt"+" -r "+ EVAL_PATH+"results/"+fileEntry.getName()+" -o "+EVAL_PATH+"trec_eval_results/"+fileEntry.getName());
        }
    }
}
