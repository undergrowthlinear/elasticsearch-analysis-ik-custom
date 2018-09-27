package org.elasticsearch.plugin.analysis.ik;

import static java.util.Collections.singletonList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.apache.lucene.analysis.Analyzer;
import org.elasticsearch.client.Client;
import org.elasticsearch.cluster.service.ClusterService;
import org.elasticsearch.common.component.LifecycleComponent;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.NamedXContentRegistry;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.IndexSettings;
import org.elasticsearch.index.analysis.AnalysisRegistry;
import org.elasticsearch.index.analysis.AnalyzerProvider;
import org.elasticsearch.index.analysis.IkAnalyzerProvider;
import org.elasticsearch.index.analysis.IkTokenizerFactory;
import org.elasticsearch.index.analysis.TokenFilterFactory;
import org.elasticsearch.index.analysis.TokenizerFactory;
import org.elasticsearch.index.analysis.synonym.DynamicSynonymAnalysisService;
import org.elasticsearch.index.analysis.synonym.DynamicSynonymTokenFilterFactory;
import org.elasticsearch.indices.analysis.AnalysisModule;
import org.elasticsearch.plugins.AnalysisPlugin;
import org.elasticsearch.plugins.Plugin;
import org.elasticsearch.script.ScriptService;
import org.elasticsearch.search.SearchRequestParsers;
import org.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.watcher.ResourceWatcherService;


public class AnalysisIkPlugin extends Plugin implements AnalysisPlugin {

    private PluginComponent pluginComponent = new PluginComponent();

    public static String PLUGIN_NAME = "analysis-ik";

    @Override
    public Collection<Object> createComponents(
        Client client,
        ClusterService clusterService,
        ThreadPool threadPool,
        ResourceWatcherService resourceWatcherService,
        ScriptService scriptService,
        SearchRequestParsers searchRequestParsers,
        NamedXContentRegistry xContentRegistry
    ) {
        Collection<Object> components = new ArrayList<>();
        components.add(pluginComponent);
        return components;
    }

    @Override
    public Collection<Class<? extends LifecycleComponent>> getGuiceServiceClasses() {
        return singletonList(DynamicSynonymAnalysisService.class);
    }

    @Override
    public Map<String, AnalysisModule.AnalysisProvider<TokenFilterFactory>> getTokenFilters() {
        Map<String, AnalysisModule.AnalysisProvider<org.elasticsearch.index.analysis.TokenFilterFactory>> extra = new HashMap<>();

        extra.put("dynamic_synonym", new AnalysisModule.AnalysisProvider<TokenFilterFactory>() {

            @Override
            public TokenFilterFactory get(IndexSettings indexSettings, Environment environment, String name, Settings settings)
                throws IOException {
                return new DynamicSynonymTokenFilterFactory(indexSettings, environment, name, settings, pluginComponent.getAnalysisRegistry());
            }

            @Override
            public boolean requiresAnalysisSettings() {
                return true;
            }
        });
        return extra;
    }

    @Override
    public Map<String, AnalysisModule.AnalysisProvider<TokenizerFactory>> getTokenizers() {
        Map<String, AnalysisModule.AnalysisProvider<TokenizerFactory>> extra = new HashMap<>();

        extra.put("ik_smart", IkTokenizerFactory::getIkSmartTokenizerFactory);
        extra.put("ik_max_word", IkTokenizerFactory::getIkTokenizerFactory);

        return extra;
    }

    @Override
    public Map<String, AnalysisModule.AnalysisProvider<AnalyzerProvider<? extends Analyzer>>> getAnalyzers() {
        Map<String, AnalysisModule.AnalysisProvider<AnalyzerProvider<? extends Analyzer>>> extra = new HashMap<>();

        extra.put("ik_smart", IkAnalyzerProvider::getIkSmartAnalyzerProvider);
        extra.put("ik_max_word", IkAnalyzerProvider::getIkAnalyzerProvider);

        return extra;
    }

    public static class PluginComponent {

        private AnalysisRegistry analysisRegistry;

        public AnalysisRegistry getAnalysisRegistry() {
            return analysisRegistry;
        }

        public void setAnalysisRegistry(AnalysisRegistry analysisRegistry) {
            this.analysisRegistry = analysisRegistry;
        }

    }

}
