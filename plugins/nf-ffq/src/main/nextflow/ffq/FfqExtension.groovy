package nextflow.ffq

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import groovyx.gpars.dataflow.DataflowWriteChannel
import nextflow.Channel
import nextflow.Session
import nextflow.extension.CH
import nextflow.plugin.extension.Factory
import nextflow.plugin.extension.PluginExtensionPoint
import nextflow.util.CheckHelper
/**
 * Implement channel factory to perform ffq queries
 * via ffq-api
 *
 * See
 *  https://github.com/pachterlab/ffq
 *  https://github.com/seqeralabs/ffq-api
 *  
 * @author Paolo Di Tommaso <paolo.ditommaso@gmail.com>
 */
@Slf4j
@CompileStatic
class FfqExtension extends PluginExtensionPoint {

    private static final Map QUERY_PARAMS = [
            links: String,
            filetype: String
    ]

    private Session session
    private FfqClient client

    @Override
    protected void init(Session session) {
        this.session = session
        this.client = new FfqClient(session)
    }

    @Factory
    DataflowWriteChannel ffq(Map opts, def query) {
        CheckHelper.checkParams('ffq', opts, QUERY_PARAMS)
        return queryToChannel(query, opts)
    }

    @Factory
    DataflowWriteChannel ffq(String query) {
        return queryToChannel(query, Collections.emptyMap())
    }

    protected DataflowWriteChannel queryToChannel(def query, Map opts) {
        final result = CH.create()
        log.trace "Creating FFQ query channel"
        session.addIgniter(it-> emit(query, opts, result))
        return result
    }

    protected void emit( def query, Map opts, DataflowWriteChannel result) {
        final files = client.fetchFiles(query, opts)
        for( def it : files )
            result.bind(it.toTuple())
        result.bind(Channel.STOP)
    }
}
