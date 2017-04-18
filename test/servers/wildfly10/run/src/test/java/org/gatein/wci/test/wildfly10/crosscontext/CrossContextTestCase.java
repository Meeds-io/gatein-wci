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
package org.gatein.wci.test.wildfly10.crosscontext;

import java.net.HttpURLConnection;
import java.net.URL;

import org.gatein.wci.test.AbstractWCITestCase;
import org.gatein.wci.test.WebAppRegistry;
import org.gatein.wci.test.crosscontext.AbstractCrossContextTestCase;
import org.gatein.wci.test.crosscontext.CrossContextServlet;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.container.test.api.Testable;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Test;

import junit.framework.Assert;

/** @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a> */
public class CrossContextTestCase extends AbstractWCITestCase {

  @Deployment(name = "crosscontextwci")
  public static Archive<?> deployment() {
    EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "crosscontextwci.ear");
    ear.addAsLibraries(Maven.resolver().loadPomFromFile("../dependencies/pom.xml")
                       .importRuntimeAndTestDependencies().resolve().withTransitivity().asFile());
    ear.addAsManifestResource("org/gatein/wci/test/wildfly10/crosscontext/jboss-all.xml", "jboss-all.xml");
    ear.setApplicationXML(getAsset(getApplicationXML("crosscontextwci", "crosscontextapp")));

    JavaArchive libJar = ShrinkWrap.create(JavaArchive.class, "lib.jar");
    libJar.addClass(WebAppRegistry.class);
    libJar.addClass(CrossContextServlet.class);
    libJar.addClass(AbstractCrossContextTestCase.class);
    libJar.addClass(AbstractWCITestCase.class);
    libJar.addAsResource("META-INF/services/org.jboss.msc.service.ServiceActivator");
    libJar.setManifest(new StringAsset("Dependencies: org.wildfly.extension.undertow, io.undertow.servlet"));
    ear.addAsLibrary(libJar);

    WebArchive war = warDeployment(ear, "crosscontextwci.war");
    war.setWebXML("org/gatein/wci/test/wildfly10/crosscontext/web.xml");
    war.addAsWebInfResource("org/gatein/wci/test/wildfly10/crosscontext/jboss-web.xml", "jboss-web.xml");
    ear.addAsModule(war);

    war = warDeployment(ear, "crosscontextapp.war");
    war = Testable.archiveToTest(war);
    ear.addAsModule(war);
    return ear;
  }

  private static String getApplicationXML(String... applications) {
    StringBuilder content = new StringBuilder("<application>");
    for (String application : applications) {
      content.append("<module>")
             .append("   <web>")
             .append("    <web-uri>")
             .append(application)
             .append(".war</web-uri>")
             .append("    <context-root>")
             .append(application)
             .append("    </context-root>")
             .append("  </web>")
             .append("</module>");
    }
    content.append("</application>");
    return content.toString();
  }

  @Test
  @RunAsClient
  public void testFoo(@ArquillianResource @OperateOnDeployment("crosscontextwci") URL requestDispatchURL) throws Exception {
    HttpURLConnection conn = (HttpURLConnection) requestDispatchURL.openConnection();
    conn.connect();
    Assert.assertEquals(200, conn.getResponseCode());
  }

  protected static WebArchive warDeployment(EnterpriseArchive ear, String name) {
    WebArchive war = name != null ? ShrinkWrap.create(WebArchive.class, name) : ShrinkWrap.create(WebArchive.class);
    return war;
  }
}
