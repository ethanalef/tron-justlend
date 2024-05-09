package org.tron.justlend.justlendapiserver.core;

/**
 * constants
 */
public final class ProjectConstant {

  public static final String BASE_PACKAGE = "org.tron.justlend.justlendapiserver";
  public static final String MODEL_PACKAGE = BASE_PACKAGE + ".model";
  public static final String MAPPER_PACKAGE = BASE_PACKAGE + ".dao";
  public static final String MAPPER_INTERFACE_REFERENCE = BASE_PACKAGE + ".core.Mapper";
  public static final String MAPPER_LOCATION = "classpath:mapper/*.xml";

  public static final String SERVICE_PACKAGE = BASE_PACKAGE + ".service";
  public static final String SERVICE_IMPL_PACKAGE = SERVICE_PACKAGE + ".impl";
  public static final String CONTROLLER_PACKAGE = BASE_PACKAGE + ".web";

}
