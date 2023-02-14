plugins {
	kotlin("jvm") version "1.8.10"
	id("fabric-loom")
	`maven-publish`
	java
}

group = property("maven_group")!!
version = property("mod_version")!!

repositories {
	// Add repositories to retrieve artifacts from in here.
	// You should only use this when depending on other mods because
	// Loom adds the essential maven repositories to download Minecraft and libraries from automatically.
	// See https://docs.gradle.org/current/userguide/declaring_repositories.html
	// for more information about repositories.
}

dependencies {
	minecraft("com.mojang:minecraft:${property("minecraft_version")}")
	mappings("net.fabricmc:yarn:${property("yarn_mappings")}:v2")
	modImplementation("net.fabricmc:fabric-loader:${property("loader_version")}")
	modImplementation("net.fabricmc:fabric-language-kotlin:${property("fabric_kotlin_version")}")
	modImplementation("net.fabricmc.fabric-api:fabric-api:${property("fabric_api_version")}")
	modImplementation("net.silkmc:silk-core:1.9.5")
	modImplementation("net.silkmc:silk-commands:1.9.5")
}

loom {
	accessWidenerPath.set(file("src/main/resources/modid.accesswidener"))
}

tasks {
	processResources {
		inputs.property("version", project.version)
		filesMatching("fabric.mod.json") {
			expand(mutableMapOf("version" to project.version))
		}
	}

	jar {
		from("LICENSE")
	}

	publishing {
		publications {
			create<MavenPublication>("mavenJava") {
				artifact(remapJar) {
					builtBy(remapJar)
				}
				artifact(kotlinSourcesJar) {
					builtBy(remapSourcesJar)
				}
			}
		}

		// select the repositories you want to publish to
		repositories {
			// uncomment to publish to the local maven
			// mavenLocal()
		}
	}

	compileKotlin {
		kotlinOptions.jvmTarget = "17"
	}
}

java {
	withSourcesJar()
}

