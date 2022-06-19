package nextflow.ffq.model

import groovy.transform.CompileStatic
import nextflow.util.TupleHelper

/**
 * Model ffq result item
 *
 * @author Paolo Di Tommaso <paolo.ditommaso@gmail.com>
 */
@CompileStatic
class FileMeta {

    private String accession
    private List<String> files = new ArrayList<>(20)

    FileMeta(String acc) {
        this.accession = acc
    }

    void add(String item) {
        files.add(item)
    }

    List<String> files() {
        return files
    }

    List toTuple() {
        return TupleHelper.listOf(accession, files)
    }
}
