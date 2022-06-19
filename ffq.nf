/* 
 * Bare minimal example fetching  
 */

include { ffq } from 'plugin/nf-ffq'


channel
  .ffq('SRR9990627', aws:true, filetype: 'fastq')
  .view()