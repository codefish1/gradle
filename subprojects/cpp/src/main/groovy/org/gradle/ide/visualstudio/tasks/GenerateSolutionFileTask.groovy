/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.gradle.ide.visualstudio.tasks
import org.gradle.api.Incubating
import org.gradle.ide.visualstudio.internal.VisualStudioSolution
import org.gradle.ide.visualstudio.internal.VisualStudioSolutionFile
import org.gradle.plugins.ide.api.GeneratorTask
import org.gradle.plugins.ide.internal.generator.generator.PersistableConfigurationObject
import org.gradle.plugins.ide.internal.generator.generator.PersistableConfigurationObjectGenerator

@Incubating
class GenerateSolutionFileTask extends GeneratorTask<PersistableConfigurationObject> {
    VisualStudioSolution solution

    GenerateSolutionFileTask() {
        generator = new ConfigurationObjectGenerator();
    }

    void setVisualStudioSolution(VisualStudioSolution solution) {
        this.solution = solution
        setOutputFile(solution.getSolutionFile())

        dependsOn {
            solution.projectConfigurations*.project
        }
    }

    private class ConfigurationObjectGenerator extends PersistableConfigurationObjectGenerator<PersistableConfigurationObject> {
        public PersistableConfigurationObject create() {
            return new VisualStudioSolutionFile()
        }

        public void configure(PersistableConfigurationObject object) {
            VisualStudioSolutionFile solutionFile = object as VisualStudioSolutionFile;
            VisualStudioSolution solution = GenerateSolutionFileTask.this.solution
            solution.projectConfigurations.each {
                solutionFile.addProjectConfiguration(it)
            }
        }
    }
}