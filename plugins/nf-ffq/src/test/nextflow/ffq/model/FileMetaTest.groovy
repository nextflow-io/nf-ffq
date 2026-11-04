package nextflow.ffq.model

import spock.lang.Specification

/**
 *
 * @author Paolo Di Tommaso <paolo.ditommaso@gmail.com>
 */
class FileMetaTest extends Specification {

    def 'should validate equals and hashcode' () {
        given:
        def m1 = new FileMeta(accession: 'x', files:['one.txt','two.txt'])
        def m2 = new FileMeta(accession: 'x', files:['one.txt','two.txt'])
        def m3 = new FileMeta(accession: 'y', files:['one.txt','due.txt'])

        expect:
        m1  == m2
        m1 != m3
        and:
        m1.hashCode() == m2.hashCode()
        m1.hashCode() != m3.hashCode()
    }

}
