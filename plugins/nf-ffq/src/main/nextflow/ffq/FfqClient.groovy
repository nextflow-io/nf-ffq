/*
 * Copyright 2021, Seqera Labs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package nextflow.ffq

import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import nextflow.Session
import nextflow.ffq.model.FileCollector
/**
 * Simple http client to query ffq service
 *
 * @author Paolo Di Tommaso <paolo.ditommaso@gmail.com>
 */
@Slf4j
@CompileStatic
class FfqClient {

    private FfqConfig config

    FfqClient(Session session) {
        this.config = new FfqConfig(session.config.ffq as Map ?: [:])
    }

    FileCollector fetchFiles(def query, Map opts) {
        assert query, "Missing ffq accession query"
        def uri = "${config.endpoint()}/${norm(query)}"
        if (opts.aws)
            uri += '?aws=true'
        
        def response = new JsonSlurper().parse(new URL(uri))
        def result = new FileCollector(opts)
        log.debug "response: ${JsonOutput.prettyPrint( JsonOutput.toJson(response) )}"
        result.crawlUrl(response)
        return result
    }

    protected String norm(def query) {
        if( !query )
            throw new IllegalArgumentException("Ffq query parameter cannot be empty")
        if( query instanceof List ) {
            return query.join(',')
        }
        if( query instanceof CharSequence )
            return query
        throw new IllegalArgumentException("Ffq invalid query parameter type: ${query.getClass().getName()} - offending value: $query")
    }

}
