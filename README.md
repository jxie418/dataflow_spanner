How to run it from command line.
mvn -Pdataflow-runner compile exec:java \
      -Dexec.mainClass=com.example.dataflow.TestDFJobJava \
      -Dexec.args="--project={projectId} \
      --zone=us-central1-a \
      --numWorkers=3 \
      --workerMachineType=n1-highmem-4 \
      --autoscalingAlgorithm=THROUGHPUT_BASED \
      --appName=TestDFJobJava \
      --maxNumWorkers=3 \
      --subnetwork=regions/us-central1/subnetworks/kubernetes \
      --runner=DataflowRunner"
