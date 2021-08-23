# Reporting System

# Requirement

We have a table in an upstream database containing our employee labour costs. Our business want us to create a regular report summarising total costs per department. This report will be fed into the Firm’s central accounting system to help give transparency to the business as to the true overhead of running the firm per department.

# Sample Data

|Employee ID   |Title  | Department  | Monthly Cost  | Currency  |
|---|---|---|---|---|
| 91847  | Associate  | Sales  |  500 | USD  |
| 10900  | Manager  | Sales  |  1200 | EUR  |
| 36197  | Senior Manager  | Sales  |300000   | JPY  |
| 91468  | Associate  | Administration  |500   | GBP  |
|  07397 |  Associate | Administration  |  700 | USD  |
|  42954 | Manager  | Administration  |  1000 |  USD |

# Reporting requirements

We need a process that on a daily basis, takes the data from the table and produces a grand total of current labour costs per department. It needs to produce an output file which will be delivered to a downstream accounting system. The file should contain one row per department, with each row having the department name and the total current labour costs for that department. 

# Design

![Alt text](report-system-diagram.jpg?raw=true)

# Solution

## Technology Stack:

     1. Java
	 2. Spring Batch
	 3. MapStruct
 	 4. Gradle
     5. MYSQL

## Running Instruction:

	1. gradle clean build
	2. cd  \reporting-system\build\libs
	3. java -jar -Dremote.client.accessKey="" -Dbatch.datasource.password="" report-0.0.1-SNAPSHOT.jar
	4. Hit GET http://localhost:8080/run/{baseCurrency}

## Explanation

We have used Spring Batch heavily to solve this problem. 

### Flow

External rest trigger starts the batch job. The job consist of a Master/partitioner step and a Follower/Worker Step.


```
public Step masterStep(PartitionHandler partitionHandler) {
    return stepBuilderFactory
        .get("partition-step")
        .partitioner("read-employee-data-step", partitioner)
        .partitionHandler(partitionHandler)
        .aggregator(stepExecutionAggregator)
        .build();
  }
```


* The Master step runs first which is configured with the custom database partitioner, a partition Handler & aggregator.
* Database partitioner calls a database to fetch the unique department name and set that in the execution context.

```
 public PartitionHandler partitionHandler(@Qualifier("readDatabaseStep") Step readDatabaseStep) {
    TaskExecutorPartitionHandler taskExecutorPartitionHandler = new TaskExecutorPartitionHandler();
    taskExecutorPartitionHandler.setTaskExecutor(taskExecutor);
    taskExecutorPartitionHandler.setStep(readDatabaseStep);
    taskExecutorPartitionHandler.setGridSize(databaseProperty.getMaxPoolSize());
    return taskExecutorPartitionHandler;
  }
```
* TaskExecutorPartitionHandler implementation of PartitionHandler has been used here.
* TaskExecutorPartitionHandler starts N number of threads and each thread runs 'read-employee-data-step' in parallel.

```
 public Step readDatabaseStep(@Value("${batch.chunkSize}") int chunkSize) {
    return this.stepBuilderFactory
        .get("read-employee-data-step")
        .<DepartmentDTO, DepartmentDataBO>chunk(chunkSize)
        .reader(employeeItemReader)
        .processor(compositeItemProcessor)
        .writer(departmentItemWriter)
        .build();
  }
```
* read-employee-data-step get the department name from the step context that's been passed by the TaskExecutorPartitionHandler.
* read-employee-data-step read the data for a particular department in small chunk from the database.

```
SELECT new com.cm.batch.report.repositories.DepartmentDTO(e.department, SUM(e.cost) , e.currency) FROM EmployeeEntity e group by  e.department, e.currency HAVING e.department = :deptName
```  
* Call the Remote currency service to get the currency conversion rate in the processor.
* This remote call is cached in memory.
* At the end of the read-employee-data-step, it writes data the sum of data for a given department in a step context.
* when all the partitioned steps get completed, Step Execution Aggregator has been called.
* Step Execution Aggregator iterate over the partitioned step context and create a list of ReportModel & set that in the job execution context.

```
public class ReportModel {
private String departmentName;
private BigDecimal totalCost;
private String baseCurrency;
}
```

* At the end job, Job listener has been called which fetches the list of ReportModel from Job execution context & pass that to the PDF generator.
* PDF generator creates a PDF file having department cost data.

  ![Alt text](report_file.jpg?raw=true)

# Further Improvement
 In place of TaskExecutorPartitionHandler, we can use MessageChannelPartitionHandler or a DeployerPartitionHandler.

MessageChannelPartitionHandler - It runs the worker step outside the master step and communicate via message channel.
DeployerPartitionHandler - Launches the worker on at the runtime.
