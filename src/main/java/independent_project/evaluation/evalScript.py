import sys

QRELS_FILE = "rts2016-qrels.txt"
CLUSTERS_FILE = "rts2016-batch-clusters.json"
TWEETS_EPOCH_FILE = "rts2016-batch-tweets2dayepoch.txt"

sys.argv = ['rts2016-batchB-eval.py', '-q rts2016-qrels.txt', '-c rts2016-batch-clusters.json', '-t rts2016-batch-tweets2dayepoch.txt', '-r results.txt', '-o outTest.txt']
execfile('rts2016-batchB-eval.py')

#proc = subprocess.Popen(['python2', 'rts2016-batchB-eval.py', '-q rts2016-qrels.txt -c rts2016-batch-clusters.json -t rts2016-batch-tweets2dayepoch.txt -r results.txt -o outTest.txt'], stdout=subprocess.PIPE, stderr=subprocess.STDOUT)
#print proc.communicate()[0]
