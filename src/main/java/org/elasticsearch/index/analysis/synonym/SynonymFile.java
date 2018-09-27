/**
 * 
 */
package org.elasticsearch.index.analysis.synonym;

import java.io.Reader;
import org.apache.lucene.analysis.synonym.SynonymMap;

/**
 * @author bellszhu
 *
 */
public interface SynonymFile {
	
	public SynonymMap reloadSynonymMap();

	public boolean isNeedReloadSynonymMap();
	
	public Reader getReader();

}