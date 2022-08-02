package nextflow.ffq

import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import nextflow.ffq.model.FileCollector
/**
 * Simple http client to query ffq service
 *
 * @author Paolo Di Tommaso <paolo.ditommaso@gmail.com>
 */
@Slf4j
@CompileStatic
class FfqClient {

    private String endpoint = 'https://ffq.seqera.io/v1alpha1'

    FileCollector fetchFiles(def query, Map opts) {
        assert query, "Missing ffq accession query"
        def uri = "$endpoint/${norm(query)}"
        if (opts.aws)
            uri += '?aws=true'
        
        def response = new JsonSlurper().parse(new URL(uri))
        def result = new FileCollector(opts)
        log.debug "response: ${JsonOutput.prettyPrint( JsonOutput.toJson(response) )}"
        result.crawlUrl(response)
        return result
    }

    protected String norm(def query) {
        if( !query )
            throw new IllegalArgumentException("Ffq query parameter cannot be empty")
        if( query instanceof List ) {
            return query.join(',')
        }
        if( query instanceof CharSequence )
            return query
        throw new IllegalArgumentException("Ffq invalid query parameter type: ${query.getClass().getName()} - offending value: $query")
    }

}

