Documentation


DB:

#setup docker
sudo apt update
sudo apt install -y docker.io
sudo systemctl enable docker
sudo systemctl start docker

sudo usermod -aG docker $USER
newgrp docker

docker --version

#add curl for minikube
curl -LO https://storage.googleapis.com/minikube/releases/latest/minikube-linux-amd64


#setup minikube and start cluster
sudo install minikube-linux-amd64 /usr/local/bin/minikube
minikube start --driver=docker

#if minikube not installed
minikube kubectl -- get nodes 

#define namespace to deploy the db on
minikube kubectl -- create namespace exam-server

#deploy Postgres statefulset
create a simple master-worker in k8 directory as postgres-statefulset.yaml then deploy it wth minikube

#apply the yaml and verify pods
minikube kubectl -- apply -f k8s/postgres-config.yaml
minikube kubectl -- apply -f k8s/postgres-statefulset.yaml
minikube kubectl -- get pods -n exam-server

#check pods and pvc
minikube kubectl -- get pods -n exam-server
minikube kubectl -- get pvc -n exam-server

#deploy postgres-service
create a simple postgres service in k8 directory as postgres-service.yaml then deploy it wth minikube

#apply and check
minikube kubectl -- apply -f k8s/postgres-service.yaml
minikube kubectl -- get svc -n exam-server

#deploy pgbouncer-config
minikube kubectl -- apply -f k8s/pgbouncer-config.yaml

#deploy pgbouncer
create a simple pgbouncer in k8 directory as pgbouncer-deployment.yaml then deploy it wth minikube

#apply and check
minikube kubectl -- apply -f k8s/pgbouncer-deployment.yaml
minikube kubectl -- get pods -n exam-server

#connection verification
minikube kubectl -- exec -it postgres-0 -n exam-server -- psql -U postgres -d exam_server_BD

#execute the sql script
minikube kubectl -- exec -i postgres-0 -n exam-server -- psql -U postgres -d exam_server_BD < sql/student_results.sql



#API


##API-SERVICE

#initialized a simple spring-boot api service from https://start.spring.io/

Project: Maven
Language: Java 17
Spring-boot: 3.5.4
Group: com.examserver
Artifact:api-service
Name:api-service
Description: Public exam server's API
Package Name: com.examserver.api-service
Packaging: Jar

Dependencies:
Spring Web
Spring Data JPA
Postgresql Driver

##Dockerize the API 

#write a Dockerfile in api-service
#from inside api-service
#build docker image and push to dockerhub


docker build -t api-service:latest .

docker tag api-service:latest aabidrahman/api-service:latest

docker push aabidrahman/api-service:latest

#for rebuild (if needed)
docker build -t aabidrahman/api-service:latest .
docker push aabidrahman/api-service:latest

#verify
docker images

# install helm-charts(IAC!!!)

#update the value.yaml, deployment.yaml andseervice.yaml in helm charts (IAC!!!)
values.yaml>>>
# Default values for api-service.
# This is a YAML-formatted file.
# Declare variables to be passed into your templates.

# This will set the replicaset count more information can be found here: https://kubernetes.io/docs/concepts/workloads/controllers/replicaset/
#replicaCount: 1
replicaCountWrite: 1
replicaCountRead: 2  

# This sets the container image more information can be found here: https://kubernetes.io/docs/concepts/containers/images/
image:
  repository: aabidrahman/api-service
  # This sets the pull policy for images.
  pullPolicy: IfNotPresent
  # Overrides the image tag whose default is the chart appVersion.
  tag: "latest"

# This is for the secrets for pulling an image from a private repository more information can be found here: https://kubernetes.io/docs/tasks/configure-pod-container/pull-image-private-registry/
imagePullSecrets: []
# This is to override the chart name.
nameOverride: ""
fullnameOverride: ""

# This section builds out the service account more information can be found here: https://kubernetes.io/docs/concepts/security/service-accounts/
serviceAccount:
  # Specifies whether a service account should be created
  create: true
  # Automatically mount a ServiceAccount's API credentials?
  automount: true
  # Annotations to add to the service account
  annotations: {}
  # The name of the service account to use.
  # If not set and create is true, a name is generated using the fullname template
  name: ""

# This is for setting Kubernetes Annotations to a Pod.
# For more information checkout: https://kubernetes.io/docs/concepts/overview/working-with-objects/annotations/
podAnnotations: {}
# This is for setting Kubernetes Labels to a Pod.
# For more information checkout: https://kubernetes.io/docs/concepts/overview/working-with-objects/labels/
podLabels: {}

podSecurityContext: {}
  # fsGroup: 2000

securityContext: {}
  # capabilities:
  #   drop:
  #   - ALL
  # readOnlyRootFilesystem: true
  # runAsNonRoot: true
  # runAsUser: 1000

# This is for setting up a service more information can be found here: https://kubernetes.io/docs/concepts/services-networking/service/
service:
  # This sets the service type more information can be found here: https://kubernetes.io/docs/concepts/services-networking/service/#publishing-services-service-types
  type: ClusterIP
  # This sets the ports more information can be found here: https://kubernetes.io/docs/concepts/services-networking/service/#field-spec-ports
  port: 8080

# This block is for setting up the ingress for more information can be found here: https://kubernetes.io/docs/concepts/services-networking/ingress/
ingress:
  enabled: false
  className: ""
  annotations: {}
    # kubernetes.io/ingress.class: nginx
    # kubernetes.io/tls-acme: "true"
  hosts:
    - host: chart-example.local
      paths:
        - path: /
          pathType: ImplementationSpecific
  tls: []
  #  - secretName: chart-example-tls
  #    hosts:
  #      - chart-example.local

resources: {}
  # We usually recommend not to specify default resources and to leave this as a conscious
  # choice for the user. This also increases chances charts run on environments with little
  # resources, such as Minikube. If you do want to specify resources, uncomment the following
  # lines, adjust them as necessary, and remove the curly braces after 'resources:'.
  # limits:
  #   cpu: 100m
  #   memory: 128Mi
  # requests:
  #   cpu: 100m
  #   memory: 128Mi

# This is to setup the liveness and readiness probes more information can be found here: https://kubernetes.io/docs/tasks/configure-pod-container/configure-liveness-readiness-startup-probes/
livenessProbe:
  httpGet:
    path: /actuator/health
    port: 8080
readinessProbe:
  httpGet:
    path: /actuator/health
    port: 8080

# This section is for setting up autoscaling more information can be found here: https://kubernetes.io/docs/concepts/workloads/autoscaling/
autoscaling:
  enabled: false
  minReplicas: 1
  maxReplicas: 100
  targetCPUUtilizationPercentage: 80
  # targetMemoryUtilizationPercentage: 80

# Additional volumes on the output Deployment definition.
volumes: []
# - name: foo
#   secret:
#     secretName: mysecret
#     optional: false

# Additional volumeMounts on the output Deployment definition.
volumeMounts: []
# - name: foo
#   mountPath: "/etc/foo"
#   readOnly: true

nodeSelector: {}

tolerations: []

affinity: {}



deployment.yaml>>>
# Deployment for Write API
apiVersion: apps/v1
kind: Deployment
metadata:
  name: {{ include "api-service.fullname" . }}-write
spec:
  replicas: {{ .Values.replicaCountWrite }}
  selector:
    matchLabels:
      app: {{ include "api-service.fullname" . }}-write
  template:
    metadata:
      labels:
        app: {{ include "api-service.fullname" . }}-write
    spec:
      containers:
        - name: api-service
          image: "{{ .Values.image.repository }}:{{ .Values.image.tag }}"
          ports:
            - containerPort: 8080

---
# Deployment for Read API
apiVersion: apps/v1
kind: Deployment
metadata:
  name: {{ include "api-service.fullname" . }}-read
spec:
  replicas: {{ .Values.replicaCountRead }}
  selector:
    matchLabels:
      app: {{ include "api-service.fullname" . }}-read
  template:
    metadata:
      labels:
        app: {{ include "api-service.fullname" . }}-read
    spec:
      containers:
        - name: api-service
          image: "{{ .Values.image.repository }}:{{ .Values.image.tag }}"
          ports:
            - containerPort: 8080


service.yml>>>
# Service for Write API
apiVersion: v1
kind: Service
metadata:
  name: {{ include "api-service.fullname" . }}-write
spec:
  type: ClusterIP
  selector:
    app: {{ include "api-service.fullname" . }}-write
  ports:
    - protocol: TCP
      port: 8080
      targetPort: 8080

---
# Service for Read API
apiVersion: v1
kind: Service
metadata:
  name: {{ include "api-service.fullname" . }}-read
spec:
  type: ClusterIP
  selector:
    app: {{ include "api-service.fullname" . }}-read
  ports:
    - protocol: TCP
      port: 8080
      targetPort: 8080



#deploy the api using helmcharts(CI-CD!!!)
helm upgrade --install api-service ./api-service \
  --namespace exam-server \
  --create-namespace \
  --set image.repository=aabidrahman/api-service \
  --set image.tag=latest

#verify read and write api are running
minikube kubectl -- get pods -n exam-server
minikube kubectl -- get svc -n exam-server

#check runtime logs for both read and write api
minikube kubectl -- logs -n exam-server deploy/api-service-read
minikube kubectl -- logs -n exam-server deploy/api-service-write

#local machine port forwarding(need to be active to send,read data)
minikube kubectl -- port-forward svc/api-service-write 8080:8080 -n exam-server
#(use new terminal for read)
minikube kubectl -- port-forward svc/api-service-read 8081:8080 -n exam-server

#verify datasource
minikube kubectl -- exec -it api-service-read-7797fbc689-rqt8l -n exam-server -- env | grep SPRING_DATASOURCE




minikube kubectl -- delete statefulset postgres -n exam-server
minikube kubectl -- apply -f k8s/postgres-config.yaml
minikube kubectl -- apply -f k8s/postgres-service.yaml
minikube kubectl -- apply -f k8s/postgres-statefulset.yaml
minikube kubectl -- get pods -n exam-server -w


minikube kubectl -- delete statefulset postgres -n exam-server
minikube kubectl -- delete pvc -n exam-server -l app=postgres

minikube ssh
sudo rm -rf /var/lib/minikube/storage/*
exit


minikube kubectl -- apply -f k8s/postgres-config.yaml
minikube kubectl -- apply -f k8s/postgres-statefulset.yaml


minikube kubectl -- get pods -n exam-server -w


minikube kubectl -- apply -f k8s/postgres-config.yaml
minikube kubectl -- apply -f k8s/postgres-statefulset.yaml
minikube kubectl -- apply -f k8s/postgres-service.yaml
minikube kubectl -- get pods -n exam-server -w


minikube kubectl -- apply -f k8s/postgres-config.yaml
minikube kubectl -- apply -f k8s/postgres-statefulset.yaml
minikube kubectl -- apply -f k8s/postgres-service.yaml
minikube kubectl -- get pods -n exam-server -w




======================================
apiVersion: apps/v1
kind: StatefulSet
metadata:
  name: postgres
  namespace: exam-server
spec:
  serviceName: "postgres-service"
  replicas: 2
  podManagementPolicy: OrderedReady
  revisionHistoryLimit: 1
  updateStrategy:
    type: RollingUpdate
  selector:
    matchLabels:
      app: postgres
  template:
    metadata:
      labels:
        app: postgres
    spec:
      initContainers:
      - name: copy-config
        image: postgres:14
        command:
        - sh
        - -c
        - |
          if [ ! -f /var/lib/postgresql/data/pgdata/pg_hba.conf ]; then
            mkdir -p /var/lib/postgresql/data/pgdata
            cp /config/pg_hba.conf /var/lib/postgresql/data/pgdata/pg_hba.conf
            chown postgres:postgres /var/lib/postgresql/data/pgdata/pg_hba.conf
          fi
        volumeMounts:
        - name: config
          mountPath: /config
        - name: postgres-storage
          mountPath: /var/lib/postgresql/data
      - name: setup-replica
        image: postgres:14
        command:
        - sh
        - -c
        - |
          if [ "$(hostname)" != "postgres-0" ]; then
            until pg_isready -h postgres-0.postgres-service.exam-server.svc.cluster.local -p 5432; do
              echo "Waiting for master to be ready..."
              sleep 5
            done
            until pg_basebackup -h postgres-0.postgres-service.exam-server.svc.cluster.local \
                                -D /var/lib/postgresql/data/pgdata \
                                -U replica -v -P -R; do
              echo "Waiting for master backup..."
              sleep 5
            done
          fi
        env:
        - name: PGPASSWORD
          value: replica123
        volumeMounts:
        - name: postgres-storage
          mountPath: /var/lib/postgresql/data
      containers:
      - name: postgres
        image: postgres:14
        ports:
        - containerPort: 5432
        env:
        - name: POSTGRES_DB
          value: exam_server_BD
        - name: POSTGRES_USER
          value: postgres
        - name: POSTGRES_PASSWORD
          value: postgres123
        - name: POSTGRES_REPLICATION_USER
          value: replica
        - name: POSTGRES_REPLICATION_PASSWORD
          value: replica123
        - name: PGDATA
          value: /var/lib/postgresql/data/pgdata
        volumeMounts:
        - name: postgres-storage
          mountPath: /var/lib/postgresql/data
      volumes:
      - name: config
        configMap:
          name: postgres-config
  volumeClaimTemplates:
  - metadata:
      name: postgres-storage
    spec:
      storageClassName: standard
      accessModes: ["ReadWriteOnce"]
      resources:
        requests:
          storage: 1Gi

===================================================

Nuclear Reset-(SETUP AND DB)-



#setup docker
sudo apt update
sudo apt install -y docker.io
sudo systemctl enable docker
sudo systemctl start docker

sudo usermod -aG docker $USER
newgrp docker

docker --version

#add curl for minikube
curl -LO https://storage.googleapis.com/minikube/releases/latest/minikube-linux-amd64


#setup minikube and start cluster
sudo install minikube-linux-amd64 /usr/local/bin/minikube




minikube kubectl -- delete statefulset postgres -n exam-server
minikube kubectl -- delete pvc -n exam-server -l app=postgres
SSH into Minikube and wipe the storage directory completely:


minikube ssh
sudo rm -rf /var/lib/minikube/storage/*
exit

Check that your PVCs are gone:

minikube kubectl -- get pvc -n exam-server

There should be nothing left.

Now re-apply your ConfigMap, StatefulSet, and Service, and watch the pods:

minikube kubectl -- apply -f k8s/postgres-config.yaml
minikube kubectl -- apply -f k8s/postgres-statefulset-single.yaml
minikube kubectl -- apply -f k8s/postgres-service.yaml
minikube kubectl -- get pods -n exam-server -w
minikube kubectl -- apply -f k8s/postgres-statefulset.yaml



#FATAL:  no pg_hba.conf entry for replication connection from host
minikube kubectl -- -n exam-server exec -it postgres-0 -- bash
echo "host replication replica 0.0.0.0/0 scram-sha-256" >> /var/lib/postgresql/data/pgdata/pg_hba.conf
psql -U postgres -c "SELECT pg_reload_conf();"
su - postgres
psql -c "SELECT pg_reload_conf();"



#FATAL: role "replica" does not exist
minikube kubectl -- -n exam-server exec -it postgres-0 -- bash
su - postgres
psql -U postgres
CREATE ROLE replica REPLICATION LOGIN ENCRYPTED PASSWORD 'replica123';
\q
exit

#pg_basebackup: error: directory "/var/lib/postgresql/data/pgdata" exists but is not empty
minikube kubectl -- delete pod postgres-1 -n exam-server

===================================================

minikube kubectl -- exec -n exam-server postgres-0 -- cat /var/lib/postgresql/data/pgdata/pg_hba.conf
minikube kubectl -- exec -n exam-server postgres-1 -- ls /var/lib/postgresql/data/pgdata/
SELECT md5('postgres123' || 'postgres');


#DB connection verification
minikube kubectl -- exec -it postgres-0 -n exam-server -- psql -U postgres -d exam_server_BD

#DB execute the sql script
minikube kubectl -- exec -i postgres-0 -n exam-server -- psql -U postgres -d exam_server_BD < sql/student_results.sql

===================================================

BOUNCER

===================================================

minikube kubectl -- apply -f k8s/pgbouncer-config.yaml

minikube kubectl -- apply -f k8s/pgbouncer-deployment.yaml

minikube kubectl -- apply -f k8s/pgbouncer-service.yaml


#API

docker login


docker build -t api-service:latest .

docker tag api-service:latest aabidrahman/api-service:latest

docker push aabidrahman/api-service:latest

#for rebuild (if needed)
docker build -t aabidrahman/api-service:latest .
docker push aabidrahman/api-service:latest

#verify
docker images



#deploy the api using helmcharts(CI-CD!!!)
helm upgrade --install api-service ./api-service \
  --namespace exam-server \
  --create-namespace \
  --set image.repository=aabidrahman/api-service \
  --set image.tag=latest

#verify read and write api are running
minikube kubectl -- get pods -n exam-server
minikube kubectl -- get svc -n exam-server

#check runtime logs for both read and write api
minikube kubectl -- logs -n exam-server deploy/api-service-read
minikube kubectl -- logs -n exam-server deploy/api-service-write

#local machine port forwarding(need to be active to send,read data)
minikube kubectl -- port-forward svc/api-service-write 8080:8080 -n exam-server
#(use new terminal for read)
minikube kubectl -- port-forward svc/api-service-read 8081:8080 -n exam-server

#verify datasource
minikube kubectl -- exec -it api-service-read-7797fbc689-rqt8l -n exam-server -- env | grep SPRING_DATASOURCE






#Test connection
minikube kubectl -- exec -it -n exam-server pgbouncer-<pod-name> -- psql -U postgres -d write_db -h localhost -p 5432

minikube kubectl -- exec -it <api-read-pod> -n exam-server -- env | grep SPRING_DATASOURCE

minikube kubectl -- exec -it pgbouncer-56ccc55cd4-m7vf2 -n exam-server -- psql -U <username> -d <dbname>

minikube kubectl -- describe pod api-service-read-57746c7bd6-8wkgx -n exam-server


minikube kubectl -- port-forward svc/api-write 8080:8080 -n exam-server

# Check logs for write API
minikube kubectl -- logs -n exam-server deploy/api-service-write

minikube kubectl -- port-forward svc/api-read 8081:8080 -n exam-server

# Check logs for read API
minikube kubectl -- logs -n exam-server deploy/api-service-read


dash====

minikube kubectl -- create namespace monitoring
helm repo add prometheus-community https://prometheus-community.github.io/helm-charts
helm repo add grafana https://grafana.github.io/helm-charts

cd/helm-charts/

helm install prometheus prometheus-community/kube-prometheus-stack \
  --namespace monitoring \
  --set prometheus.prometheusSpec.serviceMonitorSelectorNilUsesHelmValues=false

minikube kubectl -- --namespace monitoring get secrets prometheus-grafana -o jsonpath="{.data.admin-password}" | base64 -d ; echo
#prom-operator

minikube kubectl -- get pods -n monitoring
minikube kubectl -- get svc -n monitoring

helm install grafana grafana/grafana \
  --namespace monitoring \
  --set adminUser=admin \
  --set adminPassword=admin \
  --set service.type=NodePort

minikube kubectl -- get secret --namespace monitoring grafana -o jsonpath="{.data.admin-password}" | base64 --decode ; echo
#admin

minikube kubectl -- get pods -n monitoring
minikube kubectl -- get svc grafana -n monitoring

minikube service grafana -n monitoring


sum(container_memory_usage_bytes{namespace!="monitoring"}) by (pod, namespace)
sum(rate(container_cpu_usage_seconds_total[1m])) by (pod, namespace)
sum(container_memory_usage_bytes{namespace!="monitoring"}) by (pod, namespace)
sum(rate(container_cpu_usage_seconds_total[1m])) by (svc, namespace)
sum(rate(container_cpu_usage_seconds_total{namespace="exam-server"}[1m])) by (pod)
sum(rate(container_cpu_usage_seconds_total{namespace="monitoring"}[1m])) by (pod)
sum(container_memory_usage_bytes{namespace="exam-server"}) by (pod)
sum(container_memory_usage_bytes{namespace="monitoring"}) by (pod)
