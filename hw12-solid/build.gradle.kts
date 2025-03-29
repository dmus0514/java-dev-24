group = "ru.otus"
version = "unspecified"

dependencies {
    implementation("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
    implementation("org.slf4j:slf4j-api")
    implementation ("ch.qos.logback:logback-classic")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation ("org.assertj:assertj-core")
}

tasks.test {
    useJUnitPlatform()
}
