/**
 * Logback: the reliable, generic, fast and flexible logging framework.
 * Copyright (C) 1999-2011, QOS.ch. All rights reserved.
 *
 * This program and the accompanying materials are dual-licensed under
 * either the terms of the Eclipse Public License v1.0 as published by
 * the Eclipse Foundation
 *
 *   or (per the licensee's choosing)
 *
 * under the terms of the GNU Lesser General Public License version 2.1
 * as published by the Free Software Foundation.
 */
package ch.qos.logback.classic.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Set;

import android.content.res.AssetManager;
import android.util.Log;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.android.ASaxEventRecorder;
import ch.qos.logback.classic.android.BasicLogcatConfigurator;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.event.SaxEvent;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.status.ErrorStatus;
import ch.qos.logback.core.status.InfoStatus;
import ch.qos.logback.core.status.StatusManager;
import ch.qos.logback.core.status.WarnStatus;
import ch.qos.logback.core.util.Loader;
import ch.qos.logback.core.util.OptionHelper;
import android.content.Context;
import org.slf4j.Logger;

// contributors
// Ted Graham, Matt Fowles, see also http://jira.qos.ch/browse/LBCORE-32

/**
 * This class contains logback's logic for automatic configuration
 *
 * @author Ceki Gulcu
 */
public class ContextInitializer {

  final public static String  GROOVY_AUTOCONFIG_FILE = "logback.groovy";
  final public static String  AUTOCONFIG_FILE        = "logback.xml";
  final public static String  TEST_AUTOCONFIG_FILE   = "logback-test.xml";
  final public static String  CONFIG_FILE_PROPERTY   = "logback.configurationFile";
  final public static String  STATUS_LISTENER_CLASS  = "logback.statusListenerClass";
  final public static String  SYSOUT                 = "SYSOUT";
  final private static String TAG_MANIFEST           = "manifest";
  final private static String TAG_LOGBACK            = "logback";
  final private static String MANIFEST_FILE          = "AndroidManifest.xml";
  final private static String ASSETS_DIR             = "assets/";
  final private static String SDCARD_DIR             = "/sdcard/logback/";
  
  final LoggerContext loggerContext;

  public ContextInitializer(LoggerContext loggerContext) {
    this.loggerContext = loggerContext;
  }
  


//  public void configureByResource(URL url) throws JoranException {
//    if (url == null) {
//      throw new IllegalArgumentException("URL argument cannot be null");
//    }
//    if (url.toString().endsWith("groovy")) {
//      StatusManager sm = loggerContext.getStatusManager();
//      sm.add(new ErrorStatus("Groovy classes are not available on the class path. ABORTING INITIALIZATION.",
//              loggerContext));
//    }
//    if (url.toString().endsWith("xml")) {
//    	joranConfigureByResource(url);
//    }
//  }
//
//  void joranConfigureByResource(URL url) throws JoranException {
//    JoranConfigurator configurator = new JoranConfigurator();
//    configurator.setContext(loggerContext);
//    configurator.doConfigure(url);
//  }


  public void autoConfig() throws JoranException {
    StatusListenerConfigHelper.installIfAsked(loggerContext);
    
    //BasicLogcatConfigurator.configure(loggerContext);
  }

  public void config(Context context) {
    AssetManager am = context.getResources().getAssets();
    InputStream is;
    try {
      is = am.open(AUTOCONFIG_FILE);
    } catch (IOException e) {
      loggerContext.getLogger(Logger.ROOT_LOGGER_NAME).info("Logback configuration file {} not found in assets direcotry", AUTOCONFIG_FILE);
      return;
    }

    JoranConfigurator configurator = new JoranConfigurator();
    configurator.setContext(loggerContext);
    try {
      configurator.doConfigure(is);
    } catch (JoranException e) {
      loggerContext.getLogger(Logger.ROOT_LOGGER_NAME).error(String.format("Error in Logback configuration file assets/%s.", AUTOCONFIG_FILE), e);
      return;
    }


  }

}
