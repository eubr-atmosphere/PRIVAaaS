mvn clean install

docker build --no-cache -t docker-repo.example.com:5000/analyzer:0.1 .
docker push docker-repo.example.com:5000/analyzer:0.1
