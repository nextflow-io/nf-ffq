package nextflow.ffq

import nextflow.Session
import spock.lang.Specification

/**
 *
 * @author Paolo Di Tommaso <paolo.ditommaso@gmail.com>
 */
class FfqClientTest extends Specification{

    def 'should perform ffq query' () {
        given:
        def session = Mock(Session) { getConfig()>>[:] }
        def client = new FfqClient(session)

        when:
        def result = client.fetchFiles('GSM4339769', [filetype:'fastq', links:'aws'])
        println result 
        then:
        result.any { it.files()==['s3://sra-pub-src-18/SRR11181954/C141_R1.fastq.1','s3://sra-pub-src-18/SRR11181954/C141_R2.fastq.1'] }

    }

}
