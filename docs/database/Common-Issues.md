##Some last minute issues that cost me a lot of time 

#FATAL:  no pg_hba.conf entry for replication connection from host
>minikube kubectl -- -n exam-server exec -it postgres-0 -- bash
>echo "host replication replica 0.0.0.0/0 scram-sha-256" >> /var/lib/postgresql/data/pgdata/pg_hba.conf
>psql -U postgres -c "SELECT pg_reload_conf();"
>su - postgres
>psql -c "SELECT pg_reload_conf();"



#FATAL: role "replica" does not exist
>minikube kubectl -- -n exam-server exec -it postgres-0 -- bash
>su - postgres
>psql -U postgres
>CREATE ROLE replica REPLICATION LOGIN ENCRYPTED PASSWORD 'replica123';
>\q
>exit

#pg_basebackup: error: directory "/var/lib/postgresql/data/pgdata" exists but is not empty
>minikube kubectl -- delete pod postgres-1 -n exam-server
