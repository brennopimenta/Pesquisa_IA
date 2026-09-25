package com.example.pesquisa_ia.dto;

import java.util.List;

public final class ClassificationTaxonomy {

    private ClassificationTaxonomy() {
    }

    public static List<ClassificationCategory> activities() {

        return List.of(
                new ClassificationCategory("AT01", "Análise documental"),
                new ClassificationCategory("AT02", "Análise contratual"),
                new ClassificationCategory("AT03", "Análise de dados"),
                new ClassificationCategory("AT04", "Detecção de anomalias"),
                new ClassificationCategory("AT05", "Identificação de fraudes"),
                new ClassificationCategory("AT06", "Conciliação e conferência"),
                new ClassificationCategory("AT07", "Avaliação de riscos"),
                new ClassificationCategory("AT08", "Testes de auditoria"),
                new ClassificationCategory("AT09", "Geração de relatórios"),
                new ClassificationCategory("AT10", "Pesquisa e consulta"),
                new ClassificationCategory("AT11", "Planejamento da auditoria"),
                new ClassificationCategory("AT12", "Outra")
        );
    }

    public static List<ClassificationCategory> benefits() {

        return List.of(
                new ClassificationCategory("BE01", "Ganho de produtividade"),
                new ClassificationCategory("BE02", "Redução de tempo"),
                new ClassificationCategory("BE03", "Automação"),
                new ClassificationCategory("BE04", "Análise de grandes volumes"),
                new ClassificationCategory("BE05", "Detecção de padrões/anomalias"),
                new ClassificationCategory("BE06", "Apoio à qualidade"),
                new ClassificationCategory("BE07", "Apoio à tomada de decisão"),
                new ClassificationCategory("BE08", "Redução de erros operacionais"),
                new ClassificationCategory("BE09", "Acesso à informação"),
                new ClassificationCategory("BE10", "Capacitação profissional"),
                new ClassificationCategory("BE11", "Nenhum benefício identificado"),
                new ClassificationCategory("BE12", "Outro benefício")
        );
    }

    public static List<ClassificationCategory> risks() {

        return List.of(
                new ClassificationCategory("RI01", "Erros/inexatidão"),
                new ClassificationCategory("RI02", "Alucinação"),
                new ClassificationCategory("RI03", "Privacidade de dados"),
                new ClassificationCategory("RI04", "Segurança da informação"),
                new ClassificationCategory("RI05", "Confidencialidade"),
                new ClassificationCategory("RI06", "Dependência tecnológica"),
                new ClassificationCategory("RI07", "Falta de transparência"),
                new ClassificationCategory("RI08", "Viés algorítmico"),
                new ClassificationCategory("RI09", "Falta de julgamento profissional"),
                new ClassificationCategory("RI10", "Responsabilidade profissional"),
                new ClassificationCategory("RI11", "Falta de conhecimento"),
                new ClassificationCategory("RI12", "Confiabilidade dos resultados"),
                new ClassificationCategory("RI13", "Questões éticas"),
                new ClassificationCategory("RI14", "Conformidade/regulação"),
                new ClassificationCategory("RI15", "Custo/infraestrutura"),
                new ClassificationCategory("RI16", "Resistência à mudança"),
                new ClassificationCategory("RI17", "Outro risco/limitação")
        );
    }

    public static List<ClassificationCategory> nonDelegationActivities() {

        return List.of(
                new ClassificationCategory("ND01", "Julgamento profissional"),
                new ClassificationCategory("ND02", "Conclusão/opinião de auditoria"),
                new ClassificationCategory("ND03", "Avaliação de riscos críticos"),
                new ClassificationCategory("ND04", "Decisões sobre materialidade"),
                new ClassificationCategory("ND05", "Avaliação de evidências"),
                new ClassificationCategory("ND06", "Comunicação com cliente"),
                new ClassificationCategory("ND07", "Investigação de situações complexas"),
                new ClassificationCategory("ND08", "Responsabilidade e assinatura profissional"),
                new ClassificationCategory("ND09", "Atividades envolvendo aspectos éticos"),
                new ClassificationCategory("ND10", "Nenhuma atividade identificada"),
                new ClassificationCategory("ND11", "Outra")
        );
    }

    public static List<ClassificationCategory> nonDelegationReasons() {

        return List.of(
                new ClassificationCategory("MN01", "Julgamento profissional"),
                new ClassificationCategory("MN02", "Responsabilidade profissional"),
                new ClassificationCategory("MN03", "Complexidade/contexto"),
                new ClassificationCategory("MN04", "Questões éticas"),
                new ClassificationCategory("MN05", "Necessidade de interpretação humana"),
                new ClassificationCategory("MN06", "Risco de erro"),
                new ClassificationCategory("MN07", "Necessidade de interação humana"),
                new ClassificationCategory("MN08", "Confidencialidade/sensibilidade"),
                new ClassificationCategory("MN09", "Exigência normativa/regulatória"),
                new ClassificationCategory("MN10", "Outro motivo")
        );
    }

    public static List<ClassificationCategory> future() {

        return List.of(
                new ClassificationCategory("FU01", "IA como ferramenta de apoio"),
                new ClassificationCategory("FU02", "Colaboração humano-IA"),
                new ClassificationCategory("FU03", "Automação de tarefas operacionais"),
                new ClassificationCategory("FU04", "Transformação das competências"),
                new ClassificationCategory("FU05", "Maior foco analítico"),
                new ClassificationCategory("FU06", "Manutenção da supervisão humana"),
                new ClassificationCategory("FU07", "Substituição parcial de atividades"),
                new ClassificationCategory("FU08", "Substituição ampla do trabalho"),
                new ClassificationCategory("FU09", "Novos papéis profissionais"),
                new ClassificationCategory("FU10", "Incerteza"),
                new ClassificationCategory("FU11", "Outro cenário")
        );
    }
}
