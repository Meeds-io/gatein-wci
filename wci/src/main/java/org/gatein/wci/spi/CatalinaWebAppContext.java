/*
 * This file is part of the Meeds project (https://meeds.io/).
 * Copyright (C) 2020 Meeds Association
 * contact@meeds.io
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.gatein.wci.spi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.gatein.wci.command.CommandServlet;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author <a href="http://community.jboss.org/people/kenfinni">Ken Finnigan</a>
 */
public abstract class CatalinaWebAppContext implements WebAppContext {
  protected static final Logger log                            = LoggerFactory.getLogger(CatalinaWebAppContext.class);

    protected static final String GATEIN_SERVLET_NAME = "TomcatGateInServlet";
    protected static final String GATEIN_SERVLET_PATH = "/tomcatgateinservlet";
    protected static final int GATEIN_SERVLET_LOAD_ON_STARTUP = 0;

    private static final String BEAN_MGR_ATTR = "javax.enterprise.inject.spi.BeanManager";

    /**
     * .
     */
    protected ServletContext servletContext;

    /**
     * .
     */
    protected ClassLoader classLoader;

    /**
     * .
     */
    protected String contextPath;

    public CatalinaWebAppContext(ServletContext servletContext, ClassLoader classLoader, String contextPath) {
        this.servletContext = servletContext;
        this.classLoader = classLoader;
        this.contextPath = contextPath;
    }

    @Override
    public void start() throws Exception {
        performStartup();

        // Add BeanManager to ServletContext
        try {
            Object beanMgr = new InitialContext().lookup("java:comp/BeanManager");
            if (null != beanMgr) {
                servletContext.setAttribute(BEAN_MGR_ATTR, beanMgr);
            }
        } catch (NamingException e) {
            // Ignored
        }
    }

    @Override
    public void stop() {
        cleanup();
    }

    @Override
    public ServletContext getServletContext() {
        return servletContext;
    }

    @Override
    public ClassLoader getClassLoader() {
        return classLoader;
    }

    @Override
    public String getContextPath() {
        return contextPath;
    }

    @Override
    public boolean importFile(String parentDirRelativePath, String name, InputStream source, boolean overwrite) throws IOException {
        return false;
    }

    protected String getCommandServletClassName() {
        String className = null;
        try {
            className = CommandServlet.class.getName();
            classLoader.loadClass(className);
        } catch(Exception ex) {
            className = null;
            log.debug("WCI integration skipped for context: /" + contextPath);
        }
        return className;
    }

    @Override
    public void fireRequestDestroyed(ServletRequest servletRequest)
    {
       //Do Nothing
    }

    @Override
    public void fireRequestInitialized(ServletRequest servletRequest)
    {
       //Do Nothing
    }

    protected abstract void performStartup() throws Exception;

    protected abstract void cleanup();
}
