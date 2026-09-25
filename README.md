# Pesquisa IA --- Sistema de Análise de Pesquisa sobre Inteligência Artificial na Auditoria

Sistema desenvolvido para apoiar a pesquisa acadêmica:

> **O uso da Inteligência Artificial na atividade de auditoria:
> percepção de profissionais da contabilidade sobre benefícios, riscos e
> transformação das atividades profissionais.**

O sistema recebe as respostas de um questionário exportado do Google
Forms em formato XLSX, armazena os dados em MySQL, realiza análises
quantitativas, utiliza um LLM para classificação qualitativa das
respostas abertas e disponibiliza os resultados em um dashboard HTML
acessível diretamente pelo navegador.

------------------------------------------------------------------------

## 1. Visão geral

O fluxo principal do sistema é:

``` text
Google Forms
     |
     v
Arquivo XLSX
     |
     v
POST /api/research/analyze
     |
     v
Importação das respostas
     |
     +----------------------+
     |                      |
     v                      v
Análise quantitativa       LLM / Gemini
     |                      |
     |                      v
     |                Classificação qualitativa
     |                      |
     +----------+-----------+
                |
                v
        Persistência MySQL
                |
                v
        Processamento concluído
                |
                v
GET /api/research/jobs/{jobId}
                |
                v
GET /api/research/{researchId}/dashboard
                |
                v
        Dashboard HTML
```

O processamento é assíncrono. O endpoint de análise inicia o
processamento e devolve um `jobId`. O cliente pode consultar o andamento
por meio do endpoint de status.

------------------------------------------------------------------------

# 2. Principais funcionalidades

O sistema possui três operações principais expostas pela API:

  --------------------------------------------------------------------------------
  Endpoint                                     Finalidade
  -------------------------------------------- -----------------------------------
  `POST /api/research/analyze`                 Inicia a importação e análise de
                                               uma pesquisa

  `GET /api/research/jobs/{jobId}`             Consulta o status do processamento

  `GET /api/research/{researchId}/dashboard`   Exibe o dashboard da pesquisa
  --------------------------------------------------------------------------------

Além dessas operações, existem serviços internos responsáveis pela
importação do Excel, estatística quantitativa, classificação
qualitativa, persistência e geração do dashboard.

------------------------------------------------------------------------

# 3. Tecnologias utilizadas

## Backend

-   Java 21
-   Spring Boot 4.1.1
-   Spring Web MVC
-   Spring Data JPA
-   Hibernate
-   Lombok
-   Maven
-   Spring Boot DevTools

## Banco de dados

-   MySQL 8.x
-   MySQL Connector/J
-   Docker para execução local do banco

## Migração de banco

-   Flyway

O Flyway controla a criação e evolução do schema do banco.

## Processamento de Excel

-   Apache POI 5.4.1

Utilizado para leitura do arquivo `.xlsx` exportado pelo Google Forms.

## Inteligência Artificial

-   Spring AI
-   Google GenAI
-   Gemini
-   Modelo configurado atualmente: `gemini-3.5-flash-lite`

A integração é utilizada especificamente para a classificação
qualitativa das respostas abertas.

## Frontend do dashboard

O dashboard é gerado pelo próprio backend.

Não é necessário:

-   React
-   Vue
-   Angular
-   Node.js
-   npm

O resultado é HTML, CSS e JavaScript.

O CSS e o JavaScript são incorporados à resposta HTML pelo backend,
permitindo que o dashboard seja visualizado diretamente pelo navegador
sem depender de arquivos externos.

------------------------------------------------------------------------

# 4. Pré-requisitos

Para executar o projeto em um ambiente próprio, é necessário
instalar/configurar:

-   Java 21
-   Maven
-   Docker
-   Docker Compose, se utilizado para subir o MySQL
-   uma chave de API do Google Gemini

Recomenda-se utilizar:

``` text
Java 21
MySQL 8.x
Maven 3.9+
```

------------------------------------------------------------------------

# 5. Clonando o projeto

Clone o repositório:

``` bash
git clone <URL_DO_REPOSITORIO>
```

Entre no diretório:

``` bash
cd <NOME_DO_PROJETO>
```

Verifique o Java:

``` bash
java -version
```

O resultado deve indicar Java 21.

Verifique o Maven:

``` bash
mvn -version
```

------------------------------------------------------------------------

# 6. Banco de dados MySQL

O projeto utiliza o schema:

``` text
PESQUISA_IA
```

Configuração utilizada no ambiente local:

``` text
Host: localhost
Port: 3306
Database: PESQUISA_IA
User: root
Password: root
```

Uma forma simples de subir o banco é utilizar Docker.

Exemplo:

``` bash
docker run --name pesquisa-ia-mysql \
  -e MYSQL_ROOT_PASSWORD=root \
  -e MYSQL_DATABASE=PESQUISA_IA \
  -p 3306:3306 \
  -d mysql:8
```

Verifique o container:

``` bash
docker ps
```

O MySQL deverá estar disponível em:

``` text
localhost:3306
```

------------------------------------------------------------------------

# 7. Flyway

O projeto utiliza Flyway para controlar o schema do banco.

A dependência utilizada é:

``` xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-flyway</artifactId>
</dependency>
```

A migration inicial está localizada em:

``` text
src/main/resources/db/migration/
```

Com o arquivo:

``` text
V1__create_research_tables.sql
```

A migration cria as principais tabelas:

``` text
research
research_response
qualitative_analysis
```

## `research`

Representa uma execução/importação de pesquisa.

Principais informações:

-   identificador;
-   nome da pesquisa;
-   nome do arquivo;
-   status;
-   data de criação;
-   data de conclusão.

## `research_response`

Armazena as respostas originais dos participantes.

Contém:

-   perfil profissional;
-   experiência;
-   área de atuação;
-   utilização de IA;
-   questões quantitativas;
-   respostas abertas.

## `qualitative_analysis`

Armazena a classificação realizada pela IA.

São armazenadas:

-   categorias;
-   justificativas;
-   confiança informada pelo modelo;
-   data da análise.

As categorias são armazenadas em campos JSON.

------------------------------------------------------------------------

# 8. Configuração do banco

O `application.yml` utiliza variáveis de ambiente, permitindo que o
projeto seja executado em diferentes ambientes sem alterar o código.

Configuração atual:

``` yaml
spring:
  datasource:
    driverClassName: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://${_DATABASE_ENDPOINT:localhost}:${MYSQL_PORT:3306}/${_DATABASE_SCHEMA:PESQUISA_IA}
    username: ${_DATABASE_USER:root}
    password: ${_DATABASE_PASSWORD:root}

  jpa:
    open-in-view: false
    show-sql: false
    hibernate:
      ddl-auto: none
      naming:
        physical-strategy: org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
    properties:
      hibernate:
        format_sql: false

  flyway:
    url: jdbc:mysql://${_DATABASE_ENDPOINT:localhost}:${MYSQL_PORT:3306}/${_DATABASE_SCHEMA:PESQUISA_IA}
    schemas: ${_DATABASE_SCHEMA:PESQUISA_IA}
    user: ${_DATABASE_USER:root}
    password: ${_DATABASE_PASSWORD:root}
    locations: classpath:/db/migration
    baseline-on-migrate: true
    placeholders:
      schema: ${_DATABASE_SCHEMA:PESQUISA_IA}
```

Por padrão, o sistema utiliza:

``` text
DATABASE_ENDPOINT = localhost
MYSQL_PORT = 3306
DATABASE_SCHEMA = PESQUISA_IA
DATABASE_USER = root
DATABASE_PASSWORD = root
```

Em outro ambiente, essas propriedades podem ser sobrescritas por
variáveis de ambiente.

------------------------------------------------------------------------

# 9. Configuração do Gemini

A análise qualitativa utiliza integração com Google Gemini através do
Spring AI.

O modelo atualmente configurado é:

``` text
gemini-3.5-flash-lite
```

A temperatura está configurada para:

``` text
0.0
```

A temperatura baixa é utilizada para tornar a classificação mais
determinística.

É necessário configurar uma chave de API do Google Gemini no ambiente de
execução.

A chave não deve ser colocada diretamente no código-fonte ou commitada
no repositório.

Exemplo de variável de ambiente:
ENV -> Adicionar a api-key do seu GEMINI:

``` bash
export GOOGLE_API_KEY="SUA_CHAVE"
```

No Windows PowerShell:

``` powershell
$env:GOOGLE_API_KEY="SUA_CHAVE"
```

O nome exato da propriedade deve ser conferido no
`application.yml`/configuração de Spring AI utilizada pelo projeto antes
da execução.

------------------------------------------------------------------------

# 10. Execução da aplicação

Com o MySQL em execução e as configurações necessárias definidas:

``` bash
mvn clean spring-boot:run
```

Ou, para gerar o JAR:

``` bash
mvn clean package
```

Depois:

``` bash
java -jar target/<arquivo-gerado>.jar
```

Por padrão, a aplicação Spring Boot estará disponível em:

``` text
http://localhost:8080
```

Ao iniciar a aplicação, o Flyway verifica e executa as migrations
pendentes.

Como o projeto utiliza:

``` yaml
spring:
  jpa:
    hibernate:
      ddl-auto: none
```

o Hibernate não cria as tabelas.

A criação e evolução do banco são responsabilidade do Flyway.

------------------------------------------------------------------------

# 11. Endpoint 1 --- iniciar uma análise

## Endpoint

``` http
POST /api/research/analyze
```

Esse é o endpoint principal para iniciar o processamento.

Ele recebe o arquivo XLSX exportado do Google Forms.

## Requisição

Content-Type:

``` text
multipart/form-data
```

Campo:

``` text
file
```

### Exemplo com cURL

``` bash
curl -X POST \
  http://localhost:8080/api/research/analyze \
  -F "file=@pesquisa.xlsx"
```

Também pode ser executado diretamente pelo navegador?

Não. Como esse endpoint precisa enviar um arquivo via
`multipart/form-data`, normalmente deve ser utilizado por:

-   frontend;
-   cURL;
-   Postman;
-   outra aplicação cliente.

Para a etapa de visualização do resultado, entretanto, não é necessário
Postman.

## Resposta

O endpoint retorna HTTP `202 Accepted`.

Exemplo:

``` json
{
  "jobId": "8c3c1c8a-xxxx-xxxx-xxxx-xxxxxxxxxxxx",
  "status": "PROCESSING"
}
```

O `jobId` identifica o processamento assíncrono.

------------------------------------------------------------------------

# 12. O que acontece depois do POST

Após receber o arquivo, o sistema executa aproximadamente o seguinte
fluxo:

``` text
1. Cria registro em research
        |
        v
2. Status = PROCESSING
        |
        v
3. Lê o arquivo XLSX
        |
        v
4. Converte cada linha em ResearchResponse
        |
        v
5. Calcula estatísticas quantitativas
        |
        v
6. Persiste as respostas
        |
        v
7. Envia as respostas abertas ao LLM
        |
        v
8. Classifica cada participante
        |
        v
9. Persiste a análise qualitativa
        |
        v
10. Calcula estatísticas qualitativas
        |
        v
11. Status = COMPLETED
```

O processamento é executado de forma assíncrona.

Isso significa que o cliente não precisa manter a requisição POST aberta
enquanto todas as respostas são processadas.

------------------------------------------------------------------------

# 13. Endpoint 2 --- consultar o processamento

## Endpoint

``` http
GET /api/research/jobs/{jobId}
```

Exemplo:

``` bash
curl http://localhost:8080/api/research/jobs/8c3c1c8a-xxxx-xxxx-xxxx-xxxxxxxxxxxx
```

Ou simplesmente abrir no navegador:

``` text
http://localhost:8080/api/research/jobs/8c3c1c8a-xxxx-xxxx-xxxx-xxxxxxxxxxxx
```

## Durante o processamento

O sistema pode retornar informações como:

``` json
{
  "jobId": "...",
  "status": "PROCESSING",
  "totalResponses": 120,
  "processedResponses": 35,
  "message": "Analisando participante 35 de 120"
}
```

Isso permite acompanhar o progresso.

## Quando finalizar

O status será:

``` text
COMPLETED
```

O registro persistido no banco também terá o status da pesquisa
atualizado para:

``` text
COMPLETED
```

Caso ocorra um erro, o status será:

``` text
ERROR
```

------------------------------------------------------------------------

# 14. Endpoint 3 --- dashboard

## Endpoint

``` http
GET /api/research/{researchId}/dashboard
```

Esse endpoint foi projetado para ser acessado diretamente pelo
navegador.

Exemplo:

``` text
http://localhost:8080/api/research/1/dashboard
```

Não é necessário:

-   Postman;
-   frontend separado;
-   React;
-   Vue;
-   Node.js;
-   instalação de plugin.

O navegador recebe diretamente:

``` text
Content-Type: text/html
```

e renderiza o dashboard.

------------------------------------------------------------------------

# 15. Como descobrir o `researchId`

O `researchId` é o identificador da pesquisa persistida na tabela:

``` text
research
```

Durante a execução do processamento, o sistema cria o registro da
pesquisa antes de iniciar a análise.

O `jobId` e o `researchId` são identificadores diferentes:

``` text
jobId
  -> identifica a execução assíncrona

researchId
  -> identifica a pesquisa persistida no banco
```

Para obter o `researchId`, ele pode ser consultado diretamente na tabela
`research` ou disponibilizado posteriormente por um endpoint de
consulta, caso esse recurso seja implementado.

------------------------------------------------------------------------

# 16. Exemplo completo de execução

Suponha que exista o arquivo:

``` text
pesquisa_ia.xlsx
```

### Passo 1 --- subir MySQL

``` bash
docker start pesquisa-ia-mysql
```

Ou criar o container caso ainda não exista:

``` bash
docker run --name pesquisa-ia-mysql \
  -e MYSQL_ROOT_PASSWORD=root \
  -e MYSQL_DATABASE=PESQUISA_IA \
  -p 3306:3306 \
  -d mysql:8
```

### Passo 2 --- configurar a API do Gemini

``` bash
export GOOGLE_GENAI_API_KEY="SUA_CHAVE"
```

### Passo 3 --- iniciar Spring Boot

``` bash
mvn spring-boot:run
```

### Passo 4 --- enviar a pesquisa

``` bash
curl -X POST \
  http://localhost:8080/api/research/analyze \
  -F "file=@pesquisa_ia.xlsx"
```

Resposta:

``` json
{
  "jobId": "abc123..."
}
```

### Passo 5 --- consultar o processamento

No navegador:

``` text
http://localhost:8080/api/research/jobs/abc123...
```

Repetir a consulta até:

``` json
{
  "status": "COMPLETED"
}
```

### Passo 6 --- abrir o dashboard

Supondo que a pesquisa tenha:

``` text
researchId = 1
```

abrir:

``` text
http://localhost:8080/api/research/1/dashboard
```

O navegador apresentará o dashboard.

------------------------------------------------------------------------

# 17. Estrutura do dashboard

O dashboard apresenta os resultados em diferentes grupos.

## Indicadores principais

-   total de participantes;
-   percepção geral;
-   média de eficiência;
-   média de confiança.

## Perfil dos participantes

-   experiência;
-   área principal de atuação.

## Utilização da IA

-   frequência de utilização;
-   ferramentas utilizadas;
-   finalidade de utilização.

## Percepção

São apresentadas as médias das dimensões avaliadas no questionário.

## Riscos e impactos

-   riscos percebidos;
-   impacto futuro;
-   atividades adequadas para utilização da IA.

## Análise qualitativa

A análise qualitativa apresenta seis dimensões:

``` text
Atividades
Benefícios
Riscos
Atividades que não devem ser delegadas
Motivos para não delegação
Perspectivas futuras
```

------------------------------------------------------------------------

# 18. Taxonomia qualitativa

A classificação qualitativa utiliza uma taxonomia previamente definida.

A IA não deve criar categorias novas.

## Atividades

``` text
AT01 Análise documental
AT02 Análise contratual
AT03 Análise de dados
AT04 Detecção de anomalias
AT05 Identificação de fraudes
AT06 Conciliação e conferência
AT07 Avaliação de riscos
AT08 Testes de auditoria
AT09 Geração de relatórios
AT10 Pesquisa e consulta
AT11 Planejamento da auditoria
AT12 Outra atividade
```

## Benefícios

``` text
BE01 Ganho de produtividade
BE02 Redução de tempo
BE03 Automação
BE04 Análise de grandes volumes
BE05 Detecção de padrões/anomalias
BE06 Apoio à qualidade
BE07 Apoio à tomada de decisão
BE08 Redução de erros operacionais
BE09 Acesso à informação
BE10 Capacitação profissional
BE11 Nenhum benefício identificado
BE12 Outro benefício
```

## Riscos

``` text
RI01 Erros/inexatidão
RI02 Alucinação
RI03 Privacidade de dados
RI04 Segurança da informação
RI05 Confidencialidade
RI06 Dependência tecnológica
RI07 Falta de transparência
RI08 Viés algorítmico
RI09 Falta de julgamento profissional
RI10 Responsabilidade profissional
RI11 Falta de conhecimento
RI12 Confiabilidade dos resultados
RI13 Questões éticas
RI14 Conformidade/regulação
RI15 Custo/infraestrutura
RI16 Resistência à mudança
RI17 Outro risco/limitação
```

## Atividades que não devem ser delegadas

``` text
ND01 Julgamento profissional
ND02 Conclusão/opinião de auditoria
ND03 Avaliação de riscos críticos
ND04 Decisões sobre materialidade
ND05 Avaliação de evidências
ND06 Comunicação com cliente
ND07 Investigação de situações complexas
ND08 Responsabilidade e assinatura profissional
ND09 Atividades envolvendo aspectos éticos
ND10 Nenhuma atividade identificada
ND11 Outra
```

## Motivos para não delegação

``` text
MN01 Julgamento profissional
MN02 Responsabilidade profissional
MN03 Complexidade/contexto
MN04 Questões éticas
MN05 Necessidade de interpretação humana
MN06 Risco de erro
MN07 Necessidade de interação humana
MN08 Confidencialidade/sensibilidade
MN09 Exigência normativa/regulatória
MN10 Outro motivo
```

## Futuro

``` text
FU01 IA como ferramenta de apoio
FU02 Colaboração humano-IA
FU03 Automação de tarefas operacionais
FU04 Transformação das competências
FU05 Maior foco analítico
FU06 Manutenção da supervisão humana
FU07 Substituição parcial de atividades
FU08 Substituição ampla do trabalho
FU09 Novos papéis profissionais
FU10 Incerteza
FU11 Outro cenário
```

------------------------------------------------------------------------

# 19. Classificação multilabel

A análise qualitativa é multilabel.

Isso significa que uma mesma resposta pode ser associada a várias
categorias.

Exemplo conceitual:

``` text
Resposta:
"A IA pode analisar grandes volumes de documentos,
reduzir o tempo da auditoria e identificar inconsistências."

Categorias:

BE01 Ganho de produtividade
BE02 Redução de tempo
BE04 Análise de grandes volumes
BE05 Detecção de padrões/anomalias
```

Por esse motivo, os percentuais das categorias não necessariamente somam
100%.

O percentual apresentado representa:

``` text
participantes classificados na categoria
----------------------------------------- × 100
total de participantes
```

e não a participação da categoria dentro de um total de classificações.

------------------------------------------------------------------------

# 20. Integração com o LLM

A integração com o Gemini é realizada por meio do Spring AI.

O processamento qualitativo é realizado individualmente por
participante.

Conceitualmente:

``` text
ResearchResponse
      |
      v
QualitativeExtractionService
      |
      v
Prompt estruturado
      |
      +--> Taxonomia permitida
      |
      +--> Respostas do participante
      |
      v
Gemini
      |
      v
ClassificationResult
      |
      +--> categories
      +--> justification
      +--> confidence
```

Cada uma das seis dimensões possui sua própria classificação:

``` text
ACTIVITIES
BENEFITS
RISKS
NON_DELEGATION_ACTIVITIES
NON_DELEGATION_REASONS
FUTURE
```

A resposta da IA é persistida para permitir posterior análise e
auditoria dos resultados.

------------------------------------------------------------------------

# 21. Importante sobre o campo `confidence`

O campo `confidence` retornado pelo LLM representa a confiança informada
pelo próprio modelo durante a classificação.

Ele **não deve ser interpretado automaticamente como uma probabilidade
estatisticamente calibrada de acerto**.

Para utilização científica, recomenda-se realizar posteriormente uma
validação humana de uma amostra das classificações.

A validação pode seguir:

``` text
Resposta original
       |
       +----> Classificação IA
       |
       +----> Classificação humana
                    |
                    v
             Comparação
                    |
                    v
       Avaliação da concordância
```

Essa etapa é importante para avaliar a confiabilidade da classificação
automática antes da utilização dos resultados como evidência no artigo.

------------------------------------------------------------------------

# 22. Análise quantitativa

O sistema calcula estatísticas descritivas.

São utilizadas distribuições de frequência para variáveis categóricas e
médias para os itens quantitativos de percepção.

O indicador de percepção geral é calculado a partir da média dos dez
itens de percepção.

Esse indicador deve ser tratado como um indicador descritivo da
pesquisa.

Não deve ser apresentado automaticamente como uma escala psicométrica
validada.

Caso o artigo utilize esse indicador como medida agregada, recomenda-se
avaliar sua consistência interna e justificar metodologicamente sua
construção.

------------------------------------------------------------------------

# 23. Segurança e configuração

Não devem ser versionados no Git:

``` text
API keys
senhas reais
credenciais de banco de produção
arquivos XLSX contendo dados pessoais
arquivos `.env` com segredos
```

Utilize variáveis de ambiente ou mecanismos seguros de configuração.

Exemplo:

``` bash
export GOOGLE_GENAI_API_KEY="..."
export _DATABASE_ENDPOINT="localhost"
export MYSQL_PORT="3306"
export _DATABASE_SCHEMA="PESQUISA_IA"
export _DATABASE_USER="root"
export _DATABASE_PASSWORD="root"
```

Para ambientes reais, recomenda-se utilizar uma credencial de banco
específica para a aplicação em vez de `root`.

------------------------------------------------------------------------

# 24. Dados sensíveis e pesquisa acadêmica

As respostas da pesquisa podem conter informações relacionadas aos
participantes.

Antes de publicar ou compartilhar o banco de dados, deve-se verificar:

-   anonimização;
-   remoção de informações identificáveis;
-   regras do projeto de pesquisa;
-   aprovação ética, quando aplicável;
-   políticas da instituição;
-   requisitos relacionados à LGPD;
-   política de armazenamento e envio de dados para serviços externos de
    IA.

Especial atenção deve ser dada ao fato de que respostas abertas podem
conter informações pessoais mesmo quando o questionário não solicita
explicitamente essas informações.

------------------------------------------------------------------------

# 25. Reprodutibilidade

Para reproduzir a análise, recomenda-se preservar:

``` text
Versão do código
Versão da migration
Arquivo XLSX utilizado
Configuração do modelo
Taxonomia
Prompt utilizado
Data da execução
Resultados gerados
```

Como modelos de IA podem sofrer alterações de comportamento ao longo do
tempo, registrar a versão/configuração utilizada é importante para
documentação científica.

------------------------------------------------------------------------

# 26. Estrutura conceitual do projeto

A estrutura principal segue uma separação entre:

``` text
Controller
    |
    v
Service
    |
    +--> Importação Excel
    |
    +--> Estatística quantitativa
    |
    +--> Classificação qualitativa
    |
    +--> Persistência
    |
    +--> Dashboard
    |
    v
Repository
    |
    v
MySQL
```

As principais responsabilidades são:

### Controllers

Recebem as requisições HTTP e expõem os endpoints.

### Services

Contêm a lógica de negócio e processamento.

### Repositories

Realizam o acesso aos dados persistidos.

### Entities

Representam as tabelas do banco.

### DTOs

Representam dados utilizados durante o processamento e exposição dos
resultados.

### Worker assíncrono

Executa o processamento da pesquisa sem bloquear a requisição inicial.

### DashboardService

Consulta os resultados e monta o HTML final.

------------------------------------------------------------------------

# 27. Dashboard autocontido

O dashboard é desenvolvido com:

``` text
HTML
CSS
JavaScript
```

Os arquivos permanecem separados no projeto para facilitar a manutenção:

``` text
templates/dashboard.html
static/css/dashboard.css
static/js/dashboard.js
```

Durante a geração da resposta, o backend incorpora o CSS e o JavaScript
ao HTML.

Assim, a resposta final do endpoint contém:

``` html
<html>
    <head>
        <style>
            /* CSS */
        </style>
    </head>

    <body>
        <!-- Dashboard -->

        <script>
            /* JavaScript */
        </script>
    </body>
</html>
```

Isso significa que o navegador não precisa buscar o CSS ou JavaScript em
outros endpoints.

O usuário pode simplesmente acessar:

``` text
http://localhost:8080/api/research/1/dashboard
```

------------------------------------------------------------------------

# 28. Troubleshooting

## Erro de conexão com MySQL

Verifique:

``` bash
docker ps
```

Confira se a porta está disponível:

``` text
3306
```

Teste as configurações:

``` text
host: localhost
port: 3306
database: PESQUISA_IA
user: root
password: root
```

------------------------------------------------------------------------

## Erro do Flyway

Verifique se:

-   o banco existe;
-   o usuário possui permissão;
-   a migration está em `src/main/resources/db/migration`;
-   não existe uma migration com versão conflitante.

------------------------------------------------------------------------

## Erro relacionado à API do Gemini

Verifique:

-   API key configurada;
-   variável de ambiente disponível para o processo Java;
-   configuração do Spring AI;
-   acesso à API;
-   modelo configurado.

------------------------------------------------------------------------

## Dashboard não encontrado

Verifique:

``` text
src/main/resources/templates/dashboard.html
```

O arquivo precisa estar no classpath da aplicação.

------------------------------------------------------------------------

## CSS não aparece

Verifique:

``` text
src/main/resources/static/css/dashboard.css
```

e se o `DashboardService` está incorporando o CSS na resposta.

------------------------------------------------------------------------

## JavaScript não executa

Verifique:

``` text
src/main/resources/static/js/dashboard.js
```

e utilize o console do navegador para verificar mensagens ou erros.

O JavaScript não deve depender de bibliotecas externas.

------------------------------------------------------------------------

# 29. Fluxo operacional resumido

Para um usuário executar uma nova pesquisa:

``` text
1. Exportar respostas do Google Forms para XLSX

2. Iniciar MySQL

3. Iniciar aplicação Spring Boot

4. Enviar XLSX:
   POST /api/research/analyze

5. Receber jobId

6. Consultar:
   GET /api/research/jobs/{jobId}

7. Aguardar status COMPLETED

8. Identificar researchId

9. Abrir:
   GET /api/research/{researchId}/dashboard

10. Avaliar os resultados
```

------------------------------------------------------------------------

# 30. Papel do sistema na pesquisa

O sistema deve ser entendido como uma infraestrutura de apoio à
pesquisa.

Ele:

-   organiza os dados;
-   preserva as respostas;
-   calcula estatísticas;
-   automatiza parte da análise qualitativa;
-   sistematiza as categorias;
-   apresenta os resultados.

Ele não substitui:

-   a avaliação metodológica;
-   a validação das classificações;
-   a interpretação contábil;
-   a discussão científica;
-   a revisão da literatura;
-   a elaboração das conclusões.

A interpretação final dos resultados deve ser realizada pelos
pesquisadores.

------------------------------------------------------------------------

# 31. Estado atual do projeto

A implementação atual contempla:

-   importação de XLSX;
-   persistência das respostas;
-   Flyway;
-   MySQL;
-   processamento assíncrono;
-   análise quantitativa;
-   classificação qualitativa por LLM;
-   taxonomia fixa;
-   classificação multilabel;
-   persistência das classificações;
-   estatísticas qualitativas;
-   dashboard HTML;
-   CSS incorporado;
-   JavaScript incorporado;
-   acesso direto ao dashboard pelo navegador.

A próxima etapa recomendada é concentrar os esforços na **validação
científica dos resultados**, especialmente na validação humana das
classificações qualitativas realizadas pelo LLM e na avaliação
metodológica dos indicadores quantitativos.

------------------------------------------------------------------------

## Licença

Este projeto é disponibilizado sob uma **Licença de Uso e Atribuição**.

É permitido utilizar, copiar, modificar e distribuir o projeto, inclusive para
fins acadêmicos, educacionais, pessoais ou comerciais, desde que seja mantida
a atribuição de autoria a:

**Brenno Pimenta da Costa**

Os termos completos da licença estão disponíveis no arquivo
[`LICENSE`](LICENSE), localizado na raiz deste repositório.

Ao utilizar, modificar ou distribuir este projeto ou trabalhos derivados
baseados substancialmente nele, a atribuição à autoria original deve ser
preservada.
:::