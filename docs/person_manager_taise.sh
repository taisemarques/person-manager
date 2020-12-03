#!/bin/bash
# define um valor padrão caso um valor de porta não tenha sido informado
PORT="${1:-8080}"

# remove caso tenha uma pasta antiga
if [ -d person-manager ]
then
rm -Rf person-manager
fi

# clona o repositório localmente
git clone "https://github.com/taisemarques/person-manager.git"

# acessa o diretório do repositório
cd person-manager/

# Maven irá limpar e compilar o projeto
mvn clean install

# remove a img do docker caso exista
docker rmi -f taisemarques/person-manager:0.1

# criará a img do docker e configurará com o projeto nessa img
mvn docker:build

# iniciará o projeto na porta fornecida
docker run -p$PORT:8080 taisemarques/person-manager:0.1

echo "service is running on port $PORT"
