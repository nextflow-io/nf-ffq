package nextflow.ffq.model

import groovy.json.JsonSlurper
import spock.lang.Specification

/**
 *
 * @author Paolo Di Tommaso <paolo.ditommaso@gmail.com>
 */
class FileCollectorTest extends Specification {

    def 'should collect files' () {
         given:
         def JSON = '''
{
    "results": [
        {
            "accession": "SRR11181954",
            "filename": "C141_R1.fastq.1",
            "filetype": "fastq",
            "filesize": null,
            "filenumber": 1,
            "md5": null,
            "urltype": "aws",
            "url": "s3://sra-pub-src-18/SRR11181954/C141_R1.fastq.1"
        },
        {
            "accession": "SRR11181954",
            "filename": "C141_R2.fastq.1",
            "filetype": "fastq",
            "filesize": null,
            "filenumber": 2,
            "md5": null,
            "urltype": "aws",
            "url": "s3://sra-pub-src-18/SRR11181954/C141_R2.fastq.1"
        },
        {
            "accession": "SRR11181954",
            "filename": "SRR11181954",
            "filetype": "sra",
            "filesize": null,
            "filenumber": 1,
            "md5": null,
            "urltype": "aws",
            "url": "https://sra-pub-run-odp.s3.amazonaws.com/sra/SRR11181954/SRR11181954"
        },
        {
            "accession": "SRR11181954",
            "filename": "SRR11181954.lite.1",
            "filetype": "sra",
            "filesize": null,
            "filenumber": 1,
            "md5": null,
            "urltype": "aws",
            "url": "s3://sra-pub-zq-5/SRR11181954/SRR11181954.lite.1"
        },
        {
            "accession": "SRR11181956",
            "filename": "C143_R1.fastq.1",
            "filetype": "fastq",
            "filesize": null,
            "filenumber": 1,
            "md5": null,
            "urltype": "aws",
            "url": "s3://sra-pub-src-8/SRR11181956/C143_R1.fastq.1"
        },
        {
            "accession": "SRR11181956",
            "filename": "C143_R2.fastq.1",
            "filetype": "fastq",
            "filesize": null,
            "filenumber": 2,
            "md5": null,
            "urltype": "aws",
            "url": "s3://sra-pub-src-8/SRR11181956/C143_R2.fastq.1"
        },
        {
            "accession": "SRR11181956",
            "filename": "SRR11181956",
            "filetype": "sra",
            "filesize": null,
            "filenumber": 1,
            "md5": null,
            "urltype": "aws",
            "url": "https://sra-pub-run-odp.s3.amazonaws.com/sra/SRR11181956/SRR11181956"
        },
        {
            "accession": "SRR11181956",
            "filename": "SRR11181956.lite.1",
            "filetype": "sra",
            "filesize": null,
            "filenumber": 1,
            "md5": null,
            "urltype": "aws",
            "url": "s3://sra-pub-zq-5/SRR11181956/SRR11181956.lite.1"
        }
    ],
    "meta": {
        "ffq_api_version": "0.3.3",
        "ffq_version": "0.3.1",
        "query": {
            "IDs": "SRR11181954,SRR11181956",
            "search_type": null,
            "level": null,
            "links": "aws"
        },
        "request": {
            "start": "2024-03-06T17:35:23.088309",
            "finish": "2024-03-06T17:35:24.218495",
            "duration_seconds": 1.130186
        }
    }
}
'''

        when:
        def collector1 = new FileCollector([:])
        collector1.crawlUrl(new JsonSlurper().parseText(JSON))

        then:
        collector1.toList() == [
                new FileMeta(accession:'SRR11181954', files:['s3://sra-pub-src-18/SRR11181954/C141_R1.fastq.1', 's3://sra-pub-src-18/SRR11181954/C141_R2.fastq.1', 'https://sra-pub-run-odp.s3.amazonaws.com/sra/SRR11181954/SRR11181954', 's3://sra-pub-zq-5/SRR11181954/SRR11181954.lite.1']),
                new FileMeta(accession:'SRR11181956', files:['s3://sra-pub-src-8/SRR11181956/C143_R1.fastq.1', 's3://sra-pub-src-8/SRR11181956/C143_R2.fastq.1', 'https://sra-pub-run-odp.s3.amazonaws.com/sra/SRR11181956/SRR11181956', 's3://sra-pub-zq-5/SRR11181956/SRR11181956.lite.1'])
        ]

        when:
        def collector2 = new FileCollector([filetype:'fastq'])
        collector2.crawlUrl(new JsonSlurper().parseText(JSON))

        then:
        collector2.toList() == [
                new FileMeta(accession:'SRR11181954', files:['s3://sra-pub-src-18/SRR11181954/C141_R1.fastq.1', 's3://sra-pub-src-18/SRR11181954/C141_R2.fastq.1']),
                new FileMeta(accession:'SRR11181956', files:['s3://sra-pub-src-8/SRR11181956/C143_R1.fastq.1', 's3://sra-pub-src-8/SRR11181956/C143_R2.fastq.1'])
        ]

        when:
        def collector3 = new FileCollector([filetype: 'sra'])
        collector3.crawlUrl(new JsonSlurper().parseText(JSON))

        then:
        collector3.toList() == [
                new FileMeta(accession:'SRR11181954', files:['https://sra-pub-run-odp.s3.amazonaws.com/sra/SRR11181954/SRR11181954', 's3://sra-pub-zq-5/SRR11181954/SRR11181954.lite.1']),
                new FileMeta(accession:'SRR11181956', files:['https://sra-pub-run-odp.s3.amazonaws.com/sra/SRR11181956/SRR11181956', 's3://sra-pub-zq-5/SRR11181956/SRR11181956.lite.1'])
        ]
    }

}
