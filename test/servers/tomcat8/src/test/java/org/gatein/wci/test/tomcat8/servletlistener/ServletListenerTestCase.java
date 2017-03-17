package org.gatein.wci.test.tomcat8.servletlistener;

import org.gatein.wci.test.servletlistener.AbstractServletListenerTestCase;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;

/**
 * @author <a href="http://community.jboss.org/people/kenfinni">Ken Finnigan</a>
 */
public class ServletListenerTestCase extends AbstractServletListenerTestCase
{

   static
   {
      AbstractServletListenerTestCase.webXml = "org/gatein/wci/test/tomcat8/servletlistener/listener_web.xml";
      AbstractServletListenerTestCase.contextXml = "org/gatein/wci/test/tomcat8/servletlistener/context.xml";
   }

   @Deployment(name = "servletlistenerwci")
   public static WebArchive wciDeployment()
   {
      WebArchive war = ShrinkWrap.create(WebArchive.class, "servletlistenerwci.war");
      war.setWebXML("org/gatein/wci/test/tomcat8/servletlistener/web.xml");
      war.addAsManifestResource("org/gatein/wci/test/tomcat8/servletlistener/context.xml", "context.xml");
      return war;
   }
}
