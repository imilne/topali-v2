#$ -l fq=true

#$ -N TPi-PhyML
#$ -cwd
#$ -j y

hostname
$JAVA -Xmx256m -cp $TOPALi topali.cluster.jobs.phyml.PhymlAnalysis "$JOB_DIR"