# nf-ffq

Nextflow plugin to fetch fastq samples using [ffq](https://github.com/nextflow-io/nf-ffq).

## Get started 

The plugin adds a new channel factory called `channel.ffq()` which can be used to query fastq files.

A simple usage is shown below:


```
include { ffq } from 'plugin/nf-ffq'

channel
  .ffq('SRR9990627', aws:true, filetype: 'fastq')
  .view()
```

It returns: 

```
[SRR9990627, [s3://sra-pub-src-16/SRR9990627/macula_donor_1_S100_L004_R1_001.fastq.gz.1, s3://sra-pub-src-17/SRR9990627/macula_donor_1_S100_L004_R2_001.fastq.gz.1]]
```


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


Run the following command in the project root directory (ie. where the file `settings.gradle` is located):

    ./gradlew check

## Run and debug plugin in the development environment

To run and test the plugin in the development environment, configure a local Nextflow build 
using the following steps:

1. Clone the Nextflow repository in your computer into a sibling directory:

    ```
    git clone --depth 1 https://github.com/nextflow-io/nextflow ../nextflow
    ```
  
2. Instruct the plugin build setting to use the local Nextflow code, adding the following 
  line in the file `settings.gradle`: 
   
    ```
    echo "includeBuild('../nextflow')" >> settings.gradle
    ```
  
  (make sure to not add it more than once..)

3. Compile the plugin along the Nextflow code, with this command:

    ```
    ./gradlew compileGroovy
    ```

4. Run Nextflow with plugins using the `./launch.sh` script as a drop-in replacement for the `nextflow` command and 
  adding the option `-plugins nf-ffq` to load the built plugin:
   
    ```
    ./launch.sh run ffq.nf -plugins nf-ffq
    ```

## Package, upload and publish

The project should hosted in a GitHub repository whose name should match the name of the plugin,
that is the name of the directory in the `plugins` folder e.g. `nf-ffq` in this project.

Following these step to package, upload and publish the plugin:

1. Create a file named `gradle.properties` in the project root containing the following attributes
   (this file should not be committed in the project repository):

  * `github_organization`: the GitHub organisation the plugin project is hosted
  * `github_username` The GitHub username granting access to the plugin project.
  * `github_access_token`:  The GitHub access token required to upload and commit changes in the plugin repository.
  * `github_commit_email`:  The email address associated with your GitHub account.

2. The following command, package and upload the plugin in the GitHub project releases page:

    ```
    ./gradlew :plugins:nf-ffq:upload
    ```

3. Create a pull request against the [nextflow-io/plugins](https://github.com/nextflow-io/plugins/blob/main/plugins.json) 
  project to make the plugin public accessible to Nextflow app. 
