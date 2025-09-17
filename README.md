# Documentaion 
# exam-server-devops
Cloud-native Exam Results System with PostgreSQL, Kubernetes, CI/CD, and Observability
- **Project Details is available [here](docs/task-details/DevOps%20Task%20Details.pdf).**
- **TDD is available [here](docs/task-details/Documentation.pdf).**

## The Scenario

Two services will be utilized in this setup: an API service and a PostgreSQL database service.
The API service will handle all client interactions, while the database will be deployed on
Kubernetes with a master-worker architecture, ensuring data persistence. An open-source
connection pooler must be implemented to optimize database connections.

Assume this is a public exam server for Bangladesh. To simplify, we will have a single table
in the database structured as described below. To make this setup more realistic, you need
to insert at least **1 million** records into this table, each containing a roll number and random
marks, using the PostgreSQL CLI. Improving read/write performance is critical here; among
various optimization techniques, **database partitioning** will be the primary focus.


## Database Design

A single table named student_results is to be used:

| Column Name | Data Type | Notes |
|:----------|:----------|:----------|
| roll_number  | BIGINT PRIMARY KEY  | Unique roll number per student  |
| marks  | INT  | Student's marks in the exam  |
| exam_year  | INT  | Exam year for partitioning  |

A **read-to-write** ratio of **10:1.** Accordingly, the API service must be split into
separate **read** and **write** replicas —

- The **read replicas** will serve all read-only API endpoints.
- The **write replicas** will handle all write operations (create, update, delete).
- API service can be developed in **any programming language.**


## Objectives

1. API Service:
Develop RESTful APIs to Create, Read, Update, and Delete student
records. The API service should be architected with separate read and write replicas
based on the specified read/write ratio.

2. Infrastructure as Code (IAC): 
Write IAC scripts using Terraform, CloudFormation,
or Pulomi to provision the required cloud resources. You may choose any cloud
provider (AWS, Azure, or GCP). If you prefer to use local VMs instead, configure the
Kubernetes cluster with **Ansible**

3. Deployment Packaging: 
Create Helm charts for smooth and efficient deployment
of the API service.

4. CI/CD Pipeline: 
Implement a GitHub Actions pipeline supporting zero downtime
deployment. The pipeline should incorporate a widely recognized zero downtime
strategy.

5. Database Deployment: 
Deploy the PostgreSQL cluster according to the masterworker architecture, along with the chosen open-source connection pooler.

6. Access Control: 
Ensure that the connection pooler pods can exclusively
communicate with the PostgreSQL database pods, and that the API service replicas
access the connection pooler appropriately.

7. Scaling and High Availability: 
Since the read replicas will handle significantly higher
traffic, implement an appropriate scaling policy to ensure they can scale efficiently
under heavy load. Additionally, because the connection pooler represents a
potential single point of failure, design and deploy it with high availability in mind.

8. Database Partitioning:
Implement partitioning on the database table. Document the
entire process of inserting 1 million records and all steps involved in partitioning
within the project repository.

9. Observability: 
Implement observability focusing on metrics and logging. Create
and include in the repository a dashboard displaying pod persistent volume metrics
alongside other relevant metrics.


## Deliverables

The GitHub repository contains:

- Source code for the **API service**
- **IaC scripts**
- **Helm charts**
- **CI/CD pipeline configuration**
- Documentations on:
  - **Database setup** and **Partitioning process**
  - **Scaling configuration**
  - **Observability setup**
