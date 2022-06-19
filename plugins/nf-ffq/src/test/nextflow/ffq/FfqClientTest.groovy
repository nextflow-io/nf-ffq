package nextflow.ffq

import spock.lang.Specification

/**
 *
 * @author Paolo Di Tommaso <paolo.ditommaso@gmail.com>
 */
class FfqClientTest extends Specification{

    def 'should perform ffq query' () {
        given:
        def client = new FfqClient()

        when:
        def result = client.fetchFiles('SRR9990627', [:])
        println result 
        then:
        result.contains('ftp://ftp.sra.ebi.ac.uk/vol1/fastq/SRR999/007/SRR9990627/SRR9990627_1.fastq.gz')


        when:
        result = client.fetchFiles('SRR9990627', [aws:true])
        println result
        then:
        !result.contains('ftp://ftp.sra.ebi.ac.uk/vol1/fastq/SRR999/007/SRR9990627/SRR9990627_1.fastq.gz')
        result.size()>=2 
    }

}
