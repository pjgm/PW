package guided_project.search;

public class Properties {

	// Flags
	public static boolean DEBUGMODE = false;
	public static boolean KAGGLEMODE = false;

	// Offline
	public static String OFFLINEQUERIES = "src/main/java/guided_project/data/queries.offline.txt";
	public static String OFFLINERESULTS = "src/main/java/guided_project/results/offline.txt";
	public static String OFFLINEQUESTIONS_PATH = "src/main/java/guided_project/data/Questions.csv";
	public static String OFFLINEANSWERS_PATH = "src/main/java/guided_project/data/Answers.csv";
	public static String OFFLINEHEADER = "QueryID\tX\tDocID\tRank\tScore\t\tRunID\n";

	// Kaggle
	public static String KAGGLEQUERIES = "src/main/java/guided_project/data/queries.kaggle.txt";
	public static String KAGGLERESULTS = "src/main/java/guided_project/results/kaggle.csv";
	public static String KAGGLEQUESTIONS_PATH = "src/main/java/guided_project/data/Questions.csv";
	public static String KAGGLEANSWERS_PATH = "src/main/java/guided_project/data/Answers.csv";
	public static String KAGGLEHEADER = "\"ID\",\"AnswerID\"\n";

	// PageRank
	public static double DAMPING = 0.70;
	public static int ITERATIONS = 10;

	// Indexer
	public static double ALFA = 0.5;

	// Analyzer //They are in order of application
	public static boolean WHITESPACETOKENIZER = false;
	public static boolean LOWERCASE = true;
	public static boolean STOPFILTER = true;
	public static boolean PORTERSTEMFILTER = true;
	public static boolean NGRAMTOKENFILTER = false;
	public static boolean EDGENGRAMCOMMONFILTER = false;
	public static boolean SHINGLEFILTER = false;
	public static boolean SNOWBALLFILTER = false;
	public static boolean ENGLISHPOSSESSIVEFILTER = false;
	public static boolean KSTEMFILTER = false;
	
	// Random
	public static String INDEXPATH = "index";
	public static String CHARSET = "UTF-8";
}
