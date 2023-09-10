plugins {
    kotlin("jvm") version "1.9.0"
    id("org.jetbrains.compose") version "1.5.0"
    id("com.diffplug.spotless") version "6.21.0"
}

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation("dev.secondsun:java-isoengine:0.7")
}

compose.desktop {
    application {
        mainClass = "MainKt"
    }
}

configure<com.diffplug.gradle.spotless.SpotlessExtension> {
    kotlin {
        target("**/*.kt")
        targetExclude("**/build/**/*.kt")
        targetExclude("**/bin/**/*.kt")
        ktfmt()
        ktlint().editorConfigOverride(
            mapOf(
                "ktlint_standard_no-wildcard-imports" to "disabled",
                "ij_kotlin_allow_trailing_comma" to true,
                // These rules were introduced in ktlint 0.46.0 and should not be
                // enabled without further discussion. They are disabled for now.
                // See: https://github.com/pinterest/ktlint/releases/tag/0.46.0
                "disabled_rules" to
                    "filename," +
                    "annotation,annotation-spacing," +
                    "argument-list-wrapping," +
                    "double-colon-spacing," +
                    "enum-entry-name-case," +
                    "no-wildcard-imports," +
                    "multiline-if-else," +
                    "no-empty-first-line-in-method-block," +
                    "package-name," +
                    "trailing-comma," +
                    "spacing-around-angle-brackets," +
                    "spacing-between-declarations-with-annotations," +
                    "spacing-between-declarations-with-comments," +
                    "unary-op-spacing",
            ),
        )

        // diktat()   // has its own section below
        // prettier(mapOf(Pair("prettier", "3.0.3"), Pair("prettier-plugin-kotlin", "2.1.0"))) // has its own section below
        licenseHeader(
            """
 /**
 Secondsun's Game Level Editor
 Copyright (C) 2023 Summers Pittman
 secondsun@gmail.com

 This program is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation; either
 version 3 of the License, or (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public License
 along with this program; if not, write to the Free Software Foundation,
 Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 **/
            """.trimIndent(),
        ) // or licenseHeaderFile
    }
    kotlinGradle {
        target("*.gradle.kts") // default target for kotlinGradle
        ktlint() // or ktfmt() or prettier()
    }
}
