package independent_project.config;

public class Config {

    private String tweetsPath, topicsPath, indexPath, resultsPath, qRelsPath, topQRelsPath, inputPath, trecEvalResPath;

    public String getTweetsPath() {
        return tweetsPath;
    }

    public String getTopicsPath() { return topicsPath; }

    public void setTweetsPath(String tweetsPath) {
        this.tweetsPath = tweetsPath;
    }

    public void setTopicsPath(String topicsPath) {
        this.topicsPath = topicsPath;
    }

    public String getIndexPath() {
        return indexPath;
    }

    public void setIndexPath(String indexPath) {
        this.indexPath = indexPath;
    }

    public String getResultsPath() {
        return resultsPath;
    }

	public void setResultsPath(String resultsPath) {
        this.resultsPath = resultsPath;
    }

    public String getTrecEvalResPath() {
        return trecEvalResPath;
    }

    public void setTrecEvalResPath(String trecEvalResPath) {
        this.trecEvalResPath = trecEvalResPath;
    }
	
	public String getQRelsPath() {
		return qRelsPath;
	}
	
	public void setQRelsPath(String qRelsPath) {
		this.qRelsPath = qRelsPath;
	}

    public String getTopQRelsPath() { return topQRelsPath; }

    public void setTopQRelsPath(String topQRelsPath) { this.topQRelsPath = topQRelsPath; }

    public String getInputPath() { return inputPath; }

    public void setInputPath(String inputPath) { this.inputPath = inputPath; }
}
