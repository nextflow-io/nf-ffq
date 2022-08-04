# nf-ffq

Nextflow plugin to fetch fastq samples using [ffq](https://github.com/nextflow-io/nf-ffq).

## Usage

To enable the plugin, add the following snippet to your `nextflow.config`:
```
plugins {
    id 'nf-ffq'
}
```

The plugin adds a new channel factory called `channel.ffq()` which can be used to query fastq files. Refer to `ffq.nf` for an example.

## Configuration

The plugin adds a new `ffq` config scope which supports the following options:

| Config option 	| Description 	            |
|---	            |---	                      |
| `ffq.endpoint`  | URL of the ffq-api endpoint

For example:
```
ffq {
  endpoint = 'https://ffq.staging-tower.xyz/'
}
```

## Development

Refer to the [nf-hello](https://github.com/nextflow-io/nf-hello) README for instructions on how to build, test, and publish Nextflow plugins.
