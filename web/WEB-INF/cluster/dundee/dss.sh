#$ -q 32.bit-q
#$ -l www_parallel=1

#$ -N TPi-DSS
#$ -cwd
#$ -t 1-$RUN_COUNT

$JAVA -cp $TOPALi topali.cluster.dss.DSSAnalysis "$JOB_DIR/run$SGE_TASK_ID"