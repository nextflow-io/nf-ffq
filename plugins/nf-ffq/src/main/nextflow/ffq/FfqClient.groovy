package nextflow.ffq

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

    private String endpoint = 'https://ffq.seqera.io/ffq'

    FfqClient() {

    }

    FileCollector fetchFiles(String query, Map opts) {
        assert query, "Missing ffq accession query"
        def uri = "$endpoint/$query"
        if (opts.aws)
            uri += '?aws=true'
        
        def response = new JsonSlurper().parse(new URL(uri))
        def result = new FileCollector(opts)
        crawlUrl(response, result)
        return result
    }

    protected void crawlUrl(Object object, FileCollector collector) {
        if( !object )
            return
        if (object instanceof Map) {
            if( object.get('url') ) {
                // found something
                final acc = object['accession'] as String
                final url = object['url'] as String
                final type = object['filetype'] as String
                if( type == collector.opts().filetype ) {
                    collector.entry(acc).add(url)
                }
            }
            else {
                // continue traversing
                for( Map.Entry entry : (Map)object ) {
                    if (entry.value instanceof Map ) {
                        crawlUrl(entry.value, collector)
                    }
                    else if ( entry.value instanceof Collection ) {
                        crawlUrl(entry.value, collector)
                    }
                }
            }

        }
        else if( object instanceof Collection ) {
            for( def it : (Collection)object ) {
                crawlUrl(it, collector)
            }
        }
        else {
            throw new IllegalArgumentException("Unexpected value type: [${object.getClass().getSimpleName()}]: $object")
        }
    }
}

