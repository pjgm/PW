package independent_project.config;

public class Config {

    private String tweetsPath, topicsPath, indexPath, resultsPath, qRelsPath, topQRelsPath;

    public String getTweetsPath() {
        return tweetsPath;
    }

    public String getTopicsPath() {
        return topicsPath;
    }

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
	
	public String getQRelsPath() {
		return qRelsPath;
	}
	
	public void setQRelsPath(String qRelsPath) {
		this.qRelsPath = qRelsPath;
	}

    public String getTopQRelsPath() { return topQRelsPath; }

    public void setTopQRelsPath(String topQRelsPath) { this.topQRelsPath = topQRelsPath; }
}
