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

import groovy.transform.CompileStatic
/**
 * Model ffq client configuration
 *
 * @author Ben Sherman <bentshermann@gmail.com>
 */
@CompileStatic
class FfqConfig {
    final private static String DEFAULT_ENDPOINT = 'https://ffq.seqera.io/v1alpha1'
    final private String endpoint

    FfqConfig(Map opts, Map<String,String> env=System.getenv()) {
        this.endpoint = (opts.endpoint?.toString() ?: env.get('FFQ_API_ENDPOINT') ?: DEFAULT_ENDPOINT)?.stripEnd('/')
    }

    String endpoint() { this.endpoint }
}
