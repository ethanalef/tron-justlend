package generator;


import com.google.common.base.CaseFormat;
import freemarker.template.TemplateExceptionHandler;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.*;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.tron.justlend.justlendapiserver.core.ProjectConstant.*;

public class DaoGenerator {

  private static final String JDBC_URL = "jdbc:mysql://192.168.1.27:3306/justlend?useUnicode=true&characterEncoding=UTF-8&useSSL=false";
  private static final String JDBC_USERNAME = "ethan";
  private static final String JDBC_PASSWORD = "........";
  private static final String JDBC_DIVER_CLASS_NAME = "com.mysql.cj.jdbc.Driver";

  private static final String PROJECT_PATH = System.getProperty("user.dir");
  private static final String TEMPLATE_FILE_PATH =
    PROJECT_PATH + "/src/test/resources/generator/template";

  private static final String JAVA_PATH = "/src/main/java";
  private static final String RESOURCES_PATH = "/src/main/resources";

  private static final String PACKAGE_PATH_SERVICE = packageConvertPath(
    SERVICE_PACKAGE);
  private static final String PACKAGE_PATH_SERVICE_IMPL = packageConvertPath(
    SERVICE_IMPL_PACKAGE);
  private static final String PACKAGE_PATH_CONTROLLER = packageConvertPath(
    CONTROLLER_PACKAGE);

  private static final String AUTHOR = "CodeGenerator";//@author
  private static final String DATE = new SimpleDateFormat("yyyy/MM/dd").format(new Date());//@date

  public static void main(String[] args) {
    genCode("chaser_progress");
  }

  public static void genCode(String... tableNames) {
    for (String tableName : tableNames) {
      genCodeByCustomModelName(tableName, null);
    }
  }

  public static void genCodeByCustomModelName(String tableName, String modelName) {
    genModelAndMapper(tableName, modelName);
    genService(tableName, modelName);
  }


  public static void genModelAndMapper(String tableName, String modelName) {
    Context context = new Context(ModelType.FLAT);
    context.setId("Potato");
    context.setTargetRuntime("MyBatis3Simple");
    context.addProperty(PropertyRegistry.CONTEXT_BEGINNING_DELIMITER, "`");
    context.addProperty(PropertyRegistry.CONTEXT_ENDING_DELIMITER, "`");

    JDBCConnectionConfiguration jdbcConnectionConfiguration = new JDBCConnectionConfiguration();
    jdbcConnectionConfiguration.setConnectionURL(JDBC_URL);
    jdbcConnectionConfiguration.setUserId(JDBC_USERNAME);
    jdbcConnectionConfiguration.setPassword(JDBC_PASSWORD);
    jdbcConnectionConfiguration.setDriverClass(JDBC_DIVER_CLASS_NAME);
    context.setJdbcConnectionConfiguration(jdbcConnectionConfiguration);

    PluginConfiguration pluginConfiguration = new PluginConfiguration();
    pluginConfiguration.setConfigurationType("tk.mybatis.mapper.generator.MapperPlugin");
    pluginConfiguration.addProperty("mappers", MAPPER_INTERFACE_REFERENCE);
    context.addPluginConfiguration(pluginConfiguration);

    JavaModelGeneratorConfiguration javaModelGeneratorConfiguration = new JavaModelGeneratorConfiguration();
    javaModelGeneratorConfiguration.setTargetProject(PROJECT_PATH + JAVA_PATH);
    javaModelGeneratorConfiguration.setTargetPackage(MODEL_PACKAGE);
    context.setJavaModelGeneratorConfiguration(javaModelGeneratorConfiguration);

    SqlMapGeneratorConfiguration sqlMapGeneratorConfiguration = new SqlMapGeneratorConfiguration();
    sqlMapGeneratorConfiguration.setTargetProject(PROJECT_PATH + RESOURCES_PATH);
    sqlMapGeneratorConfiguration.setTargetPackage("mapper");
    context.setSqlMapGeneratorConfiguration(sqlMapGeneratorConfiguration);

    JavaClientGeneratorConfiguration javaClientGeneratorConfiguration = new JavaClientGeneratorConfiguration();
    javaClientGeneratorConfiguration.setTargetProject(PROJECT_PATH + JAVA_PATH);
    javaClientGeneratorConfiguration.setTargetPackage(MAPPER_PACKAGE);
    javaClientGeneratorConfiguration.setConfigurationType("XMLMAPPER");
    context.setJavaClientGeneratorConfiguration(javaClientGeneratorConfiguration);

    TableConfiguration tableConfiguration = new TableConfiguration(context);
    tableConfiguration.setTableName(tableName);
    tableConfiguration.addIgnoredColumn(new IgnoredColumn("create_time"));
    tableConfiguration.addIgnoredColumn(new IgnoredColumn("update_time"));

    if (StringUtils.isNotEmpty(modelName)) {
      tableConfiguration.setDomainObjectName(modelName);
    }
    tableConfiguration.setGeneratedKey(new GeneratedKey("id", "SqlServer", true, null));
    context.addTableConfiguration(tableConfiguration);

    List<String> warnings;
    MyBatisGenerator generator;
    try {
      Configuration config = new Configuration();
      config.addContext(context);
      config.validate();

      boolean overwrite = true;
      DefaultShellCallback callback = new DefaultShellCallback(overwrite);
      warnings = new ArrayList<String>();
      generator = new MyBatisGenerator(config, callback, warnings);
      generator.generate(null);
    } catch (Exception e) {
      throw new RuntimeException("Generate Model and Mapper failed", e);
    }

    if (generator.getGeneratedJavaFiles().isEmpty() || generator.getGeneratedXmlFiles().isEmpty()) {
      throw new RuntimeException("Generate Model and Mapper failed" + warnings);
    }
    if (StringUtils.isEmpty(modelName)) {
      modelName = tableNameConvertUpperCamel(tableName);
    }
    System.out.println(modelName + ".java Generate successfully");
    System.out.println(modelName + "Mapper.java Generate successfully");
    System.out.println(modelName + "Mapper.xml Generate successfully");
  }

  public static void genService(String tableName, String modelName) {
    try {
      freemarker.template.Configuration cfg = getConfiguration();

      Map<String, Object> data = new HashMap<>();
      data.put("date", DATE);
      data.put("author", AUTHOR);
      String modelNameUpperCamel =
        StringUtils.isEmpty(modelName) ? tableNameConvertUpperCamel(tableName) : modelName;
      data.put("modelNameUpperCamel", modelNameUpperCamel);
      data.put("modelNameLowerCamel", tableNameConvertLowerCamel(tableName));
      data.put("basePackage", BASE_PACKAGE);

      File file = new File(
        PROJECT_PATH + JAVA_PATH + PACKAGE_PATH_SERVICE + modelNameUpperCamel + "Service.java");
      if (!file.getParentFile().exists()) {
        file.getParentFile().mkdirs();
      }
      cfg.getTemplate("service.ftl").process(data,
        new FileWriter(file));
      System.out.println(modelNameUpperCamel + "Service.java Generate successfully");

      File file1 = new File(
        PROJECT_PATH + JAVA_PATH + PACKAGE_PATH_SERVICE_IMPL + modelNameUpperCamel
          + "ServiceImpl.java");
      if (!file1.getParentFile().exists()) {
        file1.getParentFile().mkdirs();
      }
      cfg.getTemplate("service-impl.ftl").process(data,
        new FileWriter(file1));
      System.out.println(modelNameUpperCamel + "ServiceImpl.java Generate successfully");
    } catch (Exception e) {
      throw new RuntimeException("Generate Service failed", e);
    }
  }

  public static void genController(String tableName, String modelName) {
    try {
      freemarker.template.Configuration cfg = getConfiguration();

      Map<String, Object> data = new HashMap<>();
      data.put("date", DATE);
      data.put("author", AUTHOR);
      String modelNameUpperCamel =
        StringUtils.isEmpty(modelName) ? tableNameConvertUpperCamel(tableName) : modelName;
      data.put("baseRequestMapping", modelNameConvertMappingPath(modelNameUpperCamel));
      data.put("modelNameUpperCamel", modelNameUpperCamel);
      data.put("modelNameLowerCamel",
        CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, modelNameUpperCamel));
      data.put("basePackage", BASE_PACKAGE);

      File file = new File(PROJECT_PATH + JAVA_PATH + PACKAGE_PATH_CONTROLLER + modelNameUpperCamel
                             + "Controller.java");
      if (!file.getParentFile().exists()) {
        file.getParentFile().mkdirs();
      }
      cfg.getTemplate("controller.ftl").process(data, new FileWriter(file));

      System.out.println(modelNameUpperCamel + "Controller.java Generate successfully");
    } catch (Exception e) {
      throw new RuntimeException("Generate Controller failed", e);
    }

  }

  private static freemarker.template.Configuration getConfiguration() throws IOException {
    freemarker.template.Configuration cfg = new freemarker.template.Configuration(
      freemarker.template.Configuration.VERSION_2_3_23);
    cfg.setDirectoryForTemplateLoading(new File(TEMPLATE_FILE_PATH));
    cfg.setDefaultEncoding("UTF-8");
    cfg.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
    return cfg;
  }

  private static String tableNameConvertLowerCamel(String tableName) {
    return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, tableName.toLowerCase());
  }

  private static String tableNameConvertUpperCamel(String tableName) {
    return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, tableName.toLowerCase());

  }

  private static String tableNameConvertMappingPath(String tableName) {
    tableName = tableName.toLowerCase();
    return "/" + (tableName.contains("_") ? tableName.replaceAll("_", "/") : tableName);
  }

  private static String modelNameConvertMappingPath(String modelName) {
    String tableName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, modelName);
    return tableNameConvertMappingPath(tableName);
  }

  private static String packageConvertPath(String packageName) {
    return String.format("/%s/",
      packageName.contains(".") ? packageName.replaceAll("\\.", "/") : packageName);
  }

}