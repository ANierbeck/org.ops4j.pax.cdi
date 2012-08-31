/*
 * Copyright 2012 Harald Wellmann.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ops4j.pax.cdi.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.ops4j.pax.cdi.test.TestConfiguration.regressionDefaults;
import static org.ops4j.pax.cdi.test.TestConfiguration.workspaceBundle;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.options;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.cdi.sample2.service.LibraryServiceClient;
import org.ops4j.pax.cdi.spi.CdiContainer;
import org.ops4j.pax.cdi.spi.CdiContainerFactory;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;
import org.ops4j.pax.swissbox.core.BundleUtils;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
public class TransactionalTest {

    @Inject
    private CdiContainerFactory containerFactory;

    @Inject
    private LibraryServiceClient libraryService;
    
    @Inject
    private BundleContext bc;

    @Configuration
    public Option[] config() {
        return options(
            regressionDefaults(),

            workspaceBundle("pax-cdi-extender"),
            workspaceBundle("pax-cdi-extension"),
            workspaceBundle("pax-cdi-api"),
            workspaceBundle("pax-cdi-spi"),
            workspaceBundle("pax-cdi-openwebbeans"),
            workspaceBundle("pax-cdi-samples/pax-cdi-sample2-service"),
            mavenBundle("org.ops4j.pax.jpa.samples", "pax-jpa-sample1-model", "0.1.0-SNAPSHOT"),
            
            mavenBundle("org.apache.deltaspike.core", "deltaspike-core-api", "0.4-incubating-SNAPSHOT"),
            mavenBundle("org.apache.deltaspike.core", "deltaspike-core-impl", "0.4-incubating-SNAPSHOT"),
            mavenBundle("org.apache.deltaspike.modules", "deltaspike-jpa-module-api", "0.4-incubating-SNAPSHOT"),
            mavenBundle("org.apache.deltaspike.modules", "deltaspike-jpa-module-impl", "0.4-incubating-SNAPSHOT"),

            mavenBundle("org.ops4j.pax.jpa", "pax-jpa", "0.1.0-SNAPSHOT"),
            mavenBundle( "org.ops4j.base", "ops4j-base-io", "1.3.0" ),
            mavenBundle( "org.ops4j.pax.jdbc", "pax-jdbc").versionAsInProject(),
            mavenBundle( "org.apache.openjpa", "openjpa").versionAsInProject(),
            mavenBundle( "commons-lang", "commons-lang").versionAsInProject(),
            mavenBundle( "commons-collections", "commons-collections").versionAsInProject(),
            mavenBundle( "commons-pool", "commons-pool").versionAsInProject(),
            mavenBundle( "commons-dbcp", "commons-dbcp").versionAsInProject(),
            mavenBundle( "org.apache.servicemix.bundles", "org.apache.servicemix.bundles.serp", "1.13.1_4"),

            mavenBundle( "org.apache.derby", "derby").versionAsInProject(),
            
            mavenBundle( "org.osgi", "org.osgi.enterprise" ).versionAsInProject(),
            

            mavenBundle("org.ops4j.pax.swissbox", "pax-swissbox-tracker").versionAsInProject(),
            mavenBundle("org.apache.openwebbeans", "openwebbeans-impl").versionAsInProject(),
            mavenBundle("org.apache.openwebbeans", "openwebbeans-spi").versionAsInProject(),
            mavenBundle("org.apache.servicemix.bundles", "org.apache.servicemix.bundles.javassist").versionAsInProject(),
            mavenBundle("org.apache.geronimo.bundles", "scannotation").versionAsInProject(),
            mavenBundle("org.apache.xbean", "xbean-bundleutils").versionAsInProject(),
            mavenBundle("org.apache.servicemix.bundles", "org.apache.servicemix.bundles.asm").versionAsInProject(), //
            mavenBundle("org.slf4j", "jul-to-slf4j").versionAsInProject(),
            mavenBundle("org.apache.geronimo.specs", "geronimo-servlet_3.0_spec").versionAsInProject(),
            mavenBundle("org.apache.geronimo.specs", "geronimo-jpa_2.0_spec").versionAsInProject(),
            mavenBundle("org.apache.geronimo.specs", "geronimo-jta_1.1_spec").versionAsInProject(),
            mavenBundle("org.apache.geronimo.specs", "geronimo-validation_1.0_spec").versionAsInProject(),
            mavenBundle("org.apache.geronimo.specs", "geronimo-jcdi_1.0_spec").versionAsInProject(),
            mavenBundle("org.apache.geronimo.specs", "geronimo-interceptor_1.1_spec").versionAsInProject(),
            mavenBundle("org.apache.geronimo.specs", "geronimo-el_2.2_spec").versionAsInProject());

    }

    @Test
    public void checkContainers() throws InterruptedException {
        Bundle bundle = BundleUtils.getBundle(bc, "org.ops4j.pax.cdi.sample2.service");
        assertThat(bundle, is(notNullValue()));
        CdiContainer container = containerFactory.getContainer(bundle);
        container.startContext(RequestScoped.class);
        
        libraryService.createAuthor("Charles", "Dickens");
    }

}
