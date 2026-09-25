package com.example.pesquisa_ia.service;

import com.example.pesquisa_ia.dto.*;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.google.genai.GoogleGenAiChatOptions;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AiClassificationService {

    private final ChatClient chatClient;

    public AiClassificationService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public FullClassificationResult classifyFull(
            ResearchResponse response) {

        String systemPrompt = """
            Você é um classificador de respostas de uma pesquisa
            acadêmica sobre Inteligência Artificial na atividade de auditoria.

            Sua tarefa é classificar as respostas abertas do participante
            utilizando SOMENTE as categorias fornecidas.

            A classificação deve ser realizada separadamente para cada dimensão.

            Regras obrigatórias:

            1. Não crie novas categorias.
            2. Utilize somente os códigos fornecidos.
            3. Uma resposta pode receber múltiplas categorias.
            4. Se nenhuma categoria for aplicável, retorne uma lista vazia.
            5. Não altere os códigos das categorias.
            6. Para cada dimensão, forneça uma justificativa objetiva.
            7. A classificação deve considerar somente o conteúdo da resposta
               correspondente àquela dimensão.
            8. Não utilize uma resposta de uma dimensão para classificar outra.
            9. Preserve exatamente os códigos fornecidos.
            10. Retorne obrigatoriamente todas as seis dimensões.
            
            Quando a resposta de uma dimensão estiver marcada como
            "(não informado)":
            - categories deve ser uma lista vazia;
            - classified deve ser false;
            - não invente conteúdo;
            - confidence deve ser 0.0.
            """;

        String userPrompt = """
            RESPONSES DO PARTICIPANTE

            Atividades que poderiam ser realizadas pela IA:
            %s

            Benefícios percebidos:
            %s

            Riscos percebidos:
            %s

            Atividades que não deveriam ser delegadas à IA:
            %s

            Impacto futuro da IA na profissão:
            %s


            ==================================================
            CATEGORIAS PERMITIDAS
            ==================================================

            ATIVIDADES:
            %s

            BENEFÍCIOS:
            %s

            RISCOS:
            %s

            ATIVIDADES NÃO DELEGÁVEIS:
            %s

            MOTIVOS PARA NÃO DELEGAR:
            %s

            FUTURO:
            %s
            """.formatted(
                valueOrEmpty(response.getOpenActivities()),
                valueOrEmpty(response.getOpenBenefits()),
                valueOrEmpty(response.getOpenRisks()),
                valueOrEmpty(response.getOpenDelegation()),
                valueOrEmpty(response.getOpenFuture()),

                buildCategories(ClassificationTaxonomy.activities()),
                buildCategories(ClassificationTaxonomy.benefits()),
                buildCategories(ClassificationTaxonomy.risks()),
                buildCategories(ClassificationTaxonomy.nonDelegationActivities()),
                buildCategories(ClassificationTaxonomy.nonDelegationReasons()),
                buildCategories(ClassificationTaxonomy.future())
        );

//        return chatClient.prompt()
//                .system(systemPrompt)
//                .user(userPrompt)
//                .options(
//                        GoogleGenAiChatOptions.builder()
//                                .model("gemini-2.5-flash")
//                                .temperature(0.0)
//                                .build()
//                )
//                .call()
//                .entity(FullClassificationResult.class);

        return chatClient.prompt()
                .system(systemPrompt)
                .user(userPrompt)
                .call()
                .entity(FullClassificationResult.class);
    }

    public ClassificationResult classify(String response, ClassificationDimension dimension) {

        List<ClassificationCategory> categories =
                getCategories(dimension);

        String categoriesText = buildCategories(categories);

        String systemPrompt = """
                Você é um classificador de respostas de uma pesquisa
                acadêmica sobre Inteligência Artificial na atividade de auditoria.

                Sua tarefa é classificar a resposta utilizando SOMENTE
                as categorias fornecidas.

                Regras obrigatórias:

                1. Não crie novas categorias.
                2. Utilize somente os códigos fornecidos.
                3. Uma resposta pode receber múltiplas categorias.
                4. Se nenhuma categoria for aplicável, retorne uma lista vazia.
                5. Não altere os códigos das categorias.
                6. A justificativa deve explicar objetivamente a classificação.
                """;

        String userPrompt = """
                Dimensão da classificação:
                %s

                Resposta do participante:
                %s

                Categorias permitidas:

                %s
                """.formatted(
                dimension,
                response,
                categoriesText
        );

//        return chatClient.prompt()
//                .system(systemPrompt)
//                .user(userPrompt)
//                .call()
//                .entity(
//                        ClassificationResult.class,
//                        spec -> spec.validateSchema()
//                );

        return chatClient.prompt()
                .system(systemPrompt)
                .user(userPrompt)
                .call()
                .entity(ClassificationResult.class);
    }

    private List<ClassificationCategory> getCategories(
            ClassificationDimension dimension) {

        return switch (dimension) {

            case ACTIVITIES ->
                    ClassificationTaxonomy.activities();

            case BENEFITS ->
                    ClassificationTaxonomy.benefits();

            case RISKS ->
                    ClassificationTaxonomy.risks();

            case NON_DELEGATION_ACTIVITIES ->
                    ClassificationTaxonomy.nonDelegationActivities();

            case NON_DELEGATION_REASONS ->
                    ClassificationTaxonomy.nonDelegationReasons();

            case FUTURE ->
                    ClassificationTaxonomy.future();
        };
    }

    private String buildCategories(
            List<ClassificationCategory> categories) {

        return categories.stream()
                .map(category ->
                        category.getCode() + " - " + category.getDescription())
                .collect(Collectors.joining("\n"));
    }

    private String valueOrEmpty(String value) {
        return value == null || value.isBlank()
                ? "(não informado)"
                : value;
    }

}
