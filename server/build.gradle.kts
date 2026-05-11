plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.koinCompiler)
    alias(libs.plugins.serialization)
    application
}

group = "shov.studio.pkh"
version = "1.0.0"
application {
    mainClass.set("shov.studio.pkh.ApplicationKt")
    
    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

dependencies {
    implementation(projects.data.auth.contract)
    implementation(libs.argon2.jvm)
    implementation(libs.exposed.core)
    implementation(libs.exposed.dao)
    implementation(libs.exposed.jdbc)
    implementation(libs.h2)
    implementation(libs.java.jwt)
    implementation(libs.koin.annotations)
    implementation(libs.koin.core)
    implementation(libs.koin.ktor)
    implementation(libs.logback)
    implementation(libs.ktor.serverCore)
    implementation(libs.ktor.serverContentNegotiation)
    implementation(libs.ktor.serverNetty)
    implementation(libs.ktor.serializationKotlinxJson)
    testImplementation(libs.ktor.clientContentNegotiation)
    testImplementation(libs.ktor.serverTestHost)
    testImplementation(libs.kotlin.testJunit)
}
