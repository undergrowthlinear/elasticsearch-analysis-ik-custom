package org.elasticsearch.index.analysis.synonym;

import org.elasticsearch.common.component.AbstractLifecycleComponent;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.analysis.AnalysisRegistry;
import org.elasticsearch.plugin.analysis.ik.AnalysisIkPlugin;

public class DynamicSynonymAnalysisService extends AbstractLifecycleComponent {

    @Inject
    public DynamicSynonymAnalysisService(final Settings settings, final AnalysisRegistry analysisRegistry,
        final AnalysisIkPlugin.PluginComponent pluginComponent) {
        super(settings);
        pluginComponent.setAnalysisRegistry(analysisRegistry);
    }

    @Override
    protected void doStart() {
        // nothing
    }

    @Override
    protected void doStop() {
        // nothing
    }

    @Override
    protected void doClose() {
        // nothing
    }

}
