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
        def result = client.fetchFiles('GSM4339769', [:])
        println result 
        then:
        result.any { it.files().contains('ftp://ftp.sra.ebi.ac.uk/vol1/fastq/SRR111/054/SRR11181954/SRR11181954_1.fastq.gz') }

//
//        when:
//        result = client.fetchFiles('SRR9990627', [aws:true])
//        println result
//        then:
//        !result.any { it.files().contains('ftp://ftp.sra.ebi.ac.uk/vol1/fastq/SRR999/007/SRR9990627/SRR9990627_1.fastq.gz') }
//        result.size()>=2
    }

}
