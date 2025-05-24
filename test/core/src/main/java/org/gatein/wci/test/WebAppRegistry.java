/**
 * This file is part of the Meeds project (https://meeds.io/).
 *
 * Copyright (C) 2020 - 2025 Meeds Association contact@meeds.io
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */
package org.gatein.wci.test;

import org.gatein.wci.WebApp;
import org.gatein.wci.WebAppEvent;
import org.gatein.wci.WebAppLifeCycleEvent;
import org.gatein.wci.WebAppListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class WebAppRegistry implements WebAppListener
{

   /** . */
   final Map<String, WebApp> map = new HashMap<String, WebApp>();

   public void onEvent(WebAppEvent event)
   {
      if (event instanceof WebAppLifeCycleEvent)
      {
         WebApp webApp = event.getWebApp();
         WebAppLifeCycleEvent lfEvent = (WebAppLifeCycleEvent)event;
         if (lfEvent.getType() == WebAppLifeCycleEvent.ADDED)
         {
            map.put(webApp.getContextPath(), webApp);
         }
         else
         {
            map.remove(webApp.getContextPath());
         }
      }
   }

   public WebApp getWebApp(String key)
   {
      if (key == null)
      {
         throw new IllegalArgumentException();
      }
      return map.get(key);
   }

   public Set<String> getKeys()
   {
      return map.keySet();
   }
}
