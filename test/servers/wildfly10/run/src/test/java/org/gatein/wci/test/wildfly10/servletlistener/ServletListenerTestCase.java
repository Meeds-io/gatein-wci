package org.gatein.wci.test.wildfly10.servletlistener;

import org.gatein.wci.test.WebAppRegistry;
import org.gatein.wci.test.servletlistener.AbstractServletListenerTestCase;
import org.gatein.wci.test.servletlistener.ListenerServlet;
import org.gatein.wci.test.servletlistener.ServletEventCountListener;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;

/**
 * @author <a href="http://community.jboss.org/people/kenfinni">Ken Finnigan</a>
 */
public class ServletListenerTestCase extends AbstractServletListenerTestCase
{

   static
   {
      AbstractServletListenerTestCase.webXml = "org/gatein/wci/test/wildfly10/servletlistener/listener_web.xml";
   }

   @Deployment(name = "servletlistenerwci")
   public static WebArchive wciDeployment()
   {
      WebArchive war = wciWildfly10Deployment("servletlistenerwci.war");
      war.setWebXML("org/gatein/wci/test/wildfly10/servletlistener/web.xml");
      war.addAsWebInfResource(getJBossDeploymentStructure(null), "jboss-deployment-structure.xml");
      war.addClass(WebAppRegistry.class);
      war.addClass(ListenerServlet.class);
      war.addClass(ServletEventCountListener.class);
      war.addClass(AbstractServletListenerTestCase.class);
      return war;
   }
}
