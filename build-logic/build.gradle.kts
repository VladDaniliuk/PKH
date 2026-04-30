plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

gradlePlugin {
    plugins {
        register("localDependencyGraph") {
            id = "local-dependency-graph"
            implementationClass = "shov.studio.pkh.gradle.LocalDependencyGraphPlugin"
        }
    }
}
