CREATE SCHEMA IF NOT EXISTS `${schema}`
    DEFAULT CHARACTER SET utf8mb4
    COLLATE utf8mb4_bin;

USE `${schema}`;

CREATE TABLE IF NOT EXISTS `research` (
                                          `id` BIGINT NOT NULL AUTO_INCREMENT,
                                          `name` VARCHAR(255) NOT NULL,
    `file_name` VARCHAR(255),
    `status` VARCHAR(30) NOT NULL,
    `created_at` DATETIME NOT NULL,
    `completed_at` DATETIME NULL,

    CONSTRAINT research_PK
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB
    COMMENT = 'Research execution and analysis';

CREATE TABLE IF NOT EXISTS `research_response` (
                                                   `id` BIGINT NOT NULL AUTO_INCREMENT,
                                                   `research_id` BIGINT NOT NULL,

                                                   `response_timestamp` DATETIME NULL,
                                                   `consent` VARCHAR(255),
    `experience` VARCHAR(255),
    `main_area` VARCHAR(255),
    `audit_experience` VARCHAR(255),
    `education` VARCHAR(255),

    `ai_usage_frequency` VARCHAR(255),
    `ai_tool` VARCHAR(255),
    `ai_usage_purpose` VARCHAR(255),

    `ai_efficiency` INT,
    `ai_time_reduction` INT,
    `ai_inconsistency_detection` INT,
    `ai_data_analysis` INT,
    `ai_pattern_detection` INT,
    `ai_analysis_quality` INT,
    `ai_human_review` INT,
    `ai_confidence` INT,
    `ai_interest` INT,
    `ai_new_skills` INT,

    `suitable_audit_activity` VARCHAR(1000),
    `perceived_risk` VARCHAR(1000),
    `future_impact` VARCHAR(1000),

    `open_activities` TEXT,
    `open_benefits` TEXT,
    `open_risks` TEXT,
    `open_delegation` TEXT,
    `open_future` TEXT,

    CONSTRAINT research_response_PK
    PRIMARY KEY (`id`),

    CONSTRAINT fk_research_response_research
    FOREIGN KEY (`research_id`)
    REFERENCES `research` (`id`),

    INDEX idx_research_response_research (`research_id`)
    ) ENGINE=InnoDB
    COMMENT = 'Original research participant responses';

CREATE TABLE IF NOT EXISTS `qualitative_analysis` (
                                                      `id` BIGINT NOT NULL AUTO_INCREMENT,
                                                      `research_response_id` BIGINT NOT NULL,

                                                      `activities_categories` JSON,
                                                      `activities_justification` TEXT,
                                                      `activities_confidence` DOUBLE,

                                                      `benefits_categories` JSON,
                                                      `benefits_justification` TEXT,
                                                      `benefits_confidence` DOUBLE,

                                                      `risks_categories` JSON,
                                                      `risks_justification` TEXT,
                                                      `risks_confidence` DOUBLE,

                                                      `non_delegation_activities_categories` JSON,
                                                      `non_delegation_activities_justification` TEXT,
                                                      `non_delegation_activities_confidence` DOUBLE,

                                                      `non_delegation_reasons_categories` JSON,
                                                      `non_delegation_reasons_justification` TEXT,
                                                      `non_delegation_reasons_confidence` DOUBLE,

                                                      `future_categories` JSON,
                                                      `future_justification` TEXT,
                                                      `future_confidence` DOUBLE,

                                                      `created_at` DATETIME NOT NULL,

                                                      CONSTRAINT qualitative_analysis_PK
                                                      PRIMARY KEY (`id`),

    CONSTRAINT fk_qualitative_analysis_response
    FOREIGN KEY (`research_response_id`)
    REFERENCES `research_response` (`id`),

    CONSTRAINT uk_qualitative_analysis_response
    UNIQUE (`research_response_id`)
    ) ENGINE=InnoDB
    COMMENT = 'AI qualitative classification of research responses';
