package nextflow.ffq.model

/**
 * Collects ffq result and aggregates by accession number
 * 
 * @author Paolo Di Tommaso <paolo.ditommaso@gmail.com>
 */
class FileCollector implements Iterable<FileMeta> {

    private Map opts

    private Map<String,FileMeta> entries = new HashMap<>()

    FileCollector(Map opts) {
        this.opts = new HashMap(opts)
        // default to 'fastq' file type
        if( !this.opts.filetype )
            this.opts.filetype = 'fastq'
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
}
