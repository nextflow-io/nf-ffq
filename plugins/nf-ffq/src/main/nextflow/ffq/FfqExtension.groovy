package nextflow.ffq

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import groovyx.gpars.dataflow.DataflowWriteChannel
import nextflow.Channel
import nextflow.Session
import nextflow.extension.CH
import nextflow.extension.ChannelExtensionPoint
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
class FfqExtension extends ChannelExtensionPoint{

    private static final Map QUERY_PARAMS = [
            aws: Boolean,
            filetype: String
    ]

    private Session session
    private FfqClient client

    @Override
    protected void init(Session session) {
        this.session = session
        this.client = new FfqClient()
    }

    DataflowWriteChannel ffq(Map opts, String query) {
        CheckHelper.checkParams('ffq', opts, QUERY_PARAMS)
        return queryToChannel(query, opts)
    }

    DataflowWriteChannel ffq(String query) {
        return queryToChannel(query, Collections.emptyMap())
    }

    protected DataflowWriteChannel queryToChannel(String query, Map opts) {
        final result = CH.create()
        log.debug "Creating FFQ query channel"
        session.addIgniter {-> emit(query, opts, result) }
        return result
    }

    protected void emit( String query, Map opts, DataflowWriteChannel result) {
        final files = client.fetchFiles(query, opts)
        for( def it : files )
            result.bind(it.toTuple())
        result.bind(Channel.STOP)
    }
}
