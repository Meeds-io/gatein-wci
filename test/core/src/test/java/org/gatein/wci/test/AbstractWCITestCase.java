/*
 * Copyright (C) 2010 eXo Platform SAS.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.gatein.wci.test;

import org.codehaus.plexus.util.StringUtils;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.Asset;
import org.jboss.shrinkwrap.api.asset.ByteArrayAsset;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.runner.RunWith;

/** @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a> */
@RunWith(Arquillian.class)
public abstract class AbstractWCITestCase
{

   protected static WebArchive wciWildfly10Deployment(String name)
   {
      WebArchive war = name != null ? ShrinkWrap.create(WebArchive.class, name) : ShrinkWrap.create(WebArchive.class);
      war.addAsResource("META-INF/services/org.jboss.msc.service.ServiceActivator");
      war.addAsLibraries(Maven.resolver().loadPomFromFile("../dependencies/pom.xml")
              .importRuntimeAndTestDependencies().resolve().withTransitivity().asFile());
      war.addClass(AbstractWCITestCase.class);
      return war;
   }

   protected static WebArchive wciWildfly10Deployment(EnterpriseArchive ear, String name)
   {
      WebArchive war = name != null ? ShrinkWrap.create(WebArchive.class, name) : ShrinkWrap.create(WebArchive.class);
      war.addAsResource("META-INF/services/org.jboss.msc.service.ServiceActivator");
      war.addAsLibraries(Maven.resolver().loadPomFromFile("../dependencies/pom.xml")
              .importRuntimeAndTestDependencies().resolve().withTransitivity().asFile());
      war.addClass(AbstractWCITestCase.class);
      return war;
   }

   protected static Asset getJBossDeploymentStructure(String module)
   {
     String content = "<jboss-deployment-structure>" +
       "<deployment>" +
       "<dependencies>" +
       "<module name=\"org.wildfly.extension.undertow\" export=\"true\" />";
     if (StringUtils.isNotBlank(module)) {
       content += "<module name=\"deployment." + module + ".war\" export=\"true\" />";
     }
     content += "</dependencies>" +
       "</deployment>" +
       "</jboss-deployment-structure>";
     return getAsset(content);
   }

   protected static Asset getAsset(String content)
   {
      return new ByteArrayAsset(content.getBytes());
   }
}
