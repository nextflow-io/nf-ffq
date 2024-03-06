/* 
 * Bare minimal example fetching  
 */
params.accession = 'SRR11181954,SRR11181956'

include { example } from 'plugin/nf-ffq'

channel
  .ffq(params.accession, filetype:'fastq', links: 'aws')
  .view()
