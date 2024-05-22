plugins {
	java
	id("org.springframework.boot") version "3.2.5"
	id("io.spring.dependency-management") version "1.1.4"
}

group = "org.tron.justlend"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_21
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web:3.2.5")
	implementation("org.springframework.boot:spring-boot-starter-jdbc:3.2.5")
	implementation("org.apache.commons:commons-lang3")
	implementation("org.mybatis.spring.boot:mybatis-spring-boot-starter:3.0.3")
	implementation("tk.mybatis:mapper:4.3.0")
	implementation("com.github.pagehelper:pagehelper:6.0.0")
	implementation("org.web3j:core:5.0.0")
	implementation("com.google.guava:guava:33.2.0-jre")
	implementation("org.springframework.retry:spring-retry:2.0.5")
	implementation("com.alibaba.fastjson2:fastjson2:2.0.43")
	implementation("com.alibaba.fastjson2:fastjson2-extension-spring5:2.0.43")
	implementation("ch.qos.logback:logback-core:1.5.5")
	implementation("com.mysql:mysql-connector-j:8.4.0")

	developmentOnly("org.springframework.boot:spring-boot-devtools")
	compileOnly("org.projectlombok:lombok:1.18.32")
	annotationProcessor("org.projectlombok:lombok:1.18.32")

	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

	testImplementation("org.springframework.boot:spring-boot-starter-test:3.2.5")
	testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
	testImplementation("org.freemarker:freemarker")
	testImplementation("org.mybatis.generator:mybatis-generator-core:1.4.2")
	testImplementation("ch.qos.logback:logback-classic:1.5.5")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
