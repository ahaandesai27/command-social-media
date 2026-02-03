package com.social.backend.config;

import org.hibernate.search.backend.lucene.analysis.LuceneAnalysisConfigurer;
import org.hibernate.search.backend.lucene.analysis.LuceneAnalysisConfigurationContext;
import org.springframework.stereotype.Component;

@Component("myLuceneConfigurer") // Add this
public class LuceneAnalysisConfig implements LuceneAnalysisConfigurer {

    @Override
    public void configure(LuceneAnalysisConfigurationContext context) {
        context.analyzer("username_autocomplete").custom()
                .tokenizer("standard")
                .tokenFilter("lowercase")
                .tokenFilter("edgeNGram")
                .param("minGramSize", "1")
                .param("maxGramSize", "20");
    }
}