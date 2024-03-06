package nextflow.ffq.model

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

/**
 * Collects ffq result and aggregates by accession number
 * 
 * @author Paolo Di Tommaso <paolo.ditommaso@gmail.com>
 */
@Slf4j
@CompileStatic
class FileCollector implements Iterable<FileMeta> {

    private Map opts

    private Map<String,FileMeta> entries = new HashMap<>()

    FileCollector(Map opts) {
        this.opts = new HashMap(opts)
    }

    Map opts() { return opts }

    FileMeta entry(String accession) {
        assert accession, "Accession number cannot be empty"
        def result = entries.get(accession)
        if( result == null ) {
            result = new FileMeta(accession)
            entries.put(accession, result)
        }
        return result
    }

    @Override
    Iterator<FileMeta> iterator() {
        final values = entries.values().iterator()

        return new Iterator<FileMeta>() {
            @Override
            boolean hasNext() {
                return values.hasNext()
            }

            @Override
            FileMeta next() {
                return values.next()
            }
        }
    }

    void crawlUrl(Object object) {
        crawlUrl0(object, false)
    }

    protected void crawlUrl0(Object object, boolean withinResult) {
        if( !object )
            return
        if (object instanceof Map) {
            if( object.get('url') && withinResult ) {
                // found something
                final acc = object['accession'] as String
                final url = object['url'] as String
                final type = object['filetype'] as String
                if( !opts().filetype || type==opts().filetype ) {
                    entry(acc).add(url)
                }
            }
            else {
                // continue traversing
                for( Map.Entry entry : (Map)object ) {
                    log.trace "Traversing key: $entry.key"
                    if( entry.key=='results' )
                        withinResult = true
                    if (entry.value instanceof Map ) {
                        crawlUrl0(entry.value, withinResult)
                    }
                    else if ( entry.value instanceof Collection ) {
                        crawlUrl0(entry.value, withinResult)
                    }
                }
            }

        }
        else if( object instanceof Collection ) {
            for( def it : (Collection)object ) {
                crawlUrl0(it, withinResult)
            }
        }
        else {
            throw new IllegalArgumentException("Unexpected value type: [${object.getClass().getSimpleName()}]: $object")
        }
    }
}
