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
package org.ops4j.pax.cdi.jetty.openwebbeans.impl;

import javax.servlet.ServletContainerInitializer;

import org.apache.webbeans.config.WebBeansFinder;
import org.ops4j.pax.cdi.servlet.AbstractWebCdiContainerListener;
import org.ops4j.pax.cdi.servlet.CdiServletContainerInitializer;
import org.ops4j.pax.cdi.spi.CdiContainer;
import org.osgi.framework.BundleContext;

public class OpenWebBeansWebAdapter extends AbstractWebCdiContainerListener {

    public void activate(BundleContext context) {
        WebBeansFinder.setSingletonService(new BundleSingletonService());
    }

    public void deactivate(BundleContext context) {
        // TODO the following causes an exception:
        // org.apache.webbeans.exception.WebBeansConfigurationException: 
        // Already using another custom SingletonService!

        // WebBeansFinder.setSingletonService(null);
    }

    
    @Override
    protected ServletContainerInitializer getServletContainerInitializer(CdiContainer cdiContainer) {
        ServletContainerInitializer initializer = new CdiServletContainerInitializer(
            cdiContainer, new OpenWebBeansListener());
        return initializer;
    }
}
